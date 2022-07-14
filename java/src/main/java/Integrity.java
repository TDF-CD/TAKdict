import java.io.*;
import java.security.*;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xades4j.*;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import xades4j.properties.QualifyingProperty;
import xades4j.properties.SignatureTimeStampProperty;
import xades4j.properties.SigningCertificateProperty;
import xades4j.properties.SigningTimeProperty;
import xades4j.providers.CertificateValidationProvider;
import xades4j.providers.impl.PKIXCertificateValidationProvider;
import xades4j.utils.DOMHelper;
import xades4j.utils.FileSystemDirectoryCertStore;
import xades4j.utils.XadesProfileResolutionException;
import xades4j.verification.XAdESVerificationResult;
import xades4j.verification.XadesVerificationProfile;
import xades4j.verification.XadesVerifier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Integrity {



    public String getHash(String filePath) throws IOException{

        File file = new File(filePath);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis = new FileInputStream(file);
            byte[] dataBytes = new byte[1024];

            int nread = 0;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;

            byte[] mdbytes = md.digest();

            String encoded = Base64.getEncoder().withoutPadding().encodeToString(mdbytes);
            return encoded;


        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean verifyPgpSignature(String pubKeyFilePath, String sigFilePath, String dataFilePath) throws Exception {
        InputStream inputStream;
        int bytesRead;
        PGPPublicKey publicKey;
        PGPSignature pgpSignature = null;
        boolean result;

        File pubKeyFile = new File(pubKeyFilePath);
        File sigFile = new File(sigFilePath);
        File dataFile = new File(dataFilePath);

        // Read keys from file
        inputStream = PGPUtil.getDecoderStream(new FileInputStream(pubKeyFile));
        PGPPublicKeyRingCollection publicKeyRingCollection = new PGPPublicKeyRingCollection(inputStream,
                new JcaKeyFingerprintCalculator());
        inputStream.close();

        Iterator<PGPPublicKeyRing> iterator = publicKeyRingCollection.getKeyRings();
        PGPPublicKeyRing pgpPublicKeyRing;
        if (iterator.hasNext()) {
            pgpPublicKeyRing = iterator.next();
        } else {
            throw new PGPException("Could not find public keyring in provided key file");
        }

        // Read signature from file
        inputStream = PGPUtil.getDecoderStream(new FileInputStream(sigFile));
        PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(inputStream, new JcaKeyFingerprintCalculator());
        Object o = pgpObjectFactory.nextObject();
        if (o instanceof PGPSignatureList) {
            PGPSignatureList signatureList = (PGPSignatureList) o;
            pgpSignature = signatureList.get(0);
        } else if (o instanceof PGPSignature) {
            pgpSignature = (PGPSignature) o;
        } else {
            throw new SignatureException("Could not find signature in provided signature file");
        }
        inputStream.close();

        publicKey = pgpPublicKeyRing.getPublicKey(pgpSignature.getKeyID());
        pgpSignature.init(new BcPGPContentVerifierBuilderProvider(), publicKey);

        // Read file to verify
        byte[] data = new byte[1024];
        inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFile)));
        while (true) {
            bytesRead = inputStream.read(data, 0, 1024);
            if (bytesRead == -1)
                break;
            pgpSignature.update(data, 0, bytesRead);
        }
        inputStream.close();

        // Verify the signature
        result = pgpSignature.verify();
        return result;
    }

}

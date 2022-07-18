package org.wot.tak;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentVerifierBuilderProvider;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

import java.io.*;
import java.security.SignatureException;
import java.util.Iterator;

class PgpSignatureValidator {

        public String getHash(String filePath) throws NoSuchAlgorithmException, IOException {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                byte[] dataBytes = new byte[1024]; // TODO
                int nread;
                while ((nread = fileInputStream.read(dataBytes)) != -1) {
                    messageDigest.update(dataBytes, 0, nread);
                }
            }
            byte[] digest = messageDigest.digest();
            String digestHash = Base64.getEncoder().withoutPadding().encodeToString(digest);
            return digestHash;
        }

    public boolean verifyPgpSignature(String pubKeyFilePath, String sigFilePath, String dataFilePath)
            throws IOException, SignatureException, PGPException {

        PGPPublicKeyRing pgpPublicKeyRing = getPGPPublicKeyRing(pubKeyFilePath);
        PGPSignature pgpSignature = getPGPSignature(sigFilePath, pgpPublicKeyRing);
        return verifyFileSignature(dataFilePath, pgpSignature);
    }

    private PGPPublicKeyRing getPGPPublicKeyRing(String pubKeyFilePath) throws PGPException, IOException {
        InputStream inputStream = PGPUtil.getDecoderStream(new FileInputStream(pubKeyFilePath));
        PGPPublicKeyRingCollection publicKeyRingCollection = new PGPPublicKeyRingCollection(
                inputStream, new JcaKeyFingerprintCalculator()
        );
        inputStream.close();

        Iterator<PGPPublicKeyRing> iterator = publicKeyRingCollection.getKeyRings();
        PGPPublicKeyRing pgpPublicKeyRing;
        if (iterator.hasNext()) {
            pgpPublicKeyRing = iterator.next();
        } else {
            throw new PGPException("Could not find public keyring in provided key file");
        }
        return pgpPublicKeyRing;
    }

    private PGPSignature getPGPSignature(String sigFilePath, PGPPublicKeyRing pgpPublicKeyRing)
            throws IOException, SignatureException, PGPException {
        InputStream inputStream = PGPUtil.getDecoderStream(new FileInputStream(sigFilePath));
        PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(inputStream, new JcaKeyFingerprintCalculator());
        Object o = pgpObjectFactory.nextObject();
        PGPSignature pgpSignature;
        if (o instanceof PGPSignatureList) {
            PGPSignatureList signatureList = (PGPSignatureList) o;
            pgpSignature = signatureList.get(0);
        } else if (o instanceof PGPSignature) {
            pgpSignature = (PGPSignature) o;
        } else {
            throw new SignatureException("Could not find signature in provided signature file");
        }
        inputStream.close();

        PGPPublicKey publicKey = pgpPublicKeyRing.getPublicKey(pgpSignature.getKeyID());
        pgpSignature.init(new BcPGPContentVerifierBuilderProvider(), publicKey);
        return pgpSignature;
    }

    private boolean verifyFileSignature(String dataFilePath, PGPSignature pgpSignature) throws IOException, PGPException {
        byte[] data = new byte[1024];
        InputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
        int bytesRead;
        while (true) {
            bytesRead = inputStream.read(data, 0, 1024);
            if (bytesRead == -1)
                break;
            pgpSignature.update(data, 0, bytesRead);
        }
        inputStream.close();
        return pgpSignature.verify();
    }

}

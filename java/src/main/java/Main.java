

public class Main {

    public static void main(String[] argv) throws Exception {
        String csvFilePath = "C:\\Users\\misterk\\Desktop\\tak-cot-master\\tak-cot-master\\src\\main\\java\\APP6B_to_CoT.csv";
        String pubKeyFilePath = "C:\\Users\\misterk\\Desktop\\tak-cot-master\\tak-cot-master\\src\\main\\java\\pub.rsa";
        String pgpSignatureFilePath = "C:\\Users\\misterk\\Desktop\\tak-cot-master\\tak-cot-master\\src\\main\\java\\APP6B_to_CoT.csv.sig";

        Integrity t = new Integrity();
        String hash = t.getHash(csvFilePath);
        System.out.println(hash);

        boolean pgpStatus = t.verifyPgpSignature(pubKeyFilePath, pgpSignatureFilePath, csvFilePath);
        System.out.println(pgpStatus);

    }

}

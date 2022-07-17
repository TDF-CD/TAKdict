

public class Main {

    public static void main(String[] args) throws Exception {
        String csvFilePath = "src/main/resources/APP6B_to_CoT.csv";
        String pubKeyFilePath = "src/main/resources/pub.rsa";
        String pgpSignatureFilePath = "src/main/resources/APP6B_to_CoT.csv.sig";

        PgpSignatureValidator pgpSignatureValidator = new PgpSignatureValidator();

        boolean isPgpSignatureValid = pgpSignatureValidator.verifyPgpSignature(
                pubKeyFilePath, pgpSignatureFilePath, csvFilePath
        );



        if (isPgpSignatureValid) {
            if (args.length < 4) {
                throw new Exception(
                        "Argument(s) missing. Required args: filePath, term, sourceColumn, destinationColumn. " +
                                "Optional 5th arg: delimiter"
                );
            }

            String filePath = args[0]; // "src/main/resources/APP6B_to_CoT.csv"
            String term = args[1]; // "SUG-IRSR--"

            int sourceColumn = 0;
            int destinationColumn = 0;
            try {
                sourceColumn = Integer.parseInt(args[2]); // 0
            } catch (NumberFormatException e) {
                throw new Exception("Argument \"" + args[2] + "\" must be an integer.");
            }
            try {
                destinationColumn  = Integer.parseInt(args[3]); // 3
            } catch (NumberFormatException e) {
                throw new Exception("Argument \"" + args[3] + "\" must be an integer.");
            }

            String delimiter;
            try {
                delimiter = args[4];
            }
            catch (ArrayIndexOutOfBoundsException e) {
                delimiter = ";";
            }

            CsvMapper.findInCsv(filePath, term, sourceColumn, destinationColumn, delimiter);
        } else {
            throw new Exception("File signature is invalid.");
        }

    }

}

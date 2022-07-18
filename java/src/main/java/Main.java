public class Main {

    public static void main(String[] args) throws Exception {
        Config config = parseArgs(args);

        PgpSignatureValidator pgpSignatureValidator = new PgpSignatureValidator();
        boolean isPgpSignatureValid = pgpSignatureValidator.verifyPgpSignature(
                config.getPubKeyFilePath(), config.getPgpSignatureFilePath(), config.getCsvFilePath()
        );

        if (isPgpSignatureValid) {
            CsvMapper.findInCsv(
                    config.getCsvFilePath(),
                    config.getTerm(),
                    config.getSourceColumn(),
                    config.getDestinationColumn(),
                    config.getDelimiter()
            );
        } else {
            throw new Exception("File signature is invalid.");
        }

    }

    private static Config parseArgs(String[] args) throws Exception {
        if (args.length < 6) {
            throw new Exception(
                    "Argument(s) missing. Required args: csvFilePath, pubKeyFilePath, pgpSignatureFilePath, " +
                            "term, sourceColumn, destinationColumn. " +
                            "Optional 7th arg: delimiter"
            );
        }

        String csvFilePath = args[0]; // "src/main/resources/APP6B_to_CoT.csv"
        String pubKeyFilePath = args[1]; // "src/main/resources/pub.rsa";
        String pgpSignatureFilePath = args[2]; // "src/main/resources/APP6B_to_CoT.csv.sig";
        String term = args[3]; // "SUG-IRSR--"

        int sourceColumn = 0;
        int destinationColumn = 0;
        try {
            sourceColumn = Integer.parseInt(args[4]); // 0
        } catch (NumberFormatException e) {
            throw new Exception("Argument \"" + args[4] + "\" must be an integer.");
        }
        try {
            destinationColumn  = Integer.parseInt(args[5]); // 3
        } catch (NumberFormatException e) {
            throw new Exception("Argument \"" + args[5] + "\" must be an integer.");
        }

        String delimiter;
        try {
            delimiter = args[6];
        }
        catch (ArrayIndexOutOfBoundsException e) {
            delimiter = ";";
        }

        Config config = new Config();
        config.setCsvFilePath(csvFilePath);
        config.setPubKeyFilePath(pubKeyFilePath);
        config.setPgpSignatureFilePath(pgpSignatureFilePath);
        config.setTerm(term);
        config.setSourceColumn(sourceColumn);
        config.setDestinationColumn(destinationColumn);
        config.setDelimiter(delimiter);

        return config;
    }

}

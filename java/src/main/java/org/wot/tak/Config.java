package org.wot.tak;

class Config {

    String csvFilePath;
    String pubKeyFilePath;
    String pgpSignatureFilePath;
    String term;
    int sourceColumn;
    int destinationColumn;
    String delimiter;

    public Config() {
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public String getPubKeyFilePath() {
        return pubKeyFilePath;
    }

    public void setPubKeyFilePath(String pubKeyFilePath) {
        this.pubKeyFilePath = pubKeyFilePath;
    }

    public String getPgpSignatureFilePath() {
        return pgpSignatureFilePath;
    }

    public void setPgpSignatureFilePath(String pgpSignatureFilePath) {
        this.pgpSignatureFilePath = pgpSignatureFilePath;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getSourceColumn() {
        return sourceColumn;
    }

    public void setSourceColumn(int sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    public int getDestinationColumn() {
        return destinationColumn;
    }

    public void setDestinationColumn(int destinationColumn) {
        this.destinationColumn = destinationColumn;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}

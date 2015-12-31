package classifier;

public class TempDocument {
    private final String text;
    private String classification;

    /**
     * A document consisting of text and a classification.
     *
     * @param text           - the text of the document
     * @param classification - the name of the classification
     */
    public TempDocument(String text, String classification) {
        this.text = text;
        this.classification = classification;
    }

    public String getText() {
        return text;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}

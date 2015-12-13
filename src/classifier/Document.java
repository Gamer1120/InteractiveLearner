package classifier;

public class Document {
    public final String text;
    public final String classification;

    /**
     * A document consisting of text and a classification.
     *
     * @param text           - the text of the document
     * @param classification - the name of the classification
     */
    public Document(String text, String classification) {
        this.text = text;
        this.classification = classification;
    }
}

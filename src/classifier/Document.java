package classifier;

public class Document {
    public final String[] text;
    public final String classification;

    public Document(String[] text, String classification) {
        this.text = text;
        this.classification = classification;
    }
}

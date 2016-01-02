package model;

public class Text {
    private final String text;
    private String category;

    /**
     * A document consisting of a text.
     *
     * @param text - the text of the document
     */
    public Text(String text) {
        this(text, null);
    }

    /**
     * A document consisting of a text and a category.
     *
     * @param text     - the text of the document
     * @param category - the category
     */
    public Text(String text, String category) {
        this.text = text;
        this.category = category;
    }

    /**
     * Get the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Get the category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category.
     *
     * @param category - the category to be set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}

package model;

public class Text {
    private final String text;
    private String category;

    public Text(String text) {
        this(text, null);
    }

    public Text(String text, String category) {
        this.text = text;
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

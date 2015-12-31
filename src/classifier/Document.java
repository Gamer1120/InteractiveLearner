package classifier;

import fileparser.FileUtils;
import utils.MutableInt;

import java.util.HashMap;
import java.util.Map;

public class Document {
    private Map<String, MutableInt> tokens;
    private String category;

    public Document() {
        this(null);
    }

    public Document(String category) {
        this(category, null);
    }

    public Document(String category, String text) {
        this.category = category;
        tokens = new HashMap<>();
        if (text != null) {
            addText(text);
        }
    }

    public int getCount(String token) {
        MutableInt count = tokens.get(token);
        return count == null ? 0 : count.intValue();
    }

    public Map<String, MutableInt> getTokens() {
        return tokens;
    }

    public void addToken(String token) {
        MutableInt count = tokens.get(token);
        if (count == null) {
            tokens.put(token, new MutableInt(1));
        } else {
            count.add(1);
        }
    }

    public void addText(String text) {
        for (String token : FileUtils.tokenize(text)) {
            addToken(token);
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

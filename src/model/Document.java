package model;

import utils.MutableInt;
import utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Document implements Serializable {
    // Map of tokens and how many times they occur
    private final Map<String, MutableInt> tokens;
    private String category;

    /**
     * An empty document.
     */
    public Document() {
        this(null);
    }

    /**
     * A document consisting of a map of tokens with how many times they occur.
     *
     * @param text - the text of the document
     */
    public Document(String text) {
        this(text, null);
    }

    /**
     * A document consisting of a map of tokens with how many times they occur and a category.
     *
     * @param text     - the text of the document
     * @param category - the category
     */
    public Document(String text, String category) {
        this.category = category;
        tokens = new HashMap<>();
        if (text != null) {
            // If there is a text, add it to the document
            addText(text);
        }
    }

    /**
     * Gets the count the specified token.
     *
     * @param token - the specified token
     * @return the count of the specified token
     */
    public int getCount(String token) {
        // Get the count of the specified token
        MutableInt count = tokens.get(token);
        // Return the count of the token or zero if the token isn't known
        return count == null ? 0 : count.intValue();
    }

    /**
     * Get the map of tokens and how many times they occur.
     *
     * @return the map of tokens
     */
    public Map<String, MutableInt> getTokens() {
        return tokens;
    }

    /**
     * Add the token to the document.
     *
     * @param token - the token
     */
    public void addToken(String token) {
        // Get the count of the token
        MutableInt count = tokens.get(token);
        if (count == null) {
            // If the token doesn't exist, add it with a count of one
            tokens.put(token, new MutableInt(1));
        } else {
            // Add one to the count
            count.add(1);
        }
    }

    /**
     * Add the text to the document.
     *
     * @param text - the text
     */
    public void addText(String text) {
        // For all tokens in the text
        for (String token : Utils.tokenize(text)) {
            // Add the token
            addToken(token);
        }
    }

    /**
     * Get the category of this document
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of this document
     *
     * @param category - the category
     */
    public void setCategory(String category) {
        this.category = category;
    }
}

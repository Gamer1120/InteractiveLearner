package classifier;

import fileparser.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TrainingSet {
    // Total amount of documents
    private int documentCount;
    // Map of classes and their values
    private Map<String, ClassValues> classes;
    // All unique words
    private Set<String> words;

    public TrainingSet() {
        documentCount = 0;
        classes = new HashMap<>();
        words = new HashSet<>();
    }

    public void add(Document document) {
        ++documentCount;
        ClassValues classValues = classes.get(document.getClassification());
        if (classValues == null) {
            classValues = new ClassValues();
            classes.put(document.getClassification(), classValues);
        }
        classValues.addDocument();
        for (String word : document.getText()) {
            words.add(word);
            classValues.addWord(word);
        }
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public int getDocumentCount(String c) {
        return classes.get(c).getDocumentCount();
    }

    public int getWordCount() {
        return words.size();
    }

    public int getWordCount(String c) {
        return classes.get(c).getTotalWordCount();
    }

    public int getWordCount(String c, String word) {
        return classes.get(c).getIndividualWordCount(word);
    }

    public double getPriority(String c) {
        return (double) getDocumentCount(c) / (double) getDocumentCount();
    }
}

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
        //FIXME
    }
}

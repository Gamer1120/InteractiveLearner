package classifier;

import fileparser.Document;

import java.util.*;

public class TrainingSet {
    // Total amount of documents
    private int documentCount;
    // Map of classes and their values
    private Map<String, ClassValues> classes;
    // All unique words
    private Set<String> vocabulary;

    public TrainingSet() {
        documentCount = 0;
        classes = new HashMap<>();
        vocabulary = new HashSet<>();
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
            vocabulary.add(word);
            classValues.addWord(word);
        }
    }

    public void addAll(Document... documents) {
        for (Document document : documents) {
            add(document);
        }
    }

    public void addAll(Collection<Document> documents) {
        documents.forEach(this::add);
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public boolean inVocabulary(String word) {
        return vocabulary.contains(word);
    }

    public int getVocabularySize() {
        return vocabulary.size();
    }

    public Map<String, ClassValues> getClasses() {
        return classes;
    }
}

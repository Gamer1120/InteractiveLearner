package classifier;

import java.util.Collection;

public interface Classifier {

    /**
     * Classify the given text.
     *
     * @param words - a tokenized version of the text
     * @return the name of the classification
     */
    String classify(String[] words);

    /**
     * Add a document to the training set.
     *
     * @param document - the document to be added
     */
    void add(Document document);

    /**
     * Adds all documents to the training set.
     *
     * @param documents - the documents to be added
     */
    void addAll(Collection<Document> documents);
}

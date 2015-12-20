package classifier;

import java.util.Collection;
import java.util.Set;

public interface Classifier {

    /**
     * Classify the given text.
     *
     * @param text - the text
     * @return the name of the classification
     */
    String classify(String text);

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

    /**
     * Returns all known classes
     *
     * @return a set of the classes
     */
    Set<String> getClasses();
}

package classifier;

import model.Document;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface Classifier {

    /**
     * Classify the given text.
     * Requires the classifier to be trained first.
     *
     * @param text - the text
     * @return the name of the category
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
     * Trains the classifier using the current training set.
     */
    void train();

    /**
     * Get the known categories.
     *
     * @return a set of the known categories or null if the classifier hasn't been trained yet
     */
    Set<String> getCategories();

    /**
     * Get the training set.
     *
     * @return the training set
     */
    List<Document> getTrainingSet();
}

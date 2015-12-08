package testing;

import classifier.Classifier;
import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;

import java.util.ArrayList;
import java.util.Collection;

public class Tester {
    private Classifier classifier;
    private Collection<Document> testDocuments;

    public Tester() {
        classifier = new MultinomialNaiveBayesClassifier();
        testDocuments = new ArrayList<>();
    }

    public void add(Document document, boolean training) {
        if (training) {
            classifier.add(document);
        } else {
            testDocuments.add(document);
        }
    }

    public void addAll(Collection<Document> documents, boolean training) {
        if (training) {
            classifier.addAll(documents);
        } else {
            testDocuments.addAll(documents);
        }
    }

    public void test() {
        int correct = 0;
        int incorrect = 0;
        for (Document document : testDocuments) {
            if (document.classification.equals(classifier.classify(document.text))) {
                correct++;
            } else {
                incorrect++;
            }
        }
        System.out.println("correct = " + correct + ", incorrect = " + incorrect);
    }
}

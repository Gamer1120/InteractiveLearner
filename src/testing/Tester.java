package testing;

import classifier.Classifier;
import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;
import fileparser.ClassFolder;
import fileparser.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static final double TRAINING_PERCENTAGE = 0.50;

    public static void test(ClassFolder... classFolders) {
        Classifier classifier = new MultinomialNaiveBayesClassifier();
        List<Document> testDocuments = new ArrayList<>();
        for (ClassFolder classFolder : classFolders) {
            List<Document> documents = FileUtils.readDocuments(classFolder);
            int trainingSetSize = (int) (TRAINING_PERCENTAGE * documents.size());
            classifier.addAll(documents.subList(0, trainingSetSize));
            testDocuments.addAll(documents.subList(trainingSetSize, documents.size()));
        }
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

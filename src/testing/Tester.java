package testing;

import classifier.Classifier;
import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;
import fileparser.ClassFolder;
import fileparser.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    private Classifier classifier;
    private List<Document> testDocuments;
    private double trainingPercentage;

    public Tester(double trainingPercentage, ClassFolder... classFolders) {
        this.trainingPercentage = trainingPercentage;
        classifier = new MultinomialNaiveBayesClassifier();
        testDocuments = new ArrayList<>();
        add(classFolders);
    }

    public void add(ClassFolder... classFolders) {
        for (ClassFolder classFolder : classFolders) {
            List<Document> documents = FileUtils.readDocuments(classFolder);
            int trainingSetSize = (int) (trainingPercentage * documents.size());
            classifier.addAll(documents.subList(0, trainingSetSize));
            testDocuments.addAll(documents.subList(trainingSetSize, documents.size()));
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

package testing;

import classifier.Classifier;
import fileparser.Document;
import fileparser.EmailConverter;

public class EmailTester {

    public EmailTester() {
        EmailConverter emailConverter = new EmailConverter();
        Classifier classifier = new Classifier();
        emailConverter.readDocuments();
        classifier.setTrainingSet(emailConverter.getTrainingSet());
        int correct = 0;
        int incorrect = 0;
        for (Document document : emailConverter.getTestSet()) {
            if (document.getClassification().equals(classifier.classify(document.getText()))) {
                correct++;
            } else {
                incorrect++;
            }
        }
        System.out.println("correct = " + correct + ", incorrect = " + incorrect);
    }

    public static void main(String[] args) {
        new EmailTester();
    }

}

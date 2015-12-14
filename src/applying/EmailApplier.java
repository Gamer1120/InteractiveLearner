package applying;

import classifier.Classifier;
import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;
import fileparser.FileUtils;

import java.util.List;

public class EmailApplier {
    // The percentage of documents to be used for training
    private static final double TRAINING_PERCENTAGE = 0.10;

    private Applier applier;

    public EmailApplier(Classifier classifier) {
        applier = new Applier(classifier);
        add(FileUtils.readDocuments("db/emails/ham", "ham"));
        add(FileUtils.readDocuments("db/emails/spam", "spam"));
    }

    public static void main(String[] args) {
        new EmailApplier(new MultinomialNaiveBayesClassifier());
    }

    private void add(List<Document> documents) {
        // Calculate the amount of documents for training using the training percentage
        int trainingSetSize = (int) (TRAINING_PERCENTAGE * documents.size());
        applier.trainAll(documents.subList(0, trainingSetSize));
        for (Document document : documents.subList(trainingSetSize, documents.size())) {
            applier.add(document.getText());
        }
    }

    public void train(Document document) {
        applier.train(document);
        applier.reClassify();
    }

    public List<Document> getDocuments() {
        return applier.getDocuments();
    }
}

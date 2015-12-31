package applying;

import classifier.Classifier;
import utils.Utils;

import java.util.List;

public class EmailApplier {
    // The percentage of documents to be used for training
    private static final double TRAINING_PERCENTAGE = 0.10d;

    public static Applier apply(Classifier classifier) {
        Applier applier = new Applier(classifier);
        add(applier, "ham", Utils.readFiles("db/emails/ham"));
        add(applier, "spam", Utils.readFiles("db/emails/spam"));
        return applier;
    }

    private static void add(Applier applier, String category, List<String> texts) {
        // Calculate the amount of documents for training using the training percentage
        int trainingSetSize = (int) (TRAINING_PERCENTAGE * texts.size());
        applier.trainAll(Utils.toDocuments(texts.subList(0, trainingSetSize), category));
        applier.addAll(texts.subList(trainingSetSize, texts.size()));
    }
}

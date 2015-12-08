package testing;

import classifier.Document;
import fileparser.ClassFolder;
import fileparser.FileUtils;

import java.util.List;

public class EmailTester {
    private static final double TRAINING_PERCENTAGE = 0.50;

    public static void main(String[] args) {
        Tester tester = new Tester();
        add(tester, FileUtils.readDocuments(new ClassFolder("db/emails/ham", "ham")));
        add(tester, FileUtils.readDocuments(new ClassFolder("db/emails/spam", "spam")));
        tester.test();
    }

    private static void add(Tester tester, List<Document> documents) {
        int trainingSetSize = (int) (TRAINING_PERCENTAGE * documents.size());
        tester.addAll(documents.subList(0, trainingSetSize), true);
        tester.addAll(documents.subList(trainingSetSize, documents.size()), false);
    }
}

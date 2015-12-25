package applying;

import classifier.Classifier;
import classifier.Document;
import fileparser.FileUtils;

import java.util.Collection;

public class BlogApplier {

    public static Applier apply(Classifier classifier) {
        Applier applier = new Applier(classifier);
        applier.trainAll(FileUtils.readDocuments("db/blogs/F/train", "female"));
        applier.trainAll(FileUtils.readDocuments("db/blogs/M/train", "male"));
        add(applier, FileUtils.readDocuments("db/blogs/F/test", "female"));
        add(applier, FileUtils.readDocuments("db/blogs/M/test", "male"));
        return applier;
    }

    private static void add(Applier applier, Collection<Document> documents) {
        documents.stream().map(Document::getText).forEach(applier::add);
    }
}

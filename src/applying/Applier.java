package applying;

import classifier.Classifier;
import classifier.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Applier {
    private Classifier classifier;
    private List<Document> documents;

    public Applier(Classifier classifier) {
        documents = new ArrayList<>();
        this.classifier = classifier;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void reClassify() {
        for (Document document : documents) {
            document.setClassification(classifier.classify(document.getText()));
        }
    }

    public void add(String text) {
        documents.add(new Document(text, classifier.classify(text)));
    }

    public void addAll(Collection<String> texts) {
        texts.forEach(this::add);
    }

    public void train(Document document) {
        classifier.add(document);
    }

    public void trainAll(Collection<Document> documents) {
        classifier.addAll(documents);
    }
}

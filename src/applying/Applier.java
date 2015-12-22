package applying;

import classifier.Classifier;
import classifier.Document;

import java.io.Serializable;
import java.util.*;

public class Applier implements Serializable{
    private Classifier classifier;
    private Map<String, List<String>> documents;

    public Applier(Classifier classifier) {
        documents = new HashMap<>();
        this.classifier = classifier;
    }

    public Map<String, List<String>> getDocuments() {
        return documents;
    }

    public boolean reClassify() {
        boolean changed = false;
        for (Map.Entry<String, List<String>> entry : documents.entrySet()) {
            String oldClass = entry.getKey();
            List<String> texts = entry.getValue();
            for (Iterator<String> it = texts.iterator(); it.hasNext(); ) {
                String text = it.next();
                String newClass = classifier.classify(text);
                if (!oldClass.equals(newClass)) {
                    it.remove();
                    add(newClass, text);
                    changed = true;
                }
            }
        }
        return changed;
    }

    public void add(String text) {
        add(classifier.classify(text), text);
    }

    private void add(String classification, String text) {
        List<String> texts = documents.get(classification);
        if (texts == null) {
            texts = new LinkedList<>();
            documents.put(classification, texts);
        }
        texts.add(text);
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

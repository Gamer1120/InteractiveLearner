package applying;

import classifier.Classifier;
import model.Document;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Applier implements Serializable {
    private final Classifier classifier;
    private final ConcurrentMap<String, List<String>> documents;

    public Applier(Classifier classifier) {
        this.classifier = classifier;
        documents = new ConcurrentHashMap<>();
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
            texts = addClassification(classification);
        }
        texts.add(text);
    }

    public void addAll(Collection<String> texts) {
        texts.forEach(this::add);
    }

    public List<String> addClassification(String classification) {
        List<String> texts = new LinkedList<>();
        documents.put(classification, texts);
        return texts;
    }

    public void train(Document document) {
        classifier.add(document);
        classifier.train();
    }

    public void trainAll(Collection<Document> documents) {
        classifier.addAll(documents);
        classifier.train();
    }

    public void delete(String category) {
        for (Iterator<Document> iterator = classifier.getTrainingSet().iterator(); iterator.hasNext(); ) {
            if (category.equals(iterator.next().getCategory())) {
                iterator.remove();
            }
        }
        reClassify();
        documents.remove(category);
    }
}

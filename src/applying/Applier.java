package applying;

import classifier.Classifier;
import model.Document;

import java.io.Serializable;
import java.util.*;

public class Applier implements Serializable {
    private final Classifier classifier;
    private final Map<String, List<String>> documents;

    public Applier(Classifier classifier) {
        this.classifier = classifier;
        documents = new HashMap<>();
    }

    public Map<String, List<String>> getDocuments() {
        return documents;
    }

    public boolean reClassify() {
        Map<String, List<String>> tempDocuments = new HashMap<>();
        documents.forEach((oldCategory, texts) -> {
            for (Iterator<String> it = texts.iterator(); it.hasNext(); ) {
                String text = it.next();
                String newCategory = classifier.classify(text);
                if (!oldCategory.equals(newCategory)) {
                    it.remove();
                    List<String> tempTexts = tempDocuments.get(newCategory);
                    if (tempTexts == null) {
                        tempTexts = new LinkedList<>();
                        tempDocuments.put(newCategory, tempTexts);
                    }
                    tempTexts.add(text);
                }
            }
        });
        tempDocuments.forEach((category, texts) -> texts.forEach(text -> add(category, text)));
        return !tempDocuments.isEmpty();
    }

    public void add(String text) {
        add(classifier.classify(text), text);
    }

    private void add(String category, String text) {
        List<String> texts = documents.get(category);
        if (texts == null) {
            texts = addCategory(category);
        }
        texts.add(text);
    }

    public void addAll(Collection<String> texts) {
        texts.forEach(this::add);
    }

    public List<String> addCategory(String category) {
        List<String> texts = new LinkedList<>();
        documents.put(category, texts);
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

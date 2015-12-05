package classifier;

import fileparser.Document;
import utils.MutableLogarithmicDouble;

import java.util.HashMap;
import java.util.Map;

public class Classifier {
    private TrainingSet trainingSet;

    public Classifier() {
        trainingSet = new TrainingSet();
    }

    public String classify(String... words) {
        Map<String, MutableLogarithmicDouble> conProb = new HashMap<>();
        trainingSet.getClasses().forEach((k, v) -> conProb.put(k, new MutableLogarithmicDouble((double) v.getDocumentCount() / (double) trainingSet.getDocumentCount())));
        for (String word : words) {
            if (trainingSet.inVocabulary(word)) {
                trainingSet.getClasses().forEach((k, v) -> conProb.get(k).add((double) (v.getIndividualWordCount(word) + 1) / (double) (v.getTotalWordCount() + trainingSet.getVocabularySize())));
            }
        }
        return conProb.entrySet().stream().max((entry1, entry2) -> entry1.getValue().toDouble() > entry2.getValue().toDouble() ? 1 : -1).get().getKey();
    }

    public void addTrainingDocument(Document document) {
        trainingSet.add(document);
    }

    public void setTrainingSet(TrainingSet trainingSet) {
        this.trainingSet = trainingSet;
    }
}

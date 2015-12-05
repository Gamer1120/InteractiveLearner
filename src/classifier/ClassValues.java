package classifier;

import utils.MutableInt;

import java.util.HashMap;
import java.util.Map;

public class ClassValues {
    // Amount of documents
    private int documentCount;
    // Amount of words
    private int totalWordCount;
    // Number of times a word occurs
    private Map<String, MutableInt> individualWordCount;

    public ClassValues() {
        documentCount = 0;
        totalWordCount = 0;
        individualWordCount = new HashMap<>();
    }

    public void addDocument() {
        ++documentCount;
    }

    public void addWord(String word) {
        ++totalWordCount;
        MutableInt count = individualWordCount.get(word);
        if (count == null) {
            individualWordCount.put(word, new MutableInt());
        } else {
            count.increment();
        }
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public int getTotalWordCount() {
        return totalWordCount;
    }

    public int getIndividualWordCount(String word) {
        return individualWordCount.get(word).toInt();
    }
}

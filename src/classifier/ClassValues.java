package classifier;

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

    public void addDocuments(int amount) {
        documentCount += amount;
    }

    public void addWords(String... words) {
        totalWordCount += words.length;
        for (String word : words) {
            MutableInt count = individualWordCount.get(word);
            if (count == null) {
                individualWordCount.put(word, new MutableInt());
            } else {
                count.increment();
            }
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

    class MutableInt {
        int value = 1; // note that we start at 1 since we're counting

        public void increment() {
            ++value;
        }

        public int toInt() {
            return value;
        }
    }
}

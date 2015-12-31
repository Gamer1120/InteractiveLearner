package classifier;

import fileparser.FileUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TempFeatureSelection implements Serializable {
    // Set that contains stopwords
    private Set<String> stopWords;

    /**
     * Class that contains multiple feature selection methods with toggles to turn them on or of
     */
    public TempFeatureSelection() {
        this(true);
    }

    public TempFeatureSelection(boolean stopWords) {
        if (stopWords) {
            this.stopWords = readStopWords();
        }
    }

    /**
     * Reads the file with stop words.
     *
     * @return a set with all the stop words
     */
    private static Set<String> readStopWords() {
        String[] stopWordsArray = FileUtils.fileToString("db/common-english-words.txt").split(",");
        Set<String> stopWordsSet = new HashSet<>(stopWordsArray.length);
        Collections.addAll(stopWordsSet, stopWordsArray);
        return stopWordsSet;
    }

    /**
     * Checks if a word should be used in the vocabulary using feature selection
     *
     * @param word - the word to be checked
     * @return true if the word should be used
     */
    public boolean select(String word) {
        return !(stopWords != null && stopWords.contains(word));
    }

}

package fileparser;

import classifier.Document;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.*;

public class FileUtils {
    private static final String ENCODING = "UTF-8";
    private static final HashSet<String> STOP_WORDS = readStopWords();

    /**
     * Returns all documents in a given folder and gives them the specified classification.
     *
     * @param path           - the path to the folder
     * @param classification - the name of the classification
     * @return all documents as a list of Documents
     */
    public static List<Document> readDocuments(String path, String classification) {
        List<Document> documents = new ArrayList<>();
        // Get all files in the directory
        File[] files = new File(path).listFiles();
        if (files != null) {
            // For all files
            for (File file : files) {
                // Read the file
                String text = fileToString(file.getPath());
                // Create a document with the text and classification and add it to the list
                documents.add(new Document(text, classification));
            }
        }
        return documents;
    }

    /**
     * Makes a String with the contents of the specified text file.
     *
     * @param path - the Path to the text file
     * @return a String with the contents of the specified text file
     */
    public static String fileToString(String path) {
        byte[] bytes;
        try {
            // Try to read all bytes of the file.
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        try {
            // Create a String out of those bytes.
            return new String(bytes, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(bytes);
        }
    }

    /**
     * Tokenizes and normalizes a given String.
     *
     * @param line - the String to be tokenized and normalized
     * @return the tokenized and normalized String
     */
    public static String[] tokenize(String line) {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(Normalizer.normalize(line, Normalizer.Form.NFD).toLowerCase().replaceAll("[^ a-z]", "").split("\\s+")));
        for (Iterator<String> it = tokens.iterator(); it.hasNext(); ) {
            if (STOP_WORDS.contains(it.next())) {
                it.remove();
            }
        }
        // Normalize the String, make the String lowercase, remove all non-alphabetic characters, and tokenize the String.
        return tokens.toArray(new String[tokens.size()]);
    }

    public static HashSet<String> readStopWords() {
        String[] stopWordsArray = fileToString("db/common-english-words.txt").split(",");
        HashSet<String> stopWordsSet = new HashSet<>(stopWordsArray.length);
        Collections.addAll(stopWordsSet, stopWordsArray);
        return stopWordsSet;
    }
}

package fileparser;

import classifier.Document;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String ENCODING = "UTF-8";

    /**
     * Returns all documents in a given folder and gives them the specified classification.
     *
     * @param path      - the path to the folder
     * @param className - the name of the classification
     * @return all documents as a list of Documents
     */
    public static List<Document> readDocuments(String path, String className) {
        List<Document> documents = new ArrayList<>();
        // Get all files in the directory.
        File[] directoryListing = new File(path).listFiles();
        if (directoryListing != null) {
            for (File currentFile : directoryListing) {
                // Make a tokenized and normalized String array from all files.
                String[] text = tokenizer(fileToString(currentFile.getPath()));
                // Add the String array and it's classification to the list of Documents.
                documents.add(new Document(text, className));
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
        byte[] encoded;
        try {
            // Try to read all bytes of the file.
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        try {
            // Create a String out of those bytes.
            return new String(encoded, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(encoded);
        }
    }

    /**
     * Tokenizes and normalizes a given String.
     *
     * @param line - the String to be tokenized and normalized
     * @return the tokenized and normalized String
     */
    public static String[] tokenizer(String line) {
        // Normalize the String, make the String lowercase, remove all non-alphabetic characters, and tokenize the String.
        return Normalizer.normalize(line, Normalizer.Form.NFD).toLowerCase().replaceAll("[^ a-z]", "").split("\\s+");
    }
}

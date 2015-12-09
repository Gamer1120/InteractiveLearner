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
     * Returns all classes in a given ClassFolder as a List of Documents.
     *
     * @param classFolder - the ClassFolder the classes are in.
     * @return all classes in a given ClassFolder as a list of Documents.
     */
    public static List<Document> readDocuments(ClassFolder classFolder) {
        List<Document> documents = new ArrayList<>();
        File[] directoryListing = new File(classFolder.path).listFiles();
        if (directoryListing != null) {
            for (File currentFile : directoryListing) {
                String[] text = tokenizer(fileToString(currentFile.getPath()));
                documents.add(new Document(text, classFolder.name));
            }
        }
        return documents;
    }

    /**
     * Makes a String with the contents of the specified text file.
     *
     * @param path the Path to the text file.
     * @return a String with the contents of the specified text file.
     */
    public static String fileToString(String path) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(encoded, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tokenizes and normalizes a given String.
     *
     * @param line the String to be tokenized and normalized.
     * @return the tokenized and normalized String.
     */
    public static String[] tokenizer(String line) {
        // Credits for this function go to David Conrad, http://stackoverflow.com/questions/3322152/is-there-a-way-to-get-rid-of-accents-and-convert-a-whole-string-to-regular-lette
        // Some adjustments have been made to his code by us.
        char[] out = new char[line.length()];
        line = Normalizer.normalize(line, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = line.length(); i < n; ++i) {
            char c = line.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out).toLowerCase().replaceAll("[^ a-z]", "").split("\\s+");
    }
}

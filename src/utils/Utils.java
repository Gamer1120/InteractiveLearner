package utils;

import applying.Applier;
import model.Document;
import model.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String FILE_NAME = "applier.obj";
    private static final String ENCODING = "UTF-8";

    /**
     * Reads the applier from the specified location.
     *
     * @return the applier
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws ClassCastException
     */
    public static Applier readApplier() throws IOException, ClassNotFoundException, ClassCastException {
        // Create an input stream
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME));
        // Read the applier
        Applier applier = (Applier) in.readObject();
        // Close the input stream
        in.close();
        return applier;
    }

    /**
     * Write the applier to the specified location.
     *
     * @param applier  - the applier
     * @throws IOException
     */
    public static void writeApplier(Applier applier) throws IOException {
        // Create an output stream
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        // Write the applier
        out.writeObject(applier);
        // Close the output stream
        out.close();
    }

    /**
     * Creates a list of documents from a collection of strings.
     *
     * @param strings  - the collection of strings
     * @param category - the category to be added to the strings
     * @return all documents as a list
     */
    public static List<Document> toDocuments(Collection<String> strings, String category) {
        // For all strings
        return strings.stream()
                // Create a document with the string and category
                .map(string -> new Document(string, category))
                // Collect the documents as list
                .collect(Collectors.toList());
    }

    /**
     * Creates a list of texts from a collection of strings.
     *
     * @param strings  - the collection of strings
     * @param category - the category to be added to the strings
     * @return all texts as a list
     */
    public static List<Text> toTexts(Collection<String> strings, String category) {
        // For all strings
        return strings.stream()
                // Create a text with the string and category
                .map(string -> new Text(string, category))
                // Collect the texts as list
                .collect(Collectors.toList());
    }

    /**
     * Reads all texts in a given folder.
     *
     * @param path - the path to the folder
     * @return all texts as a list of strings
     */
    public static List<String> readFiles(String path) {
        // Get all files in the directory
        File[] files = new File(path).listFiles();
        if (files != null) {
            // For all files
            return Arrays.stream(files)
                    // Get the path
                    .map(File::getPath)
                    // Read the file
                    .map(Utils::fileToString)
                    // Collect the files as list
                    .collect(Collectors.toList());
        } else {
            // No files found, return an empty list
            return Collections.emptyList();
        }
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
            // Try to read all bytes of the file
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        try {
            // Create a String out of the bytes with the specified encoding
            return new String(bytes, ENCODING);
        } catch (UnsupportedEncodingException e) {
            // Can't use the specified encoding
            e.printStackTrace();
            // Create a String out of the bytes with the standard encoding
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
        // Normalize the String
        return Normalizer.normalize(line, Normalizer.Form.NFD)
                // Make the String lowercase
                .toLowerCase()
                // Remove all non-alphabetic characters
                .replaceAll("[^ a-z]", "")
                // Tokenize the String
                .split("\\s+");
    }
}

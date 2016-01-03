package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileWriter {
    private final List<String> lines;
    private final Path path;

    /**
     * Creates a new FileWriter object, with a list of lines and a path to write to.
     * This class is used to write chiSquare values to a File.
     *
     * @param path - the path to the File
     */
    public FileWriter(String path) {
        this.path = Paths.get(path);
        lines = new ArrayList<>();
    }

    /**
     * Adds a formatted chiSquare output to the list of lines
     *
     * @param word      - the word to be written
     * @param chiSquare - the chiSquare value for that word
     */
    public void add(String word, double chiSquare) {
        lines.add(word + ": " + chiSquare);
    }

    /**
     * Writes the list of lines to the path.
     *
     * @throws IOException - if the write operation failed
     */
    public void flush() throws IOException {
        Files.write(path, lines);
    }
}

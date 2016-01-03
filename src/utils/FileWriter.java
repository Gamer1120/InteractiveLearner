package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {

    // The File to write to
    private File file;
    // The PrintWriter that is used to write to the file
    private PrintWriter writer;

    /**
     * Creates a new FileWriter object, opening a File and creating a writer to that File.
     * This class is used to write chiSquare values to a File without opening and closing it thousands of times.
     *
     * @param filename the filename of the File.
     */
    public FileWriter(String filename) {
        file = new File(filename);
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the file. Should be called when done writing to it.
     */
    public void close(){
        writer.close();
    }

    /**
     * Writes a formatted chiSquare output to a file
     * @param word the word to be written.
     * @param chiSquare the chiSquare value for that word.
     */
    public void write(String word, double chiSquare){
        writer.write(word + ": " + chiSquare + "\n");
    }
}

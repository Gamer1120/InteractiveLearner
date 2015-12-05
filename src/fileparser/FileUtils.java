package fileparser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Michael on 5-12-2015.
 */
public class FileUtils {

    private static final String ENCODING = "UTF-8";

    public static LinkedList<Document> readFolder(String path, String classification) {
        LinkedList<Document> result = new LinkedList<>();
        try {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    result.add(new Document(tokenizer(fileToString(filePath.toString())), classification));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

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
        }
        return null;
    }

    public static String[] tokenizer(String line) {
        // Credits for this function go to David Conrad, http://www.unknownerror.org/opensource/adamschwartz/letters/q/stackoverflow/3322152/is-there-a-way-to-get-rid-of-accents-and-convert-a-whole-string-to-regular-lette
        // Some adjustments have been made to his code by us.
        line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        char[] out = new char[line.length()];
        line = Normalizer.normalize(line, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = line.length(); i < n; ++i) {
            char c = line.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out).split("\\s+");
    }

}

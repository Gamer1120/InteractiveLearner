package fileparser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class EmailConverter implements DocumentConverter {

    private static final String ENCODING = "UTF-8";

    private List<Document> documents;

    public static void main(String[] args) {
        new EmailConverter().readDocuments();
    }

    @Override
    public void readDocuments() {
        this.documents = new LinkedList<>();
        try {
            Files.walk(Paths.get("db/emails/ham")).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    System.out.println("START OF FILE");
                    System.out.println(fileToString(filePath.toString()));
                    System.out.println("END OF FILE");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String fileToString(String path) {
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

    @Override
    public HashMap<Document, Integer> getDocuments() {
        return null;
    }
}

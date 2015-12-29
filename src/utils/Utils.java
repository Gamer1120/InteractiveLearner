package utils;

import applying.Applier;

import java.io.*;

public class Utils {
    public static final String FILE_NAME = "applier.obj";

    public static Applier readApplier(String fileName) throws IOException, ClassNotFoundException, ClassCastException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
        Applier applier = (Applier) in.readObject();
        in.close();
        return applier;
    }

    public static void writeApplier(Applier applier, String fileName) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(applier);
        out.close();
    }
}

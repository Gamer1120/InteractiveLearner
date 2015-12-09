package fileparser;

public class ClassFolder {
    public final String path;
    public final String name;

    /**
     * A folder containing text files of the specified class.
     *
     * @param path - the path to the folder
     * @param name - the name of the class
     */
    public ClassFolder(String path, String name) {
        this.path = path;
        this.name = name;
    }
}

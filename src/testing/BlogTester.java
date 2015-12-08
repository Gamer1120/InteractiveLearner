package testing;

import fileparser.ClassFolder;
import fileparser.FileUtils;

public class BlogTester {
    public static void main(String[] args) {
        Tester tester = new Tester();
        tester.addAll(FileUtils.readDocuments(new ClassFolder("db/blogs/F/train", "female")), true);
        tester.addAll(FileUtils.readDocuments(new ClassFolder("db/blogs/M/train", "male")), true);
        tester.addAll(FileUtils.readDocuments(new ClassFolder("db/blogs/F/test", "female")), false);
        tester.addAll(FileUtils.readDocuments(new ClassFolder("db/blogs/M/test", "male")), false);
        tester.test();
    }
}

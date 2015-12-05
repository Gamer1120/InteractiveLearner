package testing;

import fileparser.ClassFolder;

public class BlogTester {
    public static void main(String[] args) {
        Tester.test(new ClassFolder("db/blogs/F", "female"), new ClassFolder("db/blogs/M", "male"));
    }
}

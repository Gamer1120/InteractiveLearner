package testing;

import fileparser.ClassFolder;

public class BlogTester {
    public static void main(String[] args) {
        Tester tester = new Tester(0.50, new ClassFolder("db/blogs/F", "female"), new ClassFolder("db/blogs/M", "male"));
        tester.test();
    }
}

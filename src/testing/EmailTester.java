package testing;

import fileparser.ClassFolder;

public class EmailTester {
    public static void main(String[] args) {
        Tester tester = new Tester(0.50, new ClassFolder("db/emails/ham", "ham"), new ClassFolder("db/emails/spam", "spam"));
        tester.test();
    }
}

package testing;

import fileparser.ClassFolder;

public class EmailTester {
    public static void main(String[] args) {
        Tester.test(new ClassFolder("db/emails/ham", "ham"), new ClassFolder("db/emails/spam", "spam"));
    }
}

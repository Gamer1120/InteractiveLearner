# Interactive Learner
This is our Interactive Learner. It was made as an assignment for the Artificial Intelligence course. 

## Running the classifier on the blogs
Compile all files, and run src/testing/EmailTester.java.

## Running the classifier on the e-mails
Compile all files, and run src/testing/BlogTester.java.

## Credits
The list of stopwords as found in db/common-english-words.txt was found at http://www.textfixer.com/resources/common-english-words.txt

## FAQ

### Where can I find the classifier?
You can find our Multinomial Naive Bayes Classifier under src/classifier/MultinomialNaiveBayesClassifier.java.

### How are the files read?
Read the commentary in src/fileparser/FileUtils.java.

### How can I change what files are used for training, and testing?
Just put the files in the correct directory. For example, if you want to add some female blogs as test set, move it to db/blogs/F/test. The new file will be used the next time you run the tester. For the e-mails, it wasn't given which ones to use as training, and which one as test sets, so by default, the first 10% are used as training set, and the others as test set, instead of using folders.

### Can I also use custom sets to classify?
Of course! If for example you have some jokes, which can be classified as either good or bad, just put each classification in a different folder. Then, create a Java class in the src/testing package, for example called JokeTester.java. You can base your implementation on the existing classes in that package. 

### What are the minimal system requirements for this classifier?
It can run on nearly any computer, but for optimal results, please keep the following in mind:

Java: Java 8, refer to https://www.java.com/sysreq for the minimal requirements for Java 8

RAM: 512 MB

Free Disk Space: 20 MB

### How do I install the classifier?
There is no installation required. To run the classifier, see "How do I run the classifier?".

### How do I run the classifier?
Go to the build folder, and execute ApplierTUI.jar to open our TUI, ApplierGUI.jar to open our GUI, and BlogTester.jar to test the blogs and print the results

### Where do I put the documents to classify?
For emails:
Ham: db/emails/ham/
Spam: db/emails/spam
The classifier will automatically use the first 10% as trainingset, and the other 90% as testset.
For blogs:
Trainingset female: db/blogs/F/train
Testset female: db/blogs/F/test
Trainingset male: db/blogs/M/train
Testset male: db/blogs/M/test
Please note that the GUI and TUI use the blogs by default.

### How can I test different documents, for example jokes?
1. Create a folder in db named jokes.
2. Inside that folder, create a folder for each class, for example db/jokes/good and db/jokes/bad.
3. Inside of each of those folders, create two folders named train and test.
4. Put the documents you have in the appropriate folders.
5. Backup the applier.obj in the root folder if there's any. Then delete it. This file is used to save classifications inbetween sessions.
6. Open your favorite IDE. You will need to edit the source code for this to work.
7. Make a copy of src/testing/BlogTester.java in that same folder and name it JokeTester.java.
8. Open this file and change "public class BlogTester" to "public class JokeTester". Edit the lines that start with "tester." such that they point to the folders that you added. In case you added more than two classes, you will need to add some more "tester." lines.
9. Optional: Edit the line "Tester tester = new Tester(new MultinomialNaiveBayesClassifier());". To use a Binomial classifier, change "Multinomial" to "Binomial". To use different options for feature selection than the default, optimized values, add 3 arguments between the brackets after "MultinomialNaiveBayesClassifier". The first argument is whether stop words should be filtered, the second to remove words that occur very frequently or almost never at all, and the third is to apply chi-square. In case all three need to be turned off, this would become: "Tester tester = new Tester(new MultinomialNaiveBayesClassifier(false, false, false));". In case we want to enable chi-square, but none of the others, we would make it: "Tester tester = new Tester(new MultinomialNaiveBayesClassifier(false, false, true));"
10. Now, compile and execute JokeTester.java and wait a bit for the documents to be processed. Afterwards you should be able to see how many documents were correctly classified, and how many incorrectly.

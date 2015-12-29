# BayesianClassifier
This is our Bayesian Classifier. It was made as an assignment for the Artificial Intelligence course. 

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

Java: Java 8

CPU: Pentium III/Athlon or better

CPU Speed: 1.0 GHz

RAM: 256 MB

OS:	Windows 7 or higher, Mac OSX, Linux. It may work on others, but no guarantees.

Sound Card:	No

Free Disk Space: ??? MB (//TODO)

### How do I install the classifier?
There is no installation required. To run the classifier, see "How do I run the classifier?".

### How do I run the classifier?
Go to ??? (//TODO) and run the ???.jar (//TODO) file.


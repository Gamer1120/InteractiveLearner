package gui;

import applying.Applier;
import applying.EmailApplier;
import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;

import javax.print.Doc;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApplierTUI extends Thread {

    private boolean loop = true;

    private Applier applier;
    private Status status;
    private String currentDocument;

    public ApplierTUI() {
        status = Status.STARTING;
        init();
        showMainMenu();
    }

    public static void main(String[] args) {
        new ApplierTUI();
    }

    public void init() {
        applier = EmailApplier.apply(new MultinomialNaiveBayesClassifier());
        start();
    }

    public void showMainMenu() {
        System.out.println("The following classes are in the current set: ");
        Map<String, List<String>> documents = applier.getDocuments();
        String example = "";
        for (String classValue : documents.keySet()) {
            int size = documents.get(classValue).size();
            System.out.println(classValue + " - " + documents.get(classValue).size());
            if (example.equals("")) {
                example = classValue + " " + (int) (Math.random() * size);
            }
        }
        System.out.println("Please enter the classification and the number of the document you'd like to review. For example: " + example);
        status = Status.MAINMENU;
    }

    public void showReviewMenu(String classification) {
        System.out.println("This document was classified as " + classification + ". Would you like to change it? [y/N]");
        status = Status.REVIEWING;
    }

    public void showClassificationMenu() {
        System.out.println("What would you like to classify this document as? Your options are:");
        applier.getDocuments().keySet().forEach(System.out::println);
        status = Status.CLASSIFYING;
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        while (loop) {
            String[] line = scan.nextLine().split(" ");
            switch (status) {
                case STARTING:
                    break;
                case MAINMENU:
                    if (line.length == 2) {
                        int index;
                        List<String> documents = applier.getDocuments().get(line[0]);
                        if (documents != null) {
                            try {
                                index = Integer.parseInt(line[1]);
                            } catch (NumberFormatException e) {
                                System.out.println("The second argument wasn't a number or too large.");
                                break;
                            }
                            if (index < documents.size()) {
                                currentDocument = documents.get(index);
                                System.out.println(currentDocument);
                                showReviewMenu(line[0]);
                            } else {
                                System.out.println("There aren't that many documents.");
                            }
                        } else {
                            System.out.println("That's not a classification.");
                        }
                    } else {
                        System.out.println("Wrong number of arguments.");
                    }
                    break;
                case REVIEWING:
                    if (line.length == 1) {
                        switch (line[0].toLowerCase()) {
                            case "y":
                                showClassificationMenu();
                                break;
                            case "n":
                                showMainMenu();
                                break;
                            default:
                                System.out.println("Please answer with y/N.");
                                break;
                        }
                    } else {
                        System.out.println("Please answer with y/N.");
                    }
                    break;
                case CLASSIFYING:
                    if (line.length == 1) {
                        if (applier.getDocuments().keySet().contains(line[0])) {
                            String classification = line[0];
                            Document document = new Document(currentDocument, classification);
                            applier.train(document);
                            applier.reClassify();
                            System.out.println("Classification updated! Please note that it may take multiple times before a document becomes classified as your chosen classification.");
                            showMainMenu();
                        } else {
                            System.out.println("That's not a valid classification.");
                        }
                    } else {
                        System.out.println("Please enter one word.");
                    }
                    break;
            }
        }
    }

    private enum Status {
        STARTING, MAINMENU, REVIEWING, CLASSIFYING
    }

}

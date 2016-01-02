package ui;

import applying.Applier;
import applying.BlogApplier;
import classifier.MultinomialNaiveBayesClassifier;
import model.Document;
import utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ApplierTUI extends Thread {

    private Applier applier;
    private Status status;
    private String currentDocument;

    public ApplierTUI() {
        status = Status.STARTING;
        init();
        start();
    }

    public static void main(String[] args) {
        new ApplierTUI();
    }

    public void init() {
        try {
            applier = Utils.readApplier();
            System.out.println("Applier read successfully");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            applier = BlogApplier.apply(new MultinomialNaiveBayesClassifier());
            System.out.println("Using new applier");
        }
    }

    private void showMainMenu() {
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

    private void showReviewMenu(String classification) {
        System.out.println("This document was classified as " + classification + ". Would you like to change it? [y/N]");
        status = Status.REVIEWING;
    }

    private void showClassificationMenu() {
        System.out.println("What would you like to classify this document as? Your options are:");
        applier.getDocuments().keySet().forEach(System.out::println);
        System.out.println("or type new to create a new category.");
        status = Status.CLASSIFYING;
    }

    private void showNewCategory() {
        System.out.println("What would you like the name of the new category to be?");
        status = Status.NEWCATEGORY;
    }

    public void run() {
        showMainMenu();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String[] line = scan.nextLine().split(" ");
            if (line.length == 1 && line[0].toLowerCase().equals("exit")) {
                try {
                    Utils.writeApplier(applier);
                    System.out.println("Applier successfully saved");
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("Couldn't save applier!");
                    e.printStackTrace();
                }
            }
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
                            do {
                                applier.train(document);
                            } while (!applier.reClassify());
                            System.out.println("Classification updated! Please note that it may take multiple times before a document becomes classified as your chosen classification.");
                            showMainMenu();
                        } else if (line[0].equals("new")) {
                            showNewCategory();
                        } else {
                            System.out.println("That's not a valid classification.");
                        }
                    } else {
                        System.out.println("Please enter one word.");
                    }
                    break;
                case NEWCATEGORY:
                    if (line.length == 1) {
                        Document document = new Document(currentDocument, line[0]);
                        do {
                            applier.train(document);
                        } while (!applier.reClassify());
                        System.out.println("Category created and classification updated! Please note that it may take multiple times before a document becomes classified as your chosen classification.");
                        showMainMenu();
                    } else {
                        System.out.println("Please use one word as the name for the category.");
                    }
                    break;
            }
        }
    }

    private enum Status {
        STARTING, MAINMENU, REVIEWING, CLASSIFYING, NEWCATEGORY
    }

}

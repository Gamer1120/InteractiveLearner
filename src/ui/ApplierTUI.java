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

    private static Applier readApplier() {
        Applier applier;
        try {
            applier = Utils.readApplier();
            System.out.println("Applier read successfully");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            applier = BlogApplier.apply(new MultinomialNaiveBayesClassifier());
            System.out.println("Using new applier");
        }
        return applier;
    }

    private static void writeApplier(Applier applier) {
        try {
            Utils.writeApplier(applier);
            System.out.println("Applier successfully saved");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Couldn't save applier!");
            e.printStackTrace();
        }
    }

    public void init() {
        applier = readApplier();
    }

    @Override
    public void run() {
        showMainMenu();
        Scanner scan = new Scanner(System.in);
        while (true) {
            String[] command = scan.nextLine().split("\\s+");
            if ("exit".equalsIgnoreCase(command[0])) {
                writeApplier(applier);
                break;
            }
            switch (status) {
                case STARTING:
                    break;
                case MAINMENU:
                    mainMenuCommand(command);
                    break;
                case REVIEWING:
                    reviewMenuCommand(command);
                    break;
                case CLASSIFYING:
                    classificationMenuCommand(command);
                    break;
                case NEWCATEGORY:
                    newCategoryCommand(command);
                    break;
            }
        }
    }

    private void showMainMenu() {
        System.out.println("The following classes are in the current set: ");
        Map<String, List<String>> documents = applier.getDocuments();
        String example = "";
        for (Map.Entry<String, List<String>> entry : documents.entrySet()) {
            String category = entry.getKey();
            int size = entry.getValue().size();
            System.out.println(category + " - " + size);
            if ("".equals(example)) {
                example = category + " " + (int) (Math.random() * size);
            }
        }
        System.out.println("Please enter the classification and the number of the document you'd like to review. For example: " + example);
        status = Status.MAINMENU;
    }

    private void mainMenuCommand(String[] command) {
        if (command.length == 2) {
            List<String> documents = applier.getDocuments().get(command[0]);
            if (documents != null) {
                int index;
                try {
                    index = Integer.parseInt(command[1]);
                } catch (NumberFormatException e) {
                    System.out.println("The second argument wasn't a valid number.");
                    return;
                }
                if (index < documents.size()) {
                    currentDocument = documents.get(index);
                    System.out.println(currentDocument);
                    showReviewMenu(command[0]);
                } else {
                    System.out.println("There aren't that many documents.");
                }
            } else {
                System.out.println("That's not a classification.");
            }
        } else {
            System.out.println("Wrong number of arguments.");
        }
    }

    private void showReviewMenu(String classification) {
        System.out.println("This document was classified as " + classification + ". Would you like to change it? [y/N]");
        status = Status.REVIEWING;
    }

    private void reviewMenuCommand(String[] command) {
        if (command.length == 1) {
            switch (command[0].toLowerCase()) {
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
    }

    private void showClassificationMenu() {
        System.out.println("What would you like to classify this document as? Your options are:");
        applier.getDocuments().keySet().forEach(System.out::println);
        System.out.println("or type new to create a new category.");
        status = Status.CLASSIFYING;
    }

    private void classificationMenuCommand(String[] command) {
        if (command.length == 1) {
            if (applier.getDocuments().keySet().contains(command[0])) {
                train(command[0]);
                System.out.println("Classification updated! Please note that it may take multiple times before a document becomes classified as your chosen classification.");
                showMainMenu();
            } else if (command[0].equals("new")) {
                showNewCategory();
            } else {
                System.out.println("That's not a valid classification.");
            }
        } else {
            System.out.println("Please enter one word.");
        }
    }

    private void showNewCategory() {
        System.out.println("What would you like the name of the new category to be?");
        status = Status.NEWCATEGORY;
    }

    private void newCategoryCommand(String[] command) {
        if (command.length == 1) {
            train(command[0]);
            System.out.println("Category created and classification updated! Please note that it may take multiple times before a document becomes classified as your chosen classification.");
            showMainMenu();
        } else {
            System.out.println("Please use one word as the name for the category.");
        }
    }

    private void train(String category) {
        Document document = new Document(currentDocument, category);
        applier.train(document);
        applier.reClassify();
    }

    private enum Status {
        STARTING, MAINMENU, REVIEWING, CLASSIFYING, NEWCATEGORY
    }

}

package ui;

import applying.Applier;
import applying.BlogApplier;
import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Utils;

import java.io.IOException;
import java.util.Collection;

@SuppressWarnings("FieldCanBeLocal")
public class ApplierGui extends Application {
    private static final double SIZE = 250d;
    private static final double SPACING = 10d;
    private static final double MIN_WIDTH = 4d * SIZE + 3d * SPACING;
    private static final double MIN_HEIGHT = SIZE;

    private static final Insets PADDING = new Insets(SPACING);

    private static final String FILE_NAME = "applier.obj";

    private Stage primaryStage;
    private BorderPane root;

    private Applier applier;
    private String applierKey;
    private int applierIndex;

    private HBox center;
    private ListView<String> classes;
    private ObservableList<String> classList;
    private ListView<String> documents;
    private ObservableList<String> documentList;
    private ScrollPane document;
    private Text documentText;

    private HBox bottom;
    private ComboBox<String> classBox;
    private Button addButton;
    private Button trainButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        root = generateRoot();

        applier = getApplier();
        applierKey = null;
        applierIndex = -1;

        center = generateCenter();
        classes = generateClasses();
        documents = generateDocuments();
        document = generateDocument();
        center.getChildren().addAll(classes, documents, document);

        bottom = generateBottom();
        classBox = generateClassBox();
        addButton = generateAddButton();
        trainButton = generateTrainButton();
        bottom.getChildren().addAll(classBox, addButton, trainButton);

        root.setCenter(center);
        root.setBottom(bottom);
        setClasses();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Interactive Learner");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            Utils.writeApplier(applier, FILE_NAME);
            System.out.println("Applier written successfully");
        } catch (IOException e) {
            System.out.println("Couldn't write applier");
        }
    }

    private Applier getApplier() {
        Applier applier;
        try {
            applier = Utils.readApplier(FILE_NAME);
            System.out.println("Applier read successfully");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            applier = BlogApplier.apply(new MultinomialNaiveBayesClassifier());
            System.out.println("Using new applier");
        }
        return applier;
    }

    private void setClasses() {
        setClasses(applier.getDocuments().keySet());
    }

    private void setClasses(Collection<String> classCollection) {
        classList = FXCollections.observableArrayList(classCollection);
        classes.setItems(classList);
        classBox.setItems(classList);
    }

    private void resetClasses() {
        setClasses(FXCollections.emptyObservableList());
    }

    private void setDocuments() {
        if (applierKey != null) {
            setDocuments(applier.getDocuments().get(applierKey));
        } else {
            resetDocuments();
        }
    }

    private void setDocuments(Collection<String> documentCollection) {
        documentList = FXCollections.observableArrayList(documentCollection);
        documents.setItems(documentList);
    }

    private void resetDocuments() {
        setDocuments(FXCollections.emptyObservableList());
    }

    private void setText() {
        if (applierKey != null && applierIndex != -1) {
            setText(applier.getDocuments().get(applierKey).get(applierIndex));
        } else {
            resetText();
        }
    }

    private void setText(String text) {
        documentText.setText(text);
    }

    private void resetText() {
        setText("");
    }

    private void updateClassBox() {
        String selected = classes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            classBox.getSelectionModel().select(selected);
        } else {
            classBox.getSelectionModel().clearSelection();
        }
    }

    private void train(String classification, String text) {
        if (classification != null && text != null && !"".equals(text)) {
            Document document = new Document(text, classification);
            do {
                applier.train(document);
            } while (!applier.reClassify());
            setClasses();
        }
    }

    private BorderPane generateRoot() {
        return new BorderPane();
    }

    private HBox generateCenter() {
        return new HBox();
    }

    private ListView<String> generateClasses() {
        ListView<String> classes = new ListView<>();
        classes.setPrefWidth(SIZE);
        classes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            applierKey = newValue;
            setDocuments();
            updateClassBox();
        });
        return classes;
    }

    private ListView<String> generateDocuments() {
        ListView<String> documents = new ListView<>();
        documents.setPrefWidth(SIZE);
        documents.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            applierIndex = newValue.intValue();
            setText();
            updateClassBox();
        });
        return documents;
    }

    private ScrollPane generateDocument() {
        ScrollPane document = new ScrollPane();
        document.setFitToWidth(true);
        document.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        document.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        document.setPrefViewportWidth(2d * SIZE);
        documentText = generateText("");
        document.setContent(documentText);
        return document;
    }

    private HBox generateBottom() {
        HBox bottom = new HBox(SPACING);
        bottom.setPadding(PADDING);
        bottom.setAlignment(Pos.CENTER_RIGHT);
        return bottom;
    }

    private ComboBox<String> generateClassBox() {
        return new ComboBox<>();
    }

    private Button generateAddButton() {
        Button button = generateButton("New Class");
        Dialog dialog = new Dialog();
        button.setOnAction(event -> dialog.showAndWait());
        return button;
    }

    private Button generateTrainButton() {
        Button button = generateButton("Train");
        button.setOnAction(event -> train(classBox.getSelectionModel().getSelectedItem(), documentText.getText()));
        return button;
    }

    private Button generateButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(100d, 20d);
        return button;
    }

    private Text generateText(String s) {
        Text text = new Text(s);
        text.setWrappingWidth(2d * SIZE);
        return text;
    }

    private class Dialog extends Stage {
        private VBox root;
        private Text text;
        private TextField textField;

        private HBox buttons;
        private Button cancelButton;
        private Button trainButton;

        public Dialog() {
            root = generateRoot();
            text = generateText("Add class:");
            textField = generateTextField();

            buttons = generateButtons();
            cancelButton = generateCancelButton();
            trainButton = generateTrainButton();
            buttons.getChildren().addAll(cancelButton, trainButton);

            root.getChildren().addAll(text, textField, buttons);
            Scene scene = new Scene(root);
            setTitle("Add Class");
            setScene(scene);
            initModality(Modality.WINDOW_MODAL);
            initOwner(primaryStage);
            reset();
        }

        private void reset() {
            textField.setText("");
        }

        private VBox generateRoot() {
            VBox root = new VBox(SPACING);
            root.setPadding(PADDING);
            return root;
        }

        private TextField generateTextField() {
            return new TextField();
        }

        private HBox generateButtons() {
            HBox buttons = new HBox(SPACING);
            buttons.setPadding(PADDING);
            buttons.setAlignment(Pos.CENTER_RIGHT);
            return buttons;
        }

        private Button generateCancelButton() {
            Button cancelButton = generateButton("Cancel");
            cancelButton.setOnAction(event -> close());
            return cancelButton;
        }

        private Button generateTrainButton() {
            Button trainButton = generateButton("Train");
            trainButton.setOnAction(event -> {
                train(textField.getText(), documentText.getText());
                close();
            });
            return trainButton;
        }

        @Override
        public void close() {
            reset();
            super.close();
        }
    }
}

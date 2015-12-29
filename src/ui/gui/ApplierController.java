package ui.gui;

import applying.Applier;
import applying.BlogApplier;
import classifier.Document;
import classifier.FeatureSelection;
import classifier.MultinomialNaiveBayesClassifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ApplierController implements Initializable {
    private static final int MAX_LENGTH = 100;

    @FXML
    private BorderPane root;

    @FXML
    private HBox center;
    @FXML
    private ListView<String> classes;
    @FXML
    private ListView<Text> documents;
    @FXML
    private ScrollPane document;
    @FXML
    private Text documentText;

    @FXML
    private HBox bottom;
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<String> classBox;
    @FXML
    private Button deleteButton;
    @FXML
    private Button trainButton;

    private Applier applier;
    private String applierKey;
    private int applierIndex;

    private static Applier readApplier() {
        Applier applier;
        try {
            applier = Utils.readApplier(Utils.FILE_NAME);
            System.out.println("Applier read successfully");
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            applier = BlogApplier.apply(new MultinomialNaiveBayesClassifier(new FeatureSelection(true)));
            System.out.println("Using new applier");
        }
        return applier;
    }

    private static void writeApplier(Applier applier) {
        try {
            Utils.writeApplier(applier, Utils.FILE_NAME);
            System.out.println("Applier written successfully");
        } catch (IOException e) {
            System.out.println("Couldn't write applier");
        }
    }

    private static Collection<Text> toText(Collection<String> strings) {
        return strings.stream()
                .map(s -> s.replace("\r\n", " "))
                .map(s -> s.length() > MAX_LENGTH ? s.substring(0, MAX_LENGTH).concat("…") : s)
                .map(Text::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applier = readApplier();
        applierKey = null;
        applierIndex = -1;
        bind();
        addListeners();
        setClasses();
        updateButtons(true);
    }

    public void stop() {
        writeApplier(applier);
    }

    private void bind() {
        classes.prefWidthProperty().bind(center.widthProperty().divide(4));
        documents.prefWidthProperty().bind(center.widthProperty().divide(4));
        document.prefWidthProperty().bind(center.widthProperty().divide(2));
        documentText.wrappingWidthProperty().bind(document.widthProperty().subtract(40));
    }

    private void addListeners() {
        classes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            applierKey = newValue;
            setDocuments();
            updateClassBox();
        });
        documents.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            applierIndex = newValue.intValue();
            setText();
            updateClassBox();
        });
        classBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateButtons(newValue.intValue() == -1);
        });
    }

    private void setClasses() {
        setClasses(applier.getDocuments().keySet());
    }

    private void setClasses(Collection<String> classCollection) {
        ObservableList<String> classList = FXCollections.observableArrayList(classCollection);
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
        Collection<Text> textCollection = toText(documentCollection);
        textCollection.forEach(text -> text.wrappingWidthProperty().bind(documents.widthProperty().subtract(30)));
        documents.setItems(FXCollections.observableArrayList(textCollection));
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

    private void updateButtons(boolean disable) {
        deleteButton.setDisable(disable || applier.getDocuments().keySet().size() == 1);
        trainButton.setDisable(disable);
    }

    private void train(String classification, String text) {
        if (classification != null && text != null && !"".equals(classification) && !"".equals(text)) {
            Document document = new Document(text, classification);
            do {
                applier.train(document);
            } while (!applier.reClassify());
            setClasses();
        }
    }

    @FXML
    private void addButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/applierDialog.fxml"));
            fxmlLoader.setController(new ApplierDialogController());
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Class");
            stage.setScene(new Scene(root, 480d, 180d));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        String classification = classBox.getSelectionModel().getSelectedItem();
        if (classification != null) {
            applier.delete(classification);
            setClasses();
        }
    }

    @FXML
    private void trainButton() {
        train(classBox.getSelectionModel().getSelectedItem(), documentText.getText());
    }

    public class ApplierDialogController implements Initializable {
        @FXML
        private BorderPane root;

        @FXML
        private VBox center;
        @FXML
        private Label label;
        @FXML
        private TextField textField;

        @FXML
        private HBox bottom;
        @FXML
        private Button cancelButton;
        @FXML
        private Button addButton;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                addButton.setDisable("".equals(newValue));
            });
            addButton.setDisable(true);
        }

        @FXML
        private void add() {
            String classification = textField.getText();
            if (classification != null && !"".equals(classification)) {
                applier.addClassification(classification);
                setClasses();
                close();
            }
        }

        @FXML
        private void close() {
            ((Stage) root.getScene().getWindow()).close();
        }
    }
}

package gui;

import applying.Applier;
import applying.EmailApplier;
import classifier.Document;
import classifier.naivebayes.MultinomialNaiveBayesClassifier;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;

public class EmailApplierGui extends Application {
    private static final double SIZE = 250d;
    private static final double PADDING = 10d;
    private static final double MIN_WIDTH = 4d * SIZE + 3d * PADDING;
    private static final double MIN_HEIGHT = SIZE;

    private Applier applier;

    private ListView<String> classList;
    private ComboBox<String> classBox;
    private ListView<String> documentList;
    private ScrollPane scrollPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        applier = EmailApplier.apply(new MultinomialNaiveBayesClassifier());

        BorderPane root = generateRoot();

        HBox center = generateCenter();
        classList = generateClassList();
        documentList = generateDocumentList();
        scrollPane = generateScrollPane();
        center.getChildren().addAll(classList, documentList, scrollPane);

        HBox bottom = generateBottom();
        classBox = generateClassBox();
        Button trainButton = generateTrainButton();
        bottom.getChildren().addAll(classBox, trainButton);

        root.setCenter(center);
        root.setBottom(bottom);

        reset();

        Scene scene = new Scene(root);
        primaryStage.setTitle("JokeTeller");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    private void reset() {
        setClasses(applier.getDocuments().keySet());
    }

    private void setClasses(Collection<String> classes) {
        ObservableList<String> list = FXCollections.observableArrayList(classes);
        classList.setItems(list);
        classBox.setItems(list);
    }

    private void resetClasses() {
        setClasses(FXCollections.emptyObservableList());
    }

    private void setDocuments(Collection<String> documents) {
        documentList.setItems(FXCollections.observableArrayList(documents));
    }

    private void resetDocuments() {
        setDocuments(FXCollections.emptyObservableList());
    }

    private void setText(String text) {
        scrollPane.setContent(generateText(text));
    }

    private void resetText() {
        setText("");
    }

    private void updateClassBox() {
        String selected = classList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            classBox.getSelectionModel().select(selected);
        } else {
            classBox.getSelectionModel().clearSelection();
        }
    }

    private BorderPane generateRoot() {
        return new BorderPane();
    }

    private HBox generateCenter() {
        return new HBox();
    }

    private ListView<String> generateClassList() {
        ListView<String> classList = new ListView<>();
        classList.setPrefWidth(SIZE);
        classList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setDocuments(applier.getDocuments().get(newValue));
            } else {
                resetDocuments();
            }
            updateClassBox();
        });
        return classList;
    }

    private ListView<String> generateDocumentList() {
        ListView<String> documentList = new ListView<>();
        documentList.setPrefWidth(SIZE);
        documentList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            if (value != -1) {
                setText(documentList.getItems().get(value));
            } else {
                resetText();
            }
            updateClassBox();
        });
        return documentList;
    }

    private ScrollPane generateScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefViewportWidth(2d * SIZE);
        return scrollPane;
    }

    private Text generateText(String s) {
        Text text = new Text(s);
        text.setWrappingWidth(2d * SIZE);
        return text;
    }

    private HBox generateBottom() {
        HBox bottom = new HBox(PADDING);
        bottom.setPadding(new Insets(PADDING));
        bottom.setAlignment(Pos.CENTER_RIGHT);
        return bottom;
    }

    private ComboBox<String> generateClassBox() {
        return new ComboBox<>();
    }

    private Button generateTrainButton() {
        Button button = generateButton("train");
        button.setOnAction((event -> {
            String classification = classBox.getSelectionModel().getSelectedItem();
            String text = ((Text) scrollPane.getContent()).getText();
            if (classification != null && !"".equals(text)) {
                Document document = new Document(text, classification);
                applier.train(document);
                applier.reClassify();
                reset();
            }
        }));
        return button;
    }

    private Button generateButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(100d, 20d);
        return button;
    }
}

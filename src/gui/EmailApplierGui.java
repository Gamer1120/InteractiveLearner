package gui;

import applying.Applier;
import applying.EmailApplier;
import classifier.naivebayes.MultinomialNaiveBayesClassifier;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;

public class EmailApplierGui extends Application {
    private static final double SIZE = 250d;
    private static final double PADDING = 10d;
    private static final double MIN_WIDTH = SIZE;
    private static final double MIN_HEIGHT = SIZE + 2d * PADDING;

    private Applier applier;

    private ListView<String> classList;
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

        AnchorPane bottom = generateBottom();

        root.setCenter(center);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 1280d, 720d);
        primaryStage.setTitle("JokeTeller");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    private void setClasses(Collection<String> classes) {
        classList.setItems(FXCollections.observableArrayList(classes));
    }

    private void setDocuments(Collection<String> documents) {
        documentList.setItems(FXCollections.observableArrayList(documents));
    }

    private void setText(String text) {
        scrollPane.setContent(generateText(text));
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
        classList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

        });
        return classList;
    }

    private ListView<String> generateDocumentList() {
        ListView<String> documentList = new ListView<>();
        documentList.setPrefWidth(SIZE);
        documentList.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

        });
        return documentList;
    }

    private ScrollPane generateScrollPane() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefWidth(2d * SIZE);
        return scrollPane;
    }

    private Text generateText(String s) {
        Text text = new Text(s);
//        text.setWrappingWidth(SIZE);
        return text;
    }

    private AnchorPane generateBottom() {
        return new AnchorPane();
    }

    private Button generateButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(100d, 20d);
        return button;
    }
}

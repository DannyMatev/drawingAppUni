package main;

import com.sun.xml.internal.ws.wsdl.writer.document.StartWithExtensionsType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;

    public static final int CANVAS_WIDTH = 1000;
    public static final int CANVAS_HEIGHT = 800;

    private BorderPane pane;

    private MenuBar menuBar;

    private Menu file;

    private Menu operations;

    private Canvas mainCanvas;

    private VBox controls;

    private ToggleGroup shapesToggle;

    private ColorPicker colorPicker;

    private Slider transparencySlider;

    private Slider thicknessSlider;

    private Rectangle rectangle;

    private Circle circle;

    private Line line;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pane = new BorderPane();
        Scene mainScene = new Scene(pane, WIDTH, HEIGHT);

        makeMenus();
        setupCanvas();
        setupShapesMenu();
        setupColorsMenu();
        setupTransparencySlider();
        setupThicknessSlider();

        primaryStage.setTitle("Drawing application");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void makeMenus() {
        menuBar = new MenuBar();

        file = new Menu("File");
        MenuItem newFile = new MenuItem("New file");
        MenuItem openFile = new MenuItem("Open file");
        MenuItem saveFile = new MenuItem("Save file");
        MenuItem saveAsFile = new MenuItem("Save file");

        file.getItems().addAll(newFile, openFile, saveFile, saveAsFile);

        operations = new Menu("Operations");

        MenuItem undo = new MenuItem("Redo");
        MenuItem redo = new MenuItem("Undo");

        operations.getItems().addAll(undo, redo);

        menuBar.getMenus().addAll(file, operations);

        pane.setTop(menuBar);
    }

    private void setupCanvas() {
        mainCanvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

        circle = new Circle();
        rectangle = new Rectangle();
        line = new Line();

        mainCanvas.setOnMousePressed((event) -> {
            ToggleButton selectedButton = ((ToggleButton) shapesToggle.getSelectedToggle());
            String shape = selectedButton != null ? selectedButton.getText() : "";

            switch (shape) {
                case "Circle":
                    break;
                case "Rectangle":
                    break;
                case "Line":
                    line.setStartX(event.getX());
                    line.setStartY(event.getY());
                    break;
                case "Point":
                    break;
                case "Polygon":
                    break;
            }
        });

        mainCanvas.setOnMouseReleased((event) -> {
            ToggleButton selectedButton = ((ToggleButton) shapesToggle.getSelectedToggle());
            String shape = selectedButton != null ? selectedButton.getText() : "";

            switch (shape) {
                case "Circle":
                    break;
                case "Rectangle":
                    break;
                case "Line":
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());

                    GraphicsContext graphicsContext = mainCanvas.getGraphicsContext2D();
                    graphicsContext.setStroke(colorPicker.getValue());

                    graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                    break;
                case "Point":
                    break;
                case "Polygon":
                    break;
            }
        });

        pane.setRight(mainCanvas);
    }

    private void setupShapesMenu() {
        controls = new VBox();
        controls.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        shapesToggle = new ToggleGroup();

        ToggleButton circleButton = new ToggleButton("Circle");
        circleButton.setPrefSize(200, 30);
        circleButton.setOnMouseClicked(event -> {
            enableControls();
        });

        ToggleButton rectangleButton = new ToggleButton("Rectangle");
        rectangleButton.setPrefSize(200, 30);
        rectangleButton.setOnMouseClicked(event -> {
            enableControls();
        });

        ToggleButton lineButton = new ToggleButton("Line");
        lineButton.setPrefSize(200, 30);
        lineButton.setOnMouseClicked(event -> {
            enableControls();
        });

        ToggleButton pointButton = new ToggleButton("Point");
        pointButton.setPrefSize(200, 30);
        pointButton.setOnMouseClicked(event -> {
            enableControls();
        });

        ToggleButton polygonButton = new ToggleButton("Polygon");
        polygonButton.setPrefSize(200, 30);
        polygonButton.setOnMouseClicked(event -> {
            enableControls();
        });

        shapesToggle.getToggles().addAll(circleButton, rectangleButton, lineButton, pointButton, polygonButton);

        controls.getChildren().addAll(circleButton, rectangleButton, lineButton, pointButton, polygonButton);

        pane.setLeft(controls);
    }

    private void enableControls() {
        if (shapesToggle.getSelectedToggle() != null) {
            colorPicker.setDisable(false);
            transparencySlider.setDisable(false);
            thicknessSlider.setDisable(false);
        } else {
            colorPicker.setDisable(true);
            transparencySlider.setDisable(true);
            thicknessSlider.setDisable(true);
        }
    }

    private void setupColorsMenu() {
        colorPicker = new ColorPicker();
        colorPicker.setPrefSize(200, 30);
        colorPicker.setDisable(true);

        controls.getChildren().add(colorPicker);
    }

    private void setupTransparencySlider() {
        transparencySlider = new Slider(0.0, 1.0, 0.1);
        transparencySlider.setPrefSize(200, 30);
        transparencySlider.setShowTickMarks(true);
        transparencySlider.setShowTickLabels(true);
        transparencySlider.setMajorTickUnit(0.25f);
        transparencySlider.setBlockIncrement(0.1f);
        transparencySlider.setSnapToTicks(true);
        transparencySlider.setDisable(true);

        Label trans = new Label("Transparency");

        controls.getChildren().addAll(trans, transparencySlider);
    }

    private void setupThicknessSlider() {
        thicknessSlider = new Slider(1.0, 10.0, 1.0);
        thicknessSlider.setPrefSize(200, 30);
        thicknessSlider.setShowTickMarks(true);
        thicknessSlider.setShowTickLabels(true);
        thicknessSlider.setMajorTickUnit(1.0f);
        thicknessSlider.setBlockIncrement(0.1f);
        thicknessSlider.setSnapToTicks(true);
        thicknessSlider.setDisable(true);

        Label trans = new Label("Line thickness");

        controls.getChildren().addAll(trans, thicknessSlider);
    }
}

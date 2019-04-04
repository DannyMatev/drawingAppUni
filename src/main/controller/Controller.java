package main.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import main.Main;
import main.model.Layer;
import main.model.figure.*;

import javax.imageio.ImageIO;
import java.awt.font.ImageGraphicAttribute;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private ToggleButton lineToggleButton;

    @FXML
    private ToggleButton ellipseToggleButton;

    @FXML
    private ToggleButton rectangleToggleButton;

    @FXML
    private ToggleButton pointToggleButton;

    @FXML
    private ToggleButton pentagonToggleButton;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Slider transparencySlider;

    @FXML
    private Slider lineThicknessSlider;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ListView<RadioButton> layersListView;

    @FXML
    private Pane drawingPane;

    private ToggleGroup toggleGroup;

    private Map<RadioButton, Layer> layersMap;

    private Ellipse ellipse;

    private Rectangle rectangle;

    private Line line;

    private Circle point;

    private int pentagonPoints = 0;
    private double[] pentagonX = new double[5];
    private double[] pentagonY = new double[5];

    private double canvasHeight;
    private double canvasWidth;

    public Controller() {
        toggleGroup = new ToggleGroup();
        layersMap = new HashMap<>();
        ellipse = new Ellipse();
        rectangle = new Rectangle();
        line = new Line();
        point = new Circle();
    }

    @FXML
    public void initialize() {
        Layer initialLayer = new Layer(canvas);
        initialLayer.getRadioButton().setToggleGroup(toggleGroup);
        layersMap.put(initialLayer.getRadioButton(), initialLayer);
        layersListView.getItems().add(initialLayer.getRadioButton());
        toggleGroup.selectToggle(initialLayer.getRadioButton());

        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                Canvas selectedCanvas = layersMap.get(selectedRadioButton).getCanvas();


                selectedCanvas.toFront();
            }
        });

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        lineThicknessSlider.setValue(5);
        transparencySlider.setValue(1);
        colorPicker.setValue(Color.BLACK);
    }

    @FXML
    public void addLayer() {
        Layer layer = new Layer(new Canvas(canvasWidth, canvasHeight));

        drawingPane.getChildren().add(layer.getCanvas());
        layer.getRadioButton().setToggleGroup(toggleGroup);
        layersMap.put(layer.getRadioButton(), layer);
        layersListView.getItems().add(layer.getRadioButton());
    }

    @FXML
    public void removeLayer() {
        if (toggleGroup.getToggles().size() > 1) {
            RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
            Canvas canvasToBeRemoved = layersMap.get(radioButton).getCanvas();
            layersListView.getItems().remove(radioButton);
            drawingPane.getChildren().remove(canvasToBeRemoved);
            layersMap.remove(radioButton);
            toggleGroup.getToggles().remove(radioButton);
            toggleGroup.selectToggle(toggleGroup.getToggles().get(0));
        }
    }

    @FXML
    public void saveImage() {
        Canvas saveImageCanvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext context = saveImageCanvas.getGraphicsContext2D();

        for (RadioButton currentButton : layersListView.getItems()) {
            Layer layer = layersMap.get(currentButton);
            for (Figure figure : layer.getDrawnShapes()) {
                if (figure instanceof Circle) {
                    Circle circle = (Circle) figure;
                    context.setFill(circle.getColor());
                    context.setGlobalAlpha(circle.getTransparency());
                    context.fillOval(circle.getX() - circle.getRadius(), circle.getY() - circle.getRadius(), circle.getDiameter(), circle.getDiameter());
                } else if (figure instanceof Rectangle) {
                    Rectangle rect = (Rectangle) figure;
                    context.setFill(rect.getColor());
                    context.setGlobalAlpha(rect.getTransparency());
                    context.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                } else if (figure instanceof Line) {
                    Line li = (Line) figure;
                    context.setStroke(li.getColor());
                    context.setGlobalAlpha(li.getTransparency());
                    context.setLineWidth(li.getThickness());
                    context.strokeLine(li.getStartX(), li.getStartY(), li.getEndX(), li.getEndY());
                } else if (figure instanceof Ellipse) {
                    Ellipse elli = (Ellipse) figure;
                    context.setFill(elli.getColor());
                    context.setGlobalAlpha(elli.getTransparency());
                    context.fillOval(elli.getX(), elli.getY(), elli.getWidth(), elli.getHeight());
                } else if (figure instanceof LoadedImage) {
                    LoadedImage image = (LoadedImage) figure;
                    context.drawImage(image.getImage(), image.getX(), image.getY());
                }
            }
        }

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(Main.mainStage);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) canvasWidth, (int) canvasHeight);
                saveImageCanvas.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                Logger.getAnonymousLogger().warning("IO exception");
            }
        }
    }

    @FXML
    public void loadImage() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(Main.mainStage);
        Layer currentLayer = getCurrentLayer();

        if (file != null) {
            Image chosenImage = new Image(file.toURI().toString());
            LoadedImage loadedImage = new LoadedImage();
            loadedImage.setImage(chosenImage);

            currentLayer.getCanvas().getGraphicsContext2D().drawImage(loadedImage.getImage(), 0, 0);
            currentLayer.addShape(loadedImage);
        }
    }

    @FXML
    private void getStartLocation(MouseEvent event) {
        if (lineToggleButton.isSelected()) {
            line = new Line();
            line.setStartX(event.getX());
            line.setStartY(event.getY());
        } else if (ellipseToggleButton.isSelected()) {
            ellipse = new Ellipse();
            ellipse.setX(event.getX());
            ellipse.setY(event.getY());
        } else if (rectangleToggleButton.isSelected()) {
            rectangle = new Rectangle();
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
        } else if (pentagonToggleButton.isSelected()) {
            pentagonX[pentagonPoints] = event.getX();
            pentagonY[pentagonPoints] = event.getY();
            pentagonPoints++;
        }
    }

    @FXML
    private void drawShape(MouseEvent event) {
        Layer currentLayer = getCurrentLayer();
        GraphicsContext graphicsContext = currentLayer.getCanvas().getGraphicsContext2D();

        graphicsContext.setFill(colorPicker.getValue());
        graphicsContext.setGlobalAlpha(transparencySlider.getValue());
        graphicsContext.setLineWidth(lineThicknessSlider.getValue());

        if (lineToggleButton.isSelected()) {
            line.setColor(colorPicker.getValue());
            line.setThickness(lineThicknessSlider.getValue());
            line.setTransparency(transparencySlider.getValue());
            drawLine(graphicsContext, event);
            currentLayer.addShape(line);
        } else if (ellipseToggleButton.isSelected()) {
            ellipse.setColor(colorPicker.getValue());
            ellipse.setThickness(lineThicknessSlider.getValue());
            ellipse.setTransparency(transparencySlider.getValue());
            drawEllipse(graphicsContext, event);
            currentLayer.addShape(ellipse);
        } else if (rectangleToggleButton.isSelected()) {
            rectangle.setColor(colorPicker.getValue());
            rectangle.setThickness(lineThicknessSlider.getValue());
            rectangle.setTransparency(transparencySlider.getValue());
            drawRectangle(graphicsContext, event);
            currentLayer.addShape(rectangle);
        } else if (pointToggleButton.isSelected()) {
            point.setColor(colorPicker.getValue());
            point.setThickness(lineThicknessSlider.getValue());
            point.setTransparency(transparencySlider.getValue());
            drawPoint(graphicsContext, event);
            currentLayer.addShape(point);
        } else if (pentagonToggleButton.isSelected() && pentagonPoints == 5) {
            graphicsContext.fillPolygon(pentagonX, pentagonY, pentagonPoints);
            pentagonPoints = 0;
        }
    }

    private void drawLine(GraphicsContext graphicsContext, MouseEvent event) {
        line.setEndX(event.getX());
        line.setEndY(event.getY());

        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    private void drawRectangle(GraphicsContext graphicsContext, MouseEvent event) {
        double width = Math.abs(rectangle.getX() - event.getX());
        double height = Math.abs(rectangle.getY() - event.getY());

        rectangle.setWidth(width);
        rectangle.setHeight(height);

        if (rectangle.getX() > event.getX()) {
            if (rectangle.getY() > event.getY()) {
                graphicsContext.fillRect(event.getX(), event.getY(), width, height);
            } else {
                graphicsContext.fillRect(event.getX(), event.getY() - height, width, height);
            }
        } else {
            if (rectangle.getY() > event.getY()) {
                graphicsContext.fillRect(event.getX() - width, event.getY(), width, height);
            } else {
                graphicsContext.fillRect(rectangle.getX(), rectangle.getY(), width, height);
            }
        }
    }

    private void drawEllipse(GraphicsContext graphicsContext, MouseEvent event) {
        double diameterX = Math.abs(ellipse.getX() - event.getX());
        double diameterY = Math.abs(ellipse.getY() - event.getY());

        ellipse.setWidth(diameterX);
        ellipse.setHeight(diameterY);

        if (ellipse.getX() > event.getX()) {
            if (ellipse.getY() > event.getY()) {
                graphicsContext.fillOval(event.getX(), event.getY(), diameterX, diameterY);
            } else {
                graphicsContext.fillOval(event.getX(), event.getY() - diameterY, diameterX, diameterY);
            }
        } else {
            if (ellipse.getY() > event.getY()) {
                graphicsContext.fillOval(event.getX() - diameterY, event.getY(), diameterX, diameterY);
            } else {
                graphicsContext.fillOval(ellipse.getX(), ellipse.getY(), diameterX, diameterY);
            }
        }
    }

    private void drawPoint(GraphicsContext graphicsContext, MouseEvent event) {
        double diameter = lineThicknessSlider.getValue();
        point.setX(event.getX());
        point.setY(event.getY());
        point.setRadius(diameter / 2);
        graphicsContext.fillOval(point.getX() - diameter / 2, point.getY() - diameter / 2, diameter, diameter);
    }

    private Layer getCurrentLayer() {
        RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
        return layersMap.get(radioButton);
    }
}

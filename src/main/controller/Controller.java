package main.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import main.model.Layer;

import java.util.HashMap;
import java.util.Map;

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

    private Map<RadioButton, Canvas> layersMap;

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
        line = new Line();
        ellipse = new Ellipse();
        rectangle = new Rectangle();
        point = new Circle();
    }

    @FXML
    public void initialize() {
        Layer initialLayer = new Layer(canvas);
        initialLayer.getRadioButton().setToggleGroup(toggleGroup);
        layersMap.put(initialLayer.getRadioButton(), initialLayer.getCanvas());
        layersListView.getItems().add(initialLayer.getRadioButton());
        toggleGroup.selectToggle(initialLayer.getRadioButton());

        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggleGroup.getSelectedToggle() != null) {
                RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
                Canvas selectedCanvas = layersMap.get(selectedRadioButton);


                selectedCanvas.toFront();
            }
        });

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
    }

    @FXML
    private void getStartLocation(MouseEvent event) {
        if (lineToggleButton.isSelected()) {
            line.setStartX(event.getX());
            line.setStartY(event.getY());
        } else if (ellipseToggleButton.isSelected()) {
            ellipse.setCenterX(event.getX());
            ellipse.setCenterY(event.getY());
        } else if (rectangleToggleButton.isSelected()) {
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
        } else if (pentagonToggleButton.isSelected()) {
            pentagonX[pentagonPoints] = event.getX();
            pentagonY[pentagonPoints] = event.getY();
            pentagonPoints++;
        }

    }

    @FXML
    public void addLayer() {
        Layer layer = new Layer(new Canvas(canvasWidth, canvasHeight));
        drawingPane.getChildren().add(layer.getCanvas());
        layer.getRadioButton().setToggleGroup(toggleGroup);
        layersMap.put(layer.getRadioButton(), layer.getCanvas());
        layersListView.getItems().add(layer.getRadioButton());
    }

    @FXML
    public void removeLayer() {
        if(toggleGroup.getToggles().size()>1) {
            RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
            Canvas canvasToBeRemoved = layersMap.get(radioButton);
            layersListView.getItems().remove(radioButton);
            drawingPane.getChildren().remove(canvasToBeRemoved);
            layersMap.remove(radioButton);
            toggleGroup.getToggles().remove(radioButton);
            toggleGroup.selectToggle(toggleGroup.getToggles().get(0));
        }
    }

    @FXML
    private void drawShape(MouseEvent event) {
        RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();
        Canvas currentCanvas = layersMap.get(radioButton);
        GraphicsContext graphicsContext = currentCanvas.getGraphicsContext2D();

        graphicsContext.setFill(colorPicker.getValue());
        graphicsContext.setGlobalAlpha(transparencySlider.getValue());
        graphicsContext.setLineWidth(lineThicknessSlider.getValue());

        if (lineToggleButton.isSelected()) {
            drawLine(graphicsContext, event);
        } else if (ellipseToggleButton.isSelected()) {
            drawEllipse(graphicsContext, event);
        } else if (rectangleToggleButton.isSelected()) {
            drawRectangle(graphicsContext, event);
        } else if (pointToggleButton.isSelected()) {
            drawPoint(graphicsContext, event);
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
        double diameterX = Math.abs(ellipse.getCenterX() - event.getX());
        double diameterY = Math.abs(ellipse.getCenterY() - event.getY());

        if (ellipse.getCenterX() > event.getX()) {
            if (ellipse.getCenterY() > event.getY()) {
                graphicsContext.fillOval(event.getX(), event.getY(), diameterX, diameterY);
            } else {
                graphicsContext.fillOval(event.getX(), event.getY() - diameterY, diameterX, diameterY);
            }
        } else {
            if (ellipse.getCenterY() > event.getY()) {
                graphicsContext.fillOval(event.getX() - diameterY, event.getY(), diameterX, diameterY);
            } else {
                graphicsContext.fillOval(ellipse.getCenterX(), ellipse.getCenterY(), diameterX, diameterY);
            }
        }
    }

    private void drawPoint(GraphicsContext graphicsContext, MouseEvent event) {
        double diameter = lineThicknessSlider.getValue();
        point.setCenterX(event.getX());
        point.setCenterY(event.getY());
        graphicsContext.fillOval(point.getCenterX(), point.getCenterY(), diameter, diameter);
    }


}

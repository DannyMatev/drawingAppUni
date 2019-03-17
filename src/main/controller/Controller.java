package main.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

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

    private Ellipse ellipse;

    private Rectangle rectangle;

    private Line line;

    private Circle point;

    private int pentagonPoints = 0;
    private double[] pentagonX = new double[5];
    private double[] pentagonY = new double[5];

    public Controller() {
        line = new Line();
        ellipse = new Ellipse();
        rectangle = new Rectangle();
        point = new Circle();
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
    private void drawShape(MouseEvent event) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

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

package main.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Controller {

    @FXML
    private Canvas canvas;

    @FXML
    private ToggleGroup shapesToggleGroup;

    @FXML
    private ToggleButton lineToggleButton;

    @FXML
    private ToggleButton circleToggleButton;

    @FXML
    private ToggleButton rectangleToggleButton;

    @FXML
    private ToggleButton pointToggleButton;

    @FXML
    private ToggleButton polygonToggleButton;

    @FXML
    private ColorPicker colorPicker;

    private Circle circle;
    private Rectangle rectangle;
    private Line line;

    public Controller() {
        line = new Line();
        circle = new Circle();
        rectangle = new Rectangle();
    }

    @FXML
    private void getStartLocation(MouseEvent event) {
        if (lineToggleButton.isSelected()) {
            line.setStartX(event.getX());
            line.setStartY(event.getY());
        } else if (circleToggleButton.isSelected()) {
            circle.setCenterX(event.getX());
            circle.setCenterY(event.getY());
        } else if (rectangleToggleButton.isSelected()) {
            rectangle.setX(event.getX());
            rectangle.setY(event.getY());
        } else if (pointToggleButton.isSelected()) {
        } else if (polygonToggleButton.isSelected()) {
        }
    }

    @FXML
    private void drawShape(MouseEvent event) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        if (lineToggleButton.isSelected()) {
            drawLine(graphicsContext, event);
        } else if (circleToggleButton.isSelected()) {
            drawCircle(graphicsContext, event);
        } else if (rectangleToggleButton.isSelected()) {
            drawRectangle(graphicsContext, event);
        } else if (pointToggleButton.isSelected()) {
        } else if (polygonToggleButton.isSelected()) {
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

        graphicsContext.setFill(colorPicker.getValue());

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

    @FXML
    private void drawCircle(GraphicsContext graphicsContext, MouseEvent event) {
        graphicsContext.setFill(colorPicker.getValue());

        double diameter = Math.abs(circle.getCenterY() - event.getY());

        if (circle.getCenterX() > event.getX()) {
            if (circle.getCenterY() > event.getY()) {
                graphicsContext.fillOval(event.getX(), event.getY(), diameter, diameter);
            } else {
                graphicsContext.fillOval(event.getX(), event.getY() - diameter, diameter, diameter);
            }
        } else {
            if (circle.getCenterY() > event.getY()) {
                graphicsContext.fillOval(event.getX() - diameter, event.getY(), diameter, diameter);
            } else {
                graphicsContext.fillOval(circle.getCenterX(), circle.getCenterY(), diameter, diameter);
            }
        }
    }
}

package main.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.RadioButton;
import main.model.figure.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Layer {

    private static int layerCount = 0;

    private Canvas canvas;
    private RadioButton radioButton;

    private List<Figure> drawnShapes;

    public Layer() {
    }

    public Layer(Canvas canvas) {
        layerCount++;
        this.canvas = canvas;
        this.radioButton = new RadioButton(String.format("Layer %d", layerCount));
        drawnShapes = new ArrayList<>();
    }

    public static int getLayerCount() {
        return layerCount;
    }

    public static void setLayerCount(int layerCount) {
        Layer.layerCount = layerCount;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public void setRadioButton(RadioButton radioButton) {
        this.radioButton = radioButton;
    }

    public void addShape(Figure figure) {
        drawnShapes.add(figure);
    }

    public List<Figure> getDrawnShapes() {
        return drawnShapes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Layer layer = (Layer) o;
        return Objects.equals(canvas, layer.canvas) &&
                Objects.equals(radioButton, layer.radioButton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canvas, radioButton);
    }
}

package main.model;

public class Line extends Figure {

    private double startX;

    private double startY;

    private double endX;

    private double endY;

    public Line() {
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        x = startX;
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        y = startY;
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}

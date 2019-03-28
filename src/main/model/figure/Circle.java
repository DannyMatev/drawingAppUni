package main.model.figure;

public class Circle extends Figure {

    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getDiameter() {
        return radius * 2;
    }
}

package ui;

import java.awt.*;
import java.awt.Shape;

public class ShapeWithColor {
    private final Shape shape;
    private final Color color;
    private final int strokeSize;
    private final boolean isFilled;
    private final boolean isDotted;

    public ShapeWithColor(Shape shape, Color color, int strokeSize, boolean isFilled) {
        this(shape, color, strokeSize, isFilled, false);
    }

    public ShapeWithColor(Shape shape, Color color, int strokeSize, boolean isFilled, boolean isDotted) {
        this.shape = shape;
        this.color = color;
        this.strokeSize = strokeSize;
        this.isFilled = isFilled;
        this.isDotted = isDotted;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean isDotted() {
        return isDotted;
    }
}

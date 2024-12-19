package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends JPanel {
    public enum DrawMode { FREEHAND, RECTANGLE, CIRCLE, LINE, ERASER }

    private final static int DEFAULT_STROKE_SIZE = 4;

    private Color color;
    private int strokeSize;
    private List<List<ShapeWithColor>> strokes; // List to store strokes
    private List<ShapeWithColor> currentStroke;
    private Point startPoint;
    private Shape previewShape;
    private DrawMode drawMode;
    private boolean isFilled; // For filled shapes
    private boolean isDotted; // For dotted shapes

    public CanvasPanel(int width, int height) {
        super();

        setPreferredSize(new Dimension(width, height));
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Initialize variables
        color = Color.BLACK;
        strokeSize = DEFAULT_STROKE_SIZE;
        strokes = new ArrayList<>();
        drawMode = DrawMode.FREEHAND;
        isFilled = false; // Default to not filled
        isDotted = false; // Default to not dotted

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                if (drawMode == DrawMode.FREEHAND || drawMode == DrawMode.ERASER) {
                    currentStroke = new ArrayList<>();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                if (drawMode == DrawMode.FREEHAND || drawMode == DrawMode.ERASER) {
                    Color drawColor = drawMode == DrawMode.ERASER ? getBackground() : color; // Use background color for eraser
                    ShapeWithColor shape = new ShapeWithColor(new Line2D.Float(startPoint, currentPoint), drawColor, strokeSize, isFilled, isDotted);
                    currentStroke.add(shape);
                    startPoint = currentPoint;
                } else {
                    previewShape = createShape(drawMode, startPoint, currentPoint);
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawMode == DrawMode.FREEHAND || drawMode == DrawMode.ERASER) {
                    if (!currentStroke.isEmpty()) {
                        strokes.add(currentStroke);
                    }
                } else {
                    Point endPoint = e.getPoint();
                    Shape finalShape = createShape(drawMode, startPoint, endPoint);
                    Color drawColor = drawMode == DrawMode.ERASER ? getBackground() : color; // Use background color for eraser
                    strokes.add(List.of(new ShapeWithColor(finalShape, drawColor, strokeSize, isFilled, isDotted)));
                }
                currentStroke = null;
                previewShape = null;
                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    // Setters for Filled and Dotted
    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    public void setDotted(boolean dotted) {
        this.isDotted = dotted;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw all strokes
        for (List<ShapeWithColor> stroke : strokes) {
            for (ShapeWithColor shapeWithColor : stroke) {
                g2d.setColor(shapeWithColor.getColor());

                // Configure stroke for dotted or solid
                if (shapeWithColor.isDotted()) {
                    float[] dashPattern = {5, 5}; // Dotted pattern
                    g2d.setStroke(new BasicStroke(
                            shapeWithColor.getStrokeSize(),
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND,
                            1.0f,
                            dashPattern,
                            0
                    ));
                } else {
                    g2d.setStroke(new BasicStroke(shapeWithColor.getStrokeSize()));
                }

                if (shapeWithColor.isFilled() && !(shapeWithColor.getShape() instanceof Line2D)) {
                    g2d.fill(shapeWithColor.getShape());
                } else {
                    g2d.draw(shapeWithColor.getShape());
                }
            }
        }

        // Optionally draw the current stroke (while dragging)
        if (currentStroke != null) {
            for (ShapeWithColor shapeWithColor : currentStroke) {
                g2d.setColor(shapeWithColor.getColor());
                g2d.setStroke(new BasicStroke(shapeWithColor.getStrokeSize()));
                g2d.draw(shapeWithColor.getShape());
            }
        }

        // Draw the preview shape
        if (previewShape != null) {
            g2d.setColor(color);
            if (isDotted) {
                float[] dashPattern = {5, 5};
                g2d.setStroke(new BasicStroke(
                        strokeSize,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND,
                        1.0f,
                        dashPattern,
                        0
                ));
            } else {
                g2d.setStroke(new BasicStroke(strokeSize));
            }
            if (isFilled && !(previewShape instanceof Line2D)) {
                g2d.fill(previewShape);
            } else {
                g2d.draw(previewShape);
            }
        }
    }

    private Shape createShape(DrawMode mode, Point start, Point end) {
        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(end.x - start.x);
        int height = Math.abs(end.y - start.y);

        switch (mode) {
            case RECTANGLE:
                return new Rectangle(x, y, width, height);
            case CIRCLE:
                int diameter = Math.max(width, height); // Keep circle dimensions proportional
                return new Ellipse2D.Float(x, y, diameter, diameter);
            case LINE:
                return new Line2D.Float(start, end);
            default:
                return null;
        }
    }

    public void setDrawMode(DrawMode mode) {
        this.drawMode = mode;
    }

    public void setColor(Color newColor) {
        this.color = newColor;
    }

    public void setStrokeSize(int newSize) {
        this.strokeSize = newSize;
    }

    public void clearCanvas() {
        strokes.clear();
        repaint();
    }

    public void undoLastStroke() {
        if (!strokes.isEmpty()) {
            strokes.remove(strokes.size() - 1);
            repaint();
        }
    }

    public Color getColor() {
        return color;
    }
}

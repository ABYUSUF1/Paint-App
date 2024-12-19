package ui;

import javax.swing.*;
import java.awt.*;

public class PaintFrame extends JFrame {
    private CanvasPanel canvasPanel;
    private final JButton colorButton;
    private final JButton undoButton;
    private final JButton clearButton;
    private JButton filledButton, dottedButton;
    private final JRadioButton freeHandButton, rectangleButton, circleButton, lineButton, eraserButton;
    private final ButtonGroup buttonGroup;
    private JSlider strokeSlider;
    private int strokeSize = 4;
    private boolean isFilled = false;
    private boolean isDotted = false;

    public PaintFrame() {
        // Set up the frame
        setTitle("Paint Program");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize canvas panel
        canvasPanel = new CanvasPanel(800, 600);
        add(canvasPanel, BorderLayout.CENTER);

        // Set up toolbar
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout());

        // Color button
        colorButton = new JButton("Choose Color");
        colorButton.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(this, "Choose a color", canvasPanel.getColor());
            if (chosenColor != null) {
                canvasPanel.setColor(chosenColor);
            }
        });
        toolbar.add(colorButton);

        // Stroke size slider
        JLabel strokeLabel = new JLabel("Stroke Size: ");
        strokeSlider = new JSlider(1, 20, strokeSize);  // Stroke size from 1 to 20
        strokeSlider.setMajorTickSpacing(5);
        strokeSlider.setMinorTickSpacing(1);
        strokeSlider.setPaintTicks(true);
        strokeSlider.setPaintLabels(true);
        strokeSlider.addChangeListener(e -> {
            strokeSize = strokeSlider.getValue();
            canvasPanel.setStrokeSize(strokeSize);
        });
        toolbar.add(strokeLabel);
        toolbar.add(strokeSlider);

        // Undo button
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            canvasPanel.undoLastStroke();
        });
        toolbar.add(undoButton);

        // Clear button
        clearButton = new JButton("Clear All");
        clearButton.addActionListener(e -> {
            canvasPanel.clearCanvas();
        });
        toolbar.add(clearButton);

        // Filled and Dotted toggle buttons
        filledButton = new JButton("Filled");
        filledButton.setSelected(isFilled); // Set initial state
        filledButton.addActionListener(e -> {
            if (isFilled) {
                isFilled = false; // Turn off Filled
                filledButton.setBackground(null); // Reset Filled button background
            } else {
                isFilled = true; // Turn on Filled
                isDotted = false; // Ensure Dotted is turned off when Filled is on
                filledButton.setBackground(Color.GREEN); // Highlight Filled button
                dottedButton.setBackground(null); // Reset Dotted button background
            }
            canvasPanel.setFilled(isFilled); // Update the CanvasPanel
            canvasPanel.setDotted(isDotted); // Ensure Dotted is off when Filled is on
            repaint();
        });
        toolbar.add(filledButton);

        dottedButton = new JButton("Dotted");
        dottedButton.setSelected(isDotted); // Set initial state
        dottedButton.addActionListener(e -> {
            if (isDotted) {
                isDotted = false; // Turn off Dotted
                dottedButton.setBackground(null); // Reset Dotted button background
            } else {
                isDotted = true; // Turn on Dotted
                isFilled = false; // Ensure Filled is turned off when Dotted is on
                dottedButton.setBackground(Color.GREEN); // Highlight Dotted button
                filledButton.setBackground(null); // Reset Filled button background
            }
            canvasPanel.setDotted(isDotted); // Update the CanvasPanel
            canvasPanel.setFilled(isFilled); // Ensure Filled is off when Dotted is on
            repaint();
        });
        toolbar.add(dottedButton);

        // Drawing mode buttons (e.g., freehand, shapes)
        freeHandButton = new JRadioButton("Freehand");
        freeHandButton.setSelected(true);
        freeHandButton.addActionListener(e -> {
            canvasPanel.setDrawMode(CanvasPanel.DrawMode.FREEHAND);
        });

        rectangleButton = new JRadioButton("Rectangle");
        rectangleButton.addActionListener(e -> {
            canvasPanel.setDrawMode(CanvasPanel.DrawMode.RECTANGLE);
        });

        circleButton = new JRadioButton("Circle");
        circleButton.addActionListener(e -> {
            canvasPanel.setDrawMode(CanvasPanel.DrawMode.CIRCLE);
        });

        lineButton = new JRadioButton("Line");
        lineButton.addActionListener(e -> {
            canvasPanel.setDrawMode(CanvasPanel.DrawMode.LINE);
        });

        eraserButton = new JRadioButton("Eraser");
        eraserButton.addActionListener(e -> {
            canvasPanel.setDrawMode(CanvasPanel.DrawMode.ERASER);
        });

        // Group radio buttons
        buttonGroup = new ButtonGroup();
        buttonGroup.add(freeHandButton);
        buttonGroup.add(rectangleButton);
        buttonGroup.add(circleButton);
        buttonGroup.add(lineButton);
        buttonGroup.add(eraserButton);

        toolbar.add(freeHandButton);
        toolbar.add(rectangleButton);
        toolbar.add(circleButton);
        toolbar.add(lineButton);
        toolbar.add(eraserButton);

        // Add the toolbar to the frame
        add(toolbar, BorderLayout.NORTH);
    }
}

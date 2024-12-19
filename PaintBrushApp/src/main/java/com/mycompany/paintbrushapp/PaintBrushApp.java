package com.mycompany.paintbrushapp;

import javax.swing.SwingUtilities;
import ui.PaintFrame;

public class PaintBrushApp {
  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaintFrame frame = new PaintFrame();
            frame.setVisible(true);
        });
    }
}
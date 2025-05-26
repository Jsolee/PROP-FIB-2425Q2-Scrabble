package Presentacio;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitària per a components comuns de la interfície d'usuari.
 * Proporciona constants de colors i mètodes per estilitzar botons.
 */
public class CommonComponents {
    // Colors for special board cells
    public static final Color TRIPLE_WORD_COLOR = new Color(239, 83, 80); // Red
    public static final Color DOUBLE_WORD_COLOR = new Color(239, 154, 154); // Pink
    public static final Color TRIPLE_LETTER_COLOR = new Color(66, 165, 245); // Blue
    public static final Color DOUBLE_LETTER_COLOR = new Color(129, 199, 132); // Green
    public static final Color CENTER_COLOR = new Color(255, 241, 118); // Yellow
    public static final Color DEFAULT_COLOR = new Color(238, 238, 238); // Light gray
    public static final Color TILE_COLOR = new Color(255, 224, 178); // Wood-like color
    public static final Color TILE_TEXT_COLOR = new Color(97, 97, 97); // Dark gray
    public static final Color DRAG_HIGHLIGHT_COLOR = new Color(144, 238, 144); // Light green for drag highlight

    public static void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public static void styleBoardButton(JButton button, Color bgColor, String text) {
        button.setBackground(bgColor);
        button.setText(text);
        if (text.equals("★")) {
            button.setForeground(Color.RED);
        } else if (text.equals("TW") || text.equals("DW") || text.equals("TL") || text.equals("DL")) {
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Arial", Font.BOLD, 10));
        }
    }
}
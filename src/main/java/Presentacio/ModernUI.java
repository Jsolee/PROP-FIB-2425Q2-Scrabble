package Presentacio;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Modern UI utility class providing aesthetic components and styling
 */
public class ModernUI {
    
    // Modern color palette
    public static final Color PRIMARY_BLUE = new Color(63, 81, 181);
    public static final Color PRIMARY_BLUE_DARK = new Color(48, 63, 159);
    public static final Color ACCENT_PINK = new Color(233, 30, 99);
    public static final Color ACCENT_PINK_DARK = new Color(194, 24, 91);
    public static final Color SUCCESS_GREEN = new Color(76, 175, 80);
    public static final Color SUCCESS_GREEN_DARK = new Color(69, 160, 73);
    public static final Color WARNING_ORANGE = new Color(255, 152, 0);
    public static final Color WARNING_ORANGE_DARK = new Color(245, 124, 0);
    public static final Color ERROR_RED = new Color(244, 67, 54);
    public static final Color ERROR_RED_DARK = new Color(211, 47, 47);
    public static final Color BACKGROUND_LIGHT = new Color(250, 250, 250);
    public static final Color CARD_WHITE = new Color(255, 255, 255);
    public static final Color TEXT_DARK = new Color(33, 33, 33);
    public static final Color TEXT_MEDIUM = new Color(66, 66, 66);
    public static final Color TEXT_LIGHT = new Color(158, 158, 158);
    public static final Color BORDER_LIGHT = new Color(224, 224, 224);
    
    // Modern gradients
    public static final Color GRADIENT_START = new Color(63, 81, 181);
    public static final Color GRADIENT_END = new Color(233, 30, 99);
    
    // Scrabble color palette
    public static final Color SCRABBLE_BLUE = new Color(0, 102, 153);
    public static final Color SCRABBLE_GREEN = new Color(0, 153, 102);
    public static final Color SCRABBLE_TEAL = new Color(0, 153, 153);
    public static final Color SCRABBLE_LIGHT_BLUE = new Color(51, 153, 255);
    public static final Color SCRABBLE_WOOD = new Color(222, 184, 135);
    public static final Color SCRABBLE_BOARD = new Color(230, 245, 240);
    public static final Color SCRABBLE_TILE = new Color(255, 255, 204);
    public static final Color SCRABBLE_TEXT = new Color(20, 40, 40);

    // Scrabble gradients
    public static final Color SCRABBLE_GRADIENT_START = SCRABBLE_BLUE;
    public static final Color SCRABBLE_GRADIENT_END = SCRABBLE_GREEN;

    /**
     * Creates a modern styled button with gradient background and animations
     */
    public static JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(baseColor);
        button.setBorder(createRoundedBorder(10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add smooth hover animation
        Color hoverColor = baseColor.darker();
        button.addMouseListener(new MouseAdapter() {
            private Timer hoverTimer;
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(10, evt -> {
                    Color currentColor = button.getBackground();
                    Color targetColor = hoverColor;
                    
                    int r = Math.min(255, currentColor.getRed() + (targetColor.getRed() - currentColor.getRed()) / 3);
                    int g = Math.min(255, currentColor.getGreen() + (targetColor.getGreen() - currentColor.getGreen()) / 3);
                    int b = Math.min(255, currentColor.getBlue() + (targetColor.getBlue() - currentColor.getBlue()) / 3);
                    
                    button.setBackground(new Color(r, g, b));
                    
                    if (Math.abs(r - targetColor.getRed()) < 2 && 
                        Math.abs(g - targetColor.getGreen()) < 2 && 
                        Math.abs(b - targetColor.getBlue()) < 2) {
                        hoverTimer.stop();
                    }
                });
                hoverTimer.start();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(10, evt -> {
                    Color currentColor = button.getBackground();
                    Color targetColor = baseColor;
                    
                    int r = Math.max(0, currentColor.getRed() + (targetColor.getRed() - currentColor.getRed()) / 3);
                    int g = Math.max(0, currentColor.getGreen() + (targetColor.getGreen() - currentColor.getGreen()) / 3);
                    int b = Math.max(0, currentColor.getBlue() + (targetColor.getBlue() - currentColor.getBlue()) / 3);
                    
                    button.setBackground(new Color(r, g, b));
                    
                    if (Math.abs(r - targetColor.getRed()) < 2 && 
                        Math.abs(g - targetColor.getGreen()) < 2 && 
                        Math.abs(b - targetColor.getBlue()) < 2) {
                        hoverTimer.stop();
                    }
                });
                hoverTimer.start();
            }
        });
        
        return button;
    }
    
    /**
     * Creates a Scrabble-themed gradient background panel
     */
    public static JPanel createScrabbleGradientPanel() {
        return createGradientPanel(SCRABBLE_GRADIENT_START, SCRABBLE_GRADIENT_END);
    }

    /**
     * Creates a Scrabble card panel with shadow and rounded corners
     */
    public static JPanel createScrabbleCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,64,128,40)); // Deeper blue shadow
                g2.fillRoundRect(8,8,getWidth()-8,getHeight()-8,32,32);
                g2.setColor(SCRABBLE_BOARD); // Soft greenish card
                g2.fillRoundRect(0,0,getWidth()-8,getHeight()-8,32,32);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setBorder(javax.swing.BorderFactory.createEmptyBorder(36, 48, 36, 48));
        return card;
    }
    
    /**
     * Creates a Scrabble-styled button
     */
    public static JButton createScrabbleButton(String text, Color baseColor) {
        JButton button = createModernButton(text, baseColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 44));
        return button;
    }

    /**
     * Creates a modern card panel with shadow effect
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 12, 12);
                
                // Draw card
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 12, 12);
                
                g2.dispose();
            }
        };
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);
        return card;
    }
    
    /**
     * Creates a modern gradient panel
     */
    public static JPanel createGradientPanel(Color startColor, Color endColor) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, startColor,
                    getWidth(), getHeight(), endColor
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
    }
    
    /**
     * Creates a modern text field with floating label effect
     */
    public static JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Draw border
                if (hasFocus()) {
                    g2.setColor(PRIMARY_BLUE);
                    g2.setStroke(new BasicStroke(2));
                } else {
                    g2.setColor(BORDER_LIGHT);
                    g2.setStroke(new BasicStroke(1));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        field.setBackground(Color.WHITE);
        field.setOpaque(false);
        
        // Add placeholder effect
        if (placeholder != null && !placeholder.isEmpty()) {
            field.setForeground(TEXT_LIGHT);
            field.setText(placeholder);
            
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(TEXT_DARK);
                    }
                }
                
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setForeground(TEXT_LIGHT);
                        field.setText(placeholder);
                    }
                }
            });
        }
        
        return field;
    }
    
    /**
     * Creates a modern scroll pane with custom scrollbar
     */
    public static JScrollPane createModernScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Modern scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        return scrollPane;
    }
    
    /**
     * Creates a rounded border
     */
    public static Border createRoundedBorder(int radius) {
        return new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_LIGHT);
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                g2.dispose();
            }
            
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(2, 2, 2, 2);
            }
            
            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }
    
    /**
     * Gets responsive font size based on screen dimensions
     */
    public static int getResponsiveFontSize(int baseSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleFactor = Math.min(screenSize.width / 1920.0, screenSize.height / 1080.0);
        return Math.max(10, (int) (baseSize * scaleFactor));
    }
    
    /**
     * Gets responsive width based on screen dimensions
     */
    public static int getResponsiveWidth(int baseWidth) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleFactor = Math.min(screenSize.width / 1920.0, screenSize.height / 1080.0);
        return (int) (baseWidth * scaleFactor);
    }
    
    /**
     * Creates an animated loading spinner
     */
    public static JPanel createLoadingSpinner() {
        JPanel spinner = new JPanel() {
            private int angle = 0;
            private Timer timer;
            
            {
                timer = new Timer(50, e -> {
                    angle += 10;
                    if (angle >= 360) angle = 0;
                    repaint();
                });
                timer.start();
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = 15;
                
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                for (int i = 0; i < 8; i++) {
                    float alpha = (8 - i) / 8.0f;
                    g2.setColor(new Color(PRIMARY_BLUE.getRed(), PRIMARY_BLUE.getGreen(), PRIMARY_BLUE.getBlue(), (int) (255 * alpha)));
                    
                    double angleRad = Math.toRadians(angle + i * 45);
                    int x1 = centerX + (int) (radius * 0.6 * Math.cos(angleRad));
                    int y1 = centerY + (int) (radius * 0.6 * Math.sin(angleRad));
                    int x2 = centerX + (int) (radius * Math.cos(angleRad));
                    int y2 = centerY + (int) (radius * Math.sin(angleRad));
                    
                    g2.drawLine(x1, y1, x2, y2);
                }
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }
        };
        
        spinner.setOpaque(false);
        return spinner;
    }
    
    /**
     * Modern scrollbar UI
     */
    private static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(200, 200, 200);
            thumbDarkShadowColor = new Color(180, 180, 180);
            thumbHighlightColor = new Color(220, 220, 220);
            trackColor = new Color(245, 245, 245);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                            thumbBounds.width - 4, thumbBounds.height - 4, 6, 6);
            
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }
}

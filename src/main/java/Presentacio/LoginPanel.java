package Presentacio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Domini.ControladorDomini;

public class LoginPanel extends JPanel {
    private ControladorPresentacio cp;
    private ControladorDomini cd;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        // Gradient background
        JPanel backgroundPanel = ModernUI.createGradientPanel(
            new Color(63, 81, 181), new Color(233, 30, 99)
        );
        backgroundPanel.setLayout(new GridBagLayout());
        // Card panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,40));
                g2.fillRoundRect(8,8,getWidth()-8,getHeight()-8,32,32);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0,0,getWidth()-8,getHeight()-8,32,32);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        card.setPreferredSize(new Dimension(400, 480));
        // Logo & title
        JLabel logo = new JLabel("🎯", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Scrabble Login", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(63,81,181));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logo);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(30));
        // Username
        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        usernameField = ModernUI.createModernTextField("Enter your username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(20));
        // Password
        JLabel passLabel = new JLabel("🔒 Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224,224,224), 1),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(30));
        // Login button
        JButton loginBtn = ModernUI.createModernButton("🚀 Sign In", new Color(63,81,181));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setPreferredSize(new Dimension(200, 45));
        loginBtn.setForeground(Color.BLACK); // Ensure text is visible
        loginBtn.setBackground(new Color(63,81,181));
        loginBtn.addActionListener(e -> login());
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(15));
        // Register link
        JButton regBtn = ModernUI.createModernButton("📝 Register", new Color(233,30,99));
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        regBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        regBtn.setPreferredSize(new Dimension(200, 40));
        regBtn.setForeground(Color.BLACK); // Ensure text is visible
        regBtn.setBackground(new Color(233,30,99));
        regBtn.addActionListener(e -> cp.showRegisterPanel());
        card.add(regBtn);
        // Add card to background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        backgroundPanel.add(card, gbc);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (cd.iniciarSessio(username, password)) {
                cp.setCurrentUser(cd.getUsuari(username));
                cp.showMainMenuPanel();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
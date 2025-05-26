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
import java.awt.Toolkit;

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
import Domini.Persona;
import Domini.Usuari;

public class RegisterPanel extends JPanel {
    private ControladorPresentacio cp;
    private ControladorDomini cd;
    private JTextField usernameField, emailField, ageField, countryField;
    private JPasswordField passwordField, confirmPasswordField;

    public RegisterPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        // Responsive card size, always fits inside the screen with margin
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int margin = 32;
        int maxCardWidth = Math.min(420, screenSize.width - margin);
        int maxCardHeight = Math.min(650, screenSize.height - margin);
        int cardWidth = Math.max(320, maxCardWidth);
        int cardHeight = Math.max(480, maxCardHeight);

        // Gradient background
        JPanel backgroundPanel = ModernUI.createGradientPanel(
            ModernUI.GRADIENT_START, ModernUI.GRADIENT_END
        );
        backgroundPanel.setLayout(new GridBagLayout());

        // Card panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow
                g2.setColor(new Color(0,0,0,50));
                g2.fillRoundRect(10,10,getWidth()-20,getHeight()-20,36,36);
                // White card background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0,0,getWidth()-10,getHeight()-10,36,36);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 32, 36, 32));
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMaximumSize(new Dimension(cardWidth, cardHeight));
        card.setMinimumSize(new Dimension(320, 480));

        // Logo & title
        JLabel logo = new JLabel("🧩", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, Math.max(48, cardWidth/7)));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Create Your Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, Math.max(22, cardWidth/16)));
        title.setForeground(ModernUI.ACCENT_PINK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        JLabel subtitle = new JLabel("Join the Scrabble fun!", JLabel.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, Math.max(13, cardWidth/32)));
        subtitle.setForeground(ModernUI.TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));

        // Username
        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
        userLabel.setForeground(ModernUI.PRIMARY_BLUE);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(userLabel);
        card.add(Box.createVerticalStrut(4));
        usernameField = ModernUI.createModernTextField("Enter your username");
        usernameField.setMaximumSize(new Dimension(cardWidth-64, 38)); // Fit better in card
        usernameField.setPreferredSize(new Dimension(cardWidth-64, 38));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(13, cardWidth/32)));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(10));

        // Email
        card.add(new JLabel("📧 Email") {{
            setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
            setForeground(ModernUI.PRIMARY_BLUE);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }});
        card.add(Box.createVerticalStrut(4));
        emailField = ModernUI.createModernTextField("Enter your email");
        emailField.setMaximumSize(new Dimension(cardWidth-64, 38));
        emailField.setPreferredSize(new Dimension(cardWidth-64, 38));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(13, cardWidth/32)));
        card.add(emailField);
        card.add(Box.createVerticalStrut(10));

        // Age
        card.add(new JLabel("🎂 Age") {{
            setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
            setForeground(ModernUI.PRIMARY_BLUE);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }});
        card.add(Box.createVerticalStrut(4));
        ageField = ModernUI.createModernTextField("Enter your age");
        ageField.setMaximumSize(new Dimension(cardWidth-64, 38));
        ageField.setPreferredSize(new Dimension(cardWidth-64, 38));
        ageField.setAlignmentX(Component.CENTER_ALIGNMENT);
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(13, cardWidth/32)));
        card.add(ageField);
        card.add(Box.createVerticalStrut(10));

        // Country
        card.add(new JLabel("🌍 Country") {{
            setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
            setForeground(ModernUI.PRIMARY_BLUE);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }});
        card.add(Box.createVerticalStrut(4));
        countryField = ModernUI.createModernTextField("Enter your country");
        countryField.setMaximumSize(new Dimension(cardWidth-64, 38));
        countryField.setPreferredSize(new Dimension(cardWidth-64, 38));
        countryField.setAlignmentX(Component.CENTER_ALIGNMENT);
        countryField.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(13, cardWidth/32)));
        card.add(countryField);
        card.add(Box.createVerticalStrut(10));

        // Password
        card.add(new JLabel("🔒 Password") {{
            setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
            setForeground(ModernUI.PRIMARY_BLUE);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }});
        card.add(Box.createVerticalStrut(4));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(13, cardWidth/32)));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernUI.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        passwordField.setMaximumSize(new Dimension(cardWidth-64, 38));
        passwordField.setPreferredSize(new Dimension(cardWidth-64, 38));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));

        // Confirm Password
        card.add(new JLabel("🔒 Confirm Password") {{
            setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
            setForeground(ModernUI.PRIMARY_BLUE);
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }});
        card.add(Box.createVerticalStrut(4));
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(13, cardWidth/32)));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ModernUI.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        confirmPasswordField.setMaximumSize(new Dimension(cardWidth-64, 38));
        confirmPasswordField.setPreferredSize(new Dimension(cardWidth-64, 38));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(confirmPasswordField);
        card.add(Box.createVerticalStrut(18));

        // Register button
        JButton registerButton = ModernUI.createModernButton("🧩 Register", ModernUI.SUCCESS_GREEN);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(15, cardWidth/24)));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setPreferredSize(new Dimension(220, 45));
        registerButton.setFocusPainted(false);
        registerButton.setForeground(Color.BLACK); // Ensure text is visible
        registerButton.setBackground(ModernUI.SUCCESS_GREEN);
        registerButton.addActionListener(e -> register());
        card.add(registerButton);
        card.add(Box.createVerticalStrut(8));
        // Back to Login button
        JButton backButton = ModernUI.createModernButton("⬅️ Back to Login", ModernUI.ERROR_RED);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, Math.max(13, cardWidth/32)));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(220, 40));
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.BLACK); // Ensure text is visible
        backButton.setBackground(ModernUI.ERROR_RED);
        backButton.addActionListener(e -> cp.showLoginPanel());
        card.add(backButton);
        card.add(Box.createVerticalStrut(6));
        // Subtle footer
        JLabel footer = new JLabel("Already have an account? Log in and start playing!", JLabel.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(11, cardWidth/40)));
        footer.setForeground(ModernUI.TEXT_LIGHT);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(footer);

        // Make the card scrollable if needed (for small screens)
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.add(card, BorderLayout.CENTER);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(cardWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        // Add card to background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        backgroundPanel.add(scrollPane, gbc);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void register() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(cp.getFrame(), "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuari newUser = cd.crearUsuari(
                    usernameField.getText(),
                    emailField.getText(),
                    password,
                    ageField.getText(),
                    countryField.getText()
            );

            if (newUser instanceof Persona) {
                cd.afegirNouUsuariRanking((Persona) newUser);
            }

            JOptionPane.showMessageDialog(cp.getFrame(), "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            cp.showLoginPanel();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
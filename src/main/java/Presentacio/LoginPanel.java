package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private ScrabbleGUI mainGui;
    private ControladorDomini cd;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(ScrabbleGUI mainGui, ControladorDomini cd) {
        this.mainGui = mainGui;
        this.cd = cd;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Scrabble Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(usernameLabel, gbc);
        gbc.gridx++;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passwordLabel, gbc);
        gbc.gridx++;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        CommonComponents.styleButton(loginButton, new Color(66, 165, 245));
        loginButton.addActionListener(e -> login());
        add(loginButton, gbc);

        gbc.gridy++;
        JButton registerButton = new JButton("Register New User");
        CommonComponents.styleButton(registerButton, new Color(129, 199, 132));
        registerButton.addActionListener(e -> mainGui.showRegisterPanel());
        add(registerButton, gbc);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            if (cd.iniciarSessio(username, password)) {
                mainGui.setCurrentUser(cd.getUsuari(username));
                mainGui.showMainMenuPanel();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainGui.getFrame(), ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
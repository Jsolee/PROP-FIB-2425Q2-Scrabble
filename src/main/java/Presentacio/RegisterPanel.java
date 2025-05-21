package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        addField("Username:", usernameField = new JTextField(15), gbc);
        gbc.gridy++;
        addField("Email:", emailField = new JTextField(15), gbc);
        gbc.gridy++;
        addField("Age:", ageField = new JTextField(15), gbc);
        gbc.gridy++;
        addField("Country:", countryField = new JTextField(15), gbc);
        gbc.gridy++;
        addField("Password:", passwordField = new JPasswordField(15), gbc);
        gbc.gridy++;
        addField("Confirm Password:", confirmPasswordField = new JPasswordField(15), gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        CommonComponents.styleButton(registerButton, new Color(76, 175, 80));
        registerButton.addActionListener(e -> register());
        add(registerButton, gbc);

        gbc.gridy++;
        JButton backButton = new JButton("Back to Login");
        CommonComponents.styleButton(backButton, new Color(239, 83, 80));
        backButton.addActionListener(e -> cp.showLoginPanel());
        add(backButton, gbc);
    }

    private void addField(String label, JComponent field, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(jLabel, gbc);
        gbc.gridx++;
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        add(field, gbc);
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
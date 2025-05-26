package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panell de registre per a l'aplicació Scrabble Game.
 * Permet als nous usuaris crear un compte introduint la informació necessària:
 * nom d'usuari, correu electrònic, edat, país i contrasenya.
 * 
 * Aquest panell inclou validacions bàsiques per garantir que les contrasenyes coincideixen
 * i gestiona la creació d'usuaris mitjançant el ControladorDomini.
 * Un cop registrat correctament, l'usuari és redirigit al panell de login.
 */
public class RegisterPanel extends JPanel {
    /** Controlador de presentació */
    private ControladorPresentacio cp;
    /** Controlador de domini per a la lògica del joc */
    private ControladorDomini cd;
    /** Camps de text per a l'entrada de dades */
    private JTextField usernameField, emailField, ageField, countryField;
    /** Camps de text per a l'entrada de la contrasenya */
    private JPasswordField passwordField, confirmPasswordField;

    /**
     * Constructor del panell de registre.
     * Inicialitza els components gràfics i els gestors d'esdeveniments.
     *
     * @param cp Controlador de presentació per a la navegació entre pantalles
     * @param cd Controlador de domini per a la lògica del joc
     */
    public RegisterPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    /**
     * Inicialitza els components gràfics del panell de registre.
     * Configura el disseny utilitzant GridBagLayout per organitzar els elements.
     * Crea i posiciona els components:
     * - Títol del panell
     * - Camps per introduir les dades de l'usuari
     * - Botó per registrar l'usuari
     * - Botó per tornar al panell de login
     *
     * Els components s'estilitzen amb fonts i colors per millorar la interfície d'usuari.
     */
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

    /**
     * Afegeix un camp de text amb la seva etiqueta al panell.
     * 
     * @param label Etiqueta del camp de text
     * @param field Camp de text a afegir
     * @param gbc Constraints per a la posició del component
     */
    private void addField(String label, JComponent field, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(jLabel, gbc);
        gbc.gridx++;
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        add(field, gbc);
    }

    /**
     * Gestor d'esdeveniment per al botó de registre.
     * Llegeix les dades introduïdes i intenta crear un nou usuari.
     * Si les contrasenyes coincideixen i la creació és exitosa, redirigeix a l'usuari al panell de login.
     * Si hi ha un error, mostra un missatge d'error.
     */
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
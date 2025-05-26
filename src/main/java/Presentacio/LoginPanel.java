package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panell de login per a l'aplicació Scrabble Game.
 * Permet als usuaris iniciar sessió amb el seu nom d'usuari i contrasenya.
 * També proporciona una opció per registrar un nou usuari.
 */
public class LoginPanel extends JPanel {

    /** Controlador de presentació  */
    private ControladorPresentacio cp;
    /** Controlador de domini per a la lògica del joc */
    private ControladorDomini cd;
    /** Camps de text per a l'entrada de dades */
    private JTextField usernameField;
    /** Camp de text per a l'entrada de la contrasenya */
    private JPasswordField passwordField;

    /**
     * Constructor del panell de login.
     * Inicialitza els components gràfics i els gestors d'esdeveniments.
     *
     * @param cp Controlador de presentació per a la navegació entre pantalles
     * @param cd Controlador de domini per a la lògica del joc
     */
    public LoginPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    /**
     * Inicialitza els components gràfics del panell de login.
     * Configura el disseny utilitzant GridBagLayout per organitzar els elements.
     * Crea i posiciona els components:
     * - Títol de l'aplicació
     * - Camp per introduir el nom d'usuari
     * - Camp per introduir la contrasenya
     * - Botó de login que valida les credencials
     * - Botó de registre que redirigeix al panell de registre
     * 
     * Els components s'estilitzen amb fonts i colors per millorar la interfície d'usuari.
     * S'afegeixen listeners als botons per gestionar les interaccions de l'usuari.
     */
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
        registerButton.addActionListener(e -> cp.showRegisterPanel());
        add(registerButton, gbc);
    }

    /**
     * Gestor d'esdeveniment per al botó de login.
     * Llegeix les credencials introduïdes i intenta iniciar sessió.
     * Si les credencials són vàlides, redirigeix a l'usuari al menú principal.
     * Si hi ha un error, mostra un missatge d'error.
     */
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
package Presentacio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Domini.ControladorDomini;
import Domini.Partida;
import Domini.Usuari;

/**
 * Panell del menú principal de l'aplicació Scrabble Game.
 * Permet als usuaris accedir a les diferents funcionalitats del joc,
 * com jugar, veure el perfil, rànquings, opcions i tancar sessió.
 *
 * També gestiona la benvinguda a l'usuari i les accions del menú.
 */
public class MainMenuPanel extends JPanel {
    /** Controlador de presentació */
    private ControladorPresentacio cp;
    /** Controlador de domini per a la lògica del joc */
    private ControladorDomini cd;
    /** Etiqueta per mostrar el missatge de benvinguda */
    private JLabel welcomeLabel;
    /** Panell de la targeta del menú principal */
    private JPanel card;

    /**
     * Constructor del panell del menú principal.
     * Inicialitza els components gràfics i els gestors d'esdeveniments.
     *
     * @param cp Controlador de presentació per a la navegació entre pantalles
     * @param cd Controlador de domini per a la lògica del joc
     */
    public MainMenuPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    /**
     * Inicialitza els components gràfics del panell del menú principal.
     * Configura el disseny utilitzant GridBagLayout per organitzar els elements.
     * Crea i posiciona els components:
     * - Etiqueta de benvinguda
     * - Botons per accedir a les diferents funcionalitats del joc
     *
     * Els components s'estilitzen amb fonts i colors per millorar la interfície d'usuari.
     * S'afegeixen listeners als botons per gestionar les interaccions de l'usuari.
     */
    private void initialize() {
        setLayout(new BorderLayout());
        JPanel backgroundPanel = ModernUI.createScrabbleGradientPanel();
        backgroundPanel.setLayout(new java.awt.GridBagLayout());
        card = ModernUI.createScrabbleCard();
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 520));
        JLabel logo = new JLabel("🟩🟦", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Scrabble", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(ModernUI.SCRABBLE_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(logo);
        card.add(javax.swing.Box.createVerticalStrut(10));
        card.add(title);
        card.add(javax.swing.Box.createVerticalStrut(18));
        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(welcomeLabel);
        card.add(javax.swing.Box.createVerticalStrut(18));
        addMenuButton("Play Game", new Color(66, 165, 245), this::showGameOptions);
        card.add(javax.swing.Box.createVerticalStrut(10));
        addMenuButton("View Profile", new Color(129, 199, 132), cp::showProfilePanel);
        card.add(javax.swing.Box.createVerticalStrut(10));
        addMenuButton("View Rankings", new Color(255, 183, 77), cp::showRankingPanel);
        card.add(javax.swing.Box.createVerticalStrut(10));
        addMenuButton("Options", new Color(171, 71, 188), cp::showOptionsPanel);
        card.add(javax.swing.Box.createVerticalStrut(10));
        addMenuButton("Logout", new Color(239, 83, 80), this::logout);
        backgroundPanel.add(card, new java.awt.GridBagConstraints());
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private void addMenuButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        CommonComponents.styleButton(button, color);
        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> action.run());
        card.add(button);
    }

    /**
     * Actualitza el missatge de benvinguda amb el nom de l'usuari actual.
     * Si no hi ha cap usuari connectat, mostra "Guest".
     */
    public void updateWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + (cp.getCurrentUser() != null ? cp.getCurrentUser().getNom() : "Guest"));
    }

    /**
     * Mostra les opcions de joc disponibles per a l'usuari.
     * Permet seleccionar entre jugar contra un altre jugador, jugar contra un bot,
     * carregar una partida guardada o cancel·lar l'acció.
     */
    private void showGameOptions() {
        Object[] options = {"1 vs 1", "1 vs Bot", "Load Game", "Cancel"};
        int choice = JOptionPane.showOptionDialog(cp.getFrame(),
                "Select game mode:",
                "Game Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            create1vs1Game();
        } else if (choice == 1) {
            createBotGame();
        } else if (choice == 2) {
            loadGame();
        }
    }

    /**
     * Crea una partida de Scrabble contra un altre jugador.
     * Permet registrar un nou usuari o iniciar sessió amb un usuari existent.
     * Si es registra un nou usuari, es demanen les dades necessàries.
     * Si s'inicia sessió, es verifiquen les credencials.
     * Un cop l'usuari està registrat o ha iniciat sessió, es crea la partida.
     */
    private void create1vs1Game() {
        // Primero preguntamos si quiere registrar o iniciar sesión
        Object[] loginOptions = {"Login with existing user", "Register new opponent", "Cancel"};
        int loginChoice = JOptionPane.showOptionDialog(cp.getFrame(),
                "Choose option for opponent:",
                "Opponent Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                loginOptions,
                loginOptions[0]);
                
        // Cancelar el proceso
        if (loginChoice == 2 || loginChoice == JOptionPane.CLOSED_OPTION) {
            return;
        }
        
        // Registrar nuevo usuario
        if (loginChoice == 1) {
            // Solicitar datos para el nuevo usuario
            JTextField usernameField = new JTextField();
            JTextField emailField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            JTextField ageField = new JTextField();
            JTextField countryField = new JTextField();
            
            Object[] message = {
                "Username:", usernameField,
                "Email:", emailField,
                "Password:", passwordField,
                "Age:", ageField,
                "Country:", countryField
            };

            int option = JOptionPane.showConfirmDialog(cp.getFrame(), message, "Register New Player", JOptionPane.OK_CANCEL_OPTION);
            if (option != JOptionPane.OK_OPTION) {
                return;
            }
            
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String age = ageField.getText();
            String country = countryField.getText();
            
            try {
                // Crear el nuevo usuario
                Usuari opponent = cd.crearUsuari(username, email, password, age, country);
                
                // Continuar amb la creació de la partida
                createGameWithOpponent(opponent);
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // Iniciar sesión amb usuari existent
        else {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            
            Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
            };
            
            int option = JOptionPane.showConfirmDialog(cp.getFrame(), message, "Login Opponent", JOptionPane.OK_CANCEL_OPTION);
            if (option != JOptionPane.OK_OPTION) return;
            
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty()) return;

            try {
                // Verificar credenciales
                boolean opponent = cd.iniciarSessio(username, password);
                if (!opponent) {
                    JOptionPane.showMessageDialog(cp.getFrame(), "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Continuar amb la creació de la partida
                createGameWithOpponent(cd.getUsuari(username));
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Crea una partida de Scrabble contra un oponent.
     * Permet seleccionar el nom de la partida i l'idioma.
     * Un cop seleccionats, es crea la partida amb el opponent com a usuari.
     * Si hi ha un error en la creació de la partida,
     * es mostra un missatge d'error.
     * 
     * @param opponent L'usuari que serà l'oponent en la partida
     */    
    private void createGameWithOpponent(Usuari opponent) {
        String gameName = JOptionPane.showInputDialog(cp.getFrame(), "Enter game name:");
        if (gameName == null || gameName.isEmpty()) return;

        String[] languages = {"catalan", "castellano", "english"};
        String language = (String) JOptionPane.showInputDialog(cp.getFrame(),
                "Select language:",
                "Game Language",
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                languages[0]);

        if (language == null) return;

        List<Usuari> players = new ArrayList<>();
        players.add(cp.getCurrentUser());
        players.add(opponent);

        try {
            cp.setCurrentGame(cd.crearPartida(gameName, players, language));
            cp.showGamePanel();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea una partida de Scrabble contra un bot.
     * Permet seleccionar el nom de la partida i l'idioma.
     * Un cop seleccionats, es crea la partida amb el bot com a oponent.
     * Si hi ha un error en la creació de la partida,
     * es mostra un missatge d'error.
     */
    private void createBotGame() {
        String gameName = JOptionPane.showInputDialog(cp.getFrame(), "Enter game name:");
        if (gameName == null || gameName.isEmpty()) return;

        String[] languages = {"catalan", "castellano", "english"};
        String language = (String) JOptionPane.showInputDialog(cp.getFrame(),
                "Select language:",
                "Game Language",
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                languages[0]);

        if (language == null) return;

        List<Usuari> players = new ArrayList<>();
        players.add(cp.getCurrentUser());
        players.add(cd.getBot());

        try {
            cp.setCurrentGame(cd.crearPartida(gameName, players, language));
            cp.showGamePanel();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega una partida guardada per a l'usuari actual.
     * Mostra un diàleg per seleccionar la partida a carregar.
     * Si no hi ha partides guardades, mostra un missatge d'informació.
     * Un cop seleccionada la partida, es carrega i es mostra al panell de joc.
     */
    private void loadGame() {
        List<Partida> games = cd.getPartidesEnCurs(cp.getCurrentUser());
        if (games == null || games.isEmpty()) {
            JOptionPane.showMessageDialog(cp.getFrame(), "No saved games found", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] gameNames = games.stream().map(Partida::getNom).toArray(String[]::new);
        String selectedGame = (String) JOptionPane.showInputDialog(cp.getFrame(),
                "Select game to load:",
                "Load Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gameNames,
                gameNames[0]);

        if (selectedGame != null) {
            cp.setCurrentGame(cd.getPartida(selectedGame));
            cp.showGamePanel();
        }
    }

    /**
     * Tanca la sessió de l'usuari actual i mostra el panell de login.
     * Elimina l'usuari actual del controlador de presentació
     * i tanca la sessió al controlador de domini.
     */
    private void logout() {
        cd.tancarSessio(cp.getCurrentUser().getNom());
        cp.setCurrentUser(null);
        cp.showLoginPanel();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag && welcomeLabel != null) {
            welcomeLabel.setForeground(ModernUI.PRIMARY_BLUE); // Always set visible color
        }
    }
}
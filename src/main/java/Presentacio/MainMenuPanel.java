package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainMenuPanel extends JPanel {
    private ScrabbleGUI mainGui;
    private ControladorDomini cd;
    private JLabel welcomeLabel;

    public MainMenuPanel(ScrabbleGUI mainGui, ControladorDomini cd) {
        this.mainGui = mainGui;
        this.cd = cd;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        addMenuButton("Play Game", new Color(66, 165, 245), e -> showGameOptions(), gbc);
        gbc.gridy++;
        addMenuButton("View Profile", new Color(129, 199, 132), e -> {
            mainGui.showProfilePanel();
        }, gbc);
        gbc.gridy++;
        addMenuButton("View Rankings", new Color(255, 183, 77), e -> {
            mainGui.showRankingPanel();
        }, gbc);
        gbc.gridy++;
        addMenuButton("Options", new Color(171, 71, 188), e -> mainGui.showOptionsPanel(), gbc);
        gbc.gridy++;
        addMenuButton("Logout", new Color(239, 83, 80), e -> logout(), gbc);
    }

    private void addMenuButton(String text, Color color, ActionListener listener, GridBagConstraints gbc) {
        JButton button = new JButton(text);
        CommonComponents.styleButton(button, color);
        button.setPreferredSize(new Dimension(200, 60));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(listener);
        add(button, gbc);
    }

    public void updateWelcomeMessage() {
        welcomeLabel.setText("Welcome, " + (mainGui.getCurrentUser() != null ? mainGui.getCurrentUser().getNom() : "Guest"));
    }

    private void showGameOptions() {
        Object[] options = {"1 vs 1", "1 vs Bot", "Load Game", "Cancel"};
        int choice = JOptionPane.showOptionDialog(mainGui.getFrame(),
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

    private void create1vs1Game() {
        // Primero preguntamos si quiere registrar o iniciar sesión
        Object[] loginOptions = {"Login with existing user", "Register new opponent", "Cancel"};
        int loginChoice = JOptionPane.showOptionDialog(mainGui.getFrame(),
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

            int option = JOptionPane.showConfirmDialog(mainGui.getFrame(), message, "Register New Player", JOptionPane.OK_CANCEL_OPTION);
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
                
                // Continuar con la creación de la partida
                createGameWithOpponent(opponent);
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainGui.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // Iniciar sesión con usuario existente
        else {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();
            
            Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
            };
            
            int option = JOptionPane.showConfirmDialog(mainGui.getFrame(), message, "Login Opponent", JOptionPane.OK_CANCEL_OPTION);
            if (option != JOptionPane.OK_OPTION) return;
            
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty()) return;

            try {
                // Verificar credenciales
                boolean opponent = cd.iniciarSessio(username, password);
                if (!opponent) {
                    JOptionPane.showMessageDialog(mainGui.getFrame(), "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Continuar con la creación de la partida
                createGameWithOpponent(cd.getUsuari(username));
                
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainGui.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método auxiliar para crear la partida una vez que tenemos el oponente
    private void createGameWithOpponent(Usuari opponent) {
        String gameName = JOptionPane.showInputDialog(mainGui.getFrame(), "Enter game name:");
        if (gameName == null || gameName.isEmpty()) return;

        String[] languages = {"catalan", "castellano", "english"};
        String language = (String) JOptionPane.showInputDialog(mainGui.getFrame(),
                "Select language:",
                "Game Language",
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                languages[0]);

        if (language == null) return;

        List<Usuari> players = new ArrayList<>();
        players.add(mainGui.getCurrentUser());
        players.add(opponent);

        try {
            mainGui.setCurrentGame(cd.crearPartida(gameName, players, language));
            mainGui.showGamePanel();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainGui.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createBotGame() {
        String gameName = JOptionPane.showInputDialog(mainGui.getFrame(), "Enter game name:");
        if (gameName == null || gameName.isEmpty()) return;

        String[] languages = {"catalan", "castellano", "english"};
        String language = (String) JOptionPane.showInputDialog(mainGui.getFrame(),
                "Select language:",
                "Game Language",
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                languages[0]);

        if (language == null) return;

        List<Usuari> players = new ArrayList<>();
        players.add(mainGui.getCurrentUser());
        players.add(cd.getBot());

        try {
            mainGui.setCurrentGame(cd.crearPartida(gameName, players, language));
            mainGui.showGamePanel();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(mainGui.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        List<Partida> games = cd.getPartidesEnCurs(mainGui.getCurrentUser());
        if (games == null || games.isEmpty()) {
            JOptionPane.showMessageDialog(mainGui.getFrame(), "No saved games found", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] gameNames = games.stream().map(Partida::getNom).toArray(String[]::new);
        String selectedGame = (String) JOptionPane.showInputDialog(mainGui.getFrame(),
                "Select game to load:",
                "Load Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gameNames,
                gameNames[0]);

        if (selectedGame != null) {
            mainGui.setCurrentGame(cd.getPartida(selectedGame));
            mainGui.showGamePanel();
        }
    }

    private void logout() {
        cd.tancarSessio(mainGui.getCurrentUser().getNom());
        mainGui.setCurrentUser(null);
        mainGui.showLoginPanel();
    }
}
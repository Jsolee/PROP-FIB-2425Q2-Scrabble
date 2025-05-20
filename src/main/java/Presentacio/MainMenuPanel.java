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
        String username = JOptionPane.showInputDialog(mainGui.getFrame(), "Enter opponent's username:");
        if (username == null || username.isEmpty()) return;

        try {
            Usuari opponent = cd.getUsuari(username);
            if (opponent == null) {
                JOptionPane.showMessageDialog(mainGui.getFrame(), "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

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
package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ScrabbleGUI {
    private ControladorDomini cd;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Usuari currentUser;
    private Partida currentGame;

    // GUI Panels
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel mainMenuPanel;
    private JPanel gamePanel;
    private JPanel profilePanel;
    private JPanel optionsPanel;
    private JPanel rankingPanel;

    public ScrabbleGUI() {
        cd = new ControladorDomini();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createLoginPanel();
        createRegisterPanel();
        createMainMenuPanel();
        createGamePanel();
        createProfilePanel();
        createOptionsPanel();
        createRankingPanel();

        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");
        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(gamePanel, "game");
        cardPanel.add(profilePanel, "profile");
        cardPanel.add(optionsPanel, "options");
        cardPanel.add(rankingPanel, "ranking");

        frame.add(cardPanel);
        cardLayout.show(cardPanel, "login");
        frame.setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Scrabble Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (cd.iniciarSessio(username, password)) {
                    currentUser = cd.getUsuari(username);
                    cardLayout.show(cardPanel, "mainMenu");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(loginButton, gbc);

        gbc.gridy++;
        JButton registerButton = new JButton("Register New User");
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "register"));
        loginPanel.add(registerButton, gbc);
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        registerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        registerPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx++;
        JTextField emailField = new JTextField(15);
        registerPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx++;
        JTextField ageField = new JTextField(15);
        registerPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(new JLabel("Country:"), gbc);
        gbc.gridx++;
        JTextField countryField = new JTextField(15);
        registerPanel.add(countryField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        registerPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        registerPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx++;
        JPasswordField confirmPasswordField = new JPasswordField(15);
        registerPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
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

                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "login");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        registerPanel.add(registerButton, gbc);

        gbc.gridy++;
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
        registerPanel.add(backButton, gbc);
    }

    private void createMainMenuPanel() {
        mainMenuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel welcomeLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getNom() : "Guest"));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainMenuPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JButton playButton = new JButton("Play Game");
        playButton.addActionListener(e -> showGameOptions());
        mainMenuPanel.add(playButton, gbc);

        gbc.gridy++;
        JButton profileButton = new JButton("View Profile");
        profileButton.addActionListener(e -> {
            updateProfileInfo();
            cardLayout.show(cardPanel, "profile");
        });
        mainMenuPanel.add(profileButton, gbc);

        gbc.gridy++;
        JButton rankingButton = new JButton("View Rankings");
        rankingButton.addActionListener(e -> {
            updateRankingInfo(1); // Default to points ranking
            cardLayout.show(cardPanel, "ranking");
        });
        mainMenuPanel.add(rankingButton, gbc);

        gbc.gridy++;
        JButton optionsButton = new JButton("Options");
        optionsButton.addActionListener(e -> cardLayout.show(cardPanel, "options"));
        mainMenuPanel.add(optionsButton, gbc);

        gbc.gridy++;
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            cd.tancarSessio(currentUser.getNom());
            currentUser = null;
            cardLayout.show(cardPanel, "login");
        });
        mainMenuPanel.add(logoutButton, gbc);
    }

    private void showGameOptions() {
        Object[] options = {"1 vs 1", "1 vs Bot", "Load Game", "Cancel"};
        int choice = JOptionPane.showOptionDialog(frame,
                "Select game mode:",
                "Game Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) { // 1 vs 1
            create1vs1Game();
        } else if (choice == 1) { // 1 vs Bot
            createBotGame();
        } else if (choice == 2) { // Load Game
            loadGame();
        }
    }

    private void create1vs1Game() {
        String username = JOptionPane.showInputDialog(frame, "Enter opponent's username:");
        if (username == null || username.isEmpty()) return;

        try {
            Usuari opponent = cd.getUsuari(username);
            if (opponent == null) {
                JOptionPane.showMessageDialog(frame, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String gameName = JOptionPane.showInputDialog(frame, "Enter game name:");
            if (gameName == null || gameName.isEmpty()) return;

            String[] languages = {"catalan", "castellano", "english"};
            String language = (String) JOptionPane.showInputDialog(frame,
                    "Select language:",
                    "Game Language",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    languages,
                    languages[0]);

            if (language == null) return;

            List<Usuari> players = new ArrayList<>();
            players.add(currentUser);
            players.add(opponent);

            currentGame = cd.crearPartida(gameName, players, language);
            updateGameBoard();
            cardLayout.show(cardPanel, "game");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createBotGame() {
        String gameName = JOptionPane.showInputDialog(frame, "Enter game name:");
        if (gameName == null || gameName.isEmpty()) return;

        String[] languages = {"catalan", "castellano", "english"};
        String language = (String) JOptionPane.showInputDialog(frame,
                "Select language:",
                "Game Language",
                JOptionPane.QUESTION_MESSAGE,
                null,
                languages,
                languages[0]);

        if (language == null) return;

        List<Usuari> players = new ArrayList<>();
        players.add(currentUser);
        players.add(cd.getBot());

        try {
            currentGame = cd.crearPartida(gameName, players, language);
            updateGameBoard();
            cardLayout.show(cardPanel, "game");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        List<Partida> games = cd.getPartidesEnCurs(currentUser);
        if (games == null || games.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No saved games found", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] gameNames = games.stream().map(Partida::getNom).toArray(String[]::new);
        String selectedGame = (String) JOptionPane.showInputDialog(frame,
                "Select game to load:",
                "Load Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gameNames,
                gameNames[0]);

        if (selectedGame != null) {
            currentGame = cd.getPartida(selectedGame);
            updateGameBoard();
            cardLayout.show(cardPanel, "game");
        }
    }

    private void createGamePanel() {
        gamePanel = new JPanel(new BorderLayout());

        // Board panel
        JPanel boardPanel = new JPanel(new GridLayout(15, 15));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.add(boardPanel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));

        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        infoPanel.add(playerPanel);

        JPanel rackPanel = new JPanel(new FlowLayout());
        rackPanel.setBorder(BorderFactory.createTitledBorder("Your Rack"));
        infoPanel.add(rackPanel);

        gamePanel.add(infoPanel, BorderLayout.SOUTH);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());

        JButton playWordButton = new JButton("Play Word");
        playWordButton.addActionListener(e -> playWord());
        controlPanel.add(playWordButton);

        JButton exchangeButton = new JButton("Exchange Tiles");
        exchangeButton.addActionListener(e -> exchangeTiles());
        controlPanel.add(exchangeButton);

        JButton passButton = new JButton("Pass Turn");
        passButton.addActionListener(e -> {
            currentGame.passarTorn();
            if (currentGame.getJugadorActual() instanceof Bot) {
                botTurn();
            }
            updateGameBoard();
        });
        controlPanel.add(passButton);

        JButton resignButton = new JButton("Resign");
        resignButton.addActionListener(e -> resignGame());
        controlPanel.add(resignButton);

        JButton saveButton = new JButton("Save Game");
        saveButton.addActionListener(e -> {
            currentGame.guardarPartida();
            JOptionPane.showMessageDialog(frame, "Game saved successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        controlPanel.add(saveButton);

        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to exit the game?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(cardPanel, "mainMenu");
            }
        });
        controlPanel.add(exitButton);

        gamePanel.add(controlPanel, BorderLayout.NORTH);
    }

    private void updateGameBoard() {
        if (currentGame == null) return;

        JPanel boardPanel = (JPanel) ((BorderLayout) gamePanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        boardPanel.removeAll();

        Taulell board = currentGame.getTaulell();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Casella cell = board.getCasella(i, j);
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(30, 30));

                if (cell.getFitxa() != null) {
                    cellButton.setText(cell.getFitxa().getLletra());
                    cellButton.setToolTipText("Value: " + cell.getFitxa().getValor());
                }

                // Color coding for special cells could be added here
                boardPanel.add(cellButton);
            }
        }

        // Update player info
        JPanel infoPanel = (JPanel) ((BorderLayout) gamePanel.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        JPanel playerPanel = (JPanel) infoPanel.getComponent(0);
        playerPanel.removeAll();

        List<Usuari> players = currentGame.getJugadors();
        List<Integer> scores = currentGame.getPuntuacions();
        for (int i = 0; i < players.size(); i++) {
            JLabel playerLabel = new JLabel(players.get(i).getNom() + ": " + scores.get(i) + " points");
            if (players.get(i).equals(currentGame.getJugadorActual())) {
                playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD));
            }
            playerPanel.add(playerLabel);
        }

        // Update rack
        JPanel rackPanel = (JPanel) infoPanel.getComponent(1);
        rackPanel.removeAll();

        List<Fitxa> rack = currentGame.getAtril();
        for (int i = 0; i < rack.size(); i++) {
            Fitxa tile = rack.get(i);
            JButton tileButton = new JButton(tile.getLletra());
            tileButton.setToolTipText("Value: " + tile.getValor());
            rackPanel.add(tileButton);
        }

        frame.revalidate();
        frame.repaint();
    }

    private void playWord() {
        if (!currentGame.getJugadorActual().equals(currentUser)) {
            JOptionPane.showMessageDialog(frame, "It's not your turn", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog wordDialog = new JDialog(frame, "Play Word", true);
        wordDialog.setLayout(new BorderLayout());
        wordDialog.setSize(400, 300);

        JPanel inputPanel = new JPanel(new GridLayout(4, 1));

        // Tile selection
        JPanel tilePanel = new JPanel();
        tilePanel.setBorder(BorderFactory.createTitledBorder("Select Tiles"));
        List<Fitxa> rack = currentGame.getAtril();
        ButtonGroup tileGroup = new ButtonGroup();
        for (int i = 0; i < rack.size(); i++) {
            JRadioButton tileButton = new JRadioButton(rack.get(i).getLletra() + " (" + rack.get(i).getValor() + ")");
            tileButton.setActionCommand(String.valueOf(i));
            tileGroup.add(tileButton);
            tilePanel.add(tileButton);
        }
        inputPanel.add(tilePanel);

        // Position input
        JPanel positionPanel = new JPanel();
        positionPanel.setBorder(BorderFactory.createTitledBorder("Position"));
        JTextField rowField = new JTextField(2);
        JTextField colField = new JTextField(2);
        positionPanel.add(new JLabel("Row:"));
        positionPanel.add(rowField);
        positionPanel.add(new JLabel("Col:"));
        positionPanel.add(colField);
        inputPanel.add(positionPanel);

        // Orientation
        JPanel orientationPanel = new JPanel();
        orientationPanel.setBorder(BorderFactory.createTitledBorder("Orientation"));
        ButtonGroup orientationGroup = new ButtonGroup();
        JRadioButton horizontalButton = new JRadioButton("Horizontal");
        horizontalButton.setSelected(true);
        JRadioButton verticalButton = new JRadioButton("Vertical");
        orientationGroup.add(horizontalButton);
        orientationGroup.add(verticalButton);
        orientationPanel.add(horizontalButton);
        orientationPanel.add(verticalButton);
        inputPanel.add(orientationPanel);

        wordDialog.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int tileIndex = Integer.parseInt(tileGroup.getSelection().getActionCommand());
                int row = Integer.parseInt(rowField.getText());
                int col = Integer.parseInt(colField.getText());
                String orientation = horizontalButton.isSelected() ? "H" : "V";

                LinkedHashMap<int[], Fitxa> moves = new LinkedHashMap<>();
                List<Integer> indices = new ArrayList<>();
                indices.add(tileIndex);

                int[] position = {row, col};
                moves.put(position, rack.get(tileIndex));

                int score = cd.jugarParaula(currentGame, moves, orientation, indices);
                if (score != -1) {
                    JOptionPane.showMessageDialog(wordDialog, "Word played successfully! Score: " + score, "Success", JOptionPane.INFORMATION_MESSAGE);
                    currentGame.passarTorn();
                    wordDialog.dispose();

                    if (currentGame.getJugadorActual() instanceof Bot) {
                        botTurn();
                    }

                    updateGameBoard();
                } else {
                    JOptionPane.showMessageDialog(wordDialog, "Invalid word or position", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(wordDialog, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> wordDialog.dispose());
        buttonPanel.add(cancelButton);

        wordDialog.add(buttonPanel, BorderLayout.SOUTH);
        wordDialog.setVisible(true);
    }

    private void exchangeTiles() {
        if (!currentGame.getJugadorActual().equals(currentUser)) {
            JOptionPane.showMessageDialog(frame, "It's not your turn", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(frame,
                "Enter tile indices to exchange (space separated):\n" +
                        "Your tiles: " + getRackString());

        if (input == null || input.isEmpty()) return;

        try {
            String[] indices = input.split(" ");
            cd.canviDeFitxes(currentGame, indices);
            currentGame.passarTorn();

            if (currentGame.getJugadorActual() instanceof Bot) {
                botTurn();
            }

            updateGameBoard();
            JOptionPane.showMessageDialog(frame, "Tiles exchanged successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getRackString() {
        List<Fitxa> rack = currentGame.getAtril();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rack.size(); i++) {
            sb.append(i).append(":").append(rack.get(i).getLletra()).append(" ");
        }
        return sb.toString();
    }

    private void resignGame() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to resign?",
                "Confirm Resign",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cd.acabarPartida(currentGame);
            Usuari winner = currentGame.determinarGuanyador();
            JOptionPane.showMessageDialog(frame,
                    "Game over! Winner: " + winner.getNom(),
                    "Game Ended",
                    JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardPanel, "mainMenu");
        }
    }

    private void botTurn() {
        cd.posarParaulaBot(currentGame, currentGame.getJugadorActual());
        updateGameBoard();
    }

    private void createProfilePanel() {
        profilePanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name: ");
        infoPanel.add(nameLabel);

        JLabel emailLabel = new JLabel("Email: ");
        infoPanel.add(emailLabel);

        JLabel ageLabel = new JLabel("Age: ");
        infoPanel.add(ageLabel);

        JLabel countryLabel = new JLabel("Country: ");
        infoPanel.add(countryLabel);

        JLabel statsLabel = new JLabel("Statistics:");
        statsLabel.setFont(statsLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(statsLabel);

        JLabel gamesPlayedLabel = new JLabel("Games Played: ");
        infoPanel.add(gamesPlayedLabel);

        JLabel gamesWonLabel = new JLabel("Games Won: ");
        infoPanel.add(gamesWonLabel);

        JLabel gamesLostLabel = new JLabel("Games Lost: ");
        infoPanel.add(gamesLostLabel);

        JLabel totalScoreLabel = new JLabel("Total Score: ");
        infoPanel.add(totalScoreLabel);

        JLabel recordLabel = new JLabel("Personal Record: ");
        infoPanel.add(recordLabel);

        JLabel levelLabel = new JLabel("Ranking Level: ");
        infoPanel.add(levelLabel);

        profilePanel.add(infoPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));
        profilePanel.add(backButton, BorderLayout.SOUTH);
    }

    private void updateProfileInfo() {
        if (!(currentUser instanceof Persona)) return;

        Persona persona = (Persona) currentUser;
        Estadistiques stats = persona.getEstadistiques();

        JPanel infoPanel = (JPanel) profilePanel.getComponent(0);

        ((JLabel) infoPanel.getComponent(0)).setText("Name: " + persona.getNom());
        ((JLabel) infoPanel.getComponent(1)).setText("Email: " + persona.getCorreu());
        ((JLabel) infoPanel.getComponent(2)).setText("Age: " + persona.getEdat());
        ((JLabel) infoPanel.getComponent(3)).setText("Country: " + persona.getPais());

        ((JLabel) infoPanel.getComponent(5)).setText("Games Played: " + stats.getPartidesJugades());
        ((JLabel) infoPanel.getComponent(6)).setText("Games Won: " + stats.getPartidesGuanyades());
        ((JLabel) infoPanel.getComponent(7)).setText("Games Lost: " + stats.getPartidesPerdudes());
        ((JLabel) infoPanel.getComponent(8)).setText("Total Score: " + stats.getPuntuacioTotal());
        ((JLabel) infoPanel.getComponent(9)).setText("Personal Record: " + stats.getRecordPersonal());
        ((JLabel) infoPanel.getComponent(10)).setText("Ranking Level: " + stats.getNivellRanking());
    }

    private void createOptionsPanel() {
        optionsPanel = new JPanel(new BorderLayout());

        JPanel optionsContent = new JPanel(new GridLayout(0, 1));
        optionsContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton changePassButton = new JButton("Change Password");
        changePassButton.addActionListener(e -> changePassword());
        optionsContent.add(changePassButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(e -> deleteAccount());
        optionsContent.add(deleteAccountButton);

        optionsPanel.add(optionsContent, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));
        optionsPanel.add(backButton, BorderLayout.SOUTH);
    }

    private void changePassword() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JPasswordField currentPassField = new JPasswordField(15);
        JPasswordField newPassField = new JPasswordField(15);
        JPasswordField confirmPassField = new JPasswordField(15);

        panel.add(new JLabel("Current Password:"));
        panel.add(currentPassField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPassField);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(confirmPassField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(frame, "New passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                cd.restablirContrasenya(currentUser.getNom(), currentPass, newPass);
                JOptionPane.showMessageDialog(frame, "Password changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete your account? This cannot be undone.",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cd.eliminarCompte(currentUser.getNom());
            currentUser = null;
            JOptionPane.showMessageDialog(frame, "Account deleted successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardPanel, "login");
        }
    }

    private void createRankingPanel() {
        rankingPanel = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel();
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] filters = {"Total Points", "Games Played", "Games Won", "Personal Record"};
        JComboBox<String> filterCombo = new JComboBox<>(filters);
        filterCombo.addActionListener(e -> {
            updateRankingInfo(filterCombo.getSelectedIndex() + 1);
        });
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterCombo);

        rankingPanel.add(filterPanel, BorderLayout.NORTH);

        JTextArea rankingText = new JTextArea();
        rankingText.setEditable(false);
        rankingText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(rankingText);
        rankingPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));
        rankingPanel.add(backButton, BorderLayout.SOUTH);
    }

    private void updateRankingInfo(int filter) {
        JTextArea rankingText = (JTextArea) ((JScrollPane) rankingPanel.getComponent(1)).getViewport().getView();
        rankingText.setText("");

        List<Persona> ranking = cd.getRanking(filter);
        if (ranking == null || ranking.isEmpty()) {
            rankingText.setText("No ranking data available");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-20s %-15s\n", "Rank", "Name", "Value"));
        sb.append("----------------------------------------\n");

        for (int i = 0; i < ranking.size(); i++) {
            Persona player = ranking.get(i);
            int value = player.getValorEstaditiques(filter);
            sb.append(String.format("%-5d %-20s %-15d\n", i+1, player.getNom(), value));

            if (player.equals(currentUser)) {
                sb.append("----------------------------------------\n");
                sb.append(String.format("%-5d %-20s %-15d\n", i+1, player.getNom(), value));
                sb.append("----------------------------------------\n");
            }
        }

        rankingText.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScrabbleGUI());
    }
}
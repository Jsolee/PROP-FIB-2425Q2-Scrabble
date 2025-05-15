package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
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

    // Game UI components
    private JPanel boardPanel;
    private JPanel rackPanel;
    private JPanel playerPanel;
    private JButton[][] boardButtons;
    private JButton[] rackButtons;
    private List<int[]> placedTiles; // Tracks tiles placed in current turn
    private JLabel currentPlayerLabel;
    private JLabel remainingTilesLabel;

    // Colors for special board cells
    private static final Color TRIPLE_WORD_COLOR = new Color(239, 83, 80); // Red
    private static final Color DOUBLE_WORD_COLOR = new Color(239, 154, 154); // Pink
    private static final Color TRIPLE_LETTER_COLOR = new Color(66, 165, 245); // Blue
    private static final Color DOUBLE_LETTER_COLOR = new Color(129, 199, 132); // Green
    private static final Color CENTER_COLOR = new Color(255, 241, 118); // Yellow
    private static final Color DEFAULT_COLOR = new Color(238, 238, 238); // Light gray
    private static final Color TILE_COLOR = new Color(255, 224, 178); // Wood-like color
    private static final Color TILE_TEXT_COLOR = new Color(97, 97, 97); // Dark gray
    private static final Color DRAG_HIGHLIGHT_COLOR = new Color(144, 238, 144); // Light green for drag highlight

    public ScrabbleGUI() {
        cd = new ControladorDomini();
        placedTiles = new ArrayList<>();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 850);
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
        loginPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Scrabble Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(66, 165, 245));
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
        styleButton(registerButton, new Color(129, 199, 132));
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "register"));
        loginPanel.add(registerButton, gbc);
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Register New User");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(usernameLabel, gbc);
        gbc.gridx++;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(emailLabel, gbc);
        gbc.gridx++;
        JTextField emailField = new JTextField(15);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(ageLabel, gbc);
        gbc.gridx++;
        JTextField ageField = new JTextField(15);
        ageField.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel countryLabel = new JLabel("Country:");
        countryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(countryLabel, gbc);
        gbc.gridx++;
        JTextField countryField = new JTextField(15);
        countryField.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(countryField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(passwordLabel, gbc);
        gbc.gridx++;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(confirmLabel, gbc);
        gbc.gridx++;
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        registerPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        styleButton(registerButton, new Color(76, 175, 80));
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
        styleButton(backButton, new Color(239, 83, 80));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
        registerPanel.add(backButton, gbc);
    }

    private void createMainMenuPanel() {
        mainMenuPanel = new JPanel(new GridBagLayout());
        mainMenuPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel welcomeLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getNom() : "Guest"));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainMenuPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JButton playButton = new JButton("Play Game");
        styleButton(playButton, new Color(66, 165, 245));
        playButton.setPreferredSize(new Dimension(200, 60));
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.addActionListener(e -> showGameOptions());
        mainMenuPanel.add(playButton, gbc);

        gbc.gridy++;
        JButton profileButton = new JButton("View Profile");
        styleButton(profileButton, new Color(129, 199, 132));
        profileButton.setPreferredSize(new Dimension(200, 60));
        profileButton.setFont(new Font("Arial", Font.BOLD, 16));
        profileButton.addActionListener(e -> {
            updateProfileInfo();
            cardLayout.show(cardPanel, "profile");
        });
        mainMenuPanel.add(profileButton, gbc);

        gbc.gridy++;
        JButton rankingButton = new JButton("View Rankings");
        styleButton(rankingButton, new Color(255, 183, 77));
        rankingButton.setPreferredSize(new Dimension(200, 60));
        rankingButton.setFont(new Font("Arial", Font.BOLD, 16));
        rankingButton.addActionListener(e -> {
            updateRankingInfo(1); // Default to points ranking
            cardLayout.show(cardPanel, "ranking");
        });
        mainMenuPanel.add(rankingButton, gbc);

        gbc.gridy++;
        JButton optionsButton = new JButton("Options");
        styleButton(optionsButton, new Color(171, 71, 188));
        optionsButton.setPreferredSize(new Dimension(200, 60));
        optionsButton.setFont(new Font("Arial", Font.BOLD, 16));
        optionsButton.addActionListener(e -> cardLayout.show(cardPanel, "options"));
        mainMenuPanel.add(optionsButton, gbc);

        gbc.gridy++;
        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(239, 83, 80));
        logoutButton.setPreferredSize(new Dimension(200, 60));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
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
        gamePanel.setBackground(new Color(240, 240, 240));

        // Main game area panel
        JPanel gameAreaPanel = new JPanel(new BorderLayout());
        gameAreaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Board panel with grid layout
        boardPanel = new JPanel(new GridLayout(15, 15, 1, 1));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        boardPanel.setBackground(Color.BLACK);
        boardButtons = new JButton[15][15];

        // Initialize board buttons with special colors
        initializeBoardButtons();

        JScrollPane boardScroll = new JScrollPane(boardPanel);
        boardScroll.setPreferredSize(new Dimension(700, 700));
        gameAreaPanel.add(boardScroll, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Game info panel
        JPanel gameInfoPanel = new JPanel(new GridLayout(3, 1));

        currentPlayerLabel = new JLabel("", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameInfoPanel.add(currentPlayerLabel);

        remainingTilesLabel = new JLabel("", JLabel.CENTER);
        remainingTilesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gameInfoPanel.add(remainingTilesLabel);

        JLabel instructionLabel = new JLabel("Drag tiles from rack to board", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gameInfoPanel.add(instructionLabel);

        controlPanel.add(gameInfoPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton playWordButton = new JButton("Play Word");
        styleButton(playWordButton, new Color(76, 175, 80));
        playWordButton.addActionListener(e -> confirmWordPlacement());
        buttonPanel.add(playWordButton);

        JButton exchangeButton = new JButton("Exchange Tiles");
        styleButton(exchangeButton, new Color(251, 192, 45));
        exchangeButton.addActionListener(e -> exchangeTiles());
        buttonPanel.add(exchangeButton);

        JButton passButton = new JButton("Pass Turn");
        styleButton(passButton, new Color(66, 165, 245));
        passButton.addActionListener(e -> passTurn());
        buttonPanel.add(passButton);

        JButton resignButton = new JButton("Resign");
        styleButton(resignButton, new Color(239, 83, 80));
        resignButton.addActionListener(e -> resignGame());
        buttonPanel.add(resignButton);

        JButton saveButton = new JButton("Save Game");
        styleButton(saveButton, new Color(171, 71, 188));
        saveButton.addActionListener(e -> saveGame());
        buttonPanel.add(saveButton);

        controlPanel.add(buttonPanel, BorderLayout.CENTER);

        gameAreaPanel.add(controlPanel, BorderLayout.SOUTH);

        // Player and rack panel
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(250, 0));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player panel
        playerPanel = new JPanel(new GridLayout(0, 1));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        sidePanel.add(playerPanel, BorderLayout.NORTH);

        // Rack panel
        rackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        rackPanel.setBorder(BorderFactory.createTitledBorder("Your Rack"));
        rackPanel.setBackground(new Color(240, 240, 240));
        sidePanel.add(rackPanel, BorderLayout.CENTER);

        gamePanel.add(gameAreaPanel, BorderLayout.CENTER);
        gamePanel.add(sidePanel, BorderLayout.EAST);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void initializeBoardButtons() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                final int row = i;
                final int col = j;
                String position = i + "," + j;

                JButton cellButton = new JButton();
                cellButton.setOpaque(true);
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                cellButton.setPreferredSize(new Dimension(40, 40));
                cellButton.setFont(new Font("Arial", Font.BOLD, 14));

                // Set colors based on special positions
                if (i == 7 && j == 7) {
                    styleBoardButton(cellButton, CENTER_COLOR, "★");
                } else if (isSpecialPosition(position, "tripleWord")) {
                    styleBoardButton(cellButton, TRIPLE_WORD_COLOR, "TW");
                } else if (isSpecialPosition(position, "doubleWord")) {
                    styleBoardButton(cellButton, DOUBLE_WORD_COLOR, "DW");
                } else if (isSpecialPosition(position, "tripleLetter")) {
                    styleBoardButton(cellButton, TRIPLE_LETTER_COLOR, "TL");
                } else if (isSpecialPosition(position, "doubleLetter")) {
                    styleBoardButton(cellButton, DOUBLE_LETTER_COLOR, "DL");
                } else {
                    styleBoardButton(cellButton, DEFAULT_COLOR, "");
                }

                // Make all cells droppable
                setupDropTarget(cellButton, row, col);

                boardButtons[i][j] = cellButton;
                boardPanel.add(cellButton);
            }
        }
    }

    private boolean isSpecialPosition(String position, String type) {
        String[] coords = position.split(",");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        // Triple Word positions
        if (type.equals("tripleWord")) {
            return (row == 0 && col == 0) || (row == 0 && col == 7) || (row == 0 && col == 14) ||
                    (row == 7 && col == 0) || (row == 7 && col == 14) ||
                    (row == 14 && col == 0) || (row == 14 && col == 7) || (row == 14 && col == 14);
        }
        // Double Word positions
        else if (type.equals("doubleWord")) {
            return (row == 1 && col == 1) || (row == 2 && col == 2) || (row == 3 && col == 3) ||
                    (row == 4 && col == 4) || (row == 1 && col == 13) || (row == 2 && col == 12) ||
                    (row == 3 && col == 11) || (row == 4 && col == 10) || (row == 10 && col == 4) ||
                    (row == 11 && col == 3) || (row == 12 && col == 2) || (row == 13 && col == 1) ||
                    (row == 10 && col == 10) || (row == 11 && col == 11) || (row == 12 && col == 12) ||
                    (row == 13 && col == 13);
        }
        // Triple Letter positions
        else if (type.equals("tripleLetter")) {
            return (row == 1 && col == 5) || (row == 1 && col == 9) || (row == 5 && col == 1) ||
                    (row == 5 && col == 5) || (row == 5 && col == 9) || (row == 5 && col == 13) ||
                    (row == 9 && col == 1) || (row == 9 && col == 5) || (row == 9 && col == 9) ||
                    (row == 9 && col == 13) || (row == 13 && col == 5) || (row == 13 && col == 9);
        }
        // Double Letter positions
        else if (type.equals("doubleLetter")) {
            return (row == 0 && col == 3) || (row == 0 && col == 11) || (row == 2 && col == 6) ||
                    (row == 2 && col == 8) || (row == 3 && col == 0) || (row == 3 && col == 7) ||
                    (row == 3 && col == 14) || (row == 6 && col == 2) || (row == 6 && col == 6) ||
                    (row == 6 && col == 8) || (row == 6 && col == 12) || (row == 7 && col == 3) ||
                    (row == 7 && col == 11) || (row == 8 && col == 2) || (row == 8 && col == 6) ||
                    (row == 8 && col == 8) || (row == 8 && col == 12) || (row == 11 && col == 0) ||
                    (row == 11 && col == 7) || (row == 11 && col == 14) || (row == 12 && col == 6) ||
                    (row == 12 && col == 8) || (row == 14 && col == 3) || (row == 14 && col == 11);
        }
        return false;
    }

    private void styleBoardButton(JButton button, Color bgColor, String text) {
        button.setBackground(bgColor);
        button.setText(text);
        if (text.equals("★")) {
            button.setForeground(Color.RED);
        } else if (text.equals("TW") || text.equals("DW") || text.equals("TL") || text.equals("DL")) {
            button.setForeground(Color.BLACK);
            button.setFont(new Font("Arial", Font.BOLD, 10));
        }
    }

    private void setupDropTarget(JButton target, final int row, final int col) {
        target.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                // Only allow drop if it's the player's turn and the cell is empty
                boolean canImport = support.isDataFlavorSupported(DataFlavor.stringFlavor) &&
                        currentGame != null &&
                        currentGame.getJugadorActual().equals(currentUser) &&
                        currentGame.getTaulell().getCasella(row, col).getFitxa() == null;

                // Visual feedback
                if (canImport) {
                    target.setBackground(DRAG_HIGHLIGHT_COLOR);
                }
                return canImport;
            }

            @Override
            public void exportDone(JComponent c, Transferable data, int action) {
                // Reset all board button backgrounds when drag ends
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        resetBoardButtonAppearance(i, j);
                    }
                }
                super.exportDone(c, data, action);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String tileData = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    String[] parts = tileData.split(",");
                    int tileIndex = Integer.parseInt(parts[0]);

                    // Reset appearance
                    resetBoardButtonAppearance(row, col);

                    // Place the tile visually
                    List<Fitxa> rack = currentGame.getAtril();
                    if (tileIndex >= 0 && tileIndex < rack.size()) {
                        Fitxa tile = rack.get(tileIndex);
                        target.setText(tile.getLletra());
                        target.setForeground(TILE_TEXT_COLOR);
                        target.setBackground(TILE_COLOR);
                        target.setToolTipText("Value: " + tile.getValor());

                        // Store the placed tile position
                        placedTiles.add(new int[]{row, col, tileIndex});
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void resetBoardButtonAppearance(int row, int col) {
        JButton button = boardButtons[row][col];
        String position = row + "," + col;

        if (currentGame.getTaulell().getCasella(row, col).getFitxa() != null) {
            button.setBackground(TILE_COLOR);
        } else {
            if (row == 7 && col == 7) {
                styleBoardButton(button, CENTER_COLOR, "★");
            } else if (isSpecialPosition(position, "tripleWord")) {
                styleBoardButton(button, TRIPLE_WORD_COLOR, "TW");
            } else if (isSpecialPosition(position, "doubleWord")) {
                styleBoardButton(button, DOUBLE_WORD_COLOR, "DW");
            } else if (isSpecialPosition(position, "tripleLetter")) {
                styleBoardButton(button, TRIPLE_LETTER_COLOR, "TL");
            } else if (isSpecialPosition(position, "doubleLetter")) {
                styleBoardButton(button, DOUBLE_LETTER_COLOR, "DL");
            } else {
                styleBoardButton(button, DEFAULT_COLOR, "");
            }
        }
    }

    private void updateGameBoard() {
        if (currentGame == null) return;

        // Update game info
        currentPlayerLabel.setText("Current Turn: " + currentGame.getJugadorActual().getNom());
        remainingTilesLabel.setText("Tiles remaining: " + currentGame.getBossa().getQuantitatFitxes());

        Taulell board = currentGame.getTaulell();

        // Update board
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton cellButton = boardButtons[i][j];
                Casella cell = board.getCasella(i, j);

                if (cell.getFitxa() != null) {
                    cellButton.setText(cell.getFitxa().getLletra());
                    cellButton.setForeground(TILE_TEXT_COLOR);
                    cellButton.setBackground(TILE_COLOR);
                    cellButton.setToolTipText("Value: " + cell.getFitxa().getValor());
                } else {
                    resetBoardButtonAppearance(i, j);
                    cellButton.setToolTipText(null);
                }
            }
        }

        // Update player info
        playerPanel.removeAll();
        List<Usuari> players = currentGame.getJugadors();
        List<Integer> scores = currentGame.getPuntuacions();
        for (int i = 0; i < players.size(); i++) {
            JLabel playerLabel = new JLabel(players.get(i).getNom() + ": " + scores.get(i) + " points");
            playerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            if (players.get(i).equals(currentGame.getJugadorActual())) {
                playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD));
            }
            playerPanel.add(playerLabel);
        }

        // Update rack with drag support
        rackPanel.removeAll();
        List<Fitxa> rack = currentGame.getAtril();
        rackButtons = new JButton[rack.size()];

        for (int i = 0; i < rack.size(); i++) {
            final int tileIndex = i;
            Fitxa tile = rack.get(i);
            JButton tileButton = new JButton(tile.getLletra());
            tileButton.setFont(new Font("Arial", Font.BOLD, 16));
            tileButton.setForeground(TILE_TEXT_COLOR);
            tileButton.setBackground(TILE_COLOR);
            tileButton.setToolTipText("Value: " + tile.getValor());
            tileButton.setPreferredSize(new Dimension(40, 50));
            tileButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createLineBorder(new Color(139, 69, 19), 2) // Wood border
            ));

            // Make tiles draggable
            tileButton.setTransferHandler(new TileTransferHandler(tileIndex));
            tileButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    handler.exportAsDrag(comp, e, TransferHandler.COPY);
                }
            });

            // Add right-click to remove tiles from board
            tileButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        removeTileFromBoard(tileIndex);
                    }
                }
            });

            rackButtons[i] = tileButton;
            rackPanel.add(tileButton);
        }

        frame.revalidate();
        frame.repaint();
    }

    private class TileTransferHandler extends TransferHandler {
        private final int tileIndex;

        public TileTransferHandler(int tileIndex) {
            this.tileIndex = tileIndex;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(tileIndex + "");
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }

    private void removeTileFromBoard(int tileIndex) {
        // Find if this tile is placed on the board
        Iterator<int[]> iterator = placedTiles.iterator();
        while (iterator.hasNext()) {
            int[] tilePos = iterator.next();
            if (tilePos[2] == tileIndex) {
                int row = tilePos[0];
                int col = tilePos[1];
                resetBoardButtonAppearance(row, col);
                iterator.remove();
                break;
            }
        }
    }

    private void confirmWordPlacement() {
        if (placedTiles.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No tiles placed on board", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determine orientation (H or V)
        String orientation = determineOrientation();
        if (orientation == null) {
            JOptionPane.showMessageDialog(frame, "Tiles must be placed in a straight line", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the moves map
        LinkedHashMap<int[], Fitxa> moves = new LinkedHashMap<>();
        List<Integer> indices = new ArrayList<>();

        for (int[] tilePos : placedTiles) {
            int row = tilePos[0];
            int col = tilePos[1];
            int tileIndex = tilePos[2];

            moves.put(new int[]{row, col}, currentGame.getAtril().get(tileIndex));
            indices.add(tileIndex);
        }

        try {
            int score = cd.jugarParaula(currentGame, moves, orientation, indices);
            if (score != -1) {
                JOptionPane.showMessageDialog(frame, "Word played successfully! Score: " + score,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                placedTiles.clear();
                currentGame.passarTorn();

                if (currentGame.getJugadorActual() instanceof Bot) {
                    botTurn();
                }

                updateGameBoard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid word or position",
                        "Error", JOptionPane.ERROR_MESSAGE);
                // Reset the placed tiles
                placedTiles.clear();
                updateGameBoard();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            placedTiles.clear();
            updateGameBoard();
        }
    }

    private String determineOrientation() {
        if (placedTiles.size() == 1) return "H"; // Single tile can be either

        // Check if all rows are the same (horizontal)
        boolean horizontal = true;
        int firstRow = placedTiles.get(0)[0];
        for (int i = 1; i < placedTiles.size(); i++) {
            if (placedTiles.get(i)[0] != firstRow) {
                horizontal = false;
                break;
            }
        }
        if (horizontal) return "H";

        // Check if all columns are the same (vertical)
        boolean vertical = true;
        int firstCol = placedTiles.get(0)[1];
        for (int i = 1; i < placedTiles.size(); i++) {
            if (placedTiles.get(i)[1] != firstCol) {
                vertical = false;
                break;
            }
        }
        if (vertical) return "V";

        return null; // Not in a straight line
    }

    private void passTurn() {
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to pass your turn?",
                "Confirm Pass",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            placedTiles.clear();
            currentGame.passarTorn();
            if (currentGame.getJugadorActual() instanceof Bot) {
                botTurn();
            }
            updateGameBoard();
        }
    }

    private void exchangeTiles() {
        if (!currentGame.getJugadorActual().equals(currentUser)) {
            JOptionPane.showMessageDialog(frame, "It's not your turn", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dialog to select tiles to exchange
        JDialog exchangeDialog = new JDialog(frame, "Exchange Tiles", true);
        exchangeDialog.setLayout(new BorderLayout());
        exchangeDialog.setSize(400, 300);

        JPanel tilePanel = new JPanel(new GridLayout(0, 1));
        tilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Fitxa> rack = currentGame.getAtril();
        JCheckBox[] checkBoxes = new JCheckBox[rack.size()];

        for (int i = 0; i < rack.size(); i++) {
            Fitxa tile = rack.get(i);
            JCheckBox checkBox = new JCheckBox(tile.getLletra() + " (" + tile.getValor() + ")");
            checkBoxes[i] = checkBox;
            tilePanel.add(checkBox);
        }

        exchangeDialog.add(new JScrollPane(tilePanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Exchange");
        submitButton.addActionListener(e -> {
            List<String> indices = new ArrayList<>();
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    indices.add(String.valueOf(i));
                }
            }

            if (indices.isEmpty()) {
                JOptionPane.showMessageDialog(exchangeDialog, "No tiles selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                cd.canviDeFitxes(currentGame, indices.toArray(new String[0]));
                placedTiles.clear();
                currentGame.passarTorn();

                if (currentGame.getJugadorActual() instanceof Bot) {
                    botTurn();
                }

                updateGameBoard();
                exchangeDialog.dispose();
                JOptionPane.showMessageDialog(frame, "Tiles exchanged successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(exchangeDialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> exchangeDialog.dispose());
        buttonPanel.add(cancelButton);

        exchangeDialog.add(buttonPanel, BorderLayout.SOUTH);
        exchangeDialog.setVisible(true);
    }

    private void saveGame() {
        currentGame.guardarPartida();
        JOptionPane.showMessageDialog(frame, "Game saved successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
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
            placedTiles.clear();
            cardLayout.show(cardPanel, "mainMenu");
        }
    }

    private void botTurn() {
        cd.posarParaulaBot(currentGame, currentGame.getJugadorActual());
        updateGameBoard();
    }

    private void createProfilePanel() {
        profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(new Color(240, 240, 240));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(nameLabel);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(emailLabel);

        JLabel ageLabel = new JLabel("Age: ");
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(ageLabel);

        JLabel countryLabel = new JLabel("Country: ");
        countryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(countryLabel);

        JLabel statsLabel = new JLabel("Statistics:");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(statsLabel);

        JLabel gamesPlayedLabel = new JLabel("Games Played: ");
        gamesPlayedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(gamesPlayedLabel);

        JLabel gamesWonLabel = new JLabel("Games Won: ");
        gamesWonLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(gamesWonLabel);

        JLabel gamesLostLabel = new JLabel("Games Lost: ");
        gamesLostLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(gamesLostLabel);

        JLabel totalScoreLabel = new JLabel("Total Score: ");
        totalScoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(totalScoreLabel);

        JLabel recordLabel = new JLabel("Personal Record: ");
        recordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(recordLabel);

        JLabel levelLabel = new JLabel("Ranking Level: ");
        levelLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(levelLabel);

        profilePanel.add(infoPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        styleButton(backButton, new Color(66, 165, 245));
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
        optionsPanel.setBackground(new Color(240, 240, 240));

        JPanel optionsContent = new JPanel(new GridLayout(0, 1));
        optionsContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton changePassButton = new JButton("Change Password");
        styleButton(changePassButton, new Color(76, 175, 80));
        changePassButton.addActionListener(e -> changePassword());
        optionsContent.add(changePassButton);

        JButton deleteAccountButton = new JButton("Delete Account");
        styleButton(deleteAccountButton, new Color(239, 83, 80));
        deleteAccountButton.addActionListener(e -> deleteAccount());
        optionsContent.add(deleteAccountButton);

        optionsPanel.add(optionsContent, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        styleButton(backButton, new Color(66, 165, 245));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "mainMenu"));
        optionsPanel.add(backButton, BorderLayout.SOUTH);
    }

    private void changePassword() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        rankingPanel.setBackground(new Color(240, 240, 240));

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
        rankingText.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rankingText.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(rankingText);
        rankingPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        styleButton(backButton, new Color(66, 165, 245));
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
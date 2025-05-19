package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;

public class ScrabbleGUI {
    private ControladorDomini cd;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Usuari currentUser;
    private Partida currentGame;

    // Panel references
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private ProfilePanel profilePanel;
    private OptionsPanel optionsPanel;
    private RankingPanel rankingPanel;

    public ScrabbleGUI() {
        cd = new ControladorDomini();
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 850);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize panels
        loginPanel = new LoginPanel(this, cd);
        registerPanel = new RegisterPanel(this, cd);
        mainMenuPanel = new MainMenuPanel(this, cd);
        gamePanel = new GamePanel(this, cd);
        profilePanel = new ProfilePanel(this, cd);
        optionsPanel = new OptionsPanel(this, cd);
        rankingPanel = new RankingPanel(this, cd);

        // Add panels to card layout
        cardPanel.add(loginPanel, "login");
        cardPanel.add(registerPanel, "register");
        cardPanel.add(mainMenuPanel, "mainMenu");
        cardPanel.add(gamePanel, "game");
        cardPanel.add(profilePanel, "profile");
        cardPanel.add(optionsPanel, "options");
        cardPanel.add(rankingPanel, "ranking");

        frame.add(cardPanel);
        showLoginPanel();
        frame.setVisible(true);
    }

    // Navigation methods
    public void showLoginPanel() {
        cardLayout.show(cardPanel, "login");
    }

    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "register");
    }

    public void showMainMenuPanel() {
        cardLayout.show(cardPanel, "mainMenu");
        mainMenuPanel.updateWelcomeMessage();
    }

    public void showGamePanel() {
        cardLayout.show(cardPanel, "game");
    }

    public void showProfilePanel() {
        cardLayout.show(cardPanel, "profile");
        profilePanel.updateProfileInfo();
    }

    public void showOptionsPanel() {
        cardLayout.show(cardPanel, "options");
    }

    public void showRankingPanel() {
        cardLayout.show(cardPanel, "ranking");
        rankingPanel.updateRankingInfo(1);
    }

    // Getters and setters
    public Usuari getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuari currentUser) {
        this.currentUser = currentUser;
    }

    public Partida getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Partida currentGame) {
        this.currentGame = currentGame;
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScrabbleGUI());
    }
}
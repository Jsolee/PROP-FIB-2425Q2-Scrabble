package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;

/**
 * Controlador principal de la capa de presentació de l'aplicació Scrabble Game.
 * 
 * Aquesta classe és responsable de:
 * - Gestionar la interfície gràfica de l'aplicació
 * - Gestionar la navegació entre les diferents pantalles
 * - Coordinar la comunicació entre la interfície i la capa de domini
 * - Mantenir l'estat de l'usuari i la partida actuals
 * 
 * El controlador inicialitza tots els components necessaris de la interfície i 
 * estableix la connexió amb el controlador de domini per accedir a la lògica 
 * de l'aplicació i a la persistència de dades.
 */
public class ControladorPresentacio {
    /** Controlador de domini per a la lògica del joc */   
    private ControladorDomini cd;

    /** Components de la interfície gràfica */ 
    private JFrame frame;

    /** Layout per a la navegació entre pantalles */
    private CardLayout cardLayout;

    /** Panell que conté totes les pantalles de l'aplicació */
    private JPanel cardPanel;

    /** Usuari actual i partida en curs */
    private Usuari currentUser;

    /** Partida actual en curs */
    private Partida currentGame;


    /** Panells de la interfície gràfica */
    private LoginPanel loginPanel;
    /** Panell de registre d'usuaris */
    private RegisterPanel registerPanel;
    /** Panell del menú principal */
    private MainMenuPanel mainMenuPanel;
    /** Panell del joc */
    private GamePanel gamePanel;
    /** Panell del perfil de l'usuari */
    private ProfilePanel profilePanel;
    /** Panell d'opcions */
    private OptionsPanel optionsPanel;
    /** Panell de rànquing */
    private RankingPanel rankingPanel;

    
    public ControladorPresentacio() {
        cd = new ControladorDomini();
        cd.inicialitzarDadesPersistencia();
        initializeGUI();
    }

    /**
     * Inicialitza la interfície gràfica de l'aplicació Scrabble Game.
     * 
     * Aquest mètode configura tots els elements visuals del joc:
     * - Crea el marc principal de l'aplicació
     * - Inicialitza els diferents panells (login, registre, menú principal, etc.)
     * - Configura el CardLayout per a la navegació entre pantalles
     * - Estableix els listeners per a l'esdeveniment de tancament de l'aplicació
     * - Gestiona el guardat de dades en sortir de l'aplicació
     * 
     * La interfície s'inicia amb el panell de login com a primera pantalla visible.
     * Totes les altres pantalles romanen carregades però ocultes fins que l'usuari
     * navegui cap a elles mitjançant les accions corresponents.
     */
    private void initializeGUI() {
        frame = new JFrame("Scrabble Game");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // ask for confirmation
            int response = JOptionPane.showConfirmDialog(frame,
                "Estas segur que vols tancar l'aplicació?", 
                "Tancar aplicació",
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                // actualitzem dades persistencia
                boolean updated = cd.actualitzarDadesPersistencia(); 
                
                if (!updated) {
                    JOptionPane.showMessageDialog(frame, 
                        "Error al guardar les dades.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                } else  {
                    // Show confirmation after persistence update
                    JOptionPane.showMessageDialog(frame, 
                        "Les dades s'han guardat correctament!", 
                        "Informació", 
                        JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0); 
                }
            }
        }
        });
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

    /**
     * Mostra el panell de login.
     * Aquest mètode canvia la vista actual al panell de login,
     * on l'usuari pot introduir les seves credencials per accedir al joc.
     */
    public void showLoginPanel() {
        cardLayout.show(cardPanel, "login");
    }

    /**
     * Mostra el panell de registre.
     * Aquest mètode canvia la vista actual al panell de registre,
     * on l'usuari pot crear un nou compte.
     */
    public void showRegisterPanel() {
        cardLayout.show(cardPanel, "register");
    }

    /**
     * Mostra el panell de menú principal.
     * Aquest mètode canvia la vista actual al panell del menú principal,
     * on l'usuari pot accedir a les diferents opcions del joc.
     */
    public void showMainMenuPanel() {
        cardLayout.show(cardPanel, "mainMenu");
        mainMenuPanel.updateWelcomeMessage();
    }

    /**
     * Mostra el panell de joc.
     * Aquest mètode canvia la vista actual al panell del joc,
     * on l'usuari pot jugar la partida actual.
     */
    public void showGamePanel() {
        gamePanel.updateGameBoard();
        cardLayout.show(cardPanel, "game");
    }

    /**
     * Mostra el panell del perfil de l'usuari.
     * Aquest mètode canvia la vista actual al panell del perfil,
     * on l'usuari pot veure i editar la seva informació personal.
     */
    public void showProfilePanel() {
        cardLayout.show(cardPanel, "profile");
        profilePanel.updateProfileInfo();
    }

    /**
     * Mostra el panell d'opcions.
     * Aquest mètode canvia la vista actual al panell d'opcions,
     * on l'usuari pot ajustar les preferències del joc.
     */
    public void showOptionsPanel() {
        cardLayout.show(cardPanel, "options");
    }

    /**
     * Mostra el panell de rànquing.
     * Aquest mètode canvia la vista actual al panell de rànquing,
     * on l'usuari pot veure les classificacions dels jugadors.
     */
    public void showRankingPanel() {
        cardLayout.show(cardPanel, "ranking");
        rankingPanel.updateRankingInfo(1);
    }

    /**
     * 
     * Aquest mètode retorna l'usuari que ha iniciat sessió
     * i que està interactuant amb l'aplicació.
     * @return l'usuari actual
     */
    public Usuari getCurrentUser() {
        return currentUser;
    }

    /**
     * Estableix l'usuari actual que ha iniciat sessió.
     * Aquest mètode s'utilitza per guardar l'estat de l'usuari
     * que està interactuant amb l'aplicació.
     * @param currentUser l'usuari actual
     */
    public void setCurrentUser(Usuari currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Retorna la partida actual en curs.
     * Aquest mètode s'utilitza per accedir a la partida
     * que l'usuari està jugant en aquest moment.
     * @return la partida actual
     */
    public Partida getCurrentGame() {
        return currentGame;
    }

    /**
     * Estableix la partida actual en curs.
     * Aquest mètode s'utilitza per guardar l'estat de la partida
     * que l'usuari està jugant en aquest moment.
     * @param currentGame la partida actual
     */
    public void setCurrentGame(Partida currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * Retorna el marc principal de la interfície gràfica.
     * Aquest mètode s'utilitza per accedir al JFrame que conté
     * tots els panells de l'aplicació.
     * @return el JFrame principal
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Inicia l'aplicació Scrabble Game.
     * Aquest mètode és el punt d'entrada de l'aplicació,
     * on es crea una instància del controlador de presentació
     * i es mostra la interfície gràfica.
     * @param args arguments de línia de comandes (no s'utilitzen)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ControladorPresentacio());
    }
}
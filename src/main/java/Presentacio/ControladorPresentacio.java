package Presentacio;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import Domini.ControladorDomini;
import Domini.Partida;
import Domini.Usuari;

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
        setLookAndFeel();
        initializeGUI();
    }

    private void setLookAndFeel() {
        try {
            // Set system look and feel for better integration
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default if system look and feel fails
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
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
        // Get screen dimensions for responsive design
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = Math.max(1200, (int) (screenSize.width * 0.75));
        int windowHeight = Math.max(900, (int) (screenSize.height * 0.8));

        frame = new JFrame("🎯 Scrabble Game - Modern Edition");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Set window icon (optional - would need an icon file)
        // frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Enhanced confirmation dialog with modern styling
            JPanel confirmPanel = new JPanel();
            confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
            confirmPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            confirmPanel.setBackground(Color.WHITE);

            JLabel iconLabel = new JLabel("🚪", JLabel.CENTER);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel titleLabel = new JLabel("Exit Application");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            JLabel messageLabel = new JLabel("<html><center>Are you sure you want to close the application?<br/>Your progress will be saved automatically.</center></html>");
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            confirmPanel.add(iconLabel);
            confirmPanel.add(titleLabel);
            confirmPanel.add(messageLabel);

            int response = JOptionPane.showConfirmDialog(frame,
                confirmPanel, 
                "Exit Scrabble Game",
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.PLAIN_MESSAGE);
                
            if (response == JOptionPane.YES_OPTION) {
                // Show saving progress dialog
                showSavingDialog();
                
                // actualitzem dades persistencia
                boolean updated = cd.actualitzarDadesPersistencia(); 
                
                if (!updated) {
                    JOptionPane.showMessageDialog(frame, 
                        "❌ Error saving data.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                } else  {
                    // Show confirmation after persistence update
                    JOptionPane.showMessageDialog(frame, 
                        "✅ Data saved successfully!\nSee you next time!", 
                        "Goodbye", 
                        JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0); 
                }
            }
        }
        });

        // Set responsive window size and make it resizable
        frame.setSize(windowWidth, windowHeight);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        // Add component listener for responsive behavior
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Notify panels about size changes for responsive updates
                if (gamePanel != null) {
                    gamePanel.revalidate();
                    gamePanel.repaint();
                }
            }
        });

        // Set modern background color
        frame.getContentPane().setBackground(new Color(245, 247, 250));

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

    private void showSavingDialog() {
        // Create a simple saving dialog
        JDialog savingDialog = new JDialog(frame, "Saving...", true);
        savingDialog.setLayout(new BorderLayout());
        savingDialog.setSize(300, 120);
        savingDialog.setLocationRelativeTo(frame);
        savingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel savingLabel = new JLabel("💾 Saving your progress...", JLabel.CENTER);
        savingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Please wait...");
        
        contentPanel.add(savingLabel, BorderLayout.NORTH);
        contentPanel.add(progressBar, BorderLayout.CENTER);
        savingDialog.add(contentPanel);
        
        // Show dialog for a brief moment to indicate saving
        Timer timer = new Timer(1000, e -> savingDialog.dispose());
        timer.setRepeats(false);
        timer.start();
        
        savingDialog.setVisible(true);
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
package Presentacio;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import Domini.ControladorDomini;

/**
 * Panell d'opcions per a la gestió del perfil de l'usuari.
 * Permet canviar la contrasenya i eliminar el compte.
 */
public class OptionsPanel extends JPanel {
    /** Controlador de presentació*/
    private ControladorPresentacio cp;
    /** Controlador de domini per a la lògica del joc */
    private ControladorDomini cd;

    /**
     * Constructor del panell d'opcions.
     * Inicialitza els components gràfics i els gestors d'esdeveniments.
     *
     * @param cp Controlador de presentació per a la navegació entre pantalles
     * @param cd Controlador de domini per a la lògica del joc
     */
    public OptionsPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    /**
     * Inicialitza els components gràfics del panell d'opcions.
     * Configura el disseny utilitzant BorderLayout per organitzar els elements.
     * Crea i posiciona els components:
     * - Botó per canviar la contrasenya
     * - Botó per eliminar el compte
     * - Botó per tornar al menú principal
     *
     * Els components s'estilitzen amb colors personalitzats per millorar la interfície d'usuari.
     */
    private void initialize() {
        setLayout(new BorderLayout());
        // Scrabble gradient background
        JPanel backgroundPanel = ModernUI.createScrabbleGradientPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Card panel for options content
        JPanel card = ModernUI.createScrabbleCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(520, 520));

        // Logo & title
        JLabel logo = new JLabel("⚙️", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 54));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Options", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(ModernUI.SCRABBLE_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(logo);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(18));

        // Options content (assume getOptionsComponent() returns a JPanel with the options UI)
        JPanel optionsContent = getOptionsComponent();
        optionsContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(optionsContent);
        card.add(Box.createVerticalStrut(24));

        // Back button
        JButton backButton = ModernUI.createScrabbleButton("⬅ Back", ModernUI.SCRABBLE_BLUE);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cp.showMainMenuPanel());
        card.add(backButton);
        card.add(Box.createVerticalStrut(10));

        // Center card in background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        backgroundPanel.add(card, gbc);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private JPanel getOptionsComponent() {
        JPanel optionsContent = new JPanel();
        optionsContent.setLayout(new BoxLayout(optionsContent, BoxLayout.Y_AXIS));
        optionsContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        optionsContent.setOpaque(false);

        // Change Password Button
        JButton changePassButton = ModernUI.createScrabbleButton("🔒 Change Password", ModernUI.SCRABBLE_GREEN);
        changePassButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePassButton.setPreferredSize(new Dimension(280, 50));
        changePassButton.setMaximumSize(new Dimension(280, 50));
        changePassButton.addActionListener(e -> changePassword());
        
        // Delete Account Button
        JButton deleteAccountButton = ModernUI.createScrabbleButton("🗑️ Delete Account", ModernUI.ERROR_RED);
        deleteAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteAccountButton.setPreferredSize(new Dimension(280, 50));
        deleteAccountButton.setMaximumSize(new Dimension(280, 50));
        deleteAccountButton.addActionListener(e -> deleteAccount());

        optionsContent.add(changePassButton);
        optionsContent.add(Box.createVerticalStrut(16));
        optionsContent.add(deleteAccountButton);

        return optionsContent;
    }

    /**
     * Mètode per canviar la contrasenya de l'usuari actual.
     * Mostra un diàleg per introduir la contrasenya actual, la nova contrasenya i la confirmació.
     * Si les contrasenyes coincideixen, s'intenta canviar la contrasenya a través del controlador de domini.
     */
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

        int result = JOptionPane.showConfirmDialog(cp.getFrame(), panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());

            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(cp.getFrame(), "New passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                cd.restablirContrasenya(cp.getCurrentUser().getNom(), currentPass, newPass);
                JOptionPane.showMessageDialog(cp.getFrame(), "Password changed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Mètode per eliminar el compte de l'usuari actual.
     * Mostra un diàleg de confirmació abans d'eliminar el compte.
     * Si l'usuari confirma, s'elimina el compte i es redirigeix a la pantalla de login.
     */
    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(cp.getFrame(),
                "Are you sure you want to delete your account? This cannot be undone.",
                "Confirm Account Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cd.eliminarCompte(cp.getCurrentUser().getNom());
            cp.setCurrentUser(null);
            JOptionPane.showMessageDialog(cp.getFrame(), "Account deleted successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
            cp.showLoginPanel();
        }
    }
}
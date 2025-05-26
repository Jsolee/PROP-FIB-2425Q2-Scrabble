package Presentacio;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;

import Domini.ControladorDomini;
import Domini.Estadistiques;
import Domini.Persona;

public class ProfilePanel extends JPanel {
    private ControladorPresentacio cp;
    private ControladorDomini cd;

    public ProfilePanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        // Scrabble gradient background
        JPanel backgroundPanel = ModernUI.createScrabbleGradientPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Card panel for profile content
        JPanel card = ModernUI.createScrabbleCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(440, 540));
        card.setMaximumSize(new Dimension(440, 540));
        card.setMinimumSize(new Dimension(340, 420));

        // Logo & title
        JLabel logo = new JLabel("👤", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        logo.setAlignmentX(CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(18));
        card.add(logo);
        card.add(Box.createVerticalStrut(10));
        JLabel title = new JLabel("Your Profile", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ModernUI.SCRABBLE_BLUE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(18));

        // Profile info panel (assume getProfileInfoComponent() returns a JPanel with the info)
        JPanel profileInfo = getProfileInfoComponent();
        profileInfo.setOpaque(false);
        profileInfo.setAlignmentX(CENTER_ALIGNMENT);
        card.add(profileInfo);
        card.add(Box.createVerticalStrut(24));

        // Edit and Back buttons (modern Scrabble style)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        JButton editButton = ModernUI.createScrabbleButton("✏️ Edit", ModernUI.SCRABBLE_GREEN);
        editButton.setAlignmentY(CENTER_ALIGNMENT);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(16));
        JButton backButton = ModernUI.createScrabbleButton("⬅ Back", ModernUI.SCRABBLE_BLUE);
        backButton.setAlignmentY(CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cp.showMainMenuPanel());
        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalGlue());
        card.add(buttonPanel);
        card.add(Box.createVerticalStrut(18));

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

    private JPanel getProfileInfoComponent() {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addProfileField("Name: ", infoPanel);
        addProfileField("Email: ", infoPanel);
        addProfileField("Age: ", infoPanel);
        addProfileField("Country: ", infoPanel);
        infoPanel.add(new JLabel("Statistics:"));
        infoPanel.getComponent(4).setFont(new Font("Arial", Font.BOLD, 16));
        addProfileField("Games Played: ", infoPanel);
        addProfileField("Games Won: ", infoPanel);
        addProfileField("Games Lost: ", infoPanel);
        addProfileField("Total Score: ", infoPanel);
        addProfileField("Personal Record: ", infoPanel);
        addProfileField("Ranking Level: ", infoPanel);

        return infoPanel;
    }

    private void addProfileField(String label, JPanel panel) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(jLabel);
    }

    public void updateProfileInfo() {
        if (!(cp.getCurrentUser() instanceof Persona)) return;

        Persona persona = (Persona) cp.getCurrentUser();
        Estadistiques stats = persona.getEstadistiques();

        JPanel infoPanel = (JPanel) getComponent(0);

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
}
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
    private JPanel profileInfoPanel;
    private JLabel[] profileLabels;

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
        card.add(logo);
        JLabel title = new JLabel("Your Profile", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(ModernUI.SCRABBLE_BLUE);
        title.setAlignmentX(CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(18));

        // Profile info panel (assume getProfileInfoComponent() returns a JPanel with the info)
        profileInfoPanel = getProfileInfoComponent();
        profileInfoPanel.setOpaque(false);
        profileInfoPanel.setAlignmentX(CENTER_ALIGNMENT);
        card.add(profileInfoPanel);
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
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 8));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        infoPanel.setOpaque(false);

        // Initialize profile labels array
        profileLabels = new JLabel[10];
        
        profileLabels[0] = addProfileField("Name: ", infoPanel);
        profileLabels[1] = addProfileField("Email: ", infoPanel);
        profileLabels[2] = addProfileField("Age: ", infoPanel);
        profileLabels[3] = addProfileField("Country: ", infoPanel);
        
        // Statistics header
        JLabel statsHeader = new JLabel("Statistics:");
        statsHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsHeader.setForeground(ModernUI.SCRABBLE_BLUE);
        infoPanel.add(statsHeader);
        
        profileLabels[4] = addProfileField("Games Played: ", infoPanel);
        profileLabels[5] = addProfileField("Games Won: ", infoPanel);
        profileLabels[6] = addProfileField("Games Lost: ", infoPanel);
        profileLabels[7] = addProfileField("Total Score: ", infoPanel);
        profileLabels[8] = addProfileField("Personal Record: ", infoPanel);
        profileLabels[9] = addProfileField("Ranking Level: ", infoPanel);

        return infoPanel;
    }

    private JLabel addProfileField(String label, JPanel panel) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel.setForeground(ModernUI.TEXT_DARK);
        panel.add(jLabel);
        return jLabel;
    }

    public void updateProfileInfo() {
        if (!(cp.getCurrentUser() instanceof Persona)) return;

        Persona persona = (Persona) cp.getCurrentUser();
        Estadistiques stats = persona.getEstadistiques();

        // Update profile labels with current user data
        profileLabels[0].setText("Name: " + persona.getNom());
        profileLabels[1].setText("Email: " + persona.getCorreu());
        profileLabels[2].setText("Age: " + persona.getEdat());
        profileLabels[3].setText("Country: " + persona.getPais());

        profileLabels[4].setText("Games Played: " + stats.getPartidesJugades());
        profileLabels[5].setText("Games Won: " + stats.getPartidesGuanyades());
        profileLabels[6].setText("Games Lost: " + stats.getPartidesPerdudes());
        profileLabels[7].setText("Total Score: " + stats.getPuntuacioTotal());
        profileLabels[8].setText("Personal Record: " + stats.getRecordPersonal());
        profileLabels[9].setText("Ranking Level: " + stats.getNivellRanking());
        
        // Refresh the panel
        profileInfoPanel.revalidate();
        profileInfoPanel.repaint();
    }
}
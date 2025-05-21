package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;

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
        setBackground(new Color(240, 240, 240));

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

        add(infoPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        CommonComponents.styleButton(backButton, new Color(66, 165, 245));
        backButton.addActionListener(e -> cp.showMainMenuPanel());
        add(backButton, BorderLayout.SOUTH);
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
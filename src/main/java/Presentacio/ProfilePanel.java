package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;

/**
 * Panell d'opcions per a la gestió del perfil de l'usuari.
 * Aquest component mostra la informació personal i estadístiques de l'usuari actual.
 * Permet visualitzar dades com nom, correu electrònic, edat, país i estadístiques de joc.
 * Les estadístiques inclouen partides jugades, guanyades, perdudes, puntuació total,
 * rècord personal i nivell en el rànquing.
 * 
 * El panell proporciona una interfície clara per consultar el progrés de l'usuari
 * i permet tornar al menú principal mitjançant un botó.
 */
public class ProfilePanel extends JPanel {
    /** Controlador de presentació */
    private ControladorPresentacio cp;
    /** Controlador de domini per a la lògica del joc */
    private ControladorDomini cd;

    /**
     * Constructor del panell de perfil.
     * Inicialitza els components gràfics i els gestors d'esdeveniments.
     *
     * @param cp Controlador de presentació per a la navegació entre pantalles
     * @param cd Controlador de domini per a la lògica del joc
     */
    public ProfilePanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    /**
     * Inicialitza els components gràfics del panell de perfil.
     * Configura el disseny utilitzant BorderLayout per organitzar els elements.
     * Crea i posiciona els components:
     * - Etiquetes per mostrar la informació personal de l'usuari
     * - Estadístiques de joc
     * - Botó per tornar al menú principal
     *
     * Els components s'estilitzen amb colors personalitzats per millorar la interfície d'usuari.
     */
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

    /**
     * Afegeix un camp d'informació al panell de perfil.
     * Crea una etiqueta amb el text especificat i l'afegeix al panell.
     *
     * @param label El text de l'etiqueta a afegir
     * @param panel El panell on s'afegirà la etiqueta
     */
    private void addProfileField(String label, JPanel panel) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(jLabel);
    }

    /**
     * Actualitza la informació del perfil de l'usuari actual.
     * Recupera les dades de l'usuari i les estadístiques, i les mostra al panell.
     * Si l'usuari actual no és una instància de Persona, no es realitza cap acció (en el cas d'un Bot).
     */
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
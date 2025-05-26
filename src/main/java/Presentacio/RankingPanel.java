package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

/**
 * Panell de rànquing per a mostrar les estadístiques dels jugadors.
 * Permet filtrar els resultats per diferents criteris com punts totals,
 * partides jugades, partides guanyades i rècord personal.
 * 
 * Aquesta classe proporciona una interfície gràfica que:
 * - Mostra un llistat ordenat de jugadors segons diverses estadístiques
 * - Destaca la posició del jugador actual en el rànquing
 * - Permet canviar entre diferents criteris de classificació
 * - Proporciona navegació per tornar al menú principal
 * 
 * La informació del rànquing s'obté del ControladorDomini i es presenta
 * en format tabular amb les columnes: posició, nom del jugador i valor estadístic.
 */
public class RankingPanel extends JPanel {
    /** Controlador de presentació */
    private ControladorPresentacio cp;
    /** Controlador de domini per a la lògica del joc  */
    private ControladorDomini cd;
    /** Àrea de text per mostrar el rànquing */
    private JTextArea rankingText;

    /**
     * Constructor del panell de rànquing.
     * Inicialitza els components gràfics i els gestors d'esdeveniments.
     *
     * @param cp Controlador de presentació per a la navegació entre pantalles
     * @param cd Controlador de domini per a la lògica del joc
     */
    public RankingPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

    /**
     * Inicialitza els components gràfics del panell de rànquing.
     * Configura el disseny utilitzant BorderLayout per organitzar els elements.
     * Crea i posiciona els components:
     * - Panell de filtres per seleccionar el criteri de classificació
     * - Àrea de text per mostrar el rànquing
     * - Botó per tornar al menú principal
     *
     * Els components s'estilitzen amb colors personalitzats per millorar la interfície d'usuari.
     */
    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        JPanel filterPanel = new JPanel();
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] filters = {"Total Points", "Games Played", "Games Won", "Personal Record"};
        JComboBox<String> filterCombo = new JComboBox<>(filters);
        filterCombo.addActionListener(e -> {
            updateRankingInfo(filterCombo.getSelectedIndex() + 1);
        });
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterCombo);

        add(filterPanel, BorderLayout.NORTH);

        rankingText = new JTextArea();
        rankingText.setEditable(false);
        rankingText.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rankingText.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(rankingText);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        CommonComponents.styleButton(backButton, new Color(66, 165, 245));
        backButton.addActionListener(e -> cp.showMainMenuPanel());
        add(backButton, BorderLayout.SOUTH);
    }

    /**
     * Actualitza la informació del rànquing segons el filtre seleccionat.
     * Mostra les estadístiques dels jugadors en format tabular.
     *
     * @param filter Índex del filtre seleccionat (1: Punts totals, 2: Partides jugades,
     *               3: Partides guanyades, 4: Rècord personal)
     */
    public void updateRankingInfo(int filter) {
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

            if (player.equals(cp.getCurrentUser())) {
                sb.append("----------------------------------------\n");
                sb.append(String.format("%-5d %-20s %-15d\n", i+1, player.getNom(), value));
                sb.append("----------------------------------------\n");
            }
        }

        rankingText.setText(sb.toString());
    }
}
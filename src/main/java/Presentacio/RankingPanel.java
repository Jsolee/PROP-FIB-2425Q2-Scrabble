package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

public class RankingPanel extends JPanel {
    private ControladorPresentacio cp;
    private ControladorDomini cd;
    private JTextArea rankingText;

    public RankingPanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        initialize();
    }

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
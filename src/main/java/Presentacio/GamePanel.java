package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {
    protected ControladorPresentacio cp;
    private ControladorDomini cd;

    // Game UI components
    private JPanel boardPanel;
    private JPanel rackPanel;
    private JPanel playerPanel;
    private JButton[][] boardButtons;
    private JButton[] rackButtons;
    protected List<int[]> placedTiles;
    private JLabel currentPlayerLabel;
    private JLabel remainingTilesLabel;

    public GamePanel(ControladorPresentacio cp, ControladorDomini cd) {
        this.cp = cp;
        this.cd = cd;
        this.placedTiles = new ArrayList<>();
        initialize();

        // Aseguramos que el juego se inicialice correctamente al cargar el panel
        SwingUtilities.invokeLater(() -> {
            if (cp.getCurrentGame() != null) {
                // Verificar si es un juego nuevo y preparar el atril del jugador
                updateGameBoard();
            }
        });
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Main game area panel
        JPanel gameAreaPanel = new JPanel(new BorderLayout());
        gameAreaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Board panel
        initializeBoard();
        JScrollPane boardScroll = new JScrollPane(boardPanel);
        boardScroll.setPreferredSize(new Dimension(700, 700));
        gameAreaPanel.add(boardScroll, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Game info panel
        JPanel gameInfoPanel = new JPanel(new GridLayout(3, 1));

        currentPlayerLabel = new JLabel("", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameInfoPanel.add(currentPlayerLabel);

        remainingTilesLabel = new JLabel("", JLabel.CENTER);
        remainingTilesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gameInfoPanel.add(remainingTilesLabel);

        JLabel instructionLabel = new JLabel("Drag tiles from rack to board", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gameInfoPanel.add(instructionLabel);

        controlPanel.add(gameInfoPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton playWordButton = new JButton("Play Word");
        CommonComponents.styleButton(playWordButton, new Color(76, 175, 80));
        playWordButton.addActionListener(e -> confirmWordPlacement());
        buttonPanel.add(playWordButton);

        JButton exchangeButton = new JButton("Exchange Tiles");
        CommonComponents.styleButton(exchangeButton, new Color(251, 192, 45));
        exchangeButton.addActionListener(e -> exchangeTiles());
        buttonPanel.add(exchangeButton);

        JButton passButton = new JButton("Pass Turn");
        CommonComponents.styleButton(passButton, new Color(66, 165, 245));
        passButton.addActionListener(e -> passTurn());
        buttonPanel.add(passButton);

        JButton resignButton = new JButton("Resign");
        CommonComponents.styleButton(resignButton, new Color(239, 83, 80));
        resignButton.addActionListener(e -> resignGame());
        buttonPanel.add(resignButton);

        JButton saveButton = new JButton("Save Game");
        CommonComponents.styleButton(saveButton, new Color(171, 71, 188));
        saveButton.addActionListener(e -> saveGame());
        buttonPanel.add(saveButton);

        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        gameAreaPanel.add(controlPanel, BorderLayout.SOUTH);

        // Player and rack panel
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(250, 0));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player panel
        playerPanel = new JPanel(new GridLayout(0, 1));
        playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        sidePanel.add(playerPanel, BorderLayout.NORTH);

        // Rack panel
        rackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        rackPanel.setBorder(BorderFactory.createTitledBorder("Your Rack"));
        rackPanel.setBackground(new Color(240, 240, 240));
        sidePanel.add(rackPanel, BorderLayout.CENTER);

        add(gameAreaPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
    }

    private void initializeBoard() {
        boardPanel = new JPanel(new GridLayout(15, 15, 1, 1));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        boardPanel.setBackground(Color.BLACK);
        boardButtons = new JButton[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                final int row = i;
                final int col = j;
                String position = i + "," + j;

                JButton cellButton = new JButton();
                cellButton.setOpaque(true);
                cellButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                cellButton.setPreferredSize(new Dimension(40, 40));
                cellButton.setFont(new Font("Arial", Font.BOLD, 14));

                // per poder borrar fitxed del tauler amb click dret
                // Añadir listener para clic derecho sobre casillas del tablero
                cellButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            // Buscar si hay una ficha temporal colocada en esta casilla
                            for (int k = 0; k < placedTiles.size(); k++) {
                                int[] tilePos = placedTiles.get(k);
                                if (tilePos[0] == row && tilePos[1] == col) {
                                    int tileIndex = tilePos[2];
                                    removeTileFromBoard(tileIndex);
                                    break;
                                }
                            }
                        }
                    }
                });

                // Set colors based on special positions
                if (i == 7 && j == 7) {
                    CommonComponents.styleBoardButton(cellButton, CommonComponents.CENTER_COLOR, "★");
                } else if (BoardUtils.isSpecialPosition(position, "tripleWord")) {
                    CommonComponents.styleBoardButton(cellButton, CommonComponents.TRIPLE_WORD_COLOR, "TW");
                } else if (BoardUtils.isSpecialPosition(position, "doubleWord")) {
                    CommonComponents.styleBoardButton(cellButton, CommonComponents.DOUBLE_WORD_COLOR, "DW");
                } else if (BoardUtils.isSpecialPosition(position, "tripleLetter")) {
                    CommonComponents.styleBoardButton(cellButton, CommonComponents.TRIPLE_LETTER_COLOR, "TL");
                } else if (BoardUtils.isSpecialPosition(position, "doubleLetter")) {
                    CommonComponents.styleBoardButton(cellButton, CommonComponents.DOUBLE_LETTER_COLOR, "DL");
                } else {
                    CommonComponents.styleBoardButton(cellButton, CommonComponents.DEFAULT_COLOR, "");
                }

                BoardUtils.setupDropTarget(cellButton, row, col, this);
                boardButtons[i][j] = cellButton;
                boardPanel.add(cellButton);
            }
        }
    }

    public void updateGameBoard() {
        if (cp.getCurrentGame() == null) return;

        // Update game info
        currentPlayerLabel.setText("Current Turn: " + cp.getCurrentGame().getJugadorActual().getNom());
        remainingTilesLabel.setText("Tiles remaining: " + cp.getCurrentGame().getBossa().getQuantitatFitxes());

        Taulell board = cp.getCurrentGame().getTaulell();

        // Update board
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                JButton cellButton = boardButtons[i][j];
                Casella cell = board.getCasella(i, j);

                if (cell.getFitxa() != null) {
                    cellButton.setText(cell.getFitxa().getLletra());
                    cellButton.setForeground(CommonComponents.TILE_TEXT_COLOR);
                    cellButton.setBackground(CommonComponents.TILE_COLOR);
                    cellButton.setToolTipText("Value: " + cell.getFitxa().getValor());
                } else {
                    resetBoardButtonAppearance(i, j);
                    cellButton.setToolTipText(null);
                }
            }
        }

        // Update player info
        playerPanel.removeAll();
        List<Usuari> players = cp.getCurrentGame().getJugadors();
        List<Integer> scores = cp.getCurrentGame().getPuntuacions();
        for (int i = 0; i < players.size(); i++) {
            JLabel playerLabel = new JLabel(players.get(i).getNom() + ": " + scores.get(i) + " points");
            playerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            if (players.get(i).equals(cp.getCurrentGame().getJugadorActual())) {
                playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD));
            }
            playerPanel.add(playerLabel);
        }

        // Update rack with drag support
        rackPanel.removeAll();
        List<Fitxa> rack = cp.getCurrentGame().getAtril();
        rackButtons = new JButton[rack.size()];

        for (int i = 0; i < rack.size(); i++) {
            final int tileIndex = i;
            Fitxa tile = rack.get(i);
            JButton tileButton = new JButton(tile.getLletra());
            tileButton.setFont(new Font("Arial", Font.BOLD, 16));
            tileButton.setForeground(CommonComponents.TILE_TEXT_COLOR);
            tileButton.setBackground(CommonComponents.TILE_COLOR);
            tileButton.setToolTipText("Value: " + tile.getValor());
            tileButton.setPreferredSize(new Dimension(40, 50));
            tileButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createLineBorder(new Color(139, 69, 19), 2) // Wood border
            ));

            // Make tiles draggable
            tileButton.setTransferHandler(new TileTransferHandler(tileIndex));
            tileButton.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    handler.exportAsDrag(comp, e, TransferHandler.COPY);
                }
            });

            // Add right-click to remove tiles from board
            tileButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        removeTileFromBoard(tileIndex);
                    }
                }
            });

            rackButtons[i] = tileButton;
            rackPanel.add(tileButton);
        }

        cp.getFrame().revalidate();
        cp.getFrame().repaint();
    }

    private class TileTransferHandler extends TransferHandler {
        private final int tileIndex;

        public TileTransferHandler(int tileIndex) {
            this.tileIndex = tileIndex;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new StringSelection(tileIndex + "");
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }
    }

    private void removeTileFromBoard(int tileIndex) {
        // Find if this tile is placed on the board
        Iterator<int[]> iterator = placedTiles.iterator();
        while (iterator.hasNext()) {
            int[] tilePos = iterator.next();
            if (tilePos[2] == tileIndex) {
                int row = tilePos[0];
                int col = tilePos[1];
                resetBoardButtonAppearance(row, col);
                iterator.remove();

                // Make the tile visible again in the rack
                if (rackButtons != null && tileIndex < rackButtons.length) {
                    rackButtons[tileIndex].setVisible(true);
                }
                break;
            }
        }
    }

    // Nuevo método para ocultar la ficha del rack cuando se coloca en el tablero
    public void hideTileInRack(int tileIndex) {
        if (rackButtons != null && tileIndex < rackButtons.length) {
            rackButtons[tileIndex].setVisible(false);
        }
    }

    protected void resetBoardButtonAppearance(int row, int col) {
        JButton button = boardButtons[row][col];
        String position = row + "," + col;

        if (cp.getCurrentGame().getTaulell().getCasella(row, col).getFitxa() != null) {
            button.setBackground(CommonComponents.TILE_COLOR);
        } else {
            if (row == 7 && col == 7) {
                CommonComponents.styleBoardButton(button, CommonComponents.CENTER_COLOR, "★");
            } else if (BoardUtils.isSpecialPosition(position, "tripleWord")) {
                CommonComponents.styleBoardButton(button, CommonComponents.TRIPLE_WORD_COLOR, "TW");
            } else if (BoardUtils.isSpecialPosition(position, "doubleWord")) {
                CommonComponents.styleBoardButton(button, CommonComponents.DOUBLE_WORD_COLOR, "DW");
            } else if (BoardUtils.isSpecialPosition(position, "tripleLetter")) {
                CommonComponents.styleBoardButton(button, CommonComponents.TRIPLE_LETTER_COLOR, "TL");
            } else if (BoardUtils.isSpecialPosition(position, "doubleLetter")) {
                CommonComponents.styleBoardButton(button, CommonComponents.DOUBLE_LETTER_COLOR, "DL");
            } else {
                CommonComponents.styleBoardButton(button, CommonComponents.DEFAULT_COLOR, "");
            }
        }
    }

    private void confirmWordPlacement() {
        if (placedTiles.isEmpty()) {
            JOptionPane.showMessageDialog(cp.getFrame(), "No tiles placed on board", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determine orientation (H or V)
        String orientation = determineOrientation();
        if (orientation == null) {
            JOptionPane.showMessageDialog(cp.getFrame(), "Tiles must be placed in a straight line", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the moves map
        LinkedHashMap<int[], Fitxa> moves = new LinkedHashMap<>();
        List<Integer> indices = new ArrayList<>();

        for (int[] tilePos : placedTiles) {
            int row = tilePos[0];
            int col = tilePos[1];
            int tileIndex = tilePos[2];

            moves.put(new int[]{row, col}, cp.getCurrentGame().getAtril().get(tileIndex));
            indices.add(tileIndex);
        }

        try {
            int score = cd.jugarParaula(cp.getCurrentGame(), moves, orientation, indices);
            if (score != -1) {
                JOptionPane.showMessageDialog(cp.getFrame(), "Word played successfully! Score: " + score,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                placedTiles.clear();
                cp.getCurrentGame().passarTorn();
                if (cp.getCurrentGame().getJugadorActual() instanceof Bot) {
                    botTurn();
                } else {
                    cp.setCurrentUser(cd.getUsuari(cp.getCurrentGame().getJugadorActual().getNom()));
                }

                updateGameBoard();
            } else {
                JOptionPane.showMessageDialog(cp.getFrame(), "Invalid word or position",
                        "Error", JOptionPane.ERROR_MESSAGE);
                // Reset the placed tiles
                placedTiles.clear();
                updateGameBoard();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(cp.getFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            placedTiles.clear();
            updateGameBoard();
        }
    }

    private String determineOrientation() {
        if (placedTiles.size() == 1) return "H"; // Single tile can be either

        // Check if all rows are the same (horizontal)
        boolean horizontal = true;
        int firstRow = placedTiles.get(0)[0];
        for (int i = 1; i < placedTiles.size(); i++) {
            if (placedTiles.get(i)[0] != firstRow) {
                horizontal = false;
                break;
            }
        }
        if (horizontal) return "H";

        // Check if all columns are the same (vertical)
        boolean vertical = true;
        int firstCol = placedTiles.get(0)[1];
        for (int i = 1; i < placedTiles.size(); i++) {
            if (placedTiles.get(i)[1] != firstCol) {
                vertical = false;
                break;
            }
        }
        if (vertical) return "V";

        return null; // Not in a straight line
    }

    private void passTurn() {
        int confirm = JOptionPane.showConfirmDialog(cp.getFrame(),
                "Are you sure you want to pass your turn?",
                "Confirm Pass",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            placedTiles.clear();
            cp.getCurrentGame().passarTorn();

            if (cp.getCurrentGame().getJugadorActual() instanceof Bot) {
                botTurn();
            } else {
                cp.setCurrentUser(cd.getUsuari(cp.getCurrentGame().getJugadorActual().getNom()));
            }
            updateGameBoard();
        }
    }

    private void exchangeTiles() {
        if (!cp.getCurrentGame().getJugadorActual().equals(cp.getCurrentUser())) {
            JOptionPane.showMessageDialog(cp.getFrame(), "It's not your turn", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dialog to select tiles to exchange
        JDialog exchangeDialog = new JDialog(cp.getFrame(), "Exchange Tiles", true);
        exchangeDialog.setLayout(new BorderLayout());
        exchangeDialog.setSize(400, 300);

        JPanel tilePanel = new JPanel(new GridLayout(0, 1));
        tilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Fitxa> rack = cp.getCurrentGame().getAtril();
        JCheckBox[] checkBoxes = new JCheckBox[rack.size()];

        for (int i = 0; i < rack.size(); i++) {
            Fitxa tile = rack.get(i);
            JCheckBox checkBox = new JCheckBox(tile.getLletra() + " (" + tile.getValor() + ")");
            checkBoxes[i] = checkBox;
            tilePanel.add(checkBox);
        }

        exchangeDialog.add(new JScrollPane(tilePanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Exchange");
        submitButton.addActionListener(e -> {
            List<String> indices = new ArrayList<>();
            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    indices.add(String.valueOf(i));
                }
            }

            if (indices.isEmpty()) {
                JOptionPane.showMessageDialog(exchangeDialog, "No tiles selected", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                cd.canviDeFitxes(cp.getCurrentGame(), indices.toArray(new String[0]));
                placedTiles.clear();
                cp.getCurrentGame().passarTorn();

                if (cp.getCurrentGame().getJugadorActual() instanceof Bot) {
                    botTurn();
                } else {
                    cp.setCurrentUser(cd.getUsuari(cp.getCurrentGame().getJugadorActual().getNom()));
                }

                updateGameBoard();
                exchangeDialog.dispose();
                JOptionPane.showMessageDialog(cp.getFrame(), "Tiles exchanged successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(exchangeDialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(submitButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> exchangeDialog.dispose());
        buttonPanel.add(cancelButton);

        exchangeDialog.add(buttonPanel, BorderLayout.SOUTH);
        exchangeDialog.setVisible(true);
    }

    private void saveGame() {
        cp.getCurrentGame().guardarPartida();
        JOptionPane.showMessageDialog(cp.getFrame(), "Game saved successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
        
        rackPanel.removeAll();
        playerPanel.removeAll();
        cp.getFrame().revalidate();
        cp.getFrame().repaint();
        // Clear the game data
        cp.setCurrentGame(null);
        // Clear the placed tiles

        placedTiles.clear();
        cp.showMainMenuPanel();
    }

    private void resignGame() {
        int confirm = JOptionPane.showConfirmDialog(cp.getFrame(),
                "Are you sure you want to resign?",
                "Confirm Resign",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            cd.acabarPartida(cp.getCurrentGame());
            Usuari winner = cp.getCurrentGame().determinarGuanyador();
            if (winner == null) {
                JOptionPane.showMessageDialog(cp.getFrame(),
                        "Game over! It's a draw!",
                        "Game Ended",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(cp.getFrame(),
                    "Game over! Winner: " + winner.getNom(),
                    "Game Ended",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            //reset board and placed tiles
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    resetBoardButtonAppearance(i, j);
                }
            }
            rackPanel.removeAll();
            playerPanel.removeAll();
            cp.getFrame().revalidate();
            cp.getFrame().repaint();
            // Clear the game data
            cp.setCurrentGame(null);
            // Clear the placed tiles

            placedTiles.clear();
            cp.showMainMenuPanel();
        }
    }

    private void botTurn() {
        cd.posarParaulaBot(cp.getCurrentGame(), cp.getCurrentGame().getJugadorActual());
        updateGameBoard();
    }
}
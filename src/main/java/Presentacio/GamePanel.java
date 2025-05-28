package Presentacio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import Domini.Bot;
import Domini.Casella;
import Domini.ControladorDomini;
import Domini.Fitxa;
import Domini.Taulell;
import Domini.Usuari;

/**
 * Panel principal del joc Scrabble.
 * Aquest panell mostra el tauler de joc, l'atril del jugador,
 * la informació dels jugadors, el torn i les opcions per jugar, intercanviar fitxes,
 * passar torn, renunciar i guardar la partida.
 * 
 * Aquest panell també gestiona la inicialització del joc i l'actualització
 * de la interfície d'usuari en funció de l'estat del joc.
 * Permet la col·locació de fitxes al tauler mitjançant arrossegament i
 * solta (Drag&Drop), així com la interacció amb les fitxes de l'atril i la gestió d'aquestes fitxes.
 */
public class GamePanel extends JPanel {

    /** Controlador de presentació */
    protected ControladorPresentacio cp;

    /** Controlador de domini */
    private ControladorDomini cd;

    /** Panell que conté el tauler de joc */
    private JPanel boardPanel;
    /** Panell que conté l'atril del jugador */
    private JPanel rackPanel;
    /** Panell que mostra la informació dels jugadors */
    private JPanel playerPanel;
    /** Botons del tauler de joc */
    private JButton[][] boardButtons;
    /** Botons de l'atril del jugador */
    private JButton[] rackButtons;
    /** Llista de fitxes col·locades al tauler */
    protected List<int[]> placedTiles;
    /** Etiqueta per msotrar el jugador actual */
    private JLabel currentPlayerLabel;
    /** Etiqueta per mostrar el nombre de fitxes restants a la bossa */
    private JLabel remainingTilesLabel;

    /**
     * Constructor del panell de joc.
     * Inicialitza els components gràfics i prepara el tauler i l'atril.
     *
     * @param cp Controlador de presentació
     * @param cd Controlador de domini
     */
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


    /**
     * Inicialitza els components gràfics del panell de joc.
     * Configura el disseny principal (BorderLayout), crea i posiciona tots els components:
     * - El taulell de joc amb les seves 15x15 caselles i propietats especials
     * - L'atril del jugador amb les fitxes disponibles
     * - El panell d'informació dels jugadors amb puntuacions
     * - Els botons de control per jugar paraula, intercanviar fitxes, passar torn, etc.
     * - Les etiquetes informatives sobre el torn actual i les fitxes restants
     * 
     * També configura els gestors d'esdeveniments per al drag and drop de les fitxes
     * i els esdeveniments de ratolí per facilitar la interacció amb el tauler.
     */
    private void initialize() {
        setLayout(new BorderLayout());
        // Scrabble gradient background
        JPanel backgroundPanel = ModernUI.createScrabbleGradientPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        // Card panel for game content
        JPanel card = ModernUI.createScrabbleCard();
        card.setLayout(new BoxLayout(card, BoxLayout.X_AXIS));
        int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
        int cardWidth = Math.min(1100, (int)(screenW * 0.95));
        int cardHeight = Math.min(850, (int)(screenH * 0.92));
        card.setPreferredSize(new Dimension(cardWidth, cardHeight));
        card.setMaximumSize(new Dimension(cardWidth, cardHeight));

        // Main vertical panel for board + info
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Make the board a bit smaller to ensure space for buttons
        int controlsHeight = 120;
        int boardSize = Math.min(cardHeight - controlsHeight - 32, cardWidth - 340 - 32); // 340 for side+button panel
        boardSize = Math.max(boardSize, 420); // minimum size

        // Board panel
        initializeBoard();
        boardPanel.setPreferredSize(new Dimension(boardSize, boardSize));
        boardPanel.setMaximumSize(new Dimension(boardSize, boardSize));
        boardPanel.setMinimumSize(new Dimension(boardSize, boardSize));
        mainPanel.add(boardPanel);
        mainPanel.add(Box.createVerticalStrut(12));

        // Game info panel
        JPanel gameInfoPanel = new JPanel(new GridLayout(3, 1));
        gameInfoPanel.setOpaque(false);
        currentPlayerLabel = new JLabel("", JLabel.CENTER);
        currentPlayerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gameInfoPanel.add(currentPlayerLabel);
        remainingTilesLabel = new JLabel("", JLabel.CENTER);
        remainingTilesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gameInfoPanel.add(remainingTilesLabel);
        JLabel instructionLabel = new JLabel("Drag tiles from rack to board", JLabel.CENTER);
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        gameInfoPanel.add(instructionLabel);
        mainPanel.add(gameInfoPanel);
        mainPanel.add(Box.createVerticalStrut(8));

        // Button panel (vertical, right of board)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 8, 20, 8));
        JButton playWordButton = ModernUI.createScrabbleButton("✓ Play Word", ModernUI.SCRABBLE_GREEN);
        playWordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playWordButton.addActionListener(e -> confirmWordPlacement());
        buttonPanel.add(playWordButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        JButton exchangeButton = ModernUI.createScrabbleButton("🔄 Exchange", ModernUI.SCRABBLE_TEAL);
        exchangeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exchangeButton.addActionListener(e -> exchangeTiles());
        buttonPanel.add(exchangeButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        JButton passButton = ModernUI.createScrabbleButton("⏭️ Pass", ModernUI.SCRABBLE_BLUE);
        passButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        passButton.addActionListener(e -> passTurn());
        buttonPanel.add(passButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        JButton resignButton = ModernUI.createScrabbleButton("🏳️ Resign", ModernUI.ERROR_RED);
        resignButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resignButton.addActionListener(e -> resignGame());
        buttonPanel.add(resignButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        JButton saveButton = ModernUI.createScrabbleButton("💾 Save", ModernUI.SCRABBLE_LIGHT_BLUE);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> saveGame());
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Side panel (players + rack)
        JPanel sidePanel = new JPanel();
        sidePanel.setOpaque(false);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(160, cardHeight - 32));
        sidePanel.setMaximumSize(new Dimension(160, cardHeight - 32));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerPanel = new JPanel(new GridLayout(0, 1));
        playerPanel.setOpaque(false);
        playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
        sidePanel.add(playerPanel);
        sidePanel.add(Box.createVerticalStrut(16));
        rackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        rackPanel.setOpaque(false);
        rackPanel.setBorder(BorderFactory.createTitledBorder("Your Rack"));
        sidePanel.add(rackPanel);
        sidePanel.add(Box.createVerticalGlue());

        // Horizontal container for board+info, buttons, and side panel
        JPanel hBox = new JPanel();
        hBox.setOpaque(false);
        hBox.setLayout(new BoxLayout(hBox, BoxLayout.X_AXIS));
        hBox.add(mainPanel);
        hBox.add(Box.createHorizontalStrut(18));
        hBox.add(buttonPanel);
        hBox.add(Box.createHorizontalStrut(18));
        hBox.add(sidePanel);

        card.add(hBox);

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

    /**
     * Inicialitza el tauler de joc amb les seves caselles i propietats especials.
     * Crea un panell de 15x15 caselles, assigna colors i etiquetes a les posicions especials
     * (centre, triple/doble paraula, triple/doble lletra) i configura els listeners per al drag and drop.
     */
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

    /**
     * Actualitza el tauler de joc amb la informació actual del joc.
     * Mostra el jugador actual, les fitxes restants a la bossa,
     * i actualitza les caselles del tauler amb les fitxes col·locades.
     * També actualitza l'atril del jugador amb les fitxes disponibles.
     */
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

    /** 
     * Classe interna per gestionar el drag and drop de les fitxes del rack.
     */
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

    /**
     * Mètode per eliminar una fitxa del tauler de joc.
     * Busca la fitxa col·locada al tauler i la elimina, 
     * restablint l'aparença del botó corresponent i fent visible la fitxa al rack (amb click dret).
     *
     * @param tileIndex Índex de la fitxa a eliminar
     */
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

    /**
     * Mètode per ocultar una fitxa específica a l'atril del jugador.
     * Utilitzat quan una fitxa s'ha col·locat al tauler i ja no ha de ser visible a l'atril.
     *
     * @param tileIndex Índex de la fitxa a ocultar
     */
    public void hideTileInRack(int tileIndex) {
        if (rackButtons != null && tileIndex < rackButtons.length) {
            rackButtons[tileIndex].setVisible(false);
        }
    }

    /**
     * Restableix l'aparença d'un botó del tauler de joc.
     * Si la casella té una fitxa, es mostra amb el color de fitxa.
     * Si no té fitxa, es mostra amb el color per defecte o un color especial si és una posició especial.
     *
     * @param row Fila de la casella
     * @param col Columna de la casella
     */
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

    /**
     * Mètode per confirmar la col·locació de les paraules al tauler.
     * Verifica que les fitxes estiguin col·locades en línia recta (horitzontal o vertical),
     * crea un mapa de moviments i intenta jugar la paraula.
     * Si té èxit, actualitza el tauler i passa el torn al següent jugador.
     * Si falla, mostra un missatge d'error i reinicia les fitxes col·locades.
     */
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

    /**
     * Determina l'orientació de les fitxes col·locades (horitzontal o vertical).
     * Si només hi ha una fitxa, es considera que pot ser en qualsevol orientació.
     * Si totes les fitxes estan en la mateixa fila, retorna "H" (horitzontal).
     * Si totes les fitxes estan en la mateixa columna, retorna "V" (vertical).
     * Si no estan alineades, retorna null.
     *
     * @return "H", "V" o null si no estan alineades
     */
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

    /**
     * Mètode per passar el torn al següent jugador.
     * Mostra un diàleg de confirmació i, si s'accepta, neteja les fitxes col·locades,
     * passa el torn al següent jugador i actualitza el tauler.
     * Si el jugador actual és un bot, inicia el torn del bot.
     */
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

    /**
     * Mètode per intercanviar fitxes de l'atril del jugador.
     * Mostra un diàleg per seleccionar les fitxes a intercanviar,
     * i si es seleccionen, intenta realitzar l'intercanvi.
     * Si té èxit, actualitza el tauler i passa el torn al següent jugador.
     * Si falla, mostra un missatge d'error.
     */
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

    /**
     * Mètode per guardar la partida actual.
     * Realitza la comprovació de si hi ha una partida en curs,
     * guarda la partida i neteja les dades del joc i l'atril.
     * Finalment, mostra un missatge de confirmació i torna al menú principal.
     */
    private void saveGame() {
        cp.getCurrentGame().guardarPartida();
        JOptionPane.showMessageDialog(cp.getFrame(), "Game saved successfully", "Info", JOptionPane.INFORMATION_MESSAGE);
        
        rackPanel.removeAll();
        playerPanel.removeAll();
        cp.getFrame().revalidate();
        cp.getFrame().repaint();
        // Clear the game data
        cp.setCurrentUser(cp.getCurrentGame().getJugadors().getFirst());
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
            cd.acabarPartida(cp.getCurrentGame(), cp.getCurrentGame().getJugadorActual());
            List<Usuari> players = cp.getCurrentGame().getJugadors();

            Usuari winner = null;
            // choose the user who is not the current player
            if (Objects.equals(players.get(0).getNom(), cp.getCurrentGame().getJugadorActual().getNom())) {
                winner = players.get(1);
            }
            else { winner = players.get(0); }

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

    /**
     * Mètode per gestionar el torn del bot.
     * El bot col·loca una paraula al tauler i actualitza el tauler de joc.
     * Aquest mètode s'invoca quan el jugador actual és un bot.
     */
    private void botTurn() {
        cd.posarParaulaBot(cp.getCurrentGame(), cp.getCurrentGame().getJugadorActual());
        updateGameBoard();
    }
}
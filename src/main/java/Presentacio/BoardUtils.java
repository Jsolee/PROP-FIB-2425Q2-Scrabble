package Presentacio;

import Domini.*;
import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.util.List;

/**
 * Classe utilitària per a la gestió de posicions especials al tauler del joc Scrabble.
 * Proporciona mètodes per determinar si una posició és especial (triple paraula, doble paraula, etc.)
 * i per configurar els botons del tauler per acceptar fitxes arrossegades.
 *
 * Aquesta classe inclou mètodes per verificar posicions especials i gestionar la interacció
 * amb els botons del taulell durant el joc.
 */
public class BoardUtils {
    /**
     * Verifica si una posició donada és una posició especial al tauler del joc.
     * Les posicions especials inclouen triple paraula, doble paraula, triple lletra i doble lletra.
     *
     * @param position La posició en format "fila,columna" (per exemple, "0,0").
     * @param type El tipus de posició especial a verificar ("tripleWord", "doubleWord", "tripleLetter", "doubleLetter").
     * @return true si la posició és especial segons el tipus especificat, false en cas contrari.
     */
    public static boolean isSpecialPosition(String position, String type) {
        String[] coords = position.split(",");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        // Triple Word positions
        if (type.equals("tripleWord")) {
            return (row == 0 && col == 0) || (row == 0 && col == 7) || (row == 0 && col == 14) ||
                    (row == 7 && col == 0) || (row == 7 && col == 14) ||
                    (row == 14 && col == 0) || (row == 14 && col == 7) || (row == 14 && col == 14);
        }
        // Double Word positions
        else if (type.equals("doubleWord")) {
            return (row == 1 && col == 1) || (row == 2 && col == 2) || (row == 3 && col == 3) ||
                    (row == 4 && col == 4) || (row == 1 && col == 13) || (row == 2 && col == 12) ||
                    (row == 3 && col == 11) || (row == 4 && col == 10) || (row == 10 && col == 4) ||
                    (row == 11 && col == 3) || (row == 12 && col == 2) || (row == 13 && col == 1) ||
                    (row == 10 && col == 10) || (row == 11 && col == 11) || (row == 12 && col == 12) ||
                    (row == 13 && col == 13);
        }
        // Triple Letter positions
        else if (type.equals("tripleLetter")) {
            return (row == 1 && col == 5) || (row == 1 && col == 9) || (row == 5 && col == 1) ||
                    (row == 5 && col == 5) || (row == 5 && col == 9) || (row == 5 && col == 13) ||
                    (row == 9 && col == 1) || (row == 9 && col == 5) || (row == 9 && col == 9) ||
                    (row == 9 && col == 13) || (row == 13 && col == 5) || (row == 13 && col == 9);
        }
        // Double Letter positions
        else if (type.equals("doubleLetter")) {
            return (row == 0 && col == 3) || (row == 0 && col == 11) || (row == 2 && col == 6) ||
                    (row == 2 && col == 8) || (row == 3 && col == 0) || (row == 3 && col == 7) ||
                    (row == 3 && col == 14) || (row == 6 && col == 2) || (row == 6 && col == 6) ||
                    (row == 6 && col == 8) || (row == 6 && col == 12) || (row == 7 && col == 3) ||
                    (row == 7 && col == 11) || (row == 8 && col == 2) || (row == 8 && col == 6) ||
                    (row == 8 && col == 8) || (row == 8 && col == 12) || (row == 11 && col == 0) ||
                    (row == 11 && col == 7) || (row == 11 && col == 14) || (row == 12 && col == 6) ||
                    (row == 12 && col == 8) || (row == 14 && col == 3) || (row == 14 && col == 11);
        }
        return false;
    }

    /**
     * Configura un botó per acceptar fitxes arrossegades i col·locar-les al tauler.
     * El botó es configura amb un TransferHandler que gestiona la importació de dades
     * i l'aparença del botó després de col·locar una fitxa.
     *
     * @param target El botó que actuarà com a destinació per a les fitxes arrossegades.
     * @param row La fila del tauler on es col·locarà la fitxa.
     * @param col La columna del tauler on es col·locarà la fitxa.
     * @param gamePanel El panell del joc que conté la lògica del joc i l'estat actual.
     */
    public static void setupDropTarget(JButton target, int row, int col, GamePanel gamePanel) {
        target.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                boolean canImport = support.isDataFlavorSupported(DataFlavor.stringFlavor) &&
                        gamePanel.cp.getCurrentGame() != null &&
                        gamePanel.cp.getCurrentGame().getJugadorActual().equals(gamePanel.cp.getCurrentUser()) &&
                        gamePanel.cp.getCurrentGame().getTaulell().getCasella(row, col).getFitxa() == null;
                        
                return canImport;
            }

            @Override
            public void exportDone(JComponent c, Transferable data, int action) {
                gamePanel.resetBoardButtonAppearance(row, col);
                super.exportDone(c, data, action);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String tileData = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    String[] parts = tileData.split(",");
                    int tileIndex = Integer.parseInt(parts[0]);

                    gamePanel.resetBoardButtonAppearance(row, col);

                    List<Fitxa> rack = gamePanel.cp.getCurrentGame().getAtril();
                    if (tileIndex >= 0 && tileIndex < rack.size()) {
                        Fitxa tile = rack.get(tileIndex);
                        target.setText(tile.getLletra());
                        target.setForeground(CommonComponents.TILE_TEXT_COLOR);
                        target.setBackground(CommonComponents.TILE_COLOR);
                        target.setToolTipText("Value: " + tile.getValor());

                        gamePanel.placedTiles.add(new int[]{row, col, tileIndex});

                        // Ocultar la ficha en el rack después de colocarla en el tablero
                        gamePanel.hideTileInRack(tileIndex);

                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
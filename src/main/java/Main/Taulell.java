package Main;

import java.util.ArrayList;
import java.util.List;

/*

    0   1   2   3   4   5   6   7   8   9  10  11  12  13  14
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
0 |TP | · | · |DL | · | · | · |TP | · | · | · |DL | · | · |TP |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
1 | · |DP | · | · | · |TL | · | · | · |TL | · | · | · |DP | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
2 | · | · |DP | · | · | · |DL | · |DL | · | · | · |DP | · | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
3 |DL | · | · |DP | · | · | · |DL | · | · | · |DP | · | · |DL |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
4 | · | · | · | · |DP | · | · | · | · | · |DP | · | · | · | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
5 | · |TL | · | · | · |TL | · | · | · |TL | · | · | · |TL | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
6 | · | · |DL | · | · | · |DL | · |DL | · | · | · |DL | · | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
7 |TP | · | · |DL | · | · | · |★ | · | · | · |DL | · | · |TP |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
8 | · | · |DL | · | · | · |DL | · |DL | · | · | · |DL | · | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
9 | · |TL | · | · | · |TL | · | · | · |TL | · | · | · |TL | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
10| · | · | · | · |DP | · | · | · | · | · |DP | · | · | · | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
11|DL | · | · |DP | · | · | · |DL | · | · | · |DP | · | · |DL |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
12| · | · |DP | · | · | · |DL | · |DL | · | · | · |DP | · | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
13| · |DP | · | · | · |TL | · | · | · |TL | · | · | · |DP | · |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
14|TP | · | · |DL | · | · | · |TP | · | · | · |DL | · | · |TP |
  +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+

 */

public class Taulell {
    private Casella[][] caselles;
    private static final int MIDA = 15;
    private boolean primerMoviment = true;

    public Taulell() {
        caselles = new Casella[MIDA][MIDA];
        inicialitzarTaulell();
    }

    private void inicialitzarTaulell() {
        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                // Inicialitzar cada Casella amb multiplicadors apropiats
                caselles[i][j] = new Casella(i, j, getMultiplicadorLletra(i, j), getMultiplicadorParaula(i, j));
            }
        }
        // Establir la casella central com la inicial
        caselles[MIDA / 2][MIDA / 2].setEsCasellaInicial(true);
    }

    private int getMultiplicadorLletra(int x, int y) {
        // Definir caselles de doble lletra (DL)
        if ((x == 0 && y == 3) || (x == 0 && y == 11) ||
                (x == 2 && y == 6) || (x == 2 && y == 8) ||
                (x == 3 && y == 0) || (x == 3 && y == 7) || (x == 3 && y == 14) ||
                (x == 6 && y == 2) || (x == 6 && y == 6) || (x == 6 && y == 8) || (x == 6 && y == 12) ||
                (x == 7 && y == 3) || (x == 7 && y == 11) ||
                (x == 8 && y == 2) || (x == 8 && y == 6) || (x == 8 && y == 8) || (x == 8 && y == 12) ||
                (x == 11 && y == 0) || (x == 11 && y == 7) || (x == 11 && y == 14) ||
                (x == 12 && y == 6) || (x == 12 && y == 8) ||
                (x == 14 && y == 3) || (x == 14 && y == 11)) {
            return 2;
        }
        // Definir caselles de triple lletra (TL)
        if ((x == 1 && y == 5) || (x == 1 && y == 9) ||
                (x == 5 && y == 1) || (x == 5 && y == 5) || (x == 5 && y == 9) || (x == 5 && y == 13) ||
                (x == 9 && y == 1) || (x == 9 && y == 5) || (x == 9 && y == 9) || (x == 9 && y == 13) ||
                (x == 13 && y == 5) || (x == 13 && y == 9)) {
            return 3;
        }
        return 1; // Sense multiplicador
    }

    private int getMultiplicadorParaula(int x, int y) {
        // Definir caselles de doble paraula (DP)
        if ((x == 1 && y == 1) || (x == 2 && y == 2) || (x == 3 && y == 3) || (x == 4 && y == 4) ||
                (x == 10 && y == 10) || (x == 11 && y == 11) || (x == 12 && y == 12) || (x == 13 && y == 13) ||
                (x == 1 && y == 13) || (x == 2 && y == 12) || (x == 3 && y == 11) || (x == 4 && y == 10) ||
                (x == 10 && y == 4) || (x == 11 && y == 3) || (x == 12 && y == 2) || (x == 13 && y == 1) ||
                (x == 7 && y == 7)) {
            return 2;
        }
        // Definir caselles de triple paraula (TP)
        if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 0 && y == 14) ||
                (x == 7 && y == 0) || (x == 7 && y == 14) ||
                (x == 14 && y == 0) || (x == 14 && y == 7) || (x == 14 && y == 14)) {
            return 3;
        }
        return 1; // Sense multiplicador
    }

    public boolean colocarFitxa(int x, int y, Fitxa fitxa) {
        if (esMovimentValid(x, y, fitxa)) {
            boolean resultat = caselles[x][y].colocarFitxa(fitxa);
            if (resultat && caselles[x][y].isEsCasellaInicial()) {
                primerMoviment = false;
            }
            return resultat;
        }
        return false;
    }

    private boolean esMovimentValid(int x, int y, Fitxa fitxa) {
        // Verificar límits del taulell i si la casella ja està ocupada
        if (x < 0 || x >= MIDA || y < 0 || y >= MIDA) {
            return false;
        }
        if (caselles[x][y].isOcupada()) {
            return false;
        }

        // Si és el primer moviment, ha d'incloure la casella central
        if (primerMoviment) {
            return x == MIDA/2 && y == MIDA/2;
        }

        // Si no és el primer moviment, ha d'estar adjacent a alguna fitxa existent
        return teFitxaAdjacent(x, y);
    }

    private boolean teFitxaAdjacent(int x, int y) {
        // Comprovar les quatre direccions
        if (x > 0 && caselles[x-1][y].isOcupada()) return true;
        if (x < MIDA-1 && caselles[x+1][y].isOcupada()) return true;
        if (y > 0 && caselles[x][y-1].isOcupada()) return true;
        if (y < MIDA-1 && caselles[x][y+1].isOcupada()) return true;

        return false;
    }

    public int calcularPuntuacioMoviment(List<Fitxa> fitxesColocades, List<int[]> posicions) {
        if (fitxesColocades.isEmpty() || posicions.isEmpty()) {
            return 0;
        }

        // Determine if word is horizontal or vertical
        boolean horitzontal = determinarDireccio(posicions);

        // Calculate base score with letter multipliers and collect word multipliers
        int puntuacioBase = 0;
        int multiplicadorParaulaTotal = 1;

        for (int i = 0; i < fitxesColocades.size(); i++) {
            int x = posicions.get(i)[0];
            int y = posicions.get(i)[1];
            Casella casella = caselles[x][y];

            // Apply letter multiplier to this tile's value
            puntuacioBase += fitxesColocades.get(i).getValor() * casella.getMultiplicadorLetra();

            // Collect word multiplier (will be applied later)
            multiplicadorParaulaTotal *= casella.getMultiplicadorParaula();
        }

        // Apply accumulated word multipliers to the total score
        return puntuacioBase * multiplicadorParaulaTotal;
    }

    private boolean determinarDireccio(List<int[]> posicions) {
        if (posicions.size() <= 1) {
            return true; // Default to horizontal for single tile
        }

        // If all positions have the same X coordinate, word is vertical
        // Otherwise, assume horizontal
        int primerX = posicions.get(0)[0];
        for (int i = 1; i < posicions.size(); i++) {
            if (posicions.get(i)[0] != primerX) {
                return true; // Horizontal
            }
        }
        return false; // Vertical
    }

    public void mostrarTaulell() {
        System.out.print("  ");
        for (int i = 0; i < MIDA; i++) {
            System.out.print(" " + i + " ");
        }
        System.out.println();

        for (int i = 0; i < MIDA; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < MIDA; j++) {
                System.out.print(caselles[i][j].toString() + " ");
            }
            System.out.println();
        }
    }

    public Casella getCasella(int x, int y) {
        if (x >= 0 && x < MIDA && y >= 0 && y < MIDA) {
            return caselles[x][y];
        }
        return null;
    }
}
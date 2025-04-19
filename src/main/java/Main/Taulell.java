package Main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        // Determine if word is horizontal or vertical.
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


    public Casella getCasella(int x, int y) {
        if (x >= 0 && x < MIDA && y >= 0 && y < MIDA) {
            return caselles[x][y];
        }
        return null;
    }

    public Fitxa retirarFitxa(int x, int y) {
        // Check if position is within board boundaries
        if (x < 0 || x >= MIDA || y < 0 || y >= MIDA) {
            return null;
        }

        // Get the cell and remove the tile if present
        Casella casella = caselles[x][y];
        return casella.retirarFitxa();
    }

    public List<String> obtenerParaulesAdjacents(String palabra, int fila, int col, String orientacion)
    {
        List<String> paraules = new ArrayList<>();
        int sizePal = palabra.length();
        if (orientacion.equals("V"))
        {
            int fMesUp = fila - 1;
            while (fMesUp >= 0 && caselles[fMesUp][col].isOcupada()) 
            {
                palabra = caselles[fMesUp][col].getFitxa().getLletra() + palabra; //concatenar letras a la palabra
                fMesUp--;
            }

            int fMesDown = fila + sizePal;
            while (fMesDown < 15 && caselles[fMesDown][col].isOcupada()) 
            {
                palabra = palabra + caselles[fMesDown][col].getFitxa().getLletra(); //concatenar letras a la palabra
                fMesDown++;
            }
            paraules.add(palabra);
            
            for (int i = fila; i < fila + sizePal; i++) 
            {
                String aux = String.valueOf(palabra.charAt(i-fila));
                int mostLeft = col - 1;
                while (mostLeft >= 0 && caselles[i][mostLeft].isOcupada()) 
                {
                    aux = caselles[i][mostLeft].getFitxa().getLletra() + aux; //concatenar letras a la palabra
                    mostLeft--;
                }
                int mostRight = col + 1;
                while (mostRight < 15 && caselles[i][mostRight].isOcupada()) 
                {
                    aux = aux + caselles[i][mostRight].getFitxa().getLletra(); //concatenar letras a la palabra
                    mostRight++;
                }

               if (aux.length() > 1) paraules.add(aux);
            }
        }
        else 
        {
            int cMesUp = col - 1;
            while (cMesUp >= 0 && caselles[fila][cMesUp].isOcupada()) 
            {
                palabra = caselles[fila][cMesUp].getFitxa().getLletra() + palabra; //concatenar letras a la palabra
                cMesUp--;
            }

            int cMesDown = col + sizePal;
            while (cMesDown < 15 && caselles[fila][cMesDown].isOcupada()) 
            {
                palabra = palabra + caselles[fila][cMesDown].getFitxa().getLletra(); //concatenar letras a la palabra
                cMesDown++;
            }
            paraules.add(palabra);
            
            for (int i = col; i < col + sizePal; i++) 
            {
                String aux = String.valueOf(palabra.charAt(i-col));
                int mostUp = fila - 1;
                while (mostUp >= 0 && caselles[mostUp][i].isOcupada()) 
                {
                    aux = caselles[mostUp][i].getFitxa().getLletra() + aux; //concatenar letras a la palabra
                    mostUp--;
                }
                int mostDown = fila + 1;
                while (mostDown < 15 && caselles[mostDown][i].isOcupada()) 
                {
                    aux = aux + caselles[mostDown][i].getFitxa().getLletra(); //concatenar letras a la palabra
                    mostDown++;
                }
                if (aux.length() > 1) paraules.add(aux);
            }
        }

        return paraules;
    }
    /*----------------------------------------------------- */

    public boolean isEmpty() {
        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                if (caselles[i][j].isOcupada()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verificarFitxes(LinkedHashMap<int[], Fitxa> jugades, boolean across)
    {
        if (jugades.size() == 1 && isEmpty())
            return false; //no es pot colocar una fitxa nomes si el taulell esta buit
        //iterar por la palabra
        for (var entry : jugades.entrySet()) {
            int[] posicio = entry.getKey();
            if (caselles[posicio[0]][posicio[1]].isOcupada()) 
                return false; // La casella ja està ocupada
        }

        if (jugades.size() != 1) {
            // Comprovar si la jugada és horitzontal o vertical
            if (across) {
                // Comprovar si totes les posicions tenen la mateixa fila
                int fila = jugades.keySet().iterator().next()[0];
                for (int[] pos : jugades.keySet()) {
                    if (pos[0] != fila) {
                        return false; // No és horitzontal
                    }
                }
            } else {
                // Comprovar si totes les posicions tenen la mateixa columna
                int columna = jugades.keySet().iterator().next()[1];
                for (int[] pos : jugades.keySet()) {
                    if (pos[1] != columna) {
                        return false; // No és vertical
                    }
                }
            }
        } 

        return true;
        
    }


    public int validesaYPuntuacioJugada(LinkedHashMap<int[], Fitxa> jugada, Diccionari diccionari, boolean across, boolean colocarFitxes)
    {        
    // Guardar el estado inicial de las posiciones a modificar
        boolean estadoPrimerMoviment = this.primerMoviment;
        List<int[]> posicionesModificadas = new ArrayList<>();
        List<Fitxa> fichasOriginales = new ArrayList<>();
        
        // Registrar estado original de cada posición a modificar
        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            posicionesModificadas.add(posicio);
            // Guardar la ficha original (null si la casilla estaba vacía)
            fichasOriginales.add(caselles[posicio[0]][posicio[1]].isOcupada() ? 
                                caselles[posicio[0]][posicio[1]].getFitxa() : null);
        }
        
        // afegir fitxes al taulell
        boolean[][] fitxesNoves = new boolean[MIDA][MIDA];
        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                fitxesNoves[i][j] = false;
            }
        }

        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            Fitxa fitxa = entry.getValue();
            this.colocarFitxa(posicio[0], posicio[1], fitxa);
            fitxesNoves[posicio[0]][posicio[1]] = true;
        }
        
        int[] pos = jugada.keySet().iterator().next();  
        int puntuacio = -1;
        if (across) 
        {
            puntuacio = getPuntuacioParaulaHorizontal(pos, fitxesNoves, diccionari);
            if (puntuacio == -1) {
                // Deshacer los cambios
                deshacer(posicionesModificadas, fichasOriginales, estadoPrimerMoviment);
                return -1; // La paraula no és vàlida
            }

            // mirar paraules verticals
            int fila = pos[0];
            for (int i = 0; i < MIDA; i++) { 
                if (fitxesNoves[fila][i]) {
                    // Si la casella es nova, mirar les paraules verticals noves posibles
                    int[] posVertical = {fila, i};
                    int puntuacioVertical = getPuntuacioParaulaVertical(posVertical, fitxesNoves, diccionari);
                    if (puntuacioVertical == -1) {
                        // Deshacer los cambios
                        deshacer(posicionesModificadas, fichasOriginales, estadoPrimerMoviment);
                        return -1; // La paraula no és vàlida
                    }
                    puntuacio += puntuacioVertical;
                }
            }
        }
        else 
        {
            puntuacio = getPuntuacioParaulaVertical(pos, fitxesNoves, diccionari);
            if (puntuacio == -1) {
                // Deshacer los cambios
                deshacer(posicionesModificadas, fichasOriginales, estadoPrimerMoviment);
                return -1; // La paraula no és vàlida
            }

            // mirar paraules horitzontals
            int col = pos[1];
            for (int j = 0; j < MIDA; j++) {
                if (fitxesNoves[j][col]) {
                    // Si la casella es nova, mirar les paraules horitzontals
                    int[] posHorizontal = {j, col};
                    int puntuacioHorizontal = getPuntuacioParaulaHorizontal(posHorizontal, fitxesNoves, diccionari);
                    if (puntuacioHorizontal == -1) {
                        // Deshacer los cambios
                        deshacer(posicionesModificadas, fichasOriginales, estadoPrimerMoviment);
                        return -1; // La paraula no és vàlida
                    }
                    puntuacio += puntuacioHorizontal;
                }
            }
        }
    
        // Si estamos solo validando o hubo un problema, deshacer cambios
        if (!colocarFitxes) {
            deshacer(posicionesModificadas, fichasOriginales, estadoPrimerMoviment);
        }
        
        return puntuacio;
    }

    // Método auxiliar para deshacer los cambios
    private void deshacer(List<int[]> posiciones, List<Fitxa> fichasOriginales, boolean estadoPrimerMoviment) {
        // Restaurar cada ficha a su estado original
        for (int i = 0; i < posiciones.size(); i++) {
            int[] pos = posiciones.get(i);
            // Eliminar la ficha colocada
            caselles[pos[0]][pos[1]].retirarFitxa();
            // Si había una ficha original, volver a colocarla
            if (fichasOriginales.get(i) != null) {
                caselles[pos[0]][pos[1]].colocarFitxa(fichasOriginales.get(i));
            }
        }
        // Restaurar el estado de primerMoviment
        this.primerMoviment = estadoPrimerMoviment;
    }

    
    // retorna la fitxa que ja este colocada lo mes a la esquerra posible, si la paraula no existeix retorna -1
    private int getPuntuacioParaulaHorizontal(int[] pos, boolean[][] fitxesNoves, Diccionari diccionari)
    {
        int fila = pos[0];
        int col = pos[1];
        col--;
        while (col >= 0 && caselles[fila][col].isOcupada()) 
            col--;
        // ;No hi ha fitxa a l'esquerra

        col++;
        List<Fitxa> paraula = new ArrayList<>();
        

        // calcular puntuacio
        int puntuacio = 0;
        int multiplicador_paraula = 1;
        while (col < 15 && caselles[fila][col].isOcupada()) 
        {
            paraula.add(caselles[fila][col].getFitxa());
            if (fitxesNoves[fila][col])
            {
                int multiplicador_letra = caselles[fila][col].getMultiplicadorLetra();
                puntuacio += multiplicador_letra*caselles[fila][col].getFitxa().getValor();
                multiplicador_paraula *= caselles[fila][col].getMultiplicadorParaula();
            }
            else 
            {
                puntuacio += caselles[fila][col].getFitxa().getValor();
            }

            col++;
        }

        if (paraula.size() < 2)
            return 0; //nomes hi ha una fitxa colocada
            
        try 
        {
            if (diccionari.esParaula(FitxesToString(paraula))) {
                System.out.println("Paraula vàlida: " + FitxesToString(paraula));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }

        return puntuacio*multiplicador_paraula;
    }

    private int getPuntuacioParaulaVertical(int[] pos, boolean[][] fitxesNoves, Diccionari diccionari)
    {
        int fila = pos[0];
        int col = pos[1];
        fila--;
        while (fila >= 0 && caselles[fila][col].isOcupada()) 
            fila--;
        // ;No hi ha fitxa a l'esquerra

        fila++;
        List<Fitxa> paraula = new ArrayList<>();
        
        int puntuacio = 0;
        int multiplicador_paraula = 1;
        while (fila < 15 && caselles[fila][col].isOcupada()) 
        {
            paraula.add(caselles[fila][col].getFitxa());
            if (fitxesNoves[fila][col])
            {
                int multiplicador_letra = caselles[fila][col].getMultiplicadorLetra();
                puntuacio += multiplicador_letra*caselles[fila][col].getFitxa().getValor();
                multiplicador_paraula *= caselles[fila][col].getMultiplicadorParaula();
            }
            else 
            {
                puntuacio += caselles[fila][col].getFitxa().getValor();
            }
            fila++;
        }

        if (paraula.size() < 2)
            return 0; //nomes hi ha una fitxa colocada

        try 
        {
            if (diccionari.esParaula(FitxesToString(paraula))) {
                System.out.println("Paraula vàlida: " + FitxesToString(paraula));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
        
        return puntuacio*multiplicador_paraula;
    }

    private String FitxesToString(List<Fitxa> fitxes)
    {
        String paraula = "";
        for (int i = 0; i < fitxes.size(); i++) 
        {
            String lletra = fitxes.get(i).getLletra();
            if (i < fitxes.size() - 1) {
                String siguienteLetra = fitxes.get(i + 1).getLletra();
                
                // Comprobar dígrafos en español
                if ((lletra.equals("C") && siguienteLetra.equals("H")) ||
                    (lletra.equals("L") && siguienteLetra.equals("L")) ||
                    (lletra.equals("R") && siguienteLetra.equals("R"))) {
                    throw new IllegalArgumentException("Error: No es pot formar el dígraf '" + 
                        lletra + siguienteLetra + "' amb fitxes separades. Utilitza una fitxa específica de dígraf.");
                }
                
                // Comprobar dígrafos en catalán
                if ((lletra.equals("N") && siguienteLetra.equals("Y")) ||
                    (lletra.equals("L") && siguienteLetra.equals("·L"))) {
                    throw new IllegalArgumentException("Error: No es pot formar el dígraf '" + 
                        lletra + siguienteLetra + "' amb fitxes separades. Utilitza una fitxa específica de dígraf.");
                }
            }

            paraula += lletra;
        }
        return paraula;
    }

}



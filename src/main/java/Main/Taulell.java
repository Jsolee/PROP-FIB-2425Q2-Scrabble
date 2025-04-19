package Main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


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

    public boolean esPrimerMoviment() {
        return primerMoviment;
    }

    public Casella[][] getCaselles() {
        return caselles;
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

    public boolean teFitxaAdjacent(int x, int y) {
        // Comprovar les quatre direccions
        if (x > 0 && caselles[x-1][y].isOcupada()) return true;
        if (x < MIDA-1 && caselles[x+1][y].isOcupada()) return true;
        if (y > 0 && caselles[x][y-1].isOcupada()) return true;
        if (y < MIDA-1 && caselles[x][y+1].isOcupada()) return true;

        return false;
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
        
        //Guardar l'estat inicial
        Casella[][] backup = this.caselles;

        
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
                this.caselles = backup;
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
                        this.caselles = backup;
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
                this.caselles = backup;
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
                        this.caselles = backup;
                        return -1; // La paraula no és vàlida
                    }
                    puntuacio += puntuacioHorizontal;
                }
            }
        }
    
        // Si nomes es volia validar i no colocar, restaurem
        if (!colocarFitxes) {
            this.caselles = backup;
        } else {
            // Si es vol colocar, el primer moviment ja s'ha fet
            if (this.primerMoviment) {
                this.primerMoviment = false;
            }
        }
        return puntuacio;
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

        if (paraula.size() <= 1)
            return 0; //nomes hi ha una fitxa colocada, per tant no es forma paraula
            
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



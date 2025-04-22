package Domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa el taulell del joc de Scrabble.
 * Gestiona la col·locació de fitxes, la validació de jugades i el càlcul de puntuacions.
 */
public class Taulell {
    /** Matriu de caselles que formen el taulell */
    private Casella[][] caselles;
    /** Mida del taulell (15x15 caselles) */
    private static final int MIDA = 15;
    /** Indica si encara no s'ha fet cap jugada */
    private boolean primerMoviment = true;

    /**
     * Constructor per defecte.
     * Inicialitza un taulell estàndard de 15x15 caselles amb els multiplicadors corresponents.
     */
    public Taulell() {
        caselles = new Casella[MIDA][MIDA];
        inicialitzarTaulell();
    }

    /**
     * Inicialitza totes les caselles del taulell amb els seus multiplicadors corresponents.
     * Estableix la casella central com a casella inicial.
     */
    private void inicialitzarTaulell() {
        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                caselles[i][j] = new Casella(i, j, getMultiplicadorLletra(i, j), getMultiplicadorParaula(i, j));
            }
        }
        caselles[MIDA / 2][MIDA / 2].setEsCasellaInicial(true);
    }

    /**
     * Determina el multiplicador de lletra per a una posició específica del taulell.
     * 
     * @param x coordenada de fila
     * @param y coordenada de columna
     * @return multiplicador de lletra (1 per normal, 2 per DL, 3 per TL)
     */
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

    /**
     * Determina el multiplicador de paraula per a una posició específica del taulell.
     * 
     * @param x coordenada de fila
     * @param y coordenada de columna
     * @return multiplicador de paraula (1 per normal, 2 per DP, 3 per TP)
     */
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
        return 1;
    }

    /**
     * Verifica si és el primer moviment de la partida.
     * 
     * @return true si encara no s'ha col·locat cap fitxa, false si ja s'ha fet el primer moviment
     */
    public boolean esPrimerMoviment() {
        return primerMoviment;
    }

    /**
     * Obté la matriu de caselles que formen el taulell.
     * 
     * @return matriu bidimensional de Casella que representa el taulell complet
     */
    public Casella[][] getCaselles() {
        return caselles;
    }

    /**
     * Col·loca una fitxa al taulell.
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @param fitxa Fitxa a col·locar
     * @return true si la fitxa s'ha col·locat correctament, false en cas contrari
     */
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

    /**
     * Comprova si el moviment és vàlid.
     * Verifica que la posició estigui dins del taulell, la casella no estigui ocupada,
     * i que compleixi les regles de col·locació (primera jugada al centre, resta adjacents a fitxes existents).
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @param fitxa Fitxa a col·locar
     * @return true si el moviment és vàlid, false en cas contrari
     */
    private boolean esMovimentValid(int x, int y, Fitxa fitxa) {
        if (x < 0 || x >= MIDA || y < 0 || y >= MIDA) {
            return false;
        }
        if (caselles[x][y].isOcupada()) {
            return false;
        }

        if (primerMoviment) {
            return x == MIDA/2 && y == MIDA/2;
        }

        return teFitxaAdjacent(x, y);
    }

    /**
     * Comprova si hi ha fitxes adjacents a la posició especificada.
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @return true si hi ha fitxes adjacents, false en cas contrari
     */
    public boolean teFitxaAdjacent(int x, int y) {
        if (x > 0 && caselles[x-1][y].isOcupada()) return true;
        if (x < MIDA-1 && caselles[x+1][y].isOcupada()) return true;
        if (y > 0 && caselles[x][y-1].isOcupada()) return true;
        if (y < MIDA-1 && caselles[x][y+1].isOcupada()) return true;

        return false;
    }

    /**
     * Verifica si hi ha fitxes adjacents en direcció vertical (superior o inferior).
     * Utilitzat per comprovar regles de col·locació de fitxes verticals.
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @return true si hi ha alguna fitxa superior o inferior, false en cas contrari
     */
    public boolean teFitxaSuperiorOInferior(int x, int y) {
        if (y > 0 && caselles[x][y-1].isOcupada()) return true;
        if (y < MIDA-1 && caselles[x][y+1].isOcupada()) return true;

        return false;
    }

    /**
     * Obté la casella en la posició especificada.
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @return casella en la posició indicada, o null si la posició està fora del taulell
     */
    public Casella getCasella(int x, int y) {
        if (x >= 0 && x < MIDA && y >= 0 && y < MIDA) {
            return caselles[x][y];
        }
        return null;
    }

    /**
     * Retira una fitxa de la posició especificada.
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @return la fitxa retirada, o null si no hi havia cap fitxa o la posició és invàlida
     */
    public Fitxa retirarFitxa(int x, int y) {
        if (x < 0 || x >= MIDA || y < 0 || y >= MIDA) {
            return null;
        }

        Casella casella = caselles[x][y];
        return casella.retirarFitxa();
    }

    /**
     * Estableix una nova matriu de caselles pel taulell.
     * Utilitzat principalment per a proves o per carregar una partida guardada.
     * 
     * @param caselles nova matriu de caselles
     */
    public void setCaselles(Casella[][] caselles) {
        this.caselles = caselles;
    }

    /**
     * Obté les paraules adjacents formades al col·locar una paraula en una posició específica.
     * Busca extensions de la paraula principal i paraules creues formades per les noves fitxes.
     * 
     * @param palabra la paraula que s'està col·locant
     * @param fila fila inicial on es col·loca la paraula
     * @param col columna inicial on es col·loca la paraula
     * @param orientacion "V" per vertical, "H" per horitzontal
     * @return llista de paraules formades incloent la paraula principal i les paraules creuades
     */
    public List<String> obtenerParaulesAdjacents(String palabra, int fila, int col, String orientacion) {
        List<String> paraules = new ArrayList<>();
        int sizePal = palabra.length();
        if (orientacion.equals("V")) {
            int fMesUp = fila - 1;
            while (fMesUp >= 0 && caselles[fMesUp][col].isOcupada()) {
                palabra = caselles[fMesUp][col].getFitxa().getLletra() + palabra;
                fMesUp--;
            }

            int fMesDown = fila + sizePal;
            while (fMesDown < 15 && caselles[fMesDown][col].isOcupada()) {
                palabra = palabra + caselles[fMesDown][col].getFitxa().getLletra();
                fMesDown++;
            }
            paraules.add(palabra);

            for (int i = fila; i < fila + sizePal; i++) {
                String aux = String.valueOf(palabra.charAt(i-fila));
                int mostLeft = col - 1;
                while (mostLeft >= 0 && caselles[i][mostLeft].isOcupada()) {
                    aux = caselles[i][mostLeft].getFitxa().getLletra() + aux;
                    mostLeft--;
                }
                int mostRight = col + 1;
                while (mostRight < 15 && caselles[i][mostRight].isOcupada()) {
                    aux = aux + caselles[i][mostRight].getFitxa().getLletra();
                    mostRight++;
                }

                if (aux.length() > 1) paraules.add(aux);
            }
        } else {
            int cMesUp = col - 1;
            while (cMesUp >= 0 && caselles[fila][cMesUp].isOcupada()) {
                palabra = caselles[fila][cMesUp].getFitxa().getLletra() + palabra;
                cMesUp--;
            }

            int cMesDown = col + sizePal;
            while (cMesDown < 15 && caselles[fila][cMesDown].isOcupada()) {
                palabra = palabra + caselles[fila][cMesDown].getFitxa().getLletra();
                cMesDown++;
            }
            paraules.add(palabra);

            for (int i = col; i < col + sizePal; i++) {
                String aux = String.valueOf(palabra.charAt(i-col));
                int mostUp = fila - 1;
                while (mostUp >= 0 && caselles[mostUp][i].isOcupada()) {
                    aux = caselles[mostUp][i].getFitxa().getLletra() + aux;
                    mostUp--;
                }
                int mostDown = fila + 1;
                while (mostDown < 15 && caselles[mostDown][i].isOcupada()) {
                    aux = aux + caselles[mostDown][i].getFitxa().getLletra();
                    mostDown++;
                }
                if (aux.length() > 1) paraules.add(aux);
            }
        }

        return paraules;
    }

    /**
     * Verifica si el taulell està completament buit (sense fitxes).
     * 
     * @return true si no hi ha cap fitxa al taulell, false si n'hi ha alguna
     */
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

    /**
     * Verifica si les fitxes que es volen col·locar en el taulell són vàlides.
     * Comprova que les posicions no estiguin ocupades i que les fitxes estiguin
     * alineades horitzontalment o verticalment segons l'orientació indicada.
     * 
     * @param jugades llista de jugades a jugar. Cada jugada és un objecte Fitxa amb la seva posició al taulell
     * @param across valor booleà que indica si la jugada és horitzontal (true) o vertical (false)
     * @return true si les fitxes es poden col·locar al taulell, false en cas contrari
     */
    public boolean verificarFitxes(LinkedHashMap<int[], Fitxa> jugades, boolean across) {
        if (jugades.size() == 1 && isEmpty())
            return false; //no es pot col·locar una fitxa nomes si el taulell esta buit
        
        for (var entry : jugades.entrySet()) {
            int[] posicio = entry.getKey();
            if (caselles[posicio[0]][posicio[1]].isOcupada()) 
                return false; // La casella ja està ocupada
        }

        if (jugades.size() != 1) {
            if (across) {
                int fila = jugades.keySet().iterator().next()[0];
                for (int[] pos : jugades.keySet()) {
                    if (pos[0] != fila) {
                        return false; // No és horitzontal
                    }
                }
            } else {
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

    /**
     * Verifica si la jugada és vàlida i calcula la puntuació.
     * Comprova que les paraules formades existeixin al diccionari i aplica els multiplicadors corresponents.
     * 
     * @param jugada llista de jugades a jugar. Cada jugada és una fitxa amb la seva posició
     * @param diccionari diccionari que conté les paraules vàlides
     * @param across booleà que indica si la jugada és horitzontal (true) o vertical (false)
     * @param colocarFitxes booleà que indica si es volen col·locar les fitxes al taulell o només validar
     * @return la puntuació de la jugada, o -1 si la jugada no és vàlida
     */
    public int validesaYPuntuacioJugada(LinkedHashMap<int[], Fitxa> jugada, Diccionari diccionari, boolean across, boolean colocarFitxes) {
        if (jugada.isEmpty()) {
            return -1;
        }
        
        Map<int[], Fitxa> fitxesAnteriors = new HashMap<>();
        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            if (caselles[posicio[0]][posicio[1]].isOcupada()) {
                fitxesAnteriors.put(posicio, caselles[posicio[0]][posicio[1]].getFitxa());
            } else {
                fitxesAnteriors.put(posicio, null);
            }
        }

        boolean[][] fitxesNoves = new boolean[MIDA][MIDA];
        for (int i = 0; i < MIDA; i++) {
            for (int j = 0; j < MIDA; j++) {
                fitxesNoves[i][j] = false;
            }
        }

        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            Fitxa fitxa = entry.getValue();
            colocarFitxa(posicio[0], posicio[1], fitxa);
            fitxesNoves[posicio[0]][posicio[1]] = true;
        }
        
        int[] pos = jugada.keySet().iterator().next();  
        
        int puntuacio = -1;
        if (across) {
            puntuacio = getPuntuacioParaulaHorizontal(pos, fitxesNoves, diccionari);
            if (puntuacio == -1) {
                restaurarTaulell(fitxesAnteriors, jugada);
                return -1;
            }

            int fila = pos[0];
            for (int i = 0; i < MIDA; i++) { 
                if (fitxesNoves[fila][i]) {
                    int[] posVertical = {fila, i};
                    int puntuacioVertical = getPuntuacioParaulaVertical(posVertical, fitxesNoves, diccionari);
                    if (puntuacioVertical == -1) {
                        restaurarTaulell(fitxesAnteriors, jugada);
                        return -1;
                    }
                    puntuacio += puntuacioVertical;
                }
            }
        } else {
            puntuacio = getPuntuacioParaulaVertical(pos, fitxesNoves, diccionari);
            
            if (puntuacio == -1) {
                restaurarTaulell(fitxesAnteriors, jugada);
                return -1;
            }

            int col = pos[1];
            for (int j = 0; j < MIDA; j++) {
                if (fitxesNoves[j][col]) {
                    int[] posHorizontal = {j, col};
                    int puntuacioHorizontal = getPuntuacioParaulaHorizontal(posHorizontal, fitxesNoves, diccionari);
                    if (puntuacioHorizontal == -1) {
                        restaurarTaulell(fitxesAnteriors, jugada);
                        return -1;
                    }
                    puntuacio += puntuacioHorizontal;
                }
            }
        }
    
        if (!colocarFitxes) {
            restaurarTaulell(fitxesAnteriors, jugada);
        } else {
            if (this.primerMoviment) {
                this.primerMoviment = false;
            }
        }
        return puntuacio;
        
    }

    /**
     * Restaura el taulell a l'estat anterior a la jugada.
     * 
     * @param fitxesARetirar mapa amb les fitxes originals que hi havia abans de la jugada
     * @param jugada mapa amb les fitxes que es van col·locar i que s'han de retirar
     */
    private void restaurarTaulell(Map<int[], Fitxa> fitxesARetirar, LinkedHashMap<int[], Fitxa> jugada) {
        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            caselles[posicio[0]][posicio[1]].retirarFitxa();
            
            Fitxa fitxaAnterior = fitxesARetirar.get(posicio);
            if (fitxaAnterior != null) {
                caselles[posicio[0]][posicio[1]].colocarFitxa(fitxaAnterior);
            }
        }
    }
    
    /**
     * Calcula la puntuació d'una paraula horitzontal formada a partir de la posició indicada.
     * Verifica que la paraula sigui vàlida segons el diccionari i aplica els multiplicadors.
     * 
     * @param pos posició inicial de la paraula
     * @param fitxesNoves matriu que indica quines fitxes són noves
     * @param diccionari diccionari per validar la paraula
     * @return puntuació de la paraula, o -1 si no és vàlida
     */
    private int getPuntuacioParaulaHorizontal(int[] pos, boolean[][] fitxesNoves, Diccionari diccionari) {
        int fila = pos[0];
        int col = pos[1];
        col--;
        while (col >= 0 && caselles[fila][col].isOcupada()) 
            col--;

        col++;
        List<Fitxa> paraula = new ArrayList<>();
        
        int puntuacio = 0;
        int multiplicador_paraula = 1;
        while (col < 15 && caselles[fila][col].isOcupada()) {
            paraula.add(caselles[fila][col].getFitxa());
            if (fitxesNoves[fila][col]) {
                int multiplicador_letra = caselles[fila][col].getMultiplicadorLetra();
                puntuacio += multiplicador_letra * caselles[fila][col].getFitxa().getValor();
                multiplicador_paraula *= caselles[fila][col].getMultiplicadorParaula();
            } else {
                puntuacio += caselles[fila][col].getFitxa().getValor();
            }

            col++;
        }

        if (paraula.size() < 2)
            return 0;

        try {
            boolean b = diccionari.esParaula(FitxesToString(paraula));
            if (b) 
                return puntuacio * multiplicador_paraula;
        } catch (IllegalArgumentException e) {
            return -1;
        }
        return -1;
    }

    /**
     * Calcula la puntuació d'una paraula vertical formada a partir de la posició indicada.
     * Verifica que la paraula sigui vàlida segons el diccionari i aplica els multiplicadors.
     * 
     * @param pos posició inicial de la paraula
     * @param fitxesNoves matriu que indica quines fitxes són noves
     * @param diccionari diccionari per validar la paraula
     * @return puntuació de la paraula, o -1 si no és vàlida
     */
    private int getPuntuacioParaulaVertical(int[] pos, boolean[][] fitxesNoves, Diccionari diccionari) {
        int fila = pos[0];
        int col = pos[1];
        fila--;
        while (fila >= 0 && caselles[fila][col].isOcupada()) 
            fila--;

        fila++;
        List<Fitxa> paraula = new ArrayList<>();
        int puntuacio = 0;
        int multiplicador_paraula = 1;
        while (fila < 15 && caselles[fila][col].isOcupada()) {
            paraula.add(caselles[fila][col].getFitxa());
            if (fitxesNoves[fila][col]) {
                int multiplicador_letra = caselles[fila][col].getMultiplicadorLetra();
                puntuacio += multiplicador_letra * caselles[fila][col].getFitxa().getValor();
                multiplicador_paraula *= caselles[fila][col].getMultiplicadorParaula();
            } else {
                puntuacio += caselles[fila][col].getFitxa().getValor();
            }
            fila++;
        }
    
        if (paraula.size() < 2)
            return 0;

        try {
            boolean b = diccionari.esParaula(FitxesToString(paraula));
            if (b) 
                return puntuacio * multiplicador_paraula;
        } catch (IllegalArgumentException e) {
            return -1;
        }
        return -1;
    
    }

    /**
     * Converteix una llista de fitxes en una cadena de text.
     * Verifica que no s'intentin formar dígrafs amb fitxes separades.
     * 
     * @param fitxes llista de fitxes a convertir
     * @return cadena de text formada per les lletres de les fitxes
     * @throws IllegalArgumentException si s'intenta formar un dígraf amb fitxes separades
     */
    private String FitxesToString(List<Fitxa> fitxes) {
        String paraula = "";
        for (int i = 0; i < fitxes.size(); i++) {
            String lletra = fitxes.get(i).getLletra();
            if (i < fitxes.size() - 1) {
                String siguienteLetra = fitxes.get(i + 1).getLletra();
                
                // Comprovar dígrafs en castellà
                if ((lletra.equals("C") && siguienteLetra.equals("H")) ||
                    (lletra.equals("L") && siguienteLetra.equals("L")) ||
                    (lletra.equals("R") && siguienteLetra.equals("R"))) {
                    throw new IllegalArgumentException("Error: No es pot formar el dígraf '" + 
                        lletra + siguienteLetra + "' amb fitxes separades. Utilitza una fitxa específica de dígraf.");
                }
                
                // Comprovar dígrafs en català
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
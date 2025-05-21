package Domini;

import java.util.*;

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

    public void setPrimerMoviment(boolean primerMoviment) {
        this.primerMoviment = primerMoviment;
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
     * Utilitzat per comprovar regles de col·locació de fitxes verticals (cross-checks).
     * 
     * @param x fila del taulell
     * @param y columna del taulell
     * @return true si hi ha alguna fitxa superior o inferior, false en cas contrari
     */
    public boolean teFitxaSuperiorOInferior(int x, int y) {
        if (x-1 >= 0 && caselles[x-1][y].isOcupada()) return true;
        if (x+1 < MIDA && caselles[x+1][y].isOcupada()) return true;

        return false;
    }

    /**
     * Verifica si hi ha fitxes adjacents en direcció horitzontal (esquerra o dreta).
     * Utilitzat per comprovar regles de col·locació de fitxes horitzontals (cross-checks).
     *
     * @param x fila del taulell
     * @param y columna del taulell
     * @return true si hi ha alguna fitxa superior o inferior, false en cas contrari
     */
    public boolean teFitxaEsquerraODreta(int x, int y) {
        if (y-1 >= 0 && caselles[x][y-1].isOcupada()) return true;
        if (y+1 < MIDA && caselles[x][y+1].isOcupada()) return true;

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
        boolean vacio = isEmpty();
        if (jugades.size() == 1 && vacio)
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

        if (!vacio)
        {
            for (var entry : jugades.entrySet()) {
                int[] posicio = entry.getKey();
                if (teFitxaAdjacent(posicio[0], posicio[1])) {
                    return true;
                }
            }
            return false;
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

        /*System.out.println("DEBUG: jugada:");

        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            System.out.println("posicio " + posicio[0] + " , " + posicio[1]);
            System.out.println("fitxa " + entry.getValue().getLletra());
        }*/

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
            caselles[posicio[0]][posicio[1]].colocarFitxa(fitxa);
            fitxesNoves[posicio[0]][posicio[1]] = true;
        }
        
        int[] pos = jugada.keySet().iterator().next();
        
        int puntuacio = -1;
        if (across) {
            puntuacio = getPuntuacioParaulaHorizontal(pos, fitxesNoves, diccionari);
            if (puntuacio == -1) {
                restaurarTaulell(jugada);
                return -1;
            }

            int fila = pos[0];
            for (int i = 0; i < MIDA; i++) { 
                if (fitxesNoves[fila][i]) {
                    int[] posVertical = {fila, i};
                    int puntuacioVertical = getPuntuacioParaulaVertical(posVertical, fitxesNoves, diccionari);
                    if (puntuacioVertical == -1) {
                        restaurarTaulell(jugada);
                        return -1;
                    }
                    puntuacio += puntuacioVertical;
                }
            }
        } else {
            puntuacio = getPuntuacioParaulaVertical(pos, fitxesNoves, diccionari);
            if (puntuacio == -1) {
                restaurarTaulell(jugada);
                return -1;
            }

            int col = pos[1];
            for (int j = 0; j < MIDA; j++) {
                if (fitxesNoves[j][col]) {
                    int[] posHorizontal = {j, col};
                    int puntuacioHorizontal = getPuntuacioParaulaHorizontal(posHorizontal, fitxesNoves, diccionari);
                    if (puntuacioHorizontal == -1) {
                        restaurarTaulell(jugada);
                        return -1;
                    }
                    puntuacio += puntuacioHorizontal;
                }
            }
        }
    
        if (!colocarFitxes) {
            restaurarTaulell(jugada);
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
     * @param jugada mapa amb les fitxes que es van col·locar i que s'han de retirar
     */
    private void restaurarTaulell(LinkedHashMap<int[], Fitxa> jugada) {
        for (var entry : jugada.entrySet()) {
            int[] posicio = entry.getKey();
            if (caselles[posicio[0]][posicio[1]].isOcupada()) {
                caselles[posicio[0]][posicio[1]].retirarFitxa();
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
     * Calcula les caselles anchor i els cross-checks.
     * @param diccionari DAWG
     * @param alfabet conjunt de caràcters
     */
    public void calcularAnchorsICrossChecks(Diccionari diccionari, Set<String> alfabet) {
        // si es el primer moviment, només la casella inicial és anchor, y no hi han fitxes adjacents ni cross-checks.
        if (esPrimerMoviment()) {
            for (Casella[] c : caselles) {
                for (Casella casella : c) {
                    casella.setAnchor(casella.isEsCasellaInicial());
                    // si la casella es la inicial, no hi ha cross-checks
                }
            }
        } else {
            // si no es el primer moviment, busquem les anchors i cross-checks
            for (Casella[] c : caselles) {
                for (Casella casella : c) {
                    // si la casella esta buida y te alguna fitxa adjacent, es una candidata a anchor.
                    if (!casella.isOcupada() && teFitxaAdjacent(casella.getX(), casella.getY())) {
                        casella.setAnchor(true);

                        // si la casella te fitxa superior o inferior, al colocar una fitxa, es pot formar una paraula vertical, per tant necesitem cross-checks across
                        if (teFitxaSuperiorOInferior(casella.getX(), casella.getY())) {
                            casella.setNecessitaCrossCheckAcross(true);
                            casella.setCrossChecksAcross(calcularCrossChecks(casella.getX(), casella.getY(), diccionari, alfabet, true));
                        } else {
                            casella.setNecessitaCrossCheckAcross(false);
                        }

                        // si la casella te fitxa esquerra o dreta, al colocar una fitxa, es pot formar una paraula horitzontal, per tant necesitem cross-checks down
                        if (teFitxaEsquerraODreta(casella.getX(), casella.getY())) {
                            casella.setNecessitaCrossCheckDown(true);
                            casella.setCrossChecksDown(calcularCrossChecks(casella.getX(), casella.getY(), diccionari, alfabet, false));
                        } else {
                            casella.setNecessitaCrossCheckDown(false);
                        }
                    } else {
                        casella.setAnchor(false);
                        casella.setNecessitaCrossCheckAcross(false);
                        casella.setNecessitaCrossCheckDown(false);
                    }
                }
            }
        }
    }

    /**
     * Retorna llistat de lletres vàlides per a cross-checks d'una casella.
     * @param x posició fila
     * @param y posició columna
     * @param diccionari DAWG
     * @param alfabet conjunt de caràcters
     * @param across true si es volen cross-checks horitzontals, false si es volen verticals
     * @return llista de caràcters vàlids
     */
    private  ArrayList<String> calcularCrossChecks(int x, int y, Diccionari diccionari, Set<String> alfabet, boolean across) {
        String superiorEsquerrra;
        String inferiorDreta;
        if (across) {
            superiorEsquerrra = getParaulaSuperior(x, y);
            inferiorDreta = getParaulaInferior(x, y);
        } else {
            superiorEsquerrra = getParaulaEsquerra(x, y);
            inferiorDreta = getParaulaDreta(x, y);
        }

        ArrayList<String> cross_checks = new ArrayList<>();
        for (String lletra : alfabet) {
            if (!lletra.equals("#")) {
                String paraula = superiorEsquerrra + lletra + inferiorDreta;
                if (diccionari.esParaula(paraula)) {
                    cross_checks.add(lletra);
                }
            }
        }
        return cross_checks;
    }

    /**
     * Obté la part superior de la paraula vertical a partir de la casella en la posició (x, y).
     * @param x fila
     * @param y columna
     * @return cadena superior
     */
    public String getParaulaSuperior(int x, int y) {
        StringBuilder superior = new StringBuilder();
        while (x-1 >= 0 && caselles[x-1][y].isOcupada()) {
            superior.insert(0, caselles[x - 1][y].getFitxa().getLletra());
            x--;
        }
        return superior.toString();
    }

    /**
     * Obté la part inferior de la paraula vertical a partir de la casella en la posició (x, y)..
     * @param x fila
     * @param y columna
     * @return cadena inferior
     */
    private String getParaulaInferior(int x, int y) {
        StringBuilder inferior = new StringBuilder();
        while (x+1 < MIDA && caselles[x + 1][y].isOcupada()) {
            inferior.append(caselles[x + 1][y].getFitxa().getLletra());
            x++;
        }
        return inferior.toString();
    }

    /**
     * Obté la part esquerra de la paraula horitzontal a partir de la casella en la posició (x, y).
     * @param x fila
     * @param y columna
     * @return prefix de cadena
     */
    public String getParaulaEsquerra(int x, int y) {
        StringBuilder esquerra = new StringBuilder();
        while (y-1 >= 0 && caselles[x][y-1].isOcupada()) {
            esquerra.insert(0, caselles[x][y-1].getFitxa().getLletra());
            y--;
        }
        return esquerra.toString();
    }

    /**
     * Obté la part dreta de la paraula horitzontal a partir de la casella en la posició (x, y).
     * @param x fila
     * @param y columna
     * @return suffix de cadena
     */
    private String getParaulaDreta(int x, int y) {
        StringBuilder dreta = new StringBuilder();
        while (y + 1 < MIDA && caselles[x][y + 1].isOcupada()) {
            dreta.append(caselles[x][y + 1].getFitxa().getLletra());
            y++;
        }
        return dreta.toString();
    }

    /**
     * Comptabilitza posicions lliures sense anchors anterior a la casella en la posició x y.
     * @param x fila
     * @param y fila
     * @return nombre de posicions
     */
    public int getPosicionsSenseAnchors(int x, int y, boolean across) {
        int limit = 0;
        Casella anterior;
        if (across) anterior = getCasella(x, y-1);
        else anterior = getCasella(x-1, y);

        while (anterior != null && !anterior.isAnchor() && !anterior.isOcupada()) {
            limit++;

            if (across) anterior = getCasella(anterior.getX(), anterior.getY() - 1);
            else anterior = getCasella(anterior.getX() - 1, anterior.getY());
        }
        return limit;
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
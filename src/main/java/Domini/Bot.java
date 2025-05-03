package Domini;

import java.util.*;

/**
 * Singleton que representa el bot jugador de Scrabble.
 * Construeix la millor jugada possible en horitzontal i vertical
 * segons l'estat del taulell, el diccionari i les fitxes de l'atril.
 */
public class Bot extends Usuari{
    // Variable que conté l'única instancia del bot.
    /** Instància única del bot */
    private static Bot instancia;

    /** Jugada horitzontal amb millor puntuació */
    private Map.Entry<LinkedHashMap<int[], Fitxa>, Integer> millorJugadaAcross; // jugades possibles ordenades per puntuació, si hi han dues jugades amb la mateixa puntuació, només es guardara 1
    /** Jugada vertical amb millor puntuació */
    private Map.Entry<LinkedHashMap<int[], Fitxa>, Integer> millorJugadaDown; // jugades possibles ordenades per puntuació, si hi han dues jugades amb la mateixa puntuació, només es guardara 1

    /**
     * Constructor privat per garantir singleton.
     */
    private Bot() {
        super("bot");
    }

    /**
     * Retorna la instància única del bot, creant-la si cal.
     * @return instància de Bot
     */
    public static Bot getInstance() {
        if (instancia == null) {
            instancia = new Bot();
        }
        return instancia;
    }

    /**
     * Busca la millor jugada possible al taulell. La jugada es retorna com un mapa.
     *
     * @param taulell   estat actual del taulell
     * @param diccionari estructura DAWG amb paraules vàlides
     * @param atril     llista de fitxes disponibles del bot
     * @param alfabet   conjunt de caràcters vàlids (inclou # per blanks)
     * @return parella (jugada, orientació), on jugada és un mapa de posició->fitxa
     *         i orientació {@code true}=horitzontal, {@code false}=vertical
     */    
    public Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> getMillorJugada(Taulell taulell, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet) {


        this.millorJugada = new AbstractMap.SimpleEntry<>(new LinkedHashMap<>(), 0);

        // calcular anchors y cross-checks de taulell
        taulell.calcularAnchorsICrossChecks(diccionari, alfabet);

        getMillorJugadaAux(taulell, info, diccionari, atril, alfabet, true);

//
        // Taulell transposat per a la jugada vertical
        Taulell taulellTransposat = transposarTaulell(taulell);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                info[i][j] = new infoCasella();
            }
        }

        this.millorJugadaDown = new AbstractMap.SimpleEntry<>(new LinkedHashMap<>(), 0);

        calcularAnchorsICrossChecks(taulellTransposat, info , diccionari, alfabet);
        getMillorJugadaAux(taulellTransposat, info, diccionari, atril, alfabet, false);

        // comparem les dues jugades i retornem la millor
        if (millorJugadaAcross.getValue() >= millorJugadaDown.getValue()) {
            return new AbstractMap.SimpleEntry<>(millorJugadaAcross.getKey(), true);
        } else {
            return new AbstractMap.SimpleEntry<>(millorJugadaDown.getKey(), false);
        }
    }

    /**
     * Auxiliar recursiu que explora totes les jugades des d'anchors.
     * @param taulell   taulell (posicions i fitxes)
     * @param info      matriu amb anchors i cross-checks
     * @param diccionari DAWG de paraules
     * @param atril     fitxes disponibles
     * @param alfabet   caràcters vàlids
     * @param across    {@code true}=horitzontal, {@code false}=vertical
     */
    private void getMillorJugadaAux(Taulell taulell, infoCasella[][] info, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {

        for (Casella[] c : taulell.getCaselles()) {
            // per cada fila
            for (Casella casella : c) {
                int x = casella.getX();
                int y = casella.getY();
                if (info[x][y].isAnchor()) {
                    Casella anterior = taulell.getCasella(casella.getX(), casella.getY()-1);
                    if (anterior != null && anterior.isOcupada()) {
                        // si la casella anterior està ocupada, tenim un prefix definit per caselles anteriors (suposem que la paraula anterior es correcta)
                        LinkedHashMap<int[], Fitxa> prefix = new LinkedHashMap<>();
                        String paraula = getParulaAnterior(taulell, anterior);
                        DAWGnode nodeActual = diccionari.getNode(paraula);
                        extendreDreta(prefix, taulell, info, casella, nodeActual, diccionari, atril, alfabet, across);
                        continue;
                    }
                    else {
                        int limit = getPosicionsSenseAnchors(taulell, info, anterior);
                        ArrayList<Fitxa> prefix = new ArrayList<>();

                        DAWGnode node = diccionari.getArrel();
                        extendreEsquerra(prefix, taulell, info, node, casella, diccionari, atril, alfabet, limit, across);
                    }

                }

            }
        }
    }

    /**
     * Extén el prefix cap a l'esquerra per generar possibles paraules Per cada prefix trobat expandeix per la dreta.
     * @param prefix       llista de fitxes col·locades prèviament
     * @param taulell      taulell
     * @param info         matriu d'infoCasella
     * @param node         node actual del DAWG
     * @param casellaAnchor casella d'anchor
     * @param diccionari   DAWG
     * @param atril        fitxes disponibles
     * @param alfabet      conjunt de caràcters
     * @param limit        nombre màxim de fitxes a l'esquerra
     * @param across       orientació
     */
    private void extendreEsquerra(ArrayList<Fitxa> prefix, Taulell taulell, DAWGnode node, Casella casellaAnchor, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, int limit, boolean across) {


        ArrayList<Fitxa> atrilCopy = new ArrayList<>(atril);
        LinkedHashMap<int[], Fitxa> prefixCopy = ArrayToJugada(prefix, casellaAnchor, prefix.size());
        extendreDreta(prefixCopy, taulell, info, casellaAnchor, node, diccionari, atrilCopy, alfabet, across);

        if (limit == 0) {
            return;
        }

        for (int i = 0; i < atril.size(); i++) {
            String lletra = atril.get(i).getLletra();

            if (lletra.equals("#")) {
                for (String lletra2 : alfabet) {
                    if (lletra2.equals("#")) continue;
                    Fitxa blank = new Fitxa(lletra2, 0);
                    DAWGnode nouNode = getNode(blank.getLletra(), node);

                    if (nouNode != null) {
                        ArrayList<Fitxa> nouPrefix  = new ArrayList<>(prefix);
                        nouPrefix.add(blank);
                        ArrayList<Fitxa> atrilActual = new ArrayList<>(atril); // con Fitxa en vez de Main.Fitxa me daba error :(
                        atrilActual.remove(i);// eliminem la fitxa de l'atril que hem utilitzat
                        // crea nous prefixos a partir d'aquest nou prefix
                        extendreEsquerra(nouPrefix, taulell, info, nouNode, casellaAnchor, diccionari, atrilActual, alfabet, limit-1, across);
                    }
                }
            }
            else {
                DAWGnode nouNode = getNode(lletra, node);

                if (nouNode != null) {
                    ArrayList<Fitxa> nouPrefix  = new ArrayList<>(prefix);
                    nouPrefix.add(atril.get(i));
                    ArrayList<Fitxa> atrilActual = new ArrayList<>(atril); // con Fitxa en vez de Main.Fitxa me daba error :(
                    atrilActual.remove(i);// eliminem la fitxa de l'atril que hem utilitzat
                    // crea nous prefixos a partir d'aquest nou prefix
                    extendreEsquerra(nouPrefix, taulell, info, nouNode, casellaAnchor, diccionari, atrilActual, alfabet, limit-1, across);
                }
            }
        }
    }

    /**
     * Extén el prefix cap a la dreta construint paraules complertes.
     * @param prefix   mapa posició->fitxa
     * @param taulell  taulell
     * @param info     matriu d'infoCasella
     * @param casella  casella actual
     * @param node     node del DAWG
     * @param diccionari DAWG
     * @param atril    fitxes restants
     * @param alfabet  caràcters vàlids
     * @param across   orientació
     */
    private void extendreDreta(LinkedHashMap<int[], Fitxa> prefix, Taulell taulell, infoCasella[][] info, Casella casella ,DAWGnode node, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {
        if (node == null) {
            return;
        }

        if (casella == null) {
            if (node.getEsParaula())
                mirarJugada(prefix, taulell, diccionari, across);
            return;
        }

        if (casella.isOcupada()) {
            DAWGnode nouNode = getNode(casella.getFitxa().getLletra(), node);
            if (nouNode != null) {
                extendreDreta(prefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atril, alfabet, across);
            }
            return;
        }

        if (node.getEsParaula()) {
            mirarJugada(prefix, taulell, diccionari, across);
        }

        for (int i = 0; i < atril.size(); i++) {
            String lletra = atril.get(i).getLletra();
            if (lletra.equals("#")) {
                for (String lletra2 : alfabet) {
                    if (lletra2.equals("#")) continue;

                    if (info[casella.getX()][casella.getY()].necessita_cross_check()) {
                        if (info[casella.getX()][casella.getY()].crossChecks().contains(lletra2)) {

                            DAWGnode nouNode = getNode(lletra2, node);
                            if (nouNode != null) {
                                ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                                atrilActual.remove(i);
                                Fitxa blank = new Fitxa(lletra2, 0);
                                LinkedHashMap<int[], Fitxa> nouPrefix = new LinkedHashMap<int[], Fitxa>(prefix);
                                nouPrefix.put(new int[]{casella.getX(), casella.getY()}, blank);
                                extendreDreta(nouPrefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atrilActual, alfabet, across);
                            }

                        }
                    }
                    else {
                        DAWGnode nouNode = getNode(lletra2, node);
                        if (nouNode != null) {
                            ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                            atrilActual.remove(i);
                            Fitxa blank = new Fitxa(lletra2, 0);
                            LinkedHashMap<int[], Fitxa> nouPrefix = new LinkedHashMap<int[], Fitxa>(prefix);
                            nouPrefix.put(new int[]{casella.getX(), casella.getY()}, blank);
                            extendreDreta(nouPrefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atrilActual, alfabet, across);
                        }
                    }

                }
            } else {
                if (info[casella.getX()][casella.getY()].necessita_cross_check()) {
                    if (info[casella.getX()][casella.getY()].crossChecks().contains(lletra)) {
                        DAWGnode nouNode = getNode(lletra, node);
                        if (nouNode != null) {
                            ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                            atrilActual.remove(i);
                            LinkedHashMap<int[], Fitxa> nouPrefix = new LinkedHashMap<int[], Fitxa>(prefix);
                            nouPrefix.put(new int[]{casella.getX(), casella.getY()}, atril.get(i));
                            extendreDreta(nouPrefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atrilActual, alfabet, across);
                        }
                    }
                } else {
                    DAWGnode nouNode = getNode(lletra, node);
                    if (nouNode != null) {
                        ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                        atrilActual.remove(i);
                        LinkedHashMap<int[], Fitxa> nouPrefix = new LinkedHashMap<int[], Fitxa>(prefix);
                        nouPrefix.put(new int[]{casella.getX(), casella.getY()}, atril.get(i));
                        extendreDreta(nouPrefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atrilActual, alfabet, across);
                    }
                }
            }
        }
    }

    /**
     * Converteix un llistat de fitxes en una jugada, que comença "limit" posiciones més a la esquerra que la "casella".
     * @param prefix   fitxes en prefix
     * @param casella  casella guia
     * @param limit    nombre de fitxes prèvies
     * @return mapa de posició (x,y) a fitxa
     */
    private LinkedHashMap<int[], Fitxa> ArrayToJugada(ArrayList<Fitxa> prefix, Casella casella, int limit) {
        int count = 0;
        LinkedHashMap<int[], Fitxa> jugada = new LinkedHashMap<>();
        for (int j = casella.getY()-limit; j < casella.getY(); j++) {
            jugada.put(new int[]{casella.getX(), j}, prefix.get(count));
            count++;
        }

        return jugada;
    }

    /**
     * Avalua i registra la jugada si és millor que la gaurdada.
     * @param jugada  mapa posició->fitxa
     * @param taulell taulell
     * @param diccionari DAWG
     * @param across  orientació
     */
    private void mirarJugada(LinkedHashMap<int[], Fitxa> jugada, Taulell taulell, Diccionari diccionari, boolean across) {
        if (across) {
            if (!millorJugadaAcross.getKey().isEmpty() ) { // esta descomentat perque el codi no funciona del tot, si trec aixo el codi genera masa combinacions repetides i al main es veu malament.
                return;
            }
            LinkedHashMap<int[], Fitxa> jugadaCopy = new LinkedHashMap<>(jugada);
            if (taulell.verificarFitxes(jugadaCopy, across)) {
                int puntuacio = taulell.validesaYPuntuacioJugada(jugadaCopy, diccionari, across, false);
                if (puntuacio >= 0 && puntuacio > millorJugadaAcross.getValue()) {
                    millorJugadaAcross = new AbstractMap.SimpleEntry<>(jugadaCopy, puntuacio);
                }
            }
        } else {
            if (!millorJugadaDown.getKey().isEmpty() ) { // esta descomentat perque el codi no funciona del tot, si trec aixo el codi genera masa combinacions repetides i al main es veu malament.
                return;
            }
            LinkedHashMap<int[], Fitxa> jugadaCopy = new LinkedHashMap<>(jugada);
            if (taulell.verificarFitxes(jugadaCopy, across)) {
                int puntuacio = taulell.validesaYPuntuacioJugada(jugadaCopy, diccionari, across, false);
                if (puntuacio >= 0 && puntuacio > millorJugadaDown.getValue()) {
                    //transposar la jugada per guardar-la
                    LinkedHashMap<int[], Fitxa> jugadaTransposada = new LinkedHashMap<>();
                    for (Map.Entry<int[], Fitxa> entry : jugada.entrySet()) {
                        int[] pos = entry.getKey();
                        Fitxa fitxa = entry.getValue();
                        jugadaTransposada.put(new int[]{pos[1], pos[0]}, fitxa);
                    }

                    millorJugadaDown = new AbstractMap.SimpleEntry<>(jugadaTransposada, puntuacio);
                }
            }
        }
    }

    /**
     * Navega el DAWG segons la cadena i node inicial.
     * @param paraula    cadena a buscar
     * @return node final o null
     */
    private DAWGnode getNode(String paraula, DAWGnode node) {
        DAWGnode nouNode = node;
        for (int i = 0; i < paraula.length(); i++) {
            char lletra = paraula.charAt(i);
            if (nouNode != null && nouNode.getTransicio(lletra) != null) {
                nouNode = nouNode.getTransicio(lletra);
            } else {
                return null;
            }
        }
        return nouNode;
    }

    /**
     * Calcula les caselles anchor i els cross-checks.
     * @param taulell taulell
     * @param info    matriu d'infoCasella
     * @param diccionari DAWG
     * @param alfabet conjunt de caràcters
     */
    private void calcularAnchorsICrossChecks(Taulell taulell, infoCasella[][] info,  Diccionari diccionari, Set<String> alfabet) {
        // si es el primer moviment, només la casella inicial és anchor, y no hi han fitxes adjacents ni cross-checks.
        if (taulell.esPrimerMoviment()) {
            for (Casella[] c : taulell.getCaselles()) {
                for (Casella casella : c) {
                    info[casella.getX()][casella.getY()].setAnchor(casella.isEsCasellaInicial());
                    // si la casella es la inicial, no hi ha cross-checks
                }
            }
        } else {
            // si no es el primer moviment, busquem les anchors i cross-checks
            for (Casella[] c : taulell.getCaselles()) {
                for (Casella casella : c) {
                    // si la casella esta buida y te alguna fitxa adjacent, es una candidata a anchor.
                    if (!casella.isOcupada() && taulell.teFitxaAdjacent(casella.getX(), casella.getY())) {
                        info[casella.getX()][casella.getY()].setAnchor(true);

                        // si la casella te fitxa superior o inferior, al colocar una fitxa, es pot formar una paraula vertical, per tant necesitem cross-checks
                        if (taulell.teFitxaSuperiorOInferior(casella.getX(), casella.getY())) {
                            info[casella.getX()][casella.getY()].setNecessita_cross_check(true);
                            info[casella.getX()][casella.getY()].setCrossChecks(calcularCrossChecks(taulell, casella.getX(), casella.getY(), diccionari, alfabet));
                        }
                        else {
                            info[casella.getX()][casella.getY()].setNecessita_cross_check(false);
                        }
                    } else {
                        info[casella.getX()][casella.getY()].setAnchor(false);
                        info[casella.getX()][casella.getY()].setNecessita_cross_check(false);
                    }
                }
            }
        }
    }

    /**
     * Retorna llistat de lletres vàlides per a cross-checks d'una casella.
     * @param taulell taulell
     * @param x posició fila
     * @param y posició columna
     * @param diccionari DAWG
     * @param alfabet conjunt de caràcters
     * @return llista de caràcters vàlids
     */
    private  ArrayList<String> calcularCrossChecks(Taulell taulell, int x, int y, Diccionari diccionari, Set<String> alfabet) {
        String superior = getParaulaSuperior(taulell, x, y);
        String inferior = getParaulaInferior(taulell, x, y);

        ArrayList<String> cross_checks = new ArrayList<>();
        for (String lletra : alfabet) {
            if (!lletra.equals("#")) {
                String paraula = superior + lletra + inferior;
                if (diccionari.esParaula(paraula)) {
                    cross_checks.add(lletra);
                }
            }
        }
        return cross_checks;
    }

    /**
     * Obté la part superior de la paraula vertical a partir de la casella en la posició (x, y).
     * @param taulell taulell
     * @param x fila
     * @param y columna
     * @return cadena superior
     */
    private String getParaulaSuperior(Taulell taulell, int x, int y) {
        StringBuilder superior = new StringBuilder();
        while (x > 0 && taulell.getCaselles()[x-1][y].isOcupada()) {
            superior.insert(0, taulell.getCaselles()[x - 1][y].getFitxa().getLletra());
            x--;
        }
        return superior.toString();
    }

    /**
     * Obté la part inferior de la paraula vertical a partir de la casella en la posició (x, y)..
     * @param taulell taulell
     * @param x fila
     * @param y columna
     * @return cadena inferior
     */
    private String getParaulaInferior(Taulell taulell, int x, int y) {
        StringBuilder inferior = new StringBuilder();
        while (x < taulell.getCaselles().length - 1 && taulell.getCaselles()[x + 1][y].isOcupada()) {
            inferior.append(taulell.getCaselles()[x + 1][y].getFitxa().getLletra());
            x++;
        }
        return inferior.toString();
    }

    /**
     * Navega enrere per obtenir prefix anterior horitzontalment a partir de la casella.
     * @param taulell taulell
     * @param anterior casella anterior ocupada
     * @return prefix de cadena
     */
    private String getParulaAnterior(Taulell taulell, Casella anterior) {
        StringBuilder paraula = new StringBuilder();
        while (anterior != null && anterior.isOcupada()) {
            paraula.insert(0, anterior.getFitxa().getLletra());
            anterior = taulell.getCasella(anterior.getX(), anterior.getY() - 1);
        }
        return paraula.toString();
    }

    /**
     * Comptabilitza posicions lliures sense anchors anterior a la casella "anterior".
     * @param taulell taulell
     * @param info    matriu d'infoCasella
     * @param anterior casella fins on comptar
     * @return nombre de posicions
     */
    private int getPosicionsSenseAnchors(Taulell taulell, infoCasella[][] info,  Casella anterior) {
        int limit = 0;
        while (anterior != null && !info[anterior.getX()][anterior.getY()].isAnchor() && !anterior.isOcupada()) {
            limit++;
            anterior = taulell.getCasella(anterior.getX(), anterior.getY() - 1);
        }
        return limit;
    }

    /**
     * Transposa files i columnes del taulell.
     * @param taulell taulell original
     * @return nou taulell transposat
     */
    private Taulell transposarTaulell(Taulell taulell) {
        int rows = taulell.getCaselles().length;
        int cols = taulell.getCaselles()[0].length;
        Casella[][] transposat = new Casella[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposat[j][i] = taulell.getCasella(i, j);
                // transposar la fitxa
                transposat[j][i].setX(j);
                transposat[j][i].setY(i);
            }
        }

        Taulell taulellTransposat = new Taulell();
        taulellTransposat.setCaselles(transposat);

        return taulellTransposat;
    }

    /**
     * Dades precomputades per a cada casella del taulell:
     * - si és anchor (podem col·locar-hi fitxa)
     * - si cal fer cross-check (hi ha lletres a dalt o baix)
     * - llista de caràcters vàlids per al cross-check
     */
    private class infoCasella {
        private boolean anchor;
        private boolean necessita_cross_check;
        private List<String> cross_checks;

        /**
         * Crea un objecte  que no és anchor i sense requisits de cross-check.
         */
        public infoCasella(boolean anchor, boolean necessita_cross_check) {
            this.anchor = anchor;
            this.necessita_cross_check = necessita_cross_check;
            this.cross_checks = new ArrayList<>();
        }

        /**
         * Crea un objecte  que no és anchor i sense requisits de cross-check.
         */
        public infoCasella() {
            this.anchor = false;
            this.necessita_cross_check = false;
            this.cross_checks = new ArrayList<>();
        }


        /**
         * Indica si la casella és un punt d'ancoratge per a noves jugades.
         * @return true si és anchor, false si no
         */
        public boolean isAnchor() {
            return anchor;
        }


        /**
         * Indica si cal verificar les lletres adjacents de manera vertical (cross-check).
         * @return true si hi ha lletres a dalt o baix, false si no
         */
        public boolean necessita_cross_check() {
            return necessita_cross_check;
        }

        /**
         * Retorna la llista de caràcters vàlids per al cross-check d'aquesta casella.
         * @return llista de lletres
         */
        public List<String> crossChecks() {
            return cross_checks;
        }


        /**
         * Marca aquesta casella com a anchor o no anchor.
         * @param anchor valor a assignar
         */
        public void setAnchor(boolean anchor) {
            this.anchor = anchor;
        }


        /**
         * Marca si cal fer cross-check en aquesta casella.
         * @param necessita_cross_check  valor a assignar
         */
        public void setNecessita_cross_check(boolean necessita_cross_check) {
            this.necessita_cross_check = necessita_cross_check;
        }

        /**
         * Assigna la llista de caràcters vàlids per al cross-check.
         * @param cross_checks llista de caràcters
         */
        public void setCrossChecks(List<String> cross_checks) {
            this.cross_checks = cross_checks;
        }

    }

}

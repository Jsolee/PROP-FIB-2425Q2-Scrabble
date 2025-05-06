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
    private Jugada millorJugada; // jugades possibles ordenades per puntuació, si hi han dues jugades amb la mateixa puntuació, només es guardara 1

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
     * @param taulell   taulell en el qual es vol jugar
     * @param diccionari estructura DAWG amb paraules vàlides
     * @param atril     llista de fitxes disponibles del bot
     * @param alfabet   conjunt de caràcters vàlids (inclou # per blanks)
     * @return parella (jugada, orientació), on jugada és un mapa de posició->fitxa
     *         i orientació {@code true}=horitzontal, {@code false}=vertical
     */
    public Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> getMillorJugada(Taulell taulell, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet) {
        
        this.millorJugada = new Jugada();

        // calcular anchors y cross-checks de taulell
        taulell.calcularAnchorsICrossChecks(diccionari, alfabet);

        // per paraules horitzontals
        getMillorJugadaAux(taulell, diccionari, atril, alfabet, true);

        // per paraules verticals
        getMillorJugadaAux(taulell, diccionari, atril, alfabet, false);

        return new AbstractMap.SimpleEntry<>(millorJugada.getJugada(), millorJugada.isAcross());
    }

    /**
     * Auxiliar recursiu que explora totes les jugades des d'anchors.
     * @param taulell   taulell (posicions i fitxes)
     * @param diccionari DAWG de paraules
     * @param atril     fitxes disponibles
     * @param alfabet   caràcters vàlids
     * @param across    {@code true}=horitzontal, {@code false}=vertical
     */
    private void getMillorJugadaAux(Taulell taulell, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {

        for (Casella[] c : taulell.getCaselles()) {
            // per cada fila
            for (Casella casella : c) {
                int x = casella.getX();
                int y = casella.getY();

                if (casella.isAnchor()) {
//                    System.out.println("Examining anchor at: [" + x + "," + y + "]"); //-----------------------------------------------------------------------------
                    if (across) {
                        Casella anterior = taulell.getCasella(casella.getX(), casella.getY()-1);
                        if (anterior != null && anterior.isOcupada()) {
                            // si la casella anterior està ocupada, tenim un prefix definit per caselles anteriors (suposem que la paraula anterior és correcta)
                            ArrayList<Fitxa> prefix = new ArrayList<Fitxa>();
                            String paraula = taulell.getParaulaEsquerra(x, y);
                            DAWGnode nodeActual = diccionari.getNode(paraula);

//                        System.out.println("  Word from left: '" + paraula + "'");

                            extendreDreta(prefix, taulell, casella, nodeActual, diccionari, atril, alfabet, across);
                            continue;
                        }
                        else {
                            int limit = taulell.getPosicionsSenseAnchors(x, y, across);
                            ArrayList<Fitxa> prefix = new ArrayList<Fitxa>();

                            DAWGnode nodeActual = diccionari.getArrel();
                            extendreEsquerra(prefix, taulell, nodeActual, casella, diccionari, atril, alfabet, limit, across);
                        }
                    } else {
                        Casella anterior = taulell.getCasella(casella.getX()-1, casella.getY());
                        if (anterior != null && anterior.isOcupada()) {
                            // si la casella anterior està ocupada, tenim un prefix definit per caselles anteriors (suposem que la paraula anterior és correcta)
                            ArrayList<Fitxa> prefix = new ArrayList<Fitxa>();
                            String paraula = taulell.getParaulaSuperior(x, y);
                            DAWGnode nodeActual = diccionari.getNode(paraula);


                            extendreDreta(prefix, taulell, casella, nodeActual, diccionari, atril, alfabet, across);
                            continue;
                        }
                        else {
                            int limit = taulell.getPosicionsSenseAnchors(x, y, across);
                            ArrayList<Fitxa> prefix = new ArrayList<Fitxa>();

                            DAWGnode nodeActual = diccionari.getArrel();
                            extendreEsquerra(prefix, taulell, nodeActual, casella, diccionari, atril, alfabet, limit, across);
                        }
                    }
                }

            }
        }
    }

    /**
     * Extén el prefix cap a l'esquerra per generar possibles paraules Per cada prefix trobat expandeix per la dreta.
     * @param prefix       llista de fitxes col·locades prèviament
     * @param taulell      taulell
     * @param node         node actual del DAWG
     * @param casellaAnchor casella d'anchor
     * @param diccionari   DAWG
     * @param atril        fitxes disponibles
     * @param alfabet      conjunt de caràcters
     * @param limit        nombre màxim de fitxes a l'esquerra
     * @param across       orientació
     */
    private void extendreEsquerra(ArrayList<Fitxa> prefix, Taulell taulell, DAWGnode node, Casella casellaAnchor, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, int limit, boolean across) {
        // extendre cap a la dreta
        ArrayList<Fitxa> atrilCopy = new ArrayList<>(atril);

//        System.out.println("  Word from left: '" + prefix.toString() + "'"); // -------------------------------------------------
        extendreDreta(prefix, taulell, casellaAnchor, node, diccionari, atrilCopy, alfabet, across);

        if (limit == 0) {
            return;
        }

        for (int i = 0; i < atril.size(); i++) {
            Fitxa fitxa = atril.get(i);
            String lletra = fitxa.getLletra();

            if (lletra.equals("#")) {
                for (String lletra2 : alfabet) {
                    if (lletra2.equals("#")) continue;
                    Fitxa blank = new Fitxa(lletra2, 0);
                    DAWGnode nouNode = getNode(blank.getLletra(), node);

                    if (nouNode != null) {
                        ArrayList<Fitxa> nouPrefix  = new ArrayList<Fitxa>(prefix);
                        nouPrefix.add(blank);
                        ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                        atrilActual.remove(i);// eliminem la fitxa de l'atril que hem utilitzat
                        // crea nous prefixos a partir d'aquest nou prefix
                        extendreEsquerra(nouPrefix, taulell, nouNode, casellaAnchor, diccionari, atrilActual, alfabet, limit-1, across);
                    }
                }
            }
            else {
                DAWGnode nouNode = getNode(lletra, node);

                if (nouNode != null) {
                    ArrayList<Fitxa> nouPrefix  = new ArrayList<Fitxa>(prefix);
                    nouPrefix.add(fitxa);

                    ArrayList<Fitxa> atrilActual = new ArrayList<>(atril); // con Fitxa en vez de Main.Fitxa me daba error :(
                    atrilActual.remove(i);// eliminem la fitxa de l'atril que hem utilitzat
                    // crea nous prefixos a partir d'aquest nou prefix
                    extendreEsquerra(nouPrefix, taulell, nouNode, casellaAnchor, diccionari, atrilActual, alfabet, limit-1, across);
                }
            }
        }
    }

    /**
     * Extén el prefix cap a la dreta (o a baix si across=false)  construint paraules complertes.
     * @param prefix   mapa posició->fitxa
     * @param taulell  taulell
     * @param casella  casella actual
     * @param node     node del DAWG
     * @param diccionari DAWG
     * @param atril    fitxes restants
     * @param alfabet  caràcters vàlids
     * @param across   orientació
     */
    private void extendreDreta(ArrayList<Fitxa> prefix, Taulell taulell, Casella casella ,DAWGnode node, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {
        if (node == null || casella == null) {
            return;
        }

        if (node.getEsParaula()) {
            LinkedHashMap<int[], Fitxa> jugada = arrayToJugada(taulell, casella, prefix, across);
            mirarJugada(jugada, taulell, diccionari, across);
        }

        if (casella.isOcupada()) {
            DAWGnode nouNode = getNode(casella.getFitxa().getLletra(), node);
            if (nouNode != null) {
                Casella nextCasella = null;
                if (across) nextCasella = taulell.getCasella(casella.getX(), casella.getY()+1);
                else nextCasella = taulell.getCasella(casella.getX()+1, casella.getY());
                if (nextCasella == null){
                    // si la casella següent no existeix, hem arribat al final de la paraula
                    LinkedHashMap<int[], Fitxa> jugada = arrayToJugada(taulell, casella, prefix, across);
                    mirarJugada(jugada, taulell, diccionari, across);
                    return;
                }
                extendreDreta(prefix, taulell, nextCasella, nouNode, diccionari, atril, alfabet, across);
            }
            return;
        }


        for (int i = 0; i < atril.size(); i++) {
            Fitxa fitxa = atril.get(i);
            String lletra = fitxa.getLletra();

            if (lletra.equals("#")) {
                for (String lletra2 : alfabet) {
                    if (lletra2.equals("#")) continue;

                    if (across && casella.isNecessitaCrossCheckAcross() && !casella.getCrossChecksAcross().contains(lletra2)) {
                        continue;
                    }

                    if (!across && casella.isNecessitaCrossCheckDown() && !casella.getCrossChecksDown().contains(lletra2)) {
                        continue;
                    }

                    DAWGnode nouNode = getNode(lletra2, node);
                    if (nouNode != null) {
                        Fitxa blank = new Fitxa(lletra2, 0);
                        ArrayList<Fitxa> nouPrefix  = new ArrayList<Fitxa>(prefix);
                        nouPrefix.add(blank);

                        ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                        atrilActual.remove(i);

                        Casella nextCasella = null;
                        if (across) nextCasella = taulell.getCasella(casella.getX(), casella.getY()+1);
                        else nextCasella = taulell.getCasella(casella.getX()+1, casella.getY());
                        extendreDreta(nouPrefix, taulell, nextCasella, nouNode, diccionari, atrilActual, alfabet, across);
                    }

                }
            } else {
                DAWGnode nouNode = getNode(lletra, node);
                if (nouNode != null) {
                    ArrayList<Fitxa> nouPrefix  = new ArrayList<Fitxa>(prefix);
                    nouPrefix.add(fitxa);

                    ArrayList<Fitxa> atrilActual = new ArrayList<>(atril);
                    atrilActual.remove(i);

                    Casella nextCasella = null;
                    if (across) nextCasella = taulell.getCasella(casella.getX(), casella.getY()+1);
                    else nextCasella = taulell.getCasella(casella.getX()+1, casella.getY());
                    extendreDreta(nouPrefix, taulell, nextCasella, nouNode, diccionari, atrilActual, alfabet, across);
                }
            }
        }
    }

    /**
     * Converteix un llistat de fitxes en una jugada, miran a partir de la casella pasada per parámetre on es poden colocar les fitxes.
     * @param taulell taulell on es volen col·locar les fitxes
     * @param prefix   fitxes en prefix
     * @param casella  casella guia
     * @param across  orientació
     * @return mapa de posició (x,y) a fitxa
     */
    private LinkedHashMap<int[], Fitxa> arrayToJugada(Taulell taulell, Casella casella, ArrayList<Fitxa> prefix, boolean across) {
        LinkedHashMap<int[], Fitxa> jugada = new LinkedHashMap<>();
        ArrayList<Fitxa> copiaPrefix = new ArrayList<>(prefix);
        Casella casellaActual = casella;

        while (!copiaPrefix.isEmpty() && casellaActual != null) {
            if (casellaActual.isOcupada()) {
                if (across) casellaActual = taulell.getCasella(casellaActual.getX(), casellaActual.getY()-1);
                else casellaActual = taulell.getCasella(casellaActual.getX()-1, casellaActual.getY());
                continue;
            }
            Fitxa fitxa = copiaPrefix.remove(copiaPrefix.size()-1);

            int[] pos = {casellaActual.getX(), casellaActual.getY()};
            // add to the beggining of the hashmap
            jugada.putFirst(pos, fitxa);
            if (across) casellaActual = taulell.getCasella(casellaActual.getX(), casellaActual.getY()-1);
            else casellaActual = taulell.getCasella(casellaActual.getX()-1, casellaActual.getY());
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
        LinkedHashMap<int[], Fitxa> jugadaCopy = new LinkedHashMap<>(jugada);
       if (!jugada.isEmpty()) {
            int puntuacio = taulell.validesaYPuntuacioJugada(jugadaCopy, diccionari, across, false);
            if (puntuacio >= 0 && puntuacio > millorJugada.getPuntuacio()) {
                millorJugada = new Jugada(jugadaCopy, puntuacio, across);
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


    private class Jugada {
        private LinkedHashMap<int[], Fitxa> jugada;
        private int puntuacio;
        private boolean across;

        public Jugada() {
            this.jugada = new LinkedHashMap<>();
            this.puntuacio = -1;
            this.across = true;
        }

        public Jugada(LinkedHashMap<int[], Fitxa> jugada, int puntuacio, boolean across) {
            this.jugada = jugada;
            this.puntuacio = puntuacio;
            this.across = across;
        }

        public LinkedHashMap<int[], Fitxa> getJugada() {
            return jugada;
        }

        public int getPuntuacio() {
            return puntuacio;
        }

        public boolean isAcross() {
            return across;
        }

        public void setJugada(LinkedHashMap<int[], Fitxa> jugada) {
            this.jugada = jugada;
        }

        public void setPuntuacio(int puntuacio) {
            this.puntuacio = puntuacio;
        }

        public void setAcross(boolean across) {
            this.across = across;
        }
    }

}

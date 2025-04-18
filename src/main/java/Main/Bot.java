package Main;

import java.lang.reflect.Array;
import java.util.*;

// Aquesta és una clase singleton, ja que només hi haurà una instància del bot al llarg de l'execució del programa.
public class Bot extends Usuari{
    // Variable que conté l'única instancia del bot.
    private static Bot instancia;

    private Map.Entry<LinkedHashMap<int[], Fitxa>, Integer> millorJugadaAcross; // jugades possibles ordenades per puntuació, si hi han dues jugades amb la mateixa puntuació, només es guardara 1

    private Bot() {
        super("bot");
    }

    public static Bot getInstance() {
        if (instancia == null) {
            instancia = new Bot();
        }
        return instancia;
    }

    // retorna la millor jugada possible per al bot, amb boolean que indica si es across
    public Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> getMillorJugada(Taulell taulell, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet) {
        // inicialitzem el taulell amb la informació extra
        int rows = taulell.getCaselles().length;
        int cols = taulell.getCaselles()[0].length;
        infoCasella[][] info = new infoCasella[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                info[i][j] = new infoCasella();   // <-- populate each slot
            }
        }

        this.millorJugadaAcross = new AbstractMap.SimpleEntry<>(new LinkedHashMap<>(), 0);

        // calcular anchors y cross-checks de taulell
        calcularAnchorsICrossChecks(taulell, info , diccionari, alfabet);

        getMillorJugadaAux(taulell, info, diccionari, atril, alfabet, true);

        return new AbstractMap.SimpleEntry<>(millorJugadaAcross.getKey(), true);
    }

    private void getMillorJugadaAux(Taulell taulell, infoCasella[][] info, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {

        for (Casella[] c : taulell.getCaselles()) {
            // per cada fila
            for (Casella casella : c) {
                int x = casella.getX();
                int y = casella.getY();
                if (info[x][y].isAnchor()) {
                    Casella anterior = taulell.getCasella(casella.getX(), casella.getY()-1);
                    if (anterior.isOcupada()) {
                        // si la casella anterior està ocupada, tenim un prefix definit per caselles anteriors (suposem que la paraula anterior es correcta)
                        LinkedHashMap<int[], Fitxa> prefix = new LinkedHashMap<>();
                        String paraula = getParulaAnterior(taulell, anterior);
                        DAWGnode nodeActual = diccionari.getNode(paraula);
                        extendreDreta(prefix, taulell, info, casella, nodeActual, diccionari, atril, alfabet, across);
                        break;
                    }
                    else {
                        LinkedHashMap<int[], Fitxa> prefix = new LinkedHashMap<>();

                        extendreEsquerra(prefix, taulell, info, casella, anterior, diccionari, atril, alfabet, across);
                    }

                }

            }
        }
    }

    private void extendreEsquerra(LinkedHashMap<int[], Fitxa> prefix, Taulell taulell, infoCasella[][] info, Casella casellaAnchor, Casella anterior, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {

        DAWGnode nodeActual = diccionari.getNode(paraulaToString(prefix)); // aconseguir el node a partir del qual s'ha d'extendre.
        // exten per la dreta y va trobant paraules amb aquest prefix.
        ArrayList<Fitxa> atrilCopy = new ArrayList<>(atril);
        extendreDreta(prefix, taulell, info, casellaAnchor, nodeActual, diccionari, atrilCopy, alfabet, across);

        if (anterior == null || info[anterior.getX()][anterior.getY()].isAnchor()) {;
            // si la casella anterior es null o es una anchor, no podem continuar
            return;
        }


        for (int i = 0; i < atril.size(); i++) {
            String lletra = atril.get(i).getLletra();

            // para evitar tener que añadir al principio, podria simplemente añadir al final y restarles 1 en la columna a las fichas de la jugada
            if (lletra.equals("#")) {
                for (String lletra2 : alfabet) {
                    if (lletra2.equals("#")) continue;
                    LinkedHashMap<int[], Fitxa> nouPrefix = new LinkedHashMap<int[], Fitxa>();
                    Fitxa blank = new Fitxa(lletra2, 0);
                    nouPrefix.put(new int[]{anterior.getX(), anterior.getY()}, blank);
                    nouPrefix.putAll(prefix);
                    DAWGnode nouNode = diccionari.getNode(paraulaToString(nouPrefix));

                    if (nouNode != null) {
                        ArrayList<Fitxa> atrilActual = new ArrayList<>(atril); // con Fitxa en vez de Main.Fitxa me daba error :(
                        atrilActual.remove(i);// eliminem la fitxa de l'atril que hem utilitzat

                        // crea nous prefixos a partir d'aquest nou prefix
                        extendreEsquerra(nouPrefix, taulell, info, casellaAnchor, taulell.getCasella(anterior.getX(), anterior.getY()-1), diccionari, atrilActual, alfabet, across);
                    }
                }
            }
            else {
                LinkedHashMap<int[], Fitxa> nouPrefix = new LinkedHashMap<int[], Fitxa>();
                nouPrefix.put(new int[]{anterior.getX(), anterior.getY()}, atril.get(i));
                nouPrefix.putAll(prefix);
                DAWGnode nouNode = diccionari.getNode(paraulaToString(nouPrefix));

                if (nouNode != null) {
                    ArrayList<Fitxa> atrilActual = new ArrayList<>(atril); // con Fitxa en vez de Main.Fitxa me daba error :(
                    atrilActual.remove(i);// eliminem la fitxa de l'atril que hem utilitzat

                    // crea nous prefixos a partir d'aquest nou prefix
                    extendreEsquerra(nouPrefix, taulell, info, casellaAnchor, taulell.getCasella(anterior.getX(), anterior.getY()-1), diccionari, atrilActual, alfabet, across);
                }
            }
        }
    }

    private void extendreDreta(LinkedHashMap<int[], Fitxa> prefix, Taulell taulell, infoCasella[][] info, Casella casella ,DAWGnode node, Diccionari diccionari, List<Fitxa> atril, Set<String> alfabet, boolean across) {
        // if the prefix so far isn’t in the DAWG, bail out
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
                                Fitxa removed = atril.remove(i);
                                Fitxa blank = new Fitxa(lletra2, 0);
                                prefix.put(new int[]{casella.getX(), casella.getY()}, blank);
                                extendreDreta(prefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atril, alfabet, across);
                                prefix.remove(new int[]{casella.getX(), casella.getY()});
                                atril.add(i, removed);
                            }

                        }
                    }
                    else {
                        DAWGnode nouNode = getNode(lletra2, node);
                        if (nouNode != null) {
                            Fitxa removed = atril.remove(i);
                            Fitxa blank = new Fitxa(lletra2, 0);
                            prefix.put(new int[]{casella.getX(), casella.getY()}, blank);
                            extendreDreta(prefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atril, alfabet, across);
                            prefix.remove(new int[]{casella.getX(), casella.getY()});
                            atril.add(i, removed);
                        }
                    }

                }
            } else {
                if (info[casella.getX()][casella.getY()].necessita_cross_check()) {
                    if (info[casella.getX()][casella.getY()].crossChecks().contains(lletra)) {
                        DAWGnode nouNode = getNode(lletra, node);
                        if (nouNode != null) {
                            Fitxa removed = atril.remove(i);
                            prefix.put(new int[]{casella.getX(), casella.getY()}, removed);
                            extendreDreta(prefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atril, alfabet, across);
                            prefix.remove(new int[]{casella.getX(), casella.getY()});
                            atril.add(i, removed);
                        }
                    }
                } else {
                    DAWGnode nouNode = getNode(lletra, node);
                    if (nouNode != null) {
                        Fitxa removed = atril.remove(i);
                        prefix.put(new int[]{casella.getX(), casella.getY()}, removed);
                        extendreDreta(prefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), nouNode, diccionari, atril, alfabet, across);
                        extendreDreta(prefix, taulell, info, taulell.getCasella(casella.getX(), casella.getY()+1), node, diccionari, atril, alfabet, across);
                        prefix.remove(new int[]{casella.getX(), casella.getY()});
                        atril.add(i, removed);
                    }
                }
            }
        }
    }

    private void mirarJugada(LinkedHashMap<int[], Fitxa> jugada, Taulell taulell, Diccionari diccionari, boolean across) {
        if (across) {
            int puntuacio = taulell.validesaYPuntuacioJugada(jugada, diccionari, across, false);
            if (puntuacio > millorJugadaAcross.getValue()) {
                millorJugadaAcross = new AbstractMap.SimpleEntry<>(jugada, puntuacio);
            }
        }
    }

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

    private String getParaulaSuperior(Taulell taulell, int x, int y) {
        StringBuilder superior = new StringBuilder();
        while (x > 0 && taulell.getCaselles()[x-1][y].isOcupada()) {
            superior.insert(0, taulell.getCaselles()[x - 1][y].getFitxa().getLletra());
            x--;
        }
        return superior.toString();
    }

    private String getParaulaInferior(Taulell taulell, int x, int y) {
        StringBuilder inferior = new StringBuilder();
        while (x < taulell.getCaselles().length - 1 && taulell.getCaselles()[x + 1][y].isOcupada()) {
            inferior.append(taulell.getCaselles()[x + 1][y].getFitxa().getLletra());
            x++;
        }
        return inferior.toString();
    }

    private String paraulaToString(LinkedHashMap<int[], Fitxa> jugada) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<int[], Fitxa> entry : jugada.entrySet()) {
            Fitxa fitxa = entry.getValue();
            sb.append(fitxa.getLletra());
        }
        return sb.toString();
    }

    private String getParulaAnterior(Taulell taulell, Casella anterior) {
        StringBuilder paraula = new StringBuilder();
        while (anterior != null && anterior.isOcupada()) {
            paraula.insert(0, anterior.getFitxa().getLletra());
            anterior = taulell.getCasella(anterior.getX(), anterior.getY() - 1);
        }
        return paraula.toString();
    }

    private class infoCasella {
        private boolean anchor;
        private boolean necessita_cross_check;
        private List<String> cross_checks;

        public infoCasella(boolean anchor, boolean necessita_cross_check) {
            this.anchor = anchor;
            this.necessita_cross_check = necessita_cross_check;
            this.cross_checks = new ArrayList<>();
        }

        public infoCasella() {
            this.anchor = false;
            this.necessita_cross_check = false;
            this.cross_checks = new ArrayList<>();
        }

        public boolean isAnchor() {
            return anchor;
        }

        public boolean necessita_cross_check() {
            return necessita_cross_check;
        }

        public List<String> crossChecks() {
            return cross_checks;
        }


        public void setAnchor(boolean anchor) {
            this.anchor = anchor;
        }
        public void setNecessita_cross_check(boolean necessita_cross_check) {
            this.necessita_cross_check = necessita_cross_check;
        }
        public void setCrossChecks(List<String> cross_checks) {
            this.cross_checks = cross_checks;
        }

        public void afegirCrossCheck(String cross_check) {
            this.cross_checks.add(cross_check);
        }
    }

}

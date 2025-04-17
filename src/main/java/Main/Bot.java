package Main;

import java.util.*;

// Aquesta és una clase singleton, ja que només hi haurà una instància del bot al llarg de l'execució del programa.
public class Bot extends Usuari{
    // Variable que conté l'única instancia del bot.
    private static Bot instancia;

    private Bot() {
        super("bot");
    }

    public static Bot getInstancia() {
        if (instancia == null) {
            instancia = new Bot();
        }
        return instancia;
    }

    // retorna la millor jugada possible per al bot, amb boolean que indica si es across
    public Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> getMillorJugada(Taulell taulell, Diccionari diccionari, ArrayList<Fitxa> atril, ArrayList<String> alfabet) {
        // inicialitzem el taulell amb la informació extra
        infoCasella[][] info = new infoCasella[taulell.getCaselles().length][taulell.getCaselles()[0].length];

        // calcular anchors y cross-checks de taulell
        calcularAnchorsICrossChecks(taulell, diccionari, alfabet);
        return null;
    }

    private void calcularAnchorsICrossChecks(Taulell taulell, infoCasella[][] info,  Diccionari diccionari, ArrayList<String> alfabet) {
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
                                info[casella.getX()][casella.getY()].setCrossChecks(calcularCrossChecks(taulell, casella.getX(), casella.getY(), alfabet));
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

    private  ArrayList<String> calcularCrossChecks(Taulell taulell, int x, int y, ArrayList<String> alfabet) {

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

        public boolean isAnchor() {
            return anchor;
        }

        public boolean Necessita_cross_check() {
            return necessita_cross_check;
        }

        public List<String> CrossChecks() {
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

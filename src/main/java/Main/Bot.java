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
        // calcular anchors y cross-checks de taulell
        calcularAnchorsICrossChecks(taulell, alfabet);
        return null;
    }

    private void calcularAnchorsICrossChecks(Taulell taulell, ArrayList<String> alfabet) {
            // si es el primer moviment, només la casella inicial és anchor, y no hi han fitxes adjacents ni cross-checks.
            if (taulell.esPrimerMoviment()) {
                for (Casella[] c : taulell.getCaselles()) {
                    for (Casella casella : c) {
                        casella.setEsAnchor(casella.isEsCasellaInicial());
                        casella.setTeFitxaAdjacent(false);
                    }
                }
            } else {
                for (Casella[] c : taulell.getCaselles()) {
                    for (Casella casella : c) {
                        // si la casella esta buida y te alguna fitxa adjacent, es una candidata a anchor.
                        if (!casella.isOcupada() &&taulell.teFitxaAdjacent(casella.getX(), casella.getY())) {
                            casella.setTeFitxaAdjacent(true);

                            // comprovar si es anchor i setejar
                            Casella casella_a_la_esquerra = taulell.getCasella(casella.getX()-1, casella.getY());
                            // si la casella a la esquerra és null(esta a la vora del taulell) o la casella a la esquerra no és un anchor, llavors la casella si és un anchor.
                            casella.setEsAnchor(casella_a_la_esquerra == null || !casella_a_la_esquerra.isEsAnchor());

                            // comprovar el crossCecks
                            calcularCrossChecks(taulell, casella.getX(), casella.getY(), alfabet);

                        } else {
                            casella.setTeFitxaAdjacent(false);
                            casella.setEsAnchor(false);
                        }
                    }
                }
            }
    }

    private void calcularCrossChecks(Taulell taulell, int x, int y, ArrayList<String> alfabet) {

    }

}

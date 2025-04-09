package Main;

import java.util.*;

public class Partida {
    private String nom;
    private Taulell taulell;
    private List<Persona> jugadors;
    private int jugadorActual;
    private Bossa bossa;
    private boolean partidaAcabada;
    private int timeout;
    private List<Fitxa> fitxesActuals;
    private List<int[]> posicionsActuals;

    // Map to track players' scores and tiles since Persona doesn't have these fields
    private Map<Persona, Integer> puntuacions;
    private Map<Persona, List<Fitxa>> fitxesJugadors;

    public Partida(int timeout, String nom) {
        this.timeout = timeout;
        this.nom = nom;
        this.taulell = new Taulell();
        this.jugadors = new ArrayList<>();
        this.jugadorActual = 0;
        this.bossa = new Bossa();
        this.partidaAcabada = false;
        this.fitxesActuals = new ArrayList<>();
        this.posicionsActuals = new ArrayList<>();
        this.puntuacions = new HashMap<>();
        this.fitxesJugadors = new HashMap<>();
    }

    public void afegirJugador(Persona jugador) {
        jugadors.add(jugador);
        puntuacions.put(jugador, 0);
        List<Fitxa> fitxesInicials = new ArrayList<>();
        fitxesJugadors.put(jugador, fitxesInicials);

        // Repartir fitxes inicials
        for (int i = 0; i < 7; i++) {
            Fitxa fitxa = bossa.agafarFitxa();
            if (fitxa != null) {
                afegirFitxa(jugador, fitxa);
            }
        }

        // Add game to player's active games
        jugador.getPartidesEnCurs().add(this);
    }

    public String getNom() {
        return nom;
    }

    private void afegirFitxa(Persona jugador, Fitxa fitxa) {
        fitxesJugadors.get(jugador).add(fitxa);
    }

    private void eliminarFitxa(Persona jugador, Fitxa fitxa) {
        fitxesJugadors.get(jugador).remove(fitxa);
    }

    public void setBossa(Bossa bossa) {
        this.bossa = bossa;
    }

    public Bossa getBossa() {
        return bossa;
    }

    private void incrementarPuntuacio(Persona jugador, int punts) {
        int puntuacioActual = puntuacions.get(jugador);
        puntuacions.put(jugador, puntuacioActual + punts);
    }

    public boolean colocarFitxa(int x, int y, Fitxa fitxa) {
        // Verify player has this tile
        if (!fitxesJugadors.get(getJugadorActual()).contains(fitxa)) {
            return false;
        }

        if (taulell.colocarFitxa(x, y, fitxa)) {
            fitxesActuals.add(fitxa);
            posicionsActuals.add(new int[]{x, y});
            return true;
        }
        return false;
    }

    public boolean confirmarJugada() {
        if (fitxesActuals.isEmpty()) {
            return false;
        }

        int puntuacio = taulell.calcularPuntuacioMoviment(fitxesActuals, posicionsActuals);
        Persona jugadorActiu = getJugadorActual();
        incrementarPuntuacio(jugadorActiu, puntuacio);

        // Reomplir les fitxes del jugador
        for (Fitxa fitxa : fitxesActuals) {
            eliminarFitxa(jugadorActiu, fitxa);
            Fitxa novaFitxa = bossa.agafarFitxa();
            if (novaFitxa != null) {
                afegirFitxa(jugadorActiu, novaFitxa);
            }
        }

        // Update player statistics
        jugadorActiu.getEstadistiques().incrementarParaulesCreades();
        jugadorActiu.getEstadistiques().incrementarPuntTotal(puntuacio);

        // Netejar les fitxes actuals i passar al següent jugador
        fitxesActuals.clear();
        posicionsActuals.clear();
        passarTorn();

        // Verificar si la partida ha acabat
        comprovarFinalPartida();

        return true;
    }

    public boolean passarTorn() {
        jugadorActual = (jugadorActual + 1) % jugadors.size();
        return true;
    }

    public boolean intercanviarFitxes(List<Fitxa> fitxes) {
        if (bossa.getQuantitatFitxes() < fitxes.size()) {
            return false;
        }

        Persona jugadorActiu = getJugadorActual();

        // Verify player has all these tiles
        for (Fitxa fitxa : fitxes) {
            if (!fitxesJugadors.get(jugadorActiu).contains(fitxa)) {
                return false;
            }
        }

        for (Fitxa fitxa : fitxes) {
            eliminarFitxa(jugadorActiu, fitxa);
            bossa.retornarFitxa(fitxa);

            Fitxa novaFitxa = bossa.agafarFitxa();
            if (novaFitxa != null) {
                afegirFitxa(jugadorActiu, novaFitxa);
            }
        }

        passarTorn();
        return true;
    }

    private void comprovarFinalPartida() {
        // La partida acaba quan la bossa està buida i algun jugador s'ha quedat sense fitxes
        if (bossa.esBuida()) {
            for (Persona jugador : jugadors) {
                if (fitxesJugadors.get(jugador).isEmpty()) {
                    partidaAcabada = true;
                    actualitzarEstadistiques();
                    break;
                }
            }
        }
    }

    private void actualitzarEstadistiques() {
        Persona guanyador = determinarGuanyador();

        // Update winner's statistics
        if (guanyador != null) {
            guanyador.getEstadistiques().incrementarPartidesGuanyades();
        }

        // Update all players' statistics
        for (Persona jugador : jugadors) {
            jugador.getEstadistiques().incrementarPartidesJugades();
            // Remove game from active games
            jugador.borrarPartidaEnCurs(this);

            // Update ranking
            jugador.getRanking().afegirPuntuacio(puntuacions.get(jugador));
        }
    }

    public Persona determinarGuanyador() {
        Persona guanyador = null;
        int maxPuntuacio = -1;

        for (Persona jugador : jugadors) {
            int puntuacio = puntuacions.get(jugador);
            if (puntuacio > maxPuntuacio) {
                guanyador = jugador;
                maxPuntuacio = puntuacio;
            }
        }

        return guanyador;
    }

    public boolean isPartidaAcabada() {
        return partidaAcabada;
    }

    public Persona getJugadorActual() {
        return jugadors.get(jugadorActual);
    }

    public Taulell getTaulell() {
        return taulell;
    }

    public List<Persona> getJugadors() {
        return jugadors;
    }

    public int getPuntuacioJugador(Persona jugador) {
        return puntuacions.getOrDefault(jugador, 0);
    }

    public List<Fitxa> getFitxesJugador(Persona jugador) {
        return fitxesJugadors.getOrDefault(jugador, new ArrayList<>());
    }

    public int getTimeout() {
        return timeout;
    }
}

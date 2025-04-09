package Main;

import java.util.*;

public class ControladorPartida {
    private Partida partida;
    private Timer timer;
    private TimerTask timeoutTask;

    public ControladorPartida(int timeout, String nomPartida, List<Persona> jugadors) {
        partida = new Partida(timeout, nomPartida);
        inicialitzarJugadors(jugadors);
        inicialitzarTimer();
    }

    private void inicialitzarJugadors(List<Persona> jugadors) {
        for (Persona jugador : jugadors) {
            partida.afegirJugador(jugador);
        }
    }

    private void inicialitzarTimer() {
        timer = new Timer();
        reiniciarTimer();
    }

    private void reiniciarTimer() {
        if (timeoutTask != null) {
            timeoutTask.cancel();
        }

        timeoutTask = new TimerTask() {
            @Override
            public void run() {
                timeoutAccio();
            }
        };

        timer.schedule(timeoutTask, partida.getTimeout() * 1000);
    }

    private void timeoutAccio() {
        System.out.println("Temps esgotat per al jugador: " + partida.getJugadorActual().getNom());
        partida.passarTorn();
        reiniciarTimer();

        // Notify observers/UI about the timeout
        notificarCanviTorn();
    }

    public boolean colocarFitxa(int x, int y, Fitxa fitxa) {
        if (partida.colocarFitxa(x, y, fitxa)) {
            reiniciarTimer();
            return true;
        }
        return false;
    }

    public boolean confirmarJugada() {
        if (partida.confirmarJugada()) {
            reiniciarTimer();
            notificarCanviTorn();
            return true;
        }
        return false;
    }

    public boolean intercanviarFitxes(List<Fitxa> fitxes) {
        if (partida.intercanviarFitxes(fitxes)) {
            reiniciarTimer();
            notificarCanviTorn();
            return true;
        }
        return false;
    }

    public boolean passarTorn() {
        if (partida.passarTorn()) {
            reiniciarTimer();
            notificarCanviTorn();
            return true;
        }
        return false;
    }

    private void notificarCanviTorn() {
        // This would be expanded to notify UI/observers about turn changes
        System.out.println("Torn actual: " + partida.getJugadorActual().getNom());
    }

    public void finalitzarPartida() {
        if (timer != null) {
            timer.cancel();
        }

        Persona guanyador = partida.determinarGuanyador();
        System.out.println("Partida finalitzada!");
        System.out.println("Guanyador: " + guanyador.getNom() + " amb " +
                partida.getPuntuacioJugador(guanyador) + " punts");

        // Mostrar classificació
        List<Persona> jugadors = partida.getJugadors();
        jugadors.sort((j1, j2) -> Integer.compare(
                partida.getPuntuacioJugador(j2),
                partida.getPuntuacioJugador(j1)
        ));

        System.out.println("\nClassificació final:");
        for (int i = 0; i < jugadors.size(); i++) {
            Persona j = jugadors.get(i);
            System.out.println((i+1) + ". " + j.getNom() + " - " +
                    partida.getPuntuacioJugador(j) + " punts");
        }
    }

    public Partida getPartida() {
        return partida;
    }

    public void mostrarEstatPartida() {
        System.out.println("\n=== Estat actual de la partida ===");
        Persona jugadorActual = partida.getJugadorActual();
        System.out.println("Torn del jugador: " + jugadorActual.getNom());
        System.out.println("Puntuació: " + partida.getPuntuacioJugador(jugadorActual));
        System.out.println("Fitxes disponibles: " + partida.getFitxesJugador(jugadorActual));
        partida.getTaulell().mostrarTaulell();
    }
}

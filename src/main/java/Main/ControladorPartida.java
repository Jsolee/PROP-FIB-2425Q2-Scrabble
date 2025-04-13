package Main;

import java.util.*;

public class ControladorPartida {
    //private Partida partida;
    private HashMap<String, Partida> partides;
    //private Timer timer;
    //private TimerTask timeoutTask;

    public ControladorPartida() {
        partides = new HashMap<>();
    }

    public void crearPartida(int timeout, String nomPartida, List<Usuari> jugadors, String idioma) 
    {
        if (partides.containsKey(nomPartida)) 
            throw new IllegalArgumentException("Ja existeix una partida amb el nom: " + nomPartida);

        Partida partida = new Partida(timeout, nomPartida, idioma);
        inicialitzarJugadors(jugadors, partida);
        //inicialitzarTimer();
        partides.put(nomPartida, partida);
    }

    private void inicialitzarJugadors(List<Usuari> jugadors, Partida partida) 
    {
        for (Usuari jugador : jugadors) 
            partida.afegirJugador(jugador);
    }

    public boolean colocarFitxa(int x, int y, Fitxa fitxa, String nomPartida) {

        Partida partida = getPartida(nomPartida);
        if (partida.colocarFitxa(x, y, fitxa)) {
            //reiniciarTimer();
            return true;
        }
        return false;
    }

    public boolean confirmarJugada(String nomPartida) {
        Partida partida = getPartida(nomPartida);

        if (partida.confirmarJugada()) {
            //reiniciarTimer();
            notificarCanviTorn(partida);
            return true;
        }
        return false;
    }

    public boolean intercanviarFitxes(List<Fitxa> fitxes, String nomPartida) {
        Partida partida = getPartida(nomPartida);

        if (partida.intercanviarFitxes(fitxes)) {
            //reiniciarTimer();
            notificarCanviTorn(partida);
            return true;
        }
        return false;
    }

    public boolean passarTorn(String nomPartida) {
        Partida partida = getPartida(nomPartida);

        if (partida.passarTorn()) {
            //reiniciarTimer();
            notificarCanviTorn(partida);
            return true;
        }
        return false;
    }

    private void notificarCanviTorn(Partida partida) {
        System.out.println("Torn actual: " + partida.getJugadorActual().getNom());
    }

    public void finalitzarPartida(String nomPartida) 
    {
        Partida partida = getPartida(nomPartida);

        Usuari guanyador = partida.determinarGuanyador();
        System.out.println("Partida finalitzada!");
        System.out.println("Guanyador: " + guanyador.getNom());

        List<Integer> puntuacions = partida.getPuntuacions();
        System.out.println("Puntuacions finals:");

        for (int i = 0; i < puntuacions.size(); i++) 
            System.out.println("Jugador " + (i + 1) + ": " + puntuacions.get(i) + " punts");
    } 

    public Partida getPartida(String nomPartida) 
    {
        if (!partides.containsKey(nomPartida)) 
            throw new IllegalArgumentException("No existeix la partida amb el nom: " + nomPartida);
        return partides.get(nomPartida);
    }

    public void mostrarEstatPartida(String nomPartida) 
    {
        Partida partida = getPartida(nomPartida);

        System.out.println("\n=== Estat actual de la partida ===");
        Usuari jugadorActual = partida.getJugadorActual();
        System.out.println("Torn del jugador: " + jugadorActual.getNom());
        //System.out.println("Puntuació: " + partida.getPuntuacioJugador(jugadorActual));
        //System.out.println("Fitxes disponibles: " + partida.getFitxesJugador(jugadorActual));
        partida.getTaulell().mostrarTaulell();
    }
}

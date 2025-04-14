package Main;

import java.util.*;

public class ControladorPartida {
    private HashMap<String, Partida> partides; 

    public ControladorPartida() {
        partides = new HashMap<>();
    }

    public Partida getPartida(String nomPartida) 
    {
        if (!partides.containsKey(nomPartida)) 
            throw new IllegalArgumentException("No existeix la partida amb el nom: " + nomPartida);
        return partides.get(nomPartida);
    }

    public Usuari tornDe(String nomPartida)
    {
        Partida partida = getPartida(nomPartida);
        return partida.getJugadorActual();
    }

    public void crearPartida(String nomPartida, List<Usuari> jugadors, String idioma) 
    {
        if (partides.containsKey(nomPartida)) 
            throw new IllegalArgumentException("Ja existeix una partida amb el nom: " + nomPartida);

        Partida partida = new Partida(nomPartida, idioma);
        inicialitzarJugadors(jugadors, partida);
        partides.put(nomPartida, partida);
    }

    private void inicialitzarJugadors(List<Usuari> jugadors, Partida partida) 
    {
        for (Usuari jugador : jugadors) 
            partida.afegirJugador(jugador);
    }
    

    public int jugarParaula(String nomPartida, String paraula, int f, int col, String orientacion)
    {
        Partida partida = getPartida(nomPartida);

        boolean valid = partida.existeixParaula(paraula);
        if (!valid)
            throw new IllegalArgumentException("La paraula no es troba al diccionari en l'idioma " + partida.getIdioma() + ".");

        valid = valid && partida.paraulaEnAtril(paraula);

        if (!valid)
            throw new IllegalArgumentException("La paraula no es pot jugar amb les fitxes de l'atril.");

        valid = valid && partida.validaEnTaulell(paraula, f, col, orientacion);

        if (valid) 
        {
            partida.retiraFitxesAtril();

            int puntuacio = partida.calculaPuntuacioJugada();

            partida.completarAtril();
            partida.passarTorn();
            return puntuacio;
        } 
        else 
        {
                // If placement failed, remove any tiles that were placed
            partida.retiraFitxesJugades();
            throw new IllegalArgumentException("No pots posar aquesta paraula al taulell en la ubicacio solicitada.");
        }
    }

    public void canviDeFitxes(String nomParaula, String[] indexsACanviar)
    {
        Partida partida = getPartida(nomParaula);

        if (partida.getBossa().getQuantitatFitxes() < 7) 
          throw new IllegalArgumentException("No hi ha prou fitxes a la bossa per intercanviar.");
    
        partida.canviFitxesAtril(indexsACanviar);
    
        System.out.println(partida.getIndexsActuals().size() + " fitxes canviades.");
        partida.passarTorn();
    }

    public boolean esFinalPartida(String nomPartida)
    {
        Partida partida = getPartida(nomPartida);
        
        if (partida.getBossa().getQuantitatFitxes() == 0) 
        {
            List<List<Fitxa>> atrils = partida.getAtrils();
            for (List<Fitxa> atril : atrils) 
            {
                if (atril.isEmpty()) 
                {
                    partida.acabarPartida();

                    return true;
                }
            }
        }
        return false;
    }

    //EL QUE IMPRIMEIX AQUESTA FUNCIO HA DE FER-HO EL MAIN
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

    //EL QUE IMPRIMEIX AQUESTA FUNCIO HA DE FER-HO EL MAIN
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

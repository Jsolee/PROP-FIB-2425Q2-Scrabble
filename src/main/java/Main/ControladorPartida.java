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

    public Partida crearPartida(String nomPartida, List<Usuari> jugadors, String idioma)
    {
        if (partides.containsKey(nomPartida))
            throw new IllegalArgumentException("Ja existeix una partida amb el nom: " + nomPartida);

        if (!idioma.equals("catalan") && !idioma.equals("castellano") && !idioma.equals("english"))
            throw new IllegalArgumentException("L'idioma ha de ser 'catalan', 'castellano' o 'english'.");

        Partida partida = new Partida(nomPartida, idioma);
        inicialitzarJugadors(jugadors, partida);
        partides.put(nomPartida, partida);
        return partida;
    }

    /**
     * Retorna una llista dels jugadors de la partida especificada
     * @param nomPartida String que identifica la Partida
     * @return
     */
    public List<Usuari> getJugadors(String nomPartida)
    {
        Partida partida = getPartida(nomPartida);
        return partida.getJugadors();
    }

    /**inicialitza els jugadors de la Partida partida
     * @param jugadors: llista d'objectes Usuari que representen els jugadors
     * @param partida: objecte Partida on s'afegiran els jugadors
     */
    private void inicialitzarJugadors(List<Usuari> jugadors, Partida partida)
    {
        for (Usuari jugador : jugadors)
            partida.afegirJugador(jugador);
    }

    /**
     * Intenta jugar una paraula a la partida. Si la jugada es valida, actualitza el taulell i l'atril del jugador actual, i retorna la puntuacio.
     * @param partida objecte partida on es volen jugar les fitxes
     * @param jugades llista de jugades a jugar. Cada jugada es un objecte Fitxa amb la seva posicio al taulell. La clau representa la posicio i el valor la fitxa.
     * @param across String que representa si la jugada es horitzontal o vertical. "H" per horitzontal i "V" per vertical.
     * @return puntuacio de la jugada. Si la jugada no es valida, retorna -1.
     */
    public int jugarParaula(Partida partida, LinkedHashMap<int[], Fitxa> jugades, String across)
    {

        return partida.jugarParaula(jugades, across);
    }

    /**
     * Canvia les fitxes de l'atril del jugador actual per fitxes de la bossa de manera aleatoria
     * @param partida objecte partida on es volen jugar les fitxes
     * @param indexsACanviar llista d'indexs de les fitxes a canviar de l'atril del jugador actual
     * @return true si s'ha pogut canviar les fitxes, false si no s'ha pogut
     */
    public boolean canviDeFitxes(Partida partida, String[] indexsACanviar)
    {
        if (partida.getBossa().getQuantitatFitxes() < indexsACanviar.length) 
          throw new IllegalArgumentException("No hi ha prou fitxes a la bossa per intercanviar. No es por canviar de fitxes.");
    
        partida.canviFitxesAtril(indexsACanviar);

        partida.passarTorn();
        return true;
    }

    public Taulell getTaulell(String nompartida)
    {
        Partida partida = getPartida(nompartida);
        return partida.getTaulell();
    }

    /**
     * Comprova si la partida ha acabat. La partida acaba si la bossa no te fitxes, si algun jugador no te fitxes a l'atril o si la partida s'ha guardat
     * @param partida objecte partida.
     * @return true si la partida ha acabat, false si no.
     */
    public boolean esFinalPartida(Partida partida)
    {
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
        else if (partida.getPartidaAcabada() == true)
            return true;
        else if (partida.getPartidaPausada() == true)
            return true;
        return false;
    }


    /**
     * acaba la partida i elimina dels jugadors les partides en curs que te
     * @param partida objecte partida.
     */
    public void acabarPartida(Partida partida)
    {
        partida.acabarPartida();
        List<Usuari> jugadors = partida.getJugadors();
        for (int i = 0; i < 2; ++i)
        {
            Usuari jugador = jugadors.get(i);
        
            if (jugador instanceof Persona)
            {
                Persona persona = (Persona) jugador;
                persona.borrarPartidaEnCurs(partida);
                Estadistiques estadistiques = persona.getEstadistiques();
                estadistiques.incrementarPartidesJugades();
                estadistiques.actualitzarRecordPersonal(partida.getPuntuacions().get(i));  
                estadistiques.incrementarPuntTotal(partida.getPuntuacions().get(i));          
            }
        }
    }


    /**
     * Comprova si el jugador actual es un bot i si es així, intenta jugar la millor jugada possible
     * @param partida objecte partida.
     * @param bot objecte Usuari que representa el bot.
     */
    public void getMillorJugada(Partida partida, Usuari bot)
    {
        boolean posada = partida.getMillorJugada(bot);
        if (posada)
            partida.passarTorn();
        else {
            String[] indices = {"0", "1", "2", "3", "4", "5", "6"};
            try {canviDeFitxes(partida, indices);}
            catch (IllegalArgumentException e) 
            {
                partida.passarTorn();
            }            
        }
    }   
}

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

    private void inicialitzarJugadors(List<Usuari> jugadors, Partida partida)
    {
        for (Usuari jugador : jugadors)
            partida.afegirJugador(jugador);
    }


    //Retorna la puntuacio de la jugada. El LinkedHashMap conte les posicions (fila i col) de les fitxes jugades.
    //per exemple: la Fitxa 'A' a la posicio (0,0) es representaria com [0,0] -> 'A'
    public int jugarParaula(Partida partida, LinkedHashMap<int[], Fitxa> jugades, String across)
    {
        //las fichas ya estan en el atril (paso 0)
        //1 verificar que es pot posar al taulell (funcion en el tablero)
        //1.5 calcular palabras nuevas (list<list<fitxa>>)
        //2 verificar que las palabras formadas existen
        //3 calcular la puntuacion total

        return partida.jugarParaula(jugades, across);
    }

    public boolean canviDeFitxes(Partida partida, String[] indexsACanviar)
    {
        if (partida.getBossa().getQuantitatFitxes() < indexsACanviar.length)
            throw new IllegalArgumentException("No hi ha prou fitxes a la bossa per intercanviar. No es por canviar de fitxes.");

        partida.canviFitxesAtril(indexsACanviar);

        partida.passarTorn();
        return true;
    }

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


    public Taulell getTaulell(String nomPartida)
    {
        Partida partida = getPartida(nomPartida);
        return partida.getTaulell();
    }

    public void acabarPartida(Partida partida)
    {
        partida.acabarPartida();
        List<Usuari> jugadors = partida.getJugadors();
        for (Usuari jugador : jugadors)
        {
            if (jugador instanceof Persona)
            {
                Persona persona = (Persona) jugador;
                persona.borrarPartidaEnCurs(partida);
            }
        }
    }

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

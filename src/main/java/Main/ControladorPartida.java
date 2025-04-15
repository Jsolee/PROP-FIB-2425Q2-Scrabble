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
    

    public int jugarParaula(Partida partida, String paraula, int f, int col, String orientacion)
    {
        if (f < 0 || f >= 15 || col < 0 || col >= 15) 
            throw new IllegalArgumentException("La fila i columna han de ser entre 0 i 14.");

        orientacion = orientacion.toUpperCase();
        if ((!orientacion.equals("H") && !orientacion.equals("V")))
            throw new IllegalArgumentException("L'orientacio ha de ser 'H' (horitzontal) o 'V' (vertical).");

        paraula = paraula.toUpperCase();
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
            partida.actualitzaPuntuacio(puntuacio);

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

    public void canviDeFitxes(Partida partida, String[] indexsACanviar)
    {
        if (partida.getBossa().getQuantitatFitxes() < 7) 
          throw new IllegalArgumentException("No hi ha prou fitxes a la bossa per intercanviar. No es por canviar de fitxes.");
    
        partida.canviFitxesAtril(indexsACanviar);
    
        partida.passarTorn();
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
                persona.eliminarPartidaEnCurs(partida);
            }
        }
    }
}

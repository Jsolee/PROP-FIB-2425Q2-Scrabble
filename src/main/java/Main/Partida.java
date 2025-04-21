package Main;

import java.util.*;

/**
 * Classe que representa una partida de Scrabble.
 * Conté informació sobre el taulell, els jugadors, la bossa de fitxes, i la puntuació dels jugadors.
 */
public class Partida {
    private String nom;
    private Taulell taulell;
    private List<Usuari> jugadors;
    private int jugadorActual;
    private Bossa bossa;
    private boolean partidaAcabada;
    private List<Integer> indexsActuals;
    private List<List<Fitxa>> atrils;
    private List<Integer> puntuacioJugadors;
    private String idioma;
    private boolean partidaPausada;
    private Diccionari diccionari;

    /**
     * Constructor de la classe Partida.
     * @param nom Nom de la partida.
     * @param idioma Idioma del diccionari a utilitzar.
     */
    public Partida(String nom, String idioma) {
        this.nom = nom;
        this.taulell = new Taulell();
        this.jugadors = new ArrayList<>();
        this.jugadorActual = 0;
        this.bossa = new Bossa(idioma);
        this.partidaAcabada = false;
        this.indexsActuals = new ArrayList<>();
        this.atrils = new ArrayList<>();
        this.puntuacioJugadors = new ArrayList<>();
        this.idioma = idioma;
        this.partidaPausada = false;
        this.diccionari = new Diccionari(idioma);
    }

    /**
     * Afegeix el jugador a la llista de jugadors de la partida. Transforma a classe Persona si l'usuari no es un d'objecte Bot. Omple l'atril del jugador especificat
     * @param jugador objecte Usuari que representa el jugador a afegir.
     */
    public void afegirJugador(Usuari jugador) 
    {
        jugadors.add(jugador);
        // Repartir fitxes inicials
        omplirAtril(-1);
        puntuacioJugadors.add(0);

        if (jugador instanceof Persona)
        {
            Persona persona = (Persona) jugador;
            persona.getPartidesEnCurs().add(this);

        }
    }

    /**
     * Omple l'atril del jugador especificat amb fitxes de la bossa.
     * @param index index del jugador a omplir l'atril. Si es -1, s'omple l'atril sencer del jugador actual.
     */
    private void omplirAtril(int index) 
    {
        if (index < 0)
        {
            List<Fitxa> atril = new ArrayList<>();
            for (int i = 0; i < 7; i++)
            {
                Fitxa fitxa = bossa.agafarFitxa();
                if (fitxa != null)
                    atril.add(fitxa);
            }
            atrils.add(atril);
        }
        else
        {
            List<Fitxa> atril = atrils.get(index);
            for (int i = atril.size(); i < 7; i++)
            {
                Fitxa fitxa = bossa.agafarFitxa();
                if (fitxa != null)
                    atril.add(fitxa);
            }
        }
    }

    public String getNom()
    {
        return nom;
    }


    public void setBossa(Bossa bossa)
    {
        this.bossa = bossa;
    }

    public Bossa getBossa()
    {
        return bossa;
    }



    /** Passa el torn al següent jugador
     */
    public boolean passarTorn() {
        jugadorActual = (jugadorActual + 1) % jugadors.size();
        return true;
    }

    /**
     * Determina el guanyador de la partida. Si hi ha un empat, retorna null.
     * @return Usuari que representa el guanyador de la partida. Si hi ha un empat, retorna null.
     */
    public Usuari determinarGuanyador() 
    {
        int puntuacioMaxima = -1;
        int indexGuanyador = -1;
        for (int p : puntuacioJugadors)
        {
            if (p > puntuacioMaxima)
            {
                puntuacioMaxima = p;
                indexGuanyador = puntuacioJugadors.indexOf(p);
            }
            else if (p == puntuacioMaxima) 
            {
                indexGuanyador = -1; // Empat
            }
        }

        if (indexGuanyador == -1) 
            return null; // Empat
        return jugadors.get(indexGuanyador);
    }

    public Usuari getTornActual() 
    {
        return jugadors.get(jugadorActual);
    }

    /**
     * Retorna el jugador actual de la partida.
     * @return Usuari que representa el jugador actual de la partida.
     */
    public Usuari getJugadorActual() 
    {
        return jugadors.get(jugadorActual);
    }

    public Taulell getTaulell() {
        return taulell;
    }

    public List<Usuari> getJugadors() {
        return jugadors;
    }

    public List<Integer> getPuntuacions() {
        return puntuacioJugadors;
    }

    public List<Fitxa> getAtril(){
        return atrils.get(jugadorActual);
    }

    public boolean getPartidaPausada() {
        return partidaPausada;
    }

    public void guardarPartida() {
        partidaPausada = true;
    }

    public boolean getPartidaAcabada() {
        return partidaAcabada;
    }


    public void acabarPartida() {
        partidaAcabada = true;
    }

    public List<Integer> getIndexsActuals()
    {
        return indexsActuals;
    }

    public String getIdioma()
    {
        return idioma;
    }

    public List<List<Fitxa>> getAtrils()
    {
        return atrils;
    }

    public void setPuntuacio(int puntuacio, int index) 
    {
        puntuacioJugadors.set(index, puntuacio);
    }

    public boolean existeixParaula(String paraula) 
    {
        return diccionari.esParaula(paraula);
    }


    
    /**
     * Omple l'atril del jugador actual amb fitxes de la bossa.
     */
    public void completarAtril() 
    {
        omplirAtril(jugadorActual);
    }



    /**
     * Canvia les fitxes de l'atril del jugador actual per fitxes de la bossa.
     * @param posicions llista de posicions de les fitxes a canviar. Cada posicio es un string amb el format "x,y" on x i y son les coordenades de la fitxa a canviar.
     * @return true si les fitxes s'han pogut canviar, false si no s'han pogut canviar.
     */
    public boolean canviFitxesAtril(String [] posicions)
    {
        List<Fitxa> atril = atrils.get(jugadorActual);
        indexsActuals.clear();

        for (String s : posicions) {
            int index = Integer.parseInt(s) - 1;
            if (index >= 0 && index < atril.size())
                indexsActuals.add(index);
        }

        indexsActuals.sort((a, b) -> b - a); //ordenar per evitar problemes de shifting

        // Exchange tiles
        for (int index : indexsActuals){
            Fitxa f = atril.remove(index);
            bossa.retornarFitxa(f);
        }

        omplirAtril(jugadorActual);
        return true;
    }

    /**
     * Actualitza la puntuacio del jugador actual.
     * @param puntuacio la puntuacio a afegir a la puntuacio actual del jugador.
     */
    public void actualitzaPuntuacio(int puntuacio)
    {
        puntuacioJugadors.set(jugadorActual, puntuacioJugadors.get(jugadorActual) + puntuacio);
    }

    public void setNoGuardada()
    {
        partidaPausada = false;
    }

    /**
     * Realitza una jugada al taulell.
     * @param jugades llista de jugades a jugar. Cada jugada es un objecte Fitxa amb la seva posicio al taulell. La clau representa la posicio i el valor la fitxa.
     * @param across String que representa si la jugada es horitzontal o vertical. "H" per horitzontal i "V" per vertical.
     * @return la puntuacio de la jugada. Si la jugada no es valida, retorna -1.
     */
    public int jugarParaula(LinkedHashMap<int[], Fitxa> jugades, String across)
    {
        if (jugades.isEmpty())
            return -1;
//            throw new IllegalArgumentException("No hi ha fitxes per jugar.");

        across = across.toUpperCase();
        if (!across.equals("H") && !across.equals("V"))
            return -1;
//            throw new IllegalArgumentException("La orientacio ha de ser H o V");

        if (!taulell.verificarFitxes(jugades, across.equals("H")))
            return -1;
//            throw new IllegalArgumentException("No es pot posar la paraula al taulell en la ubicacio solicitada.");

        //1.5 calcular palabras nuevas (list<list<fitxa>>)
        int puntuacio =  taulell.validesaYPuntuacioJugada(jugades, diccionari, across.equals("H"), true);


        if (puntuacio == -1)
            return -1;
            //throw new IllegalArgumentException("La/es paraula/es formada/es no es troba/en al diccionari.");
        //2 verificar que las palabras formadas existen
        //3 calcular la puntuacion total
        puntuacioJugadors.set(jugadorActual, puntuacioJugadors.get(jugadorActual) + puntuacio);
        // Remove used tiles from the player's rack
        List<Fitxa> atril = atrils.get(jugadorActual);
        for (Fitxa fitxa : jugades.values()) {
            int index = atril.indexOf(fitxa);
            if (index != -1) {
                atril.remove(index);
            }
        }
        completarAtril();
        passarTorn();
        return puntuacio;
    }

    /**
     * Realitza una jugada al taulell d'acord amb la millor jugada que pugui fer el bot.
     * @param bot objecte Usuari que representa el bot que realitza la jugada.
     * @return true si s'ha pogut realitzar la jugada, false si no s'ha pogut.
     */
    public boolean getMillorJugada(Usuari bot)
    {
        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> resultat =  ((Bot)bot).getMillorJugada(taulell, diccionari, atrils.get(jugadorActual), bossa.getAlfabet());
        LinkedHashMap<int[], Fitxa> jugades = resultat.getKey();
        Boolean across = resultat.getValue();
        String orientacio = across ? "H" : "V";
        if (jugades.isEmpty())
            return false;

        jugarParaula(jugades, orientacio);
        return true;
    }
}
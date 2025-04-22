package Domini;

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

    /**
     * Obté el nom de la partida.
     * @return nom de la partida.
     */
    public String getNom()
    {
        return nom;
    }

    /**
     * Estableix una bossa de fitxes per la partida.
     * @param bossa objecte Bossa que es vol assignar a la partida.
     */
    public void setBossa(Bossa bossa)
    {
        this.bossa = bossa;
    }

    /**
     * Obté la bossa de fitxes de la partida.
     * @return objecte Bossa amb les fitxes disponibles.
     */
    public Bossa getBossa()
    {
        return bossa;
    }

    /**
     * Passa el torn al següent jugador.
     * @return true si el torn s'ha passat correctament.
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
                indexGuanyador = -1;
            }
        }

        if (indexGuanyador == -1) 
            return null;
        return jugadors.get(indexGuanyador);
    }

    /**
     * Obté l'usuari que té el torn actual.
     * @return Usuari que té el torn actualment.
     */
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

    /**
     * Obté el taulell de la partida.
     * @return objecte Taulell de la partida.
     */
    public Taulell getTaulell() {
        return taulell;
    }

    /**
     * Obté la llista de jugadors de la partida.
     * @return llista d'objectes Usuari que participen a la partida.
     */
    public List<Usuari> getJugadors() {
        return jugadors;
    }

    /**
     * Obté les puntuacions de tots els jugadors.
     * @return llista amb les puntuacions de cada jugador.
     */
    public List<Integer> getPuntuacions() {
        return puntuacioJugadors;
    }

    /**
     * Obté l'atril del jugador actual.
     * @return llista de fitxes de l'atril del jugador.
     */
    public List<Fitxa> getAtril(){
        return atrils.get(jugadorActual);
    }

    /**
     * Indica si la partida està pausada.
     * @return true si la partida està pausada, false en cas contrari.
     */
    public boolean getPartidaPausada() {
        return partidaPausada;
    }

    /**
     * Marca la partida com a guardada i pausada.
     */
    public void guardarPartida() {
        partidaPausada = true;
    }

    /**
     * Indica si la partida ja ha finalitzat.
     * @return true si la partida ha finalitzat, false en cas contrari.
     */
    public boolean getPartidaAcabada() {
        return partidaAcabada;
    }

    /**
     * Marca la partida com a finalitzada.
     */
    public void acabarPartida() {
        partidaAcabada = true;
    }

    /**
     * Obté els índexs de les fitxes seleccionades per canviar.
     * @return llista amb els índexs de les fitxes a canviar.
     */
    public List<Integer> getIndexsActuals()
    {
        return indexsActuals;
    }

    /**
     * Obté l'idioma de la partida.
     * @return string que representa l'idioma de la partida.
     */
    public String getIdioma()
    {
        return idioma;
    }

    /**
     * Obté els atrils de tots els jugadors.
     * @return llista amb els atrils de tots els jugadors.
     */
    public List<List<Fitxa>> getAtrils()
    {
        return atrils;
    }

    /**
     * Estableix la puntuació d'un jugador específic.
     * @param puntuacio nova puntuació a establir.
     * @param index índex del jugador a qui s'assigna la puntuació.
     */
    public void setPuntuacio(int puntuacio, int index) 
    {
        puntuacioJugadors.set(index, puntuacio);
    }

    /**
     * Verifica si una paraula existeix al diccionari.
     * @param paraula paraula a verificar.
     * @return true si la paraula existeix al diccionari, false en cas contrari.
     */
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

        indexsActuals.sort((a, b) -> b - a);

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

    /**
     * Marca la partida com a no guardada.
     */
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

        across = across.toUpperCase();
        if (!across.equals("H") && !across.equals("V"))
            return -1;

        if (!taulell.verificarFitxes(jugades, across.equals("H")))
            return -1;

        int puntuacio =  taulell.validesaYPuntuacioJugada(jugades, diccionari, across.equals("H"), true);

        if (puntuacio == -1)
            return -1;

        puntuacioJugadors.set(jugadorActual, puntuacioJugadors.get(jugadorActual) + puntuacio);
        
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
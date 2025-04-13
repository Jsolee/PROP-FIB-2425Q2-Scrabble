package Main;

import java.util.*;

public class Partida {
    private String nom;
    private Taulell taulell;
    private List<Usuari> jugadors;
    private int jugadorActual;
    private Bossa bossa;
    private boolean partidaAcabada;
    private int timeout;
    private List<Fitxa> fitxesActuals;
    private List<int[]> posicionsActuals; //posicions de les fitxes colocades al taulell en cada torn
    private List<List<Fitxa>> atrils;
    private List<Integer> puntuacioJugadors;
    private String idioma;
    private boolean partidaPausada;

    public Partida(int timeout, String nom, String idioma) {
        this.timeout = timeout;
        this.nom = nom;
        this.taulell = new Taulell();
        this.jugadors = new ArrayList<>();
        this.jugadorActual = 0;
        this.bossa = new Bossa(idioma);
        this.partidaAcabada = false;
        this.fitxesActuals = new ArrayList<>();
        this.posicionsActuals = new ArrayList<>();
        this.atrils = new ArrayList<>();
        this.puntuacioJugadors = new ArrayList<>();
        this.idioma = idioma;
        this.partidaPausada = false;
    }

    public void afegirJugador(Usuari jugador) 
    {
        jugadors.add(jugador);
        // Repartir fitxes inicials
        omplirAtril(-1);
        puntuacioJugadors.add(0);
    
        // Add game to player's active games
        jugador.getPartidesEnCurs().add(this);
    }

    private void omplirAtril(int index) 
    {
        if (index < 0) //if per a quan necessitem crear un atril a l'inici
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


    private void eliminarFitxa(Fitxa fitxa) 
    {
        List<Fitxa> atril = atrils.get(jugadorActual);
        atril.remove(fitxa);
    }

    public void setBossa(Bossa bossa) 
    {
        this.bossa = bossa;
    }

    public Bossa getBossa() 
    {
        return bossa;
    }

    private void incrementarPuntuacio(int punts) 
    {
        int puntuacio = puntuacioJugadors.get(jugadorActual);
        puntuacio += punts;
        puntuacioJugadors.set(jugadorActual, puntuacio);
        //int puntuacioActual = puntuacions.get(jugador);
        //puntuacions.put(jugador, puntuacioActual + punts);
    }

    public boolean colocarFitxa(int x, int y, Fitxa fitxa) 
    {
        // Verify player has this tile
        /*if (!fitxesJugadors.get(getJugadorActual()).contains(fitxa)) {
            return false;
        }*/

        /*if (jugadorActual == 0) 
        {
            if (!atrilJugador1.contains(fitxa)) 
                return false;
        } 
        else 
        {
            if (!atrilJugador2.contains(fitxa)) 
                return false;
        }*/
        List<Fitxa> atril = atrils.get(jugadorActual);
        if (!atril.contains(fitxa)) 
            return false;

        if (taulell.colocarFitxa(x, y, fitxa)) 
        {
            fitxesActuals.add(fitxa);
            posicionsActuals.add(new int[]{x, y});
            return true;
        }
        return false;
    }

    public boolean confirmarJugada() 
    {
        if (fitxesActuals.isEmpty())
            return false;

        int puntuacioJugada = taulell.calcularPuntuacioMoviment(fitxesActuals, posicionsActuals);
        incrementarPuntuacio(puntuacioJugada);
        List<Fitxa> atril = atrils.get(jugadorActual);

        for (Fitxa f : fitxesActuals) 
            atril.remove(f);

        omplirAtril(jugadorActual);

        // Actualitzar estadístiques del jugador si no es un bot
        Usuari jugadorActiu = getJugadorActual();
        if (jugadorActiu instanceof Persona) 
        {
            jugadorActiu.getEstadistiques().incrementarParaulesCreades();
            jugadorActiu.getEstadistiques().incrementarPuntTotal(puntuacioJugada);
        }

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

    public boolean intercanviarFitxes(List<Fitxa> fitxes) 
    {
        if (bossa.getQuantitatFitxes() < fitxes.size())
            return false;

        // Verify player has all these tiles
        for (Fitxa fitxa : fitxes) 
        {
            if (!conteLaFitxa(fitxa))
                return false;
        }

        for (Fitxa fitxa : fitxes) 
        {
            eliminarFitxa(fitxa);
            bossa.retornarFitxa(fitxa);
        }

        omplirAtril(jugadorActual);

        passarTorn();
        return true;
    }

    private boolean conteLaFitxa(Fitxa f)
    {
        List<Fitxa> atril = atrils.get(jugadorActual);
        for (Fitxa fitxa : atril) 
        {
            if (fitxa.equals(f)) 
                return true;
        }
        return false;
    }

    private void comprovarFinalPartida() 
    {
        // La partida acaba quan la bossa està buida i algun jugador s'ha quedat sense fitxes
        if (bossa.esBuida()) 
        {
            /*if (atrilJugador1.isEmpty() || atrilJugador2.isEmpty()) 
            {
                partidaAcabada = true;
                actualitzarEstadistiques();
            }*/
            for (List<Fitxa> atril : atrils) 
            {
                if (atril.isEmpty()) 
                {
                    partidaAcabada = true;
                    actualitzarEstadistiques();
                    break;
                }
            }
            /*for (int i = 0; i < jugadors.size(); i++) 
            {
                if (fitxesJugadors.get(jugador).isEmpty()) 
                {
                    partidaAcabada = true;
                    actualitzarEstadistiques();
                    break;
                }
    
            }*/
        }
    }

    private void actualitzarEstadistiques() 
    {
        Usuari guanyador = determinarGuanyador();

        // Update winner's statistics
        if (guanyador != null)
            guanyador.getEstadistiques().incrementarPartidesGuanyades();

        // Update all players' statistics
        for (int i = 0; i < jugadors.size();++i) 
        {
            Usuari jugador = jugadors.get(i);
            jugador.getEstadistiques().incrementarPartidesJugades();
            jugador.borrarPartidaEnCurs(this);
            jugador.getEstadistiques().incrementarPuntTotal(puntuacioJugadors.get(i));
        }
    }

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
        }

        return jugadors.get(indexGuanyador);
    }

    public boolean isPartidaAcabada() {
        return partidaAcabada;
    }

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

    public int getTimeout() {
        return timeout;
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
}
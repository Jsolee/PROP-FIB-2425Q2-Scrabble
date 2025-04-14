package Main;

import java.util.*;

public class Partida {
    private String nom;
    private Taulell taulell;
    private List<Usuari> jugadors;
    private int jugadorActual;
    private Bossa bossa;
    private boolean partidaAcabada;
    private List<Fitxa> fitxesActuals;
    private List<int[]> posicionsActuals; //posicions de les fitxes colocades al taulell en cada torn
    private List<Integer> indexsActuals;
    private List<List<Fitxa>> atrils;
    private List<Integer> puntuacioJugadors;
    private String idioma;
    private boolean partidaPausada;
    private Diccionari diccionari;

    public Partida(String nom, String idioma) {
        this.nom = nom;
        this.taulell = new Taulell();
        this.jugadors = new ArrayList<>();
        this.jugadorActual = 0;
        this.bossa = new Bossa(idioma);
        this.partidaAcabada = false;
        this.fitxesActuals = new ArrayList<>();
        this.posicionsActuals = new ArrayList<>();
        this.indexsActuals = new ArrayList<>();
        this.atrils = new ArrayList<>();
        this.puntuacioJugadors = new ArrayList<>();
        this.idioma = idioma;
        this.partidaPausada = false;
        this.diccionari = new Diccionari(idioma);
    }

    public void afegirJugador(Usuari jugador) 
    {
        jugadors.add(jugador);
        // Repartir fitxes inicials
        omplirAtril(-1);
        puntuacioJugadors.add(0);
    
        // Add game to player's active games
        ((Persona)jugador).getPartidesEnCurs().add(this);
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


    public void setBossa(Bossa bossa) 
    {
        this.bossa = bossa;
    }

    public Bossa getBossa() 
    {
        return bossa;
    }


    public boolean colocarFitxa(int x, int y, Fitxa fitxa) 
    {
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



    public boolean passarTorn() {
        jugadorActual = (jugadorActual + 1) % jugadors.size();
        return true;
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

    public boolean paraulaEnAtril(String paraula) 
    {
        boolean valida = true;
        List<Fitxa> atril = atrils.get(jugadorActual);
        indexsActuals.clear();
        fitxesActuals.clear();
        posicionsActuals.clear();

        for (char c : paraula.toCharArray()) 
        {
            boolean found = false;
            for (int i = 0; i < atril.size(); i++) 
            {
                if (!indexsActuals.contains(i) && (atril.get(i).getLletra().equals(String.valueOf(c)) || atril.get(i).getLletra().equals("#")))
                {
                    fitxesActuals.add(atril.get(i));
                    indexsActuals.add(i);
                    found = true;
                    break;
                }
            }

            if (!found) 
            {
                valida = false;
                break;
            }
        }

        if (!valida) 
        {
            System.out.println("You don't have the necessary tiles to form this word.");
            return false;
        }
        else return true;
    }

    public boolean existeixParaula(String paraula) 
    {
        return diccionari.esParaula(paraula);
    }

    public boolean validaEnTaulell(String paraula, int f, int col, String orientacio) 
    {
        boolean placed = true;

        for (int i = 0; i < paraula.length(); i++) 
        {
            int r = f;
            int c = col;
            if (orientacio.equals("H")) 
                c += i;
            else 
                r += i;

            Fitxa fitxa = fitxesActuals.get(i);
            if (fitxa.getLletra().equals("#")) 
            {
                fitxa.setLletra(String.valueOf(paraula.charAt(i)));
            } //canvi de valor si es un comodin

            else if (!fitxa.getLletra().equals(String.valueOf(paraula.charAt(i)))) 
            {
                placed = false;
                break;
            }
            if (!taulell.colocarFitxa(r, c, fitxa)) {
                placed = false;
                break;
            }
            posicionsActuals.add(new int[]{r, c});
        }

        return placed;
    }

    public void retiraFitxesAtril()
    {
        List<Fitxa> atril = atrils.get(jugadorActual);
        indexsActuals.sort(Collections.reverseOrder());
        for (int idx : indexsActuals)
            atril.remove(idx);
    }

    public int calculaPuntuacioJugada()
    {
        // Calculate score
        return taulell.calcularPuntuacioMoviment(fitxesActuals, posicionsActuals);
    }

    public void completarAtril() 
    {
        omplirAtril(jugadorActual);
    }

    public void retiraFitxesJugades()
    {
        for (int[] pos : posicionsActuals) {
            taulell.retirarFitxa(pos[0], pos[1]);
        }
    }

    public boolean canviFitxesAtril(String [] posicions)
    {
        List<Fitxa> atril = atrils.get(jugadorActual);
        indexsActuals.clear();
        posicionsActuals.clear();
        fitxesActuals.clear();

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
}
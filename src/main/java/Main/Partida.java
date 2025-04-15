package Main;

import java.util.*;

public class Partida {
    private final String nom;
    private final Taulell taulell;
    private final Jugador jugador1;
    private final Jugador jugador2;
    private Jugador jugadorActual;
    private boolean partidaAcabada;
    private boolean partidaPausada;
    private final Bossa bossa;
    private final Diccionari diccionari;

    public Partida(String nom, String nomDiccionari, Usuari usuari1, Usuari usuari2) {
        this.nom = nom;
        this.taulell = new Taulell();
        this.partidaAcabada = false;
        this.partidaPausada = false;
        this.bossa = new Bossa(nomDiccionari);
        this.diccionari = new Diccionari(nomDiccionari);

        this.jugador1 = new Jugador(usuari1, bossa.agafarFitxes(7));
        this.jugador2 = new Jugador(usuari2, bossa.agafarFitxes(7));
        this.jugadorActual = jugador1;
    }


    // Getters
    public String getNom() {
        return nom;
    }
    public Taulell getTaulell() {
        return taulell;
    }
    public Usuari getJugador1() {
        return jugador1.getUsuari();
    }
    public Usuari getJugador2() {
        return jugador2.getUsuari();
    }
    public Usuari getJugadorActual() {
        return jugadorActual.getUsuari();
    }
    public List<Fitxa> getAtrilJugador(Usuari usuari) {
        if (usuari == jugador1.getUsuari()) {
            return jugador1.getAtril();
        } else if (usuari == jugador2.getUsuari()) {
            return jugador2.getAtril();
        } else {
            throw new IllegalArgumentException("Usuari no trobat");
        }
    }
    public int getPuntuacioJugador(Usuari usuari) {
        if (usuari == jugador1.getUsuari()) {
            return jugador1.getPuntuacio();
        } else if (usuari == jugador2.getUsuari()) {
            return jugador2.getPuntuacio();
        } else {
            throw new IllegalArgumentException("Usuari no trobat");
        }
    }
    public Jugada getJugadaJugador(Usuari usuari) {
        if (usuari == jugador1.getUsuari()) {
            return jugador1.jugada;
        } else if (usuari == jugador2.getUsuari()) {
            return jugador2.jugada;
        } else {
            throw new IllegalArgumentException("Usuari no trobat");
        }
    }

    public boolean isPartidaAcabada() {
        return partidaAcabada;
    }
    public boolean isPartidaPausada() {
        return partidaPausada;
    }
    public Bossa getBossa() {
        return bossa;
    }
    public Diccionari getDiccionari() {
        return diccionari;
    }

    // Setters
    public void setPartidaAcabada(boolean partidaAcabada) {
        this.partidaAcabada = partidaAcabada;
    }
    public void setPartidaPausada(boolean partidaPausada) {
        this.partidaPausada = partidaPausada;
    }


    public void omplirAtril() {
        int fitxesNecesaries = jugadorActual.fitxesNecesaries();
        jugadorActual.afegirFitxesAlAtril(bossa.agafarFitxes(fitxesNecesaries));
    }

    public boolean afegirFitxaJugada(Posicio posicio, Fitxa fitxa) {
        jugadorActual.
    }



    public void canviTorn() {
        // si el jugador te fitxes a la jugada, les col·loca de volta el seu atril ja que ha canviat el torn sense jugar
        if (!jugadorActual.jugada.getFitxes().isEmpty()) {
            TreeMap<Posicio, Fitxa> fitxesJugada = jugadorActual.jugada.getFitxes();
            for (Map.Entry<Posicio, Fitxa> entry : fitxesJugada.entrySet()) {
                Fitxa fitxa = entry.getValue();
                jugadorActual.afegirFitxesAlAtril();
            }
        } else {
            jugadorActual.jugada = new Jugada();
        }

        if (jugadorActual == jugador1) {
            jugadorActual = jugador2;
        } else {
            jugadorActual = jugador1;
        }
    }

    public Usuari guanyador()
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

    private class Jugador {
        private final Usuari usuari;
        private int puntuacio;
        private List<Fitxa> atril;
        private Jugada jugada;

        public Jugador(Usuari usuari) {
            this.usuari = usuari;
            this.puntuacio = 0;
            this.atril = new ArrayList<Fitxa>();
            this.jugada = new Jugada();
        }
        // contructor amb atril
        public Jugador(Usuari usuari, ArrayList<Fitxa> atril) {
            this.usuari = usuari;
            this.puntuacio = 0;
            this.atril = atril;
            this.jugada = new Jugada();
        }

        //getters i setters
        public Usuari getUsuari() {
            return usuari;
        }
        public int getPuntuacio() {
            return puntuacio;
        }
        public List<Fitxa> getAtril() {
            return atril;
        }
        public Jugada getJugada() {
            return jugada;
        }


        public void setPuntuacio(int puntuacio) {
            this.puntuacio = puntuacio;
        }
        public void setAtril(List<Fitxa> atril) {
            this.atril = atril;
        }
        public void setJugada(Jugada jugada) {
            this.jugada = jugada;
        }

        public int fitxesNecesaries() {
            // com un atril ha de tenir 7 fitxes:
            return 7 - atril.size();
        }

        public void afegirFitxa(Fitxa fitxa) {
            if (atril.size() > 7)
                throw new IllegalArgumentException("Atril ple");
            atril.add(fitxa);
        }
        public void afegirFitxesAlAtril(List<Fitxa> fitxes) {
            for (Fitxa fitxa : fitxes)
            {
                if (atril.size() > 7)
                    throw new IllegalArgumentException("Atril ple");
                atril.add(fitxa);
            }
        }

        public void afegirFitxesJugada(List<Fitxa> fitxes) {

        }
    }

}
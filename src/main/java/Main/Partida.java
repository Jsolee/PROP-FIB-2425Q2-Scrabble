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
        if (jugador instanceof Persona)
        {
            Persona persona = (Persona) jugador;
            persona.getPartidesEnCurs().add(this);

        }
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

    public List<int[]> getPosicionsActuals() {
        return posicionsActuals;
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

    public List<Fitxa> getFitxesActuals() {
        return fitxesActuals;
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

    public boolean paraulaEnAtril(String paraula) {
        boolean valida = true;
        List<Fitxa> atril = atrils.get(jugadorActual);
        indexsActuals.clear();
        fitxesActuals.clear();
        posicionsActuals.clear();
        paraula = paraula.toUpperCase(); // Normalizar a mayúsculas
    
        List<String> possiblesDigrafs = new ArrayList<>();
    
        if (idioma.equals("catalan")) {
            possiblesDigrafs.add("NY");
            possiblesDigrafs.add("L·L");
        } 
        else if (idioma.equals("castellano")) {
            possiblesDigrafs.add("CH");
            possiblesDigrafs.add("LL");
            possiblesDigrafs.add("RR");
        }
    
        for (int i = 0; i < paraula.length(); i++) {
            boolean found = false;
            char a = paraula.charAt(i);
            String c = String.valueOf(a);
            
            // Buscar posibles dígrafos
            if (idioma.equals("catalan")) {
                if (c.equals("N") && i + 1 < paraula.length()) {
                    String nextChar = String.valueOf(paraula.charAt(i + 1));
                    if (nextChar.equals("Y") && possiblesDigrafs.contains("NY")) {
                        c = "NY"; // Usamos el dígrafo completo
                        i++; // Avanzamos para saltar la 'Y'
                    }
                } 
                else if (c.equals("L") && i + 2 < paraula.length()) {
                    String nextChar = String.valueOf(paraula.charAt(i + 1));
                    String nextChar2 = String.valueOf(paraula.charAt(i + 2));
                    if (nextChar.equals("·") && nextChar2.equals("L") && 
                        possiblesDigrafs.contains("L·L")) {
                        c = "L·L"; // Usamos el dígrafo completo
                        i += 2; // Avanzamos para saltar '·' y 'L'
                    }
                }
            } 
            else if (idioma.equals("castellano")) {
                if (c.equals("C") && i + 1 < paraula.length()) {
                    String nextChar = String.valueOf(paraula.charAt(i + 1));
                    if (nextChar.equals("H") && possiblesDigrafs.contains("CH")) {
                        c = "CH";
                        i++;
                    }
                } 
                else if (c.equals("L") && i + 1 < paraula.length()) {
                    String nextChar = String.valueOf(paraula.charAt(i + 1));
                    if (nextChar.equals("L") && possiblesDigrafs.contains("LL")) {
                        c = "LL";
                        i++;
                    }
                } 
                else if (c.equals("R") && i + 1 < paraula.length()) {
                    String nextChar = String.valueOf(paraula.charAt(i + 1));
                    if (nextChar.equals("R") && possiblesDigrafs.contains("RR")) {
                        c = "RR";
                        i++;
                    }
                }
            }
    
            // Buscar la letra o dígrafo en el atril
            for (int j = 0; j < atril.size(); j++) {
                if (!indexsActuals.contains(j)) {
                    Fitxa fitxa = atril.get(j);
                    
                    if (fitxa.getLletra().equals(c) || fitxa.getLletra().equals("#")) {
                        fitxesActuals.add(fitxa);
                        indexsActuals.add(j);
                        found = true;
                        break;
                    }
                }
            }
    
            // Si no se encontró la letra o dígrafo
            if (!found) {
                valida = false;
                break;
            }
        }
    
        return valida;
    }

    public boolean existeixParaula(String paraula) 
    {
        return diccionari.esParaula(paraula);
    }

    public boolean validaEnTaulell(String paraula, int f, int col, String orientacio) 
    {
        boolean placed = true;
        List<Fitxa> comodins = new ArrayList<>();
        
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
                comodins.add(fitxa);
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

        if (!placed)
        {
            for (Fitxa c : comodins)
                c.setLletra("#");
            retiraFitxesJugades();
        }
        return placed;
    }

    public void retiraFitxaAtril(List<Integer> indexs)
    {
        List<Fitxa> atril = atrils.get(jugadorActual);
        for (int idx : indexs)
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

    public void actualitzaPuntuacio(int puntuacio)
    {
        puntuacioJugadors.set(jugadorActual, puntuacioJugadors.get(jugadorActual) + puntuacio);
    }

    public void setNoGuardada()
    {
        partidaPausada = false;
    }

    /*-------------------------------------------------------------*/

    public int jugarParaula(LinkedHashMap<int[], Fitxa> jugades, String across)
    {
        //las fichas ya estan en el atril (paso 0)
        //1 verificar que es pot posar al taulell (funcion en el tablero)
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

    //retorna true si posa una paraula al taulell, false si no (ha de demanar fitxes)
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
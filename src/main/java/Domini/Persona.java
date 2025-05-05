package Domini;

import java.util.ArrayList;

/**
 * Classe que representa un usuari humà del joc d'Scrabble.
 * Hereta d'Usuari i afegeix informació personal, credencials, estadístiques i
 * gestió de les partides en curs.
 */
public class Persona extends Usuari {
    /** Correu electrònic de l'usuari */
    private String correu;
    /** Contrasenya de l'usuari */
    private String contrasenya;
    /** Edat de l'usuari */
    private String edat;
    /** País de residència de l'usuari */
    private String pais;
    /** Estadístiques de joc de l'usuari */
    private Estadistiques estadistiques;
    /** Indica si l'usuari té la sessió iniciada */
    private boolean SessioIniciada;
    /** Llista de partides en curs de l'usuari */
    private ArrayList<Partida> partidesEnCurs;

    /**
     * Constructor per defecte per GSON
     */
    public Persona(){}

    /**
     * Constructor que crea un nou usuari amb les dades especificades.
     * 
     * @param nom nom d'usuari
     * @param correu correu electrònic
     * @param contrasenya contrasenya per autenticar-se
     * @param edat edat de l'usuari
     * @param pais país de residència
     */
    public Persona(String nom, String correu, String contrasenya, String edat, String pais) {
        super(nom);
        this.SessioIniciada = true;
        this.partidesEnCurs = new ArrayList<>();
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.edat = edat;
        this.pais = pais;
        this.estadistiques = new Estadistiques();
    }

    /**
     * Obté la llista de partides en curs de l'usuari.
     * 
     * @return llista de partides en curs
     */
    public ArrayList<Partida> getPartidesEnCurs() {
        return this.partidesEnCurs;
    }

    /**
     * Elimina una partida de la llista de partides en curs de l'usuari.
     * 
     * @param partida partida a eliminar
     */
    public void borrarPartidaEnCurs(Partida partida) {
        this.partidesEnCurs.remove(partida);
    }

    /**
     * Comprova si l'usuari té la sessió iniciada.
     * 
     * @return true si la sessió està iniciada, false en cas contrari
     */
    public boolean teSessioIniciada() {
        return this.SessioIniciada;
    }

    /**
     * Tanca la sessió de l'usuari.
     */
    public void tancarSessio() {
        this.SessioIniciada = false;
    }

    /**
     * Inicia la sessió de l'usuari.
     */
    public void iniciarSessio() {
        this.SessioIniciada = true;
    }

    /**
     * Obté el correu electrònic de l'usuari.
     * 
     * @return correu electrònic
     */
    public String getCorreu() {
        return this.correu;
    }

    /**
     * Obté la contrasenya de l'usuari.
     * 
     * @return contrasenya
     */
    public String getContrasenya() {
        return this.contrasenya;
    }

    /**
     * Obté les estadístiques de joc de l'usuari.
     * 
     * @return objecte Estadistiques amb totes les estadístiques de l'usuari
     */
    public Estadistiques getEstadistiques() {
        return this.estadistiques;
    }

    /**
     * Obté l'edat de l'usuari.
     * 
     * @return edat en format string
     */
    public String getEdat() {
        return this.edat;
    }

    /**
     * Obté el país de residència de l'usuari.
     * 
     * @return país
     */
    public String getPais() {
        return this.pais;
    }

    /**
     * Modifica la contrasenya de l'usuari.
     * 
     * @param contrasenya nova contrasenya
     * @return true si s'ha canviat correctament
     */
    public boolean setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
        return true;
    }

    /**
     * Obté el valor específic d'una estadística segons el tipus indicat.
     * 
     * @param num tipus d'estadística a obtenir:
     *            1 - Puntuació total
     *            2 - Partides jugades
     *            3 - Partides guanyades
     *            4 - Rècord personal
     *            5 - Paraules totals
     * @return valor de l'estadística sol·licitada, o -1 si el tipus no és vàlid
     */
    public int getValorEstaditiques(int num) {
        switch (num) {
            case 1:
                return estadistiques.getPuntTotal();
            case 2:
                return estadistiques.getPartidesJugades();
            case 3:
                return estadistiques.getPartidesGuanyades();
            case 4:
                return estadistiques.getRecordPersonal();
            case 5:
                return estadistiques.getParaulesTotals();
            default:
                return -1;
        }
    }
}
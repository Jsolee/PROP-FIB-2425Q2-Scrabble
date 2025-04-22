package Main;

/**
 * Classe abstracta que representa un usuari del joc d'Scrabble.
 * Serveix com a base per als diferents tipus d'usuaris del joc (Persona, Bot).
 * Proporciona funcionalitats bàsiques com obtenir i modificar el nom d'usuari.
 */
public abstract class Usuari {
    /** Nom d'usuari que identifica l'usuari */
    private String username;

    /**
     * Constructor que crea un nou usuari amb el nom especificat.
     * 
     * @param nom nom d'usuari
     */
    public Usuari(String nom) {
        this.username = nom;
    }

    /**
     * Obté el nom d'usuari.
     * 
     * @return nom d'usuari
     */
    public String getNom() {
        return username;
    }

    /**
     * Modifica el nom d'usuari.
     * 
     * @param nom nou nom d'usuari
     */
    public void setNom(String nom) {
        this.username = nom;
    }
}
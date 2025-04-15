package Main;

import java.util.ArrayList;

public abstract class Usuari {
    private String nom;
    private Ranking ranking;
    private boolean SessioIniciada;
    private Estadistiques estadistiques;

    public Usuari(String nom) {
        this.nom = nom;
        this.SessioIniciada = true;
        this.ranking = new Ranking();
        this.estadistiques = new Estadistiques();
    }

    //getters
    public String getNom() {
        return this.nom;
    }
    
    public Ranking getRanking() {
        return this.ranking;
    }

    public boolean teSessioIniciada() {
        return this.SessioIniciada;
    }

    public void tancarSessio() {
        this.SessioIniciada = false;
    }

    public void iniciarSessio() {
        this.SessioIniciada = true;
    }

    public Estadistiques getEstadistiques() {
        return estadistiques;
    }

}

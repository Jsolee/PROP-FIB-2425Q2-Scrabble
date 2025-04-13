package Main;

import java.util.ArrayList;

public abstract class Usuari {
    private String nom;
    private ArrayList<Partida> partidesEnCurs ;
    private Ranking ranking;
    private boolean SessioIniciada;
    private Estadistiques estadistiques;

    public Usuari(String nom) {
        this.nom = nom;
        this.SessioIniciada = true;
        this.partidesEnCurs = new ArrayList<>();
        this.ranking = new Ranking();
        this.estadistiques = new Estadistiques();
    }

    //getters
    public String getNom() {
        return this.nom;
    }

    public ArrayList<Partida> getPartidesEnCurs() {
        return this.partidesEnCurs;
    }
    
    public Ranking getRanking() {
        return this.ranking;
    }
    
    public void borrarPartidaEnCurs(Partida partida) {
        this.partidesEnCurs.remove(partida);
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

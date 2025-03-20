package Main;

import java.util.ArrayList;

public class Usuari {
    private String nom;
    private ArrayList<Partida> partidesEnCurs = new ArrayList<>();
    private Ranking ranking = new Ranking();
    private boolean SessioIniciada;

    public Usuari(String nom) {
        this.nom = nom;
        this.SessioIniciada = true;
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


}

package Main;

import java.util.ArrayList;

public class Usuari {
    private String nom;
    private ArrayList<Partida> partidesEnCurs = new ArrayList<>();
    private ArrayList<Partida> partidesTotal = new ArrayList<>();
    private Ranking ranking;

    public Usuari(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }





}

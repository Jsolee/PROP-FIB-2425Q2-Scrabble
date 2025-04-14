package Main;

import java.util.ArrayList;

public abstract class Usuari {
    private String nom;

    public Usuari(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

}

package Main;


public abstract class Usuari {
    private String username;

    public Usuari(String nom) {
        this.username = nom;
    }

    public String getNom() {
        return username;
    }

    public void setNom(String nom) {
        this.username = nom;
    }

}

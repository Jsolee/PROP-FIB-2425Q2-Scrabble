package Main;

import java.util.ArrayList;

public class Persona extends Usuari {
    private String correu;
    private String contrasenya;
    private Estadistiques estadistiques;
    private boolean SessioIniciada;
    private ArrayList<Partida> partidesEnCurs ;

    public Persona(String nom, String correu, String contrasenya) {
        super(nom);
        this.SessioIniciada = true;
        this.partidesEnCurs = new ArrayList<>();
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.estadistiques = new Estadistiques();
    }

    //getters
    public ArrayList<Partida> getPartidesEnCurs() {
        return this.partidesEnCurs;
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

    public String getCorreu() {
        return this.correu;
    }

    public String getContrasenya() {
        return this.contrasenya;
    }

    public Estadistiques getEstadistiques() {
        return this.estadistiques;
    }

    public boolean setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
        return true;
    }


}

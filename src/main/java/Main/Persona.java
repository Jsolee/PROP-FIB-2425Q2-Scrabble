package Main;

import java.util.ArrayList;

public class Persona extends Usuari {
    private String correu;
    private String contrasenya;
    private String edat;
    private String pais;
    private Estadistiques estadistiques;
    private boolean SessioIniciada;
    private ArrayList<Partida> partidesEnCurs;

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


    public String getEdat() {
        return this.edat;
    }

    public String getPais() {
        return this.pais;
    }

    public boolean setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
        return true;
    }

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

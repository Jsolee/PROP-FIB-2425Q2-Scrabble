package Main;

public class Persona extends Usuari {
    private String correu;
    private String contrasenya;
    private Estadistiques estadistiques;

    public Persona(String nom, String correu, String contrasenya) {
        super(nom);
        this.correu = correu;
        this.contrasenya = contrasenya;
        this.estadistiques = new Estadistiques();
    }

    //getters
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

package Main;

public class Persona extends Usuari {
    private String correu;
    private String contrasenya;

    public Persona(String nom, String correu, String contrasenya) {
        super(nom);
        this.correu = correu;
        this.contrasenya = contrasenya;
    }
}

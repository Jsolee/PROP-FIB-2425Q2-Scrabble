package Main;

public class Casella {
    int x, y;
    int multiplicador_letra;
    int multiplicador_paraula;
    boolean ocupada;

    public Casella(int x, int y, int multiplicador_letra, int multiplicador_paraula) {
        this.x = x;
        this.y = y;
        this.multiplicador_letra = multiplicador_letra;
        this.multiplicador_paraula = multiplicador_paraula;
        this.ocupada = false;
    }

}

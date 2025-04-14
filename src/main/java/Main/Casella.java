package Main;

import java.util.List;

public class Casella {
    private int x, y;
    private int multiplicador_letra;
    private int multiplicador_paraula;
    private boolean ocupada;
    private Fitxa fitxa;
    private boolean esCasellaInicial;

    // parametres que utilitza l'algorisme
    private boolean esAnchor;
    // cross-checks de la casella, s'inicialitzaran quan es fagi l'algorisme
    private List<String> cross_checks;

    public Casella(int x, int y, int multiplicador_letra, int multiplicador_paraula) {
        this.x = x;
        this.y = y;
        this.multiplicador_letra = multiplicador_letra;
        this.multiplicador_paraula = multiplicador_paraula;
        this.ocupada = false;
        this.fitxa = null;
        this.esCasellaInicial = false;
    }

    // Getters and Setters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getMultiplicadorLetra() { return multiplicador_letra; }
    public int getMultiplicadorParaula() { return multiplicador_paraula; }
    public boolean isOcupada() { return ocupada; }
    public Fitxa getFitxa() { return fitxa; }
    public boolean isEsCasellaInicial() { return esCasellaInicial; }

    public void setEsCasellaInicial(boolean esCasellaInicial) {
        this.esCasellaInicial = esCasellaInicial;
    }

    // Methods to check multiplier type
    public boolean isDobleLetra() { return multiplicador_letra == 2; }
    public boolean isTripleLetra() { return multiplicador_letra == 3; }
    public boolean isDobleParaula() { return multiplicador_paraula == 2; }
    public boolean isTripleParaula() { return multiplicador_paraula == 3; }

    // Methods to handle placing and removing tiles
    public boolean colocarFitxa(Fitxa f) {
        if (!ocupada) {
            fitxa = f;
            ocupada = true;
            return true;
        }
        return false;
    }

    public Fitxa retirarFitxa() {
        if (ocupada) {
            Fitxa f = fitxa;
            fitxa = null;
            ocupada = false;
            return f;
        }
        return null;
    }

    @Override
    public String toString() {
        if (ocupada) {
            return "[" + fitxa.getLletra() + " ]";
        } else if (isDobleLetra()) {
            return "[DL]";
        } else if (isTripleLetra()) {
            return "[TL]";
        } else if (isDobleParaula()) {
            return "[DP]";
        } else if (isTripleParaula()) {
            return "[TP]";
        } else if (esCasellaInicial) {
            return "[★ ]";
        } else {
            return "[  ]";
        }
    }
}


package Main;

import java.util.Objects;

public class Fitxa {
    private String lletra;  // Changed from char to String to support special tiles
    private int valor;
    private boolean blank;


    public Fitxa(String lletra, int valor) {
        this.lletra = lletra;
        this.valor = valor;
        this.blank = false;  // Multi-character tiles are never blank
    }

    public String getLletra() {
        return lletra;
    }

    public String getValorLletra() {
        return lletra;
    }

    public int getValor() {
        return valor;
    }

    public boolean isBlank() {
        return (lletra.equals("#"));
    }

    @Override
    public String toString() {
        return blank ? "_" : lletra + "(" + valor + ")";
    }
}
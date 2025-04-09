package Main;

public class Fitxa {
    private String lletres;  // Changed from char to String to support special tiles
    private int valor;
    private boolean blank;

    public Fitxa(char lletra, int valor) {
        this.lletres = String.valueOf(lletra);
        this.valor = valor;
        this.blank = (lletra == ' ');
    }

    public Fitxa(String lletres, int valor) {
        this.lletres = lletres;
        this.valor = valor;
        this.blank = false;  // Multi-character tiles are never blank
    }

    public char getLletra() {
        return !lletres.isEmpty() ? lletres.charAt(0) : ' ';
    }

    public String getValorLletra() {
        return lletres;
    }

    public int getValor() {
        return valor;
    }

    public boolean isBlank() {
        return blank;
    }

    @Override
    public String toString() {
        return blank ? "_" : lletres + "(" + valor + ")";
    }
}
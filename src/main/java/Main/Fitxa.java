package Main;

public class Fitxa {
    private char lletra;
    private int valor;
    private boolean esComodin;

    public Fitxa(char lletra, int valor) {
        this.lletra = lletra;
        this.valor = valor;
        this.esComodin = (lletra == '*' || lletra == ' ');
    }

    // Getters
    public char getLletra() {
        return lletra;
    }

    public int getValor() {
        return valor;
    }

    public boolean esComodin() {
        return esComodin;
    }

    // For blank tiles
    public void assignarLletra(char novaLletra) {
        if (esComodin) {
            this.lletra = novaLletra;
        }
    }

    // Reset blank tile
    public void resetComodin() {
        if (esComodin) {
            this.lletra = '*';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fitxa fitxa = (Fitxa) o;
        return lletra == fitxa.lletra && valor == fitxa.valor;
    }

    @Override
    public String toString() {
        return String.valueOf(lletra) + "(" + valor + ")";
    }
}

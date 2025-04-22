package Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una casella del tauler del joc.
 * Cada casella pot contenir multiplicadors de lletra o paraula,
 * així com una fitxa col·locada sobre ella.
 *
 */
public class Casella {
    /** Coordenada X de la casella al tauler. */
    private int x, y;

    /** Coordenada Y de la casella al tauler. */
    private int multiplicador_letra;

    /** Multiplicador que afecta el valor de la letra. */

    /** Multiplicador que afecta el valor de la palabra completa. */
    private int multiplicador_paraula;

    /** Indica si la casilla está ocupada por una ficha. */
    private boolean ocupada;

    /** Ficha que ocupa la casilla, si existe. */
    private Fitxa fitxa;

    /** Indica si la casilla es la casilla inicial del tablero. */
    private boolean esCasellaInicial;


    /**
     * Constructor para crear una casilla con multiplicadores especificados.
     * @param x coordenada x de la casilla
     * @param y coordenada y de la casilla
     * @param multiplicador_letra multiplicador de letra
     * @param multiplicador_paraula multiplicador de palabra
     */
    public Casella(int x, int y, int multiplicador_letra, int multiplicador_paraula) {
        this.x = x;
        this.y = y;
        this.multiplicador_letra = multiplicador_letra;
        this.multiplicador_paraula = multiplicador_paraula;
        this.ocupada = false;
        this.fitxa = null;
        this.esCasellaInicial = false;
    }

    /** @return coordenada X */
    public int getX() { return x; }

    /** @return coordenada Y */
    public int getY() { return y; }

    /** @return multiplicador de letra */
    public int getMultiplicadorLetra() { return multiplicador_letra; }

    /** @return multiplicador de palabra */
    public int getMultiplicadorParaula() { return multiplicador_paraula; }

    /** @return true si la casilla está ocupada */
    public boolean isOcupada() { return ocupada; }

    /** @return ficha que ocupa la casilla */
    public Fitxa getFitxa() { return fitxa; }

    /** @return true si la casilla es la casilla inicial */
    public boolean isEsCasellaInicial() { return esCasellaInicial; }

    /** Establece la coordenada X de la casilla. */
    public void setX(int x) { this.x = x; }

    /** Establece la coordenada Y de la casilla. */
    public void setY(int y) { this.y = y; }

    /**
     * Define si esta casilla es la casilla inicial.
     * @param esCasellaInicial true si es la casilla inicial, false en caso contrario
     */
    public void setEsCasellaInicial(boolean esCasellaInicial) {
        this.esCasellaInicial = esCasellaInicial;
    }

    /** @return true si la casilla duplica el valor de la letra */
    public boolean isDobleLetra() { return multiplicador_letra == 2; }

    /** @return true si la casilla triplica el valor de la letra */
    public boolean isTripleLetra() { return multiplicador_letra == 3; }

    /** @return true si la casilla duplica el valor de la palabra */
    public boolean isDobleParaula() { return multiplicador_paraula == 2; }

    /** @return true si la casilla triplica el valor de la palabra */
    public boolean isTripleParaula() { return multiplicador_paraula == 3; }


    /**
     * Coloca una ficha en la casilla si está libre.
     * @param f ficha a colocar
     * @return true si se colocó la ficha correctamente, false en caso contrario
     */
    public boolean colocarFitxa(Fitxa f) {
        if (!ocupada) {
            fitxa = f;
            ocupada = true;
            return true;
        }
        return false;
    }

    /**
     * Retira la ficha actual de la casilla, dejándola vacía.
     * @return ficha retirada o null si la casilla estaba vacía
     */
    public Fitxa retirarFitxa() {
        if (ocupada) {
            Fitxa f = fitxa;
            fitxa = null;
            ocupada = false;
            return f;
        }
        return null;
    }


    /**
     * Representación en texto de la casilla para el tablero.
     * @return representación visual de la casilla
     */
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


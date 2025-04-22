package Domini;

/**
 * Classe que representa una fitxa del joc d'Scrabble.
 * Cada fitxa té una lletra associada, un valor numèric i pot ser una fitxa en blanc (comodí).
 */
public class Fitxa {
    /** Lletra que representa la fitxa */
    private String lletra;
    /** Valor numèric de la fitxa per calcular puntuacions */
    private int valor;
    /** Indica si la fitxa és un comodí/blank */
    private boolean blank;

    /**
     * Constructor que crea una nova fitxa amb la lletra i valor especificats.
     * 
     * @param lletra lletra o símbol que apareixerà a la fitxa
     * @param valor valor numèric de la fitxa
     */
    public Fitxa(String lletra, int valor) {
        this.lletra = lletra;
        this.valor = valor;
        this.blank = false;
    }

    /**
     * Obté la lletra de la fitxa.
     * 
     * @return lletra o símbol de la fitxa
     */
    public String getLletra() {
        return lletra;
    }

    /**
     * Obté el valor de lletra de la fitxa.
     * Aquest mètode és equivalent a getLletra().
     * 
     * @return lletra o símbol de la fitxa
     */
    public String getValorLletra() {
        return lletra;
    }

    /**
     * Obté el valor numèric de la fitxa.
     * 
     * @return valor numèric utilitzat pel càlcul de puntuacions
     */
    public int getValor() {
        return valor;
    }

    /**
     * Comprova si la fitxa és un comodí (blank).
     * Les fitxes comodí es representen amb el caràcter "#".
     * 
     * @return true si la fitxa és un comodí, false en cas contrari
     */
    public boolean isBlank() {
        return (lletra.equals("#"));
    }

    /**
     * Canvia la lletra de la fitxa.
     * Utilitzat principalment per assignar una lletra específica a un comodí.
     * 
     * @param lletra nova lletra o símbol a assignar a la fitxa
     */
    public void setLletra(String lletra) {
        this.lletra = lletra;
    }

    /**
     * Converteix la fitxa a una representació en format text.
     * Per fitxes comodí mostra "_"; per la resta, mostra la lletra i el valor entre parèntesis.
     * 
     * @return representació en text de la fitxa
     */
    @Override
    public String toString() {
        return blank ? "_" : lletra + "(" + valor + ")";
    }
}
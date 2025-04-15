package Main;

import java.util.*;

public class Jugada {
    private String paraula;
    // fitxes y posiciones
    TreeMap<Posicio, Fitxa> fitxes;
    private boolean across; // si no es across, sera down.


    public Jugada() {
        this.paraula = "";
        this.fitxes = new TreeMap<Posicio, Fitxa>();
        this.across = true;
    }

    public Jugada(String paraula, TreeMap<Posicio, Fitxa> fitxes, boolean across) {
        this.paraula = paraula;
        this.fitxes = fitxes;
        this.across = across;
    }

    // getters i setters
    public String getParaula() {
        return paraula;
    }
    public TreeMap<Posicio, Fitxa> getFitxes() {
        return fitxes;
    }
    public boolean isAcross() {
        return across;
    }

    public void setParaula(String paraula) {
        this.paraula = paraula;
    }
    public void setFitxes(TreeMap<Posicio, Fitxa> fitxes) {
        this.fitxes = fitxes;
    }
    public void setAcross(boolean across) {
        this.across = across;
    }


    public void afegirFitxa(Fitxa fitxa, Posicio posicio) {
        fitxes.put(posicio, fitxa);
    }

}

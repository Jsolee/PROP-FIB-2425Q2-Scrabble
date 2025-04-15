package Main;

import java.util.*;

public class Jugada {
    private String paraula;
    private List<Fitxa> fitxes;
    private Posicio posicio;
    private boolean across; // si no es across, sera down.

    public Jugada(String paraula, ArrayList<Fitxa> fitxes, Posicio posicio, boolean across) {
        this.paraula = paraula;
        this.fitxes = fitxes;
        this.posicio = posicio;
        this.across = across;
    }

    // getters i setters
    public String getParaula() {
        return paraula;
    }
    public List<Fitxa> getFitxes() {
        return fitxes;
    }
    public Posicio getPosicio() {
        return posicio;
    }
    public boolean isAcross() {
        return across;
    }

    public void setParaula(String paraula) {
        this.paraula = paraula;
    }
    public void setFitxes(ArrayList<Fitxa> fitxes) {
        this.fitxes = fitxes;
    }
    public void setPosicio(Posicio posicio) {
        this.posicio = posicio;
    }
    public void setPosicio(int x, int y) {
        this.posicio = new Posicio(x, y);
    }
    public void setAcross(boolean across) {
        this.across = across;
    }

}

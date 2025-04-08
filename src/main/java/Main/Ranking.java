package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking {
    private List<Integer> puntuacions;
    private int recordPersonal;
    private int partidesJugades;
    private double mitjana;

    public Ranking() {
        puntuacions = new ArrayList<>();
        recordPersonal = 0;
        partidesJugades = 0;
        mitjana = 0.0;
    }

    public void afegirPuntuacio(int punts) {
        puntuacions.add(punts);
        partidesJugades++;

        // Update personal record if this score is higher
        if (punts > recordPersonal) {
            recordPersonal = punts;
        }

        // Recalculate average
        actualitzarMitjana();
    }

    private void actualitzarMitjana() {
        if (puntuacions.isEmpty()) {
            mitjana = 0.0;
            return;
        }

        int sumaPunts = 0;
        for (int punts : puntuacions) {
            sumaPunts += punts;
        }

        mitjana = (double) sumaPunts / puntuacions.size();
    }

    public List<Integer> getUltimsResultats(int quantitat) {
        if (puntuacions.isEmpty()) {
            return Collections.emptyList();
        }

        int inici = Math.max(0, puntuacions.size() - quantitat);
        return new ArrayList<>(puntuacions.subList(inici, puntuacions.size()));
    }

    public int getRecordPersonal() {
        return recordPersonal;
    }

    public int getPartidesJugades() {
        return partidesJugades;
    }

    public double getMitjana() {
        return mitjana;
    }

    public List<Integer> getTotesPuntuacions() {
        return new ArrayList<>(puntuacions);
    }

    @Override
    public String toString() {
        return "Ranking [partidesJugades=" + partidesJugades +
                ", recordPersonal=" + recordPersonal +
                ", mitjana=" + String.format("%.2f", mitjana) + "]";
    }
}
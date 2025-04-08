package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bossa {
    private List<Fitxa> fitxes;

    public Bossa() {
        fitxes = new ArrayList<>();
        inicialitzarFitxes();
    }

    private void inicialitzarFitxes() {
        // Distribution based on Catalan Scrabble
        afegirFitxes('A', 1, 12);
        afegirFitxes('B', 3, 2);
        afegirFitxes('C', 2, 3);
        afegirFitxes('Ç', 10, 1);
        afegirFitxes('D', 2, 3);
        afegirFitxes('E', 1, 13);
        afegirFitxes('F', 4, 1);
        afegirFitxes('G', 2, 2);
        afegirFitxes('H', 8, 1);
        afegirFitxes('I', 1, 8);
        afegirFitxes('J', 8, 1);
        afegirFitxes('L', 1, 4);
        afegirFitxes('M', 2, 3);
        afegirFitxes('N', 1, 6);
        afegirFitxes('O', 1, 5);
        afegirFitxes('P', 3, 2);
        afegirFitxes('Q', 8, 1);
        afegirFitxes('R', 1, 8);
        afegirFitxes('S', 1, 8);
        afegirFitxes('T', 1, 5);
        afegirFitxes('U', 1, 4);
        afegirFitxes('V', 4, 1);
        afegirFitxes('X', 10, 1);
        afegirFitxes('Z', 8, 1);
        afegirFitxes(' ', 0, 2); // Blank tiles

        // Shuffle the bag to randomize tile selection
        barrejar();
    }

    private void afegirFitxes(char lletra, int valor, int quantitat) {
        for (int i = 0; i < quantitat; i++) {
            fitxes.add(new Fitxa(lletra, valor));
        }
    }

    private void afegirFitxes(String lletra, int valor, int quantitat) {
        for (int i = 0; i < quantitat; i++) {
            fitxes.add(new Fitxa(lletra.charAt(0), valor));
        }
    }

    public void barrejar() {
        Collections.shuffle(fitxes);
    }

    public Fitxa agafarFitxa() {
        if (fitxes.isEmpty()) {
            return null;
        }
        return fitxes.remove(0);
    }

    public void retornarFitxa(Fitxa fitxa) {
        fitxes.add(fitxa);
        // Re-shuffle after returning tiles
        barrejar();
    }

    public boolean esBuida() {
        return fitxes.isEmpty();
    }

    public int getQuantitatFitxes() {
        return fitxes.size();
    }

    public Map<Character, Integer> getDistribucioActual() {
        Map<Character, Integer> distribucio = new HashMap<>();
        for (Fitxa fitxa : fitxes) {
            char lletra = fitxa.getLletra();
            distribucio.put(lletra, distribucio.getOrDefault(lletra, 0) + 1);
        }
        return distribucio;
    }

    @Override
    public String toString() {
        return "Bossa [quantitat=" + getQuantitatFitxes() + "]";
    }
}
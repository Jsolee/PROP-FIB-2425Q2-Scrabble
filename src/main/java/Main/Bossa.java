package Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bossa {
    private List<Fitxa> fitxes;
    private String idioma;

    public static final String CATALA = "catala";
    public static final String ESPANYOL = "espanyol";
    public static final String ANGLES = "angles";

    public Bossa() {
        this(CATALA); // Default to Catalan
    }

    public Bossa(String idioma) {
        this.idioma = idioma.toLowerCase();
        this.fitxes = new ArrayList<>();
        inicialitzarFitxes();
    }

    private void inicialitzarFitxes() {
        switch (idioma) {
            case CATALA:
                inicialitzarFitxesCatala();
                break;
            case ESPANYOL:
                inicialitzarFitxesEspanyol();
                break;
            case ANGLES:
                inicialitzarFitxesAngles();
                break;
            default:
                inicialitzarFitxesCatala(); // Default fallback
        }

        // Shuffle the bag to randomize tile selection
        barrejar();
    }

    private void inicialitzarFitxesCatala() {
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
        afegirFitxes("L·L", 10, 1); // Adding L·L as a special tile
        afegirFitxes("LL", 8, 1);   // Adding LL as a special tile
        afegirFitxes('M', 2, 3);
        afegirFitxes('N', 1, 6);
        afegirFitxes("NY", 8, 1);   // Adding NY as a special tile
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
    }

    private void inicialitzarFitxesEspanyol() {
        // Distribution based on Spanish Scrabble
        afegirFitxes('A', 1, 12);
        afegirFitxes('B', 3, 2);
        afegirFitxes('C', 3, 4);
        afegirFitxes('D', 2, 5);
        afegirFitxes('E', 1, 12);
        afegirFitxes('F', 4, 1);
        afegirFitxes('G', 2, 2);
        afegirFitxes('H', 4, 2);
        afegirFitxes('I', 1, 6);
        afegirFitxes('J', 8, 1);
        afegirFitxes('L', 1, 4);
        afegirFitxes("LL", 8, 1);   // Adding LL as a special tile
        afegirFitxes('M', 3, 2);
        afegirFitxes('N', 1, 5);
        afegirFitxes('Ñ', 8, 1);    // Spanish specific: Ñ
        afegirFitxes('O', 1, 9);
        afegirFitxes('P', 3, 2);
        afegirFitxes('Q', 5, 1);
        afegirFitxes('R', 1, 5);
        afegirFitxes('S', 1, 6);
        afegirFitxes('T', 1, 4);
        afegirFitxes('U', 1, 5);
        afegirFitxes('V', 4, 1);
        afegirFitxes('X', 8, 1);
        afegirFitxes('Y', 4, 1);
        afegirFitxes('Z', 10, 1);
        afegirFitxes(' ', 0, 2); // Blank tiles
    }

    private void inicialitzarFitxesAngles() {
        // Distribution based on English Scrabble
        afegirFitxes('A', 1, 9);
        afegirFitxes('B', 3, 2);
        afegirFitxes('C', 3, 2);
        afegirFitxes('D', 2, 4);
        afegirFitxes('E', 1, 12);
        afegirFitxes('F', 4, 2);
        afegirFitxes('G', 2, 3);
        afegirFitxes('H', 4, 2);
        afegirFitxes('I', 1, 9);
        afegirFitxes('J', 8, 1);
        afegirFitxes('K', 5, 1);
        afegirFitxes('L', 1, 4);
        afegirFitxes('M', 3, 2);
        afegirFitxes('N', 1, 6);
        afegirFitxes('O', 1, 8);
        afegirFitxes('P', 3, 2);
        afegirFitxes('Q', 10, 1);
        afegirFitxes('R', 1, 6);
        afegirFitxes('S', 1, 4);
        afegirFitxes('T', 1, 6);
        afegirFitxes('U', 1, 4);
        afegirFitxes('V', 4, 2);
        afegirFitxes('W', 4, 2);
        afegirFitxes('X', 8, 1);
        afegirFitxes('Y', 4, 2);
        afegirFitxes('Z', 10, 1);
        afegirFitxes(' ', 0, 2); // Blank tiles
    }

    private void afegirFitxes(char lletra, int valor, int quantitat) {
        for (int i = 0; i < quantitat; i++) {
            fitxes.add(new Fitxa(lletra, valor));
        }
    }

    private void afegirFitxes(String lletres, int valor, int quantitat) {
        for (int i = 0; i < quantitat; i++) {
            fitxes.add(new Fitxa(lletres, valor));
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

    public String getIdioma() {
        return idioma;
    }

    public Map<String, Integer> getDistribucioActual() {
        Map<String, Integer> distribucio = new HashMap<>();
        for (Fitxa fitxa : fitxes) {
            String lletra = fitxa.getValorLletra();
            distribucio.put(lletra, distribucio.getOrDefault(lletra, 0) + 1);
        }
        return distribucio;
    }

    @Override
    public String toString() {
        return "Bossa [idioma=" + idioma + ", quantitat=" + getQuantitatFitxes() + "]";
    }
}
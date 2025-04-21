package Main;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

// aquesta clase es una clase que representa una bossa de fitxes per a un joc de Scrabble, es carrega desde un fitxer en resources.
public class Bossa {

    private String nom;
    private LinkedList<Fitxa> fitxes;

    private final Set<String> alfabet;

    public Bossa(String nom) {
        this.nom = nom;
        this.fitxes = new LinkedList<Fitxa>();
        this.alfabet = new HashSet<String>();
        carregarFitxesIAlfabet();
        barrejar();
    }

    // getters i setters
    public String getNom() {
        return nom;
    }
    public LinkedList<Fitxa> getFitxes() {
        return fitxes;
    }
    public Set<String> getAlfabet() {
        return this.alfabet;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setFitxes(LinkedList<Fitxa> fitxes) {
        this.fitxes = fitxes;
    }


    private void carregarFitxesIAlfabet() {

        // Dentro de una clase de tu paquete Main, por ejemplo:
        String resourcePath = "/" + nom + "/letras_" + nom + ".txt";
        // Aixo de l'InputStream es per a que funciona l'execucio amb un .jar

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                // Intentem carregar el fitxer des d'una ruta relativa en entorn de desenvolupament (per si no estàs executant des d'un JAR)
                File file = new File("src/main/resources" + resourcePath);
                if (!file.exists()) {
                    throw new IOException("No encontré el recurso: " + resourcePath);
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String linia;
                    while ((linia = br.readLine()) != null) {
                        // cada linia del fitxer conté una lletra, la quantitat de fitxes i el valor de la lletra
                        String[] data = linia.split("\\s+");
                        String lletra = data[0];
                        int quantitat = Integer.parseInt(data[1]);
                        int valor = Integer.parseInt(data[2]);

                        // afegim les fitxes a la bossa
                        if (quantitat > 0) {
                            afegirFitxa(lletra, quantitat, valor);
                        }
                        // afegim lletra a l'alfabet
                        alfabet.add(lletra);
                    }
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String linia;
                    while ((linia = br.readLine()) != null) {
                        // cada linia del fitxer conté una lletra, la quantitat de fitxes i el valor de la lletra
                        String[] data = linia.split("\\s+");
                        String lletra = data[0];
                        int quantitat = Integer.parseInt(data[1]);
                        int valor = Integer.parseInt(data[2]);

                        // afegim les fitxes a la bossa
                        if (quantitat > 0) {
                            afegirFitxa(lletra, quantitat, valor);
                        }
                        // afegim lletra a l'alfabet
                        alfabet.add(lletra);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void afegirFitxa(String lletra, int quantitat, int valor) {
        for (int i = 0; i < quantitat; i++) {
            fitxes.add(new Fitxa(lletra, valor));
        }
    }

    private void barrejar() {
        Collections.shuffle(fitxes);
    }

    public Fitxa agafarFitxa() {
        if (fitxes.isEmpty()) {
            return null;
        }
        return fitxes.pollFirst();
    }

    public ArrayList<Fitxa> agafarFitxes(int quantitat) {
        ArrayList<Fitxa> fitxesAgafades = new ArrayList<>();
        for (int i = 0; i < quantitat; i++) {
            Fitxa fitxa = agafarFitxa();
            if (fitxa != null) {
                fitxesAgafades.add(fitxa);
            } else {
                break;
            }
        }
        return fitxesAgafades;
    }

    public void retornarFitxa(Fitxa fitxa) {
        fitxes.add(fitxa);
        barrejar();
    }

    public boolean esBuida() {
        return fitxes.isEmpty();
    }

    public int getQuantitatFitxes() {
        return fitxes.size();
    }

    @Override
    public String toString() {
        return "Bossa [nom=" + nom + ", quantitat=" + getQuantitatFitxes() + "]";
    }
}
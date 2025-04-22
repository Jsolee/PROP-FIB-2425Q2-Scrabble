package Domini;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Classe que representa una bossa de fitxes per a un joc de Scrabble.
 * Carrega les fitxes des d'un recurs del classpath i permet agafar-les,
 * retornar-les i consultar-ne l'estat.
 */
public class Bossa {

    /** Nom de la bossa */
    private String nom;
    /** Llista de fitxes actuals a la bossa */
    private LinkedList<Fitxa> fitxes;
    /** Conjunt de lletres disponibles a la bossa */
    private final Set<String> alfabet;

    /**
     * Crea una bossa amb el nom indicat, carregant-hi les fitxes
     * i barrejant-les.
     * @param nom nom de la bossa i carpeta de recursos (per exemple "english")
     */
    public Bossa(String nom) {
        this.nom = nom;
        this.fitxes = new LinkedList<Fitxa>();
        this.alfabet = new HashSet<String>();
        carregarFitxesIAlfabet();
        barrejar();
    }

    //getters i setters

    /**
     * Retorna el nom de la bossa.
     * @return nom de la bossa
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retorna la llista de fitxes actuals.
     * @return llista de fitxes
     */
    public LinkedList<Fitxa> getFitxes() {
        return fitxes;
    }


    /**
     * Retorna el conjunt de tots els caràcters disponibles.
     * @return conjunt de lletres de l'alfabet
     */
    public Set<String> getAlfabet() {
        return this.alfabet;
    }


    /**
     * Canvia el nom de la bossa.
     * @param nom nou nom de la bossa
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    /**
     * Estableix una nova llista de fitxes.
     * @param fitxes llista de fitxes a assignar
     */
    public void setFitxes(LinkedList<Fitxa> fitxes) {
        this.fitxes = fitxes;
    }


    /**
     * Llegeix les fitxes i l'alfabet des del recurs
     * "/{nom}/letras_{nom}.txt" dins del JAR.
     */
    private void carregarFitxesIAlfabet() {

        String resourcePath = "/" + nom + "/letras_" + nom + ".txt";

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

    /**
     * Afegeix un nombre de fitxes amb la mateixa lletra i valor.
     * @param lletra símbol de la fitxa
     * @param quantitat quantes fitxes afegir
     * @param valor puntuació de cada fitxa
     */
    private void afegirFitxa(String lletra, int quantitat, int valor) {
        for (int i = 0; i < quantitat; i++) {
            fitxes.add(new Fitxa(lletra, valor));
        }
    }


    /**
     * Barreja l'ordre de les fitxes a l'atzar.
     */
    private void barrejar() {
        Collections.shuffle(fitxes);
    }

    /**
     * Agafa una fitxa de la bossa i la retorna.
     * @return fitxa agafada, o null si la bossa és buida
     */
    public Fitxa agafarFitxa() {
        if (fitxes.isEmpty()) {
            return null;
        }
        return fitxes.pollFirst();
    }

    /**
     * Agafa vàries fitxes de la bossa.
     * @param quantitat nombre de fitxes a agafar
     * @return llista de fitxes agafades (pot ser més curta si s'acaben)
     */
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


    /**
     * Retorna una fitxa a la bossa i torna a barrejar.
     * @param fitxa fitxa a retornar
     */
    public void retornarFitxa(Fitxa fitxa) {
        fitxes.add(fitxa);
        barrejar();
    }

    /**
     * Comprueba si la bossa està buida.
     * @return true si no queden fitxes, false en cas contrari
     */
    public boolean esBuida() {
        return fitxes.isEmpty();
    }

    /**
     * Retorna el nombre de fitxes restants.
     * @return quantitat de fitxes actuals
     */
    public int getQuantitatFitxes() {
        return fitxes.size();
    }


    /**
     * Representació en cadena de la bossa.
     * @return string amb nom i quantitat de fitxes
     */
    @Override
    public String toString() {
        return "Bossa [nom=" + nom + ", quantitat=" + getQuantitatFitxes() + "]";
    }
}
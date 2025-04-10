package Main;


import java.util.HashMap;
import java.util.Map;
// per llegir els fitxers del diccionari
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Diccionari {


    // constructora
    public Diccionari(String nom) {
        this.nom = nom;
        // en la ruta relativa resources/ ha d'haver-hi un directori amb el nom del diccionari, i un fitxer <nom-diccionari>.txt
        // amb la llista de paraules ordenades del diccionari, en la seguent instrucció carreguem el fitxer, el llegim y creem el DAWG.
        String ruta = "/" + nom + "/" + nom + ".txt";
        try (InputStream is = Diccionari.class.getResourceAsStream(ruta)) {
            if (is == null) {
                throw new RuntimeException("No se encontró el recurso: " + ruta);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.arrel = new DAWGnode();
        this.bossa = new Bossa();
    }

    // atributs
    private String nom;
    private DAWGnode arrel;
    private Bossa bossa;

    // getters i setters
    public String getNom() {
        return this.nom;
    }

    public Bossa getBossa() {
        return this.bossa;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setBossa(Bossa bossa) {
        this.bossa = bossa;
    }


    // classe per implementar el DAWG
    private class DAWGnode {
        Map<Character, DAWGnode> produccions;
        Boolean esParaula;
        String signatura; // només son valides quan el DAWG ha estat minimitzar, doncs es precomputen en aquell moment

        public DAWGnode() {
            produccions = new HashMap<Character, DAWGnode>();
            esParaula = false;
            signatura = "";
        }

        // getters i setters
        public Boolean getEsParaula() {
            return this.esParaula;
        }

        public String getSignatura() {
            return this.signatura;
        }

        public void setEsParaula(Boolean esParaula) {
            this.esParaula = esParaula;
        }

        public void setSignatura(String signatura) {
            this.signatura = signatura;
        }


    }
}

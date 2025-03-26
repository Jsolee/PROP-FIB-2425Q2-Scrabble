package Main;


import java.util.HashMap;
import java.util.Map;

public class Diccionari {

    // constructora
    public Diccionari(String nom, String idioma) {
        this.nom = nom;
        this.idioma = idioma;
        this.arrel = new DAWGnode();
    }

    // atributs
    private String nom;
    private String idioma;
    private DAWGnode arrel;

    // getters i setters
    public String getNom() {
        return this.nom;
    }
    public String getIdioma() {
        return this.idioma;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
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

        // mètodes
        public void calcularSignatura() {
            // calcula la signatura del node y actualitza el atribut
        }
    }

    public boolean buscarParaula(String paraula) {
        DAWGnode node = NodeDePrefix();
        return node != null && node.esParaula;
    }

    public boolean buscarPrefix(String prefix) {
        DAWGnode node = NodeDePrefix(prefix);
        return node != null;
    }

    public void afegirParaula(String paraula) {
        afegirParaulaTrie(paraula);
        minimitzar();
    }

    // Utilitzar aquesta funció per afegir moltes paraules seguides ja que t'estalvies la minimització.
    public void afegirParaulaTrie(String paraula) { // afegeix paraula com si fos un trie en comptes d'un DAWG, per tant s'haura de minimitzar després
        DAWGnode node = arrel;
        for (char lletra : paraula.toCharArray()) {
            if (!node.produccions.containsKey(lletra)) {
                node.produccions.put(lletra, new DAWGnode());
            }
            node = node.produccions.get(lletra);
        }
        node.setEsParaula(true);
    }

    public void eliminarParaula(String paraula) {
        eliminarParaulaTrie(paraula);
        minimitzar();
    }

    public void eliminarParaulaTrie(String paraula) {
        DAWGnode node = arrel;
        for (char lletra : paraula.toCharArray()) {
            if (!node.produccions.containsKey(lletra)) {
                return;
            }
            node = node.produccions.get(lletra);
        }
        node.setEsParaula(false);
    }

    private void minimitzar() {
        // minimitza el trie per obtenir el DAWG

        // eliminar nodes unicos, inaccesibles(aunque creo que no hay), ramas inutiles,

        // minimizar a partir de la signatura.

    }

    private DAWGnode NodeDePrefix(String prefix) {
        DAWGnode node = new DAWGnode();
        for (char lletra : prefix.toCharArray()) {
            if (!node.produccions.containsKey(lletra)) {
                return null;
            }
            node = node.produccions.get(lletra);
        }
        return node;
    }

}

package Main;


import java.util.HashMap;
import java.util.Map;

public class Diccionari {

    public Diccionari(String nom, String idioma) {
        this.nom = nom;
        this.idioma = idioma;
        this.arrel = new DAWGnode();
    }

    private String nom;
    private String idioma;
    private DAWGnode arrel;

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

    private class DAWGnode {
        Map<Character, DAWGnode> produccions;
        Boolean esParaula;
        String signatura;

        public DAWGnode() {
            produccions = new HashMap<Character, DAWGnode>();
            esParaula = false;
            signatura = "";
        }

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

    public void afegirParaula(String paraula) {
        afegirParaulaTrie(paraula);
        minimitzar();
    }

    // Utilitzar aquesta funció per afegir moltes paraules seguides ja que t'estalvies la minimització.
    public void afegirParaulaTrie(String paraula) { // afegeix paraula com si fos un trie en comptes d'un DAWG, per tant s'haura de minimitzar després
        DAWGnode node = arrel;
        for (int i = 0; i < paraula.length(); i++) {
            char lletra = paraula.charAt(i);
            if (!node.produccions.containsKey(lletra)) {
                node.produccions.put(lletra, new DAWGnode());
            }
            node = node.produccions.get(lletra);
        }
        node.setEsParaula(true);
    }

    private void minimitzar() {

    }

    public void eliminarParaula(String paraula) {
        eliminarParaulaTrie(paraula);
        minimitzar();
    }

    public void eliminarParaulaTrie(String paraula) {

    }

}

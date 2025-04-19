package Main;


import java.util.*;
// per llegir els fitxers del diccionari
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Diccionari {


    // constructora
    public Diccionari(String nom) {
        this.nom = nom;
        this.registre = new HashMap<>();
        this.arrel = new DAWGnode();

        carregarDiccionari(nom);
    }

    // atributs
    private String nom;
    private final DAWGnode arrel;
    // abans utilitzava un ArrayList, pero aixo provocava que comprovar si un node estava registrat fos O(n) i no O(1)
    // d'aquesta manera vaig aconseguir que el diccionari english cargues en 1 segon y no en 1 minut.
    private final Map<DAWGnode, DAWGnode> registre;

    // getters i setters
    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    // metodes publics per consultar paraules, puntuaciones, etc.

    public boolean esParaula(String paraula) {
        DAWGnode node = getNode(paraula);
        if (node != null) {
            return node.getEsParaula();
        }
        return false;
    }


    public int getNumeroNodes() {
        Set<DAWGnode> visited = new HashSet<>();
        return getNumeroNodesRec(arrel, visited);
    }

    // Función recursiva para contar los nodos únicos en el DAWG.
    private int getNumeroNodesRec(DAWGnode node, Set<DAWGnode> visited) {
        if (visited.contains(node)) {
            return 0;
        }
        visited.add(node);
        int count = 1; // Contamos el nodo actual.
        for (DAWGnode child : node.getTransicions().values()) {
            count += getNumeroNodesRec(child, visited);
        }
        return count;
    }


    // metodes privats per implementar el DAWG

    private void carregarDiccionari(String nom) {

        // llegim el fitxer del diccionari
        String ruta = "src/main/resources/" + nom + "/" + nom + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String paraula;
            while ((paraula = br.readLine()) != null) {

                // algorisme per afegir la paraula al DAWG
                String prefixComu = getPrefixComu(paraula);
                DAWGnode ultimNode = getNode(prefixComu);


                // aquest if saltarà si la nova palabra té un sufix diferent al de les paraules que s'estaven afegint previament
                if (ultimNode.teFills()) {
                    minimitzarAPartirDe(ultimNode);
                }
                // es crea una nova branca amb el sufix que difereix de les paraules anteriors, aquesta branca es minimitzara quan entri una paraula amb un sufix diferent a aquest.
                String sufixActual = paraula.substring(prefixComu.length());
                afegirSufix(ultimNode, sufixActual);

            }

            //s'ha de executar aquesta funció perque l'última branca afegida no es minimitza perquè no hi ha cap paraula després que tingui un sufix diferent
            minimitzarAPartirDe(arrel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPrefixComu(String paraula) {
        String prefixComu = "";
        DAWGnode node = arrel;
        for (int i = 0; i < paraula.length(); i++) {
            char lletra = paraula.charAt(i);
            if (node.getTransicio(lletra) != null) {
                prefixComu += lletra;
                node = node.getTransicio(lletra);
            } else {
                break;
            }
        }
        return prefixComu;
    }

    public DAWGnode getNode(String paraula) {
        DAWGnode node = arrel;
        for (int i = 0; i < paraula.length(); i++) {
            char lletra = paraula.charAt(i);
            if (node.getTransicio(lletra) != null) {
                node = node.getTransicio(lletra);
            } else {
                return null;
            }
        }
        return node;
    }

    private void afegirSufix(DAWGnode ultimNode, String sufixActual) {
        DAWGnode node = ultimNode;
        for (int i = 0; i < sufixActual.length(); i++) {
            char lletra = sufixActual.charAt(i);
            DAWGnode nouNode = new DAWGnode();
            node.afegirTransicio(lletra, nouNode);
            node = nouNode;
        }
        node.setEsParaula(true);
    }

    private void minimitzarAPartirDe(DAWGnode node) {
        Map.Entry<Character, DAWGnode> transicioMesGran = node.getTransicioMesGran();
        if (transicioMesGran == null) return;

        DAWGnode fill = transicioMesGran.getValue();
        // si te fills, minimitzem recursivament
        if (fill.teFills()) {
            minimitzarAPartirDe(fill);
        }

        // si el fill esta en el register, el fill es pot intercanviar per el node
        DAWGnode existent = registre.get(fill);
        if (existent != null) {
            node.afegirTransicio(transicioMesGran.getKey(), existent);
        } else {
            registre.put(fill, fill);
        }
    }
}

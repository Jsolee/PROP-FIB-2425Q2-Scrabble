package Main;

import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;


/**
 * Classe Diccionari basada en un DAWG (Grau Acyclic Dirigit de Paraules).
 * Permet carregar un fitxer de paraules des de recursos, construir el DAWG
 * i consultar si una cadena és una paraula vàlida o obtenir estadístiques.
 */
public class Diccionari {

    // atributs
    private String nom;
    private final DAWGnode arrel;
    // abans utilitzava un ArrayList, pero aixo provocava que comprovar si un node estava registrat fos O(n) i no O(1)
    // d'aquesta manera vaig aconseguir que el diccionari english cargues en 1 segon y no en 1 minut.
    private final Map<DAWGnode, DAWGnode> registre;

    /**
     * Crea un nou diccionari amb el nom indicat.
     * El fitxer s'ha de trobar a resources/{nom}/{nom}.txt dins del JAR o classpath.
     * @param nom nom de la carpeta i fitxer de diccionari (sense extensió)
     */
    public Diccionari(String nom) {
        this.nom = nom;
        this.registre = new HashMap<>();
        this.arrel = new DAWGnode();

        carregarDiccionari(nom);
    }


    /**
     * Retorna el nom associat al diccionari.
     * @return nom del diccionari
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Canvia el nom del diccionari (no recarrega automàticament).
     * @param nom nou nom del diccionari
     */
    public void setNom(String nom) {
        this.nom = nom;
    }


    // metodes publics per consultar paraules, puntuaciones, etc.


    /**
     * Comprova si una cadena és una paraula existent al DAWG.
     * @param paraula cadena a consultar
     * @return {@code true} si la paraula està registrada (el node del DAWG corresponen es acceptador), {@code false} en cas contrari
     */
    public boolean esParaula(String paraula) {
        DAWGnode node = getNode(paraula);
        if (node != null) {
            return node.getEsParaula();
        }
        return false;
    }

    /**
     * Calcula el nombre total de nodes únics al DAWG. (Útil per fer tests i consultar la mida del diccionari).
     * @return nombre de nodes
     */
    public int getNumeroNodes() {
        Set<DAWGnode> visited = new HashSet<>();
        return getNumeroNodesRec(arrel, visited);
    }



    /**
     * Recursiu intern per comptar nodes sense repetir.
     * @param node node actual
     * @param visited conjunt de nodes ja visitats
     * @return nombre de nodes comptats des de node
     */
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


    /**
     * Carrega el fitxer de diccionari i construeix el DAWG.
     * @param nom nom de la carpeta i fitxer del diccionari
     */
    private void carregarDiccionari(String nom) {

        // Dentro de una clase de tu paquete Main, por ejemplo:
        String resourcePath = "/" + nom + "/" + nom + ".txt";
        // Aixo de l'InputStream es per a que funciona l'execucio amb un .jar
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("No encontré el recurso: " + resourcePath);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Obté el prefix comú més llarg ja existent al DAWG.
     * @param paraula cadena d'entrada
     * @return prefix comú més llarg; pot ser cadena buida
     */
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


    /**
     * Navega el DAWG seguint els caràcters de la cadena.
     * @param paraula cadena de cerca
     * @return node final, o {@code null} si no existeix la ruta completa
     */
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

    /**
     * Retorna el node arrel del DAWG.
     * @return node inicial
     */
    public DAWGnode getArrel() {
        return this.arrel;
    }

    /**
     * Afegeix el sufix restant a partir d'un node donat i marca el node final.
     * @param ultimNode node des del qual començar
     * @param sufixActual cadena de caràcters a afegir
     */
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


    /**
     * Minimitza el DAWG a partir d'un node, reutilitzant nodes ja existents.
     * @param node node des del qual començar la minimització
     */
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

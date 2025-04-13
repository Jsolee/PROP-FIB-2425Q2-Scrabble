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
        this.bossa = new Bossa(nom);

        carregarDiccionari(nom);
    }

    // atributs
    private String nom;
    private DAWGnode arrel;
    private Bossa bossa;

    // abans utilitzava un ArrayList, pero aixo provocava que comprovar si un node estava registrat fos O(n) i no O(1)
    // d'aquesta manera vaig aconseguir que el diccionari english cargues en 1 segon y no en 1 minut.
    private Map<DAWGnode, DAWGnode> registre;

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

    private DAWGnode getNode(String paraula) {
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


    // classe per implementar el node del DAWG
    private class DAWGnode {
        Map<Character, DAWGnode> transicions;
        Boolean esParaula;

        public DAWGnode() {
            transicions = new HashMap<Character, DAWGnode>();
            esParaula = false;
        }

        // getters i setters
        public Boolean getEsParaula() {
            return this.esParaula;
        }
        public Map<Character, DAWGnode> getTransicions() {
            return this.transicions;
        }
        public DAWGnode getTransicio(Character lletra) {
            return this.transicions.get(lletra);
        }

        public void setEsParaula(Boolean esParaula) {
            this.esParaula = esParaula;
        }
        public void setProduccions(Map<Character, DAWGnode> transicions) {
            this.transicions = transicions;
        }

        public boolean teFills() {
            return !this.transicions.isEmpty();
        }

        // funcions que modifiquen les transicions
        public void afegirTransicio(Character lletra, DAWGnode node) {
            this.transicions.put(lletra, node);
        }

        // a diferencia de afegirTransicio, aquest mètode substitueix la transició si aquesta previament existeix
        public void canviarTransicio(Character lletra, DAWGnode node) {
            this.transicions.replace(lletra, node);
        }

        //retorna la transició(en forme de entrada d'un map) amb el valor del caracter més gran lexicogràficament
        public Map.Entry<Character, DAWGnode> getTransicioMesGran() {
            if (this.transicions.isEmpty()) return null;

            Character keyMesGran = null;
            for (Character key : transicions.keySet()) {
                if (keyMesGran == null || key > keyMesGran) {
                    keyMesGran = key;
                }
            }
            // necesitem retornar una entrada perque despres el métode
            return new AbstractMap.SimpleEntry<>(keyMesGran, transicions.get(keyMesGran));
        }

        public boolean esEquivalent(DAWGnode node2) {
            // si els dos nodes no son acceptadors, o no acceptadors
            if (this.esParaula != node2.getEsParaula()) return false;
            // si tenen diferents quantitats de fills
            if (this.transicions.size() != node2.getTransicions().size()) return false;

            // si tenen els mateixos fills, son equivalents, no fa falta fer una cerca recursiva dintre de cada d'aquest fills ja que al construir el DAWG incrementalment, els fills ja han estat minimitzats.
            // per tant, només fa falta comprovar que els fills son els mateixos.
            for (Map.Entry<Character, DAWGnode> trans : this.transicions.entrySet()) {
                Character lletra = trans.getKey();
                DAWGnode fill1 = trans.getValue();
                DAWGnode fill2 = node2.getTransicio(lletra);
                if (fill2 == null) {
                    return false;
                }
                if (fill1 != fill2) {
                    return false;
                }
            }
            return true;
        }

        // fer override d'aquestes funcions serveix per saber quants estats té el DAWG en el test pertinent, pero no afecta al algorisme que comprova una paraula
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            DAWGnode dawgNode = (DAWGnode) obj;
            return Objects.equals(transicions, dawgNode.transicions) &&
                    Objects.equals(esParaula, dawgNode.esParaula);
        }

        @Override
        public int hashCode() {
            return Objects.hash(transicions, esParaula);
        }
    }

}

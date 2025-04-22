package Main;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Node d'un DAWG (Grau Acyclic Dirigit de Paraules).
 * Cada node pot indicar si és final d'una paraula i
 * contenir transicions a altres nodes segons caràcters.
 */
public class DAWGnode {
    /** Mapa de transicions associades a aquest node.
     * Cada entrada conté un caràcter i el node destí corresponent.
     */
    private Map<Character, DAWGnode> transicions;

    /** Indica si aquest node representa el final d'una paraula. */
    private Boolean esParaula;


    /**
     * Crea un node buit, sense transicions i sense marcar com a paraula.
     */
    public DAWGnode() {
        transicions = new HashMap<Character, DAWGnode>();
        esParaula = false;
    }


    /**
     * Comprova si aquest node representa el final d'una paraula.
     * @return true si aquí acaba una paraula, false en cas contrari
     */
    public Boolean getEsParaula() {
        return this.esParaula;
    }

    /**
     * Retorna totes les transicions des d'aquest node.
     * @return mapa de caràcter que surten del node
     */
    public Map<Character, DAWGnode> getTransicions() {
        return this.transicions;
    }

    /**
     * Obté la transició associada a un caràcter concret.
     * @param lletra el caràcter de la transició
     * @return el node destí, o null si no existeix
     */
    public DAWGnode getTransicio(Character lletra) {
        return this.transicions.get(lletra);
    }


    /**
     * Marca o desmarca aquest node com a final de paraula.
     * @param esParaula true per indicar que aquí finalitza una paraula
     */
    public void setEsParaula(Boolean esParaula) {
        this.esParaula = esParaula;
    }


    /**
     * Substitueix totes les transicions d'aquest node.
     * @param transicions nou mapa de caràcter a node
     */
    public void setTransicions(Map<Character, DAWGnode> transicions) {
        this.transicions = transicions;
    }


    /**
     * Indica si aquest node té almenys una transició.
     * @return true si hi ha alguna transició, false si està buit
     */
    public boolean teFills() {
        return !this.transicions.isEmpty();
    }

    // funcions que modifiquen les transicions:


    /**
     * Afegeix una transició nova o sobrescriu la existent.
     * @param lletra el caràcter de la transició
     * @param node el node al que apunta aquesta transició
     */
    public void afegirTransicio(Character lletra, DAWGnode node) {
        this.transicions.put(lletra, node);
    }

    /**
     * Substitueix la transició donada per un nou node, NOMES SI JA EXISTEIX, a diferencia de afegirTransaccio.
     * @param lletra el caràcter de la transició
     * @param node el node que reemplaçarà l'anterior
     */
    public void canviarTransicio(Character lletra, DAWGnode node) {
        this.transicions.replace(lletra, node);
    }

    /**
     * Cerca la transició amb caràcter més gran en ordre lexicogràfic.
     * @return l'entrada (caràcter, node) corresponent, o null si no hi ha transicions
     */
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

    // fer override d'aquestes funcions serveix per saber quants estats té el DAWG en el test pertinent(que comproba el nombre d'estats), pero no afecta al algorisme que comprova una paraula ni a la implementació del DAWG
    // s'utilitzen internament per comparar nodes i veure si son iguals
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


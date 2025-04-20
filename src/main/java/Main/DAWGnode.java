package Main;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// classe per implementar el node del DAWG
public class DAWGnode {
    private Map<Character, DAWGnode> transicions;
    private Boolean esParaula;

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


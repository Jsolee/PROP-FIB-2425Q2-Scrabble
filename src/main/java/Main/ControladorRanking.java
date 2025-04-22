package Main;

import java.util.List;

/**
 * Controlador que gestiona el rànquing de jugadors del joc.
 * Proporciona mètodes per afegir i eliminar usuaris del rànquing,
 * així com per obtenir diferents tipus de classificacions ordenades.
 */
public class ControladorRanking {
    /** Objecte Ranking que emmagatzema i gestiona les classificacions dels jugadors */
    Ranking ranking;

    /**
     * Constructor per defecte.
     * Inicialitza un nou objecte Ranking per gestionar les classificacions.
     */
    public ControladorRanking() {
        this.ranking = new Ranking();
    }

    /**
     * Afegeix un nou usuari al rànquing.
     * 
     * @param persona objecte Persona que es vol afegir al rànquing
     */
    public void afegirNouUsuari(Persona persona) {
        ranking.afegirPersona(persona);
    }

    /**
     * Elimina un usuari del rànquing.
     * 
     * @param persona objecte Persona que es vol eliminar del rànquing
     */
    public void eliminarUsuari(Persona persona) {
        ranking.esborrarPersona(persona);
    }

    /**
     * Obté una llista d'usuaris ordenada segons el tipus de rànquing especificat.
     * 
     * @param n tipus de rànquing a obtenir:
     *          1 - Rànquing ordenat per punts totals
     *          2 - Rànquing ordenat per partides jugades
     *          3 - Rànquing ordenat per partides guanyades
     *          4 - Rànquing ordenat per rècord personal
     *          5 - Rànquing ordenat per paraules totals
     * @return llista de Persona ordenada segons el criteri especificat
     * @throws IllegalArgumentException si el tipus de rànquing no és vàlid
     */
    public List<Persona> getRanking(int n) {
        switch (n) {
            case 1:
                return ranking.getRankingPuntsTotals();
            case 2:
                return ranking.getRankingPartidesJugades();
            case 3:
                return ranking.getRankingPartidesGuanyades();
            case 4:
                return ranking.getRankingRecordPersonal();
            case 5:
                return ranking.getRankingParaulesTotals();
            default:
                throw new IllegalArgumentException("Invalid ranking type: " + n);
        }
    }

}
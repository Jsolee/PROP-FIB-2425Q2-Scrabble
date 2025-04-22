package Main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe que gestiona els diferents rànquings de jugadors per a diverses categories estadístiques.
 * Manté llistes ordenades de jugadors segons punts totals, partides jugades, partides guanyades,
 * rècord personal i paraules totals.
 */
public class Ranking {
    /** Llista de jugadors ordenada per puntuació total */
    private List<Persona> rankingPuntsTotals;
    /** Llista de jugadors ordenada per nombre de partides jugades */
    private List<Persona> rankingPartidesJugades;
    /** Llista de jugadors ordenada per nombre de partides guanyades */
    private List<Persona> rankingPartidesGuanyades;
    /** Llista de jugadors ordenada per millor puntuació personal */
    private List<Persona> rankingRecordPersonal;

    /**
     * Constructor per defecte.
     * Inicialitza totes les llistes de rànquing com a llistes buides.
     */
    public Ranking() {
        this.rankingPuntsTotals = new ArrayList<>();
        this.rankingPartidesJugades = new ArrayList<>();
        this.rankingPartidesGuanyades = new ArrayList<>();
        this.rankingRecordPersonal = new ArrayList<>();
    }

    /**
     * Afegeix una persona a tots els rànquings i actualitza l'ordre.
     * No l'afegirà si ja existeix a les llistes.
     * 
     * @param persona jugador a afegir als rànquings
     */
    public void afegirPersona(Persona persona) {
        if (!rankingPuntsTotals.contains(persona)) {
            rankingPuntsTotals.add(persona);
            rankingPartidesJugades.add(persona);
            rankingPartidesGuanyades.add(persona);
            rankingRecordPersonal.add(persona);
        }
        actualitzarRanking();
    }

    /**
     * Elimina una persona de tots els rànquings.
     * 
     * @param persona jugador a eliminar dels rànquings
     */
    public void esborrarPersona(Persona persona) {
        rankingPuntsTotals.remove(persona);
        rankingPartidesJugades.remove(persona);
        rankingPartidesGuanyades.remove(persona);
        rankingRecordPersonal.remove(persona);
    }

    /**
     * Actualitza l'ordenació de totes les llistes de rànquing.
     * Cada llista s'ordena de manera descendent segons el criteri corresponent.
     */
    public void actualitzarRanking() {
        this.rankingPuntsTotals.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(1)));
        this.rankingPartidesJugades.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(2)));
        this.rankingPartidesGuanyades.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(3)));
        this.rankingRecordPersonal.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(4)));
    }

    /**
     * Mostra per consola el rànquing especificat.
     * 
     * @param num tipus de rànquing a imprimir:
     *            1 - Punts totals
     *            2 - Partides jugades
     *            3 - Partides guanyades
     *            4 - Rècord personal
     *            5 - Paraules totals
     */
    public void imprimirRanking(int num) {
        List<Persona> ranking;
        switch (num) {
            case 1:
                ranking = rankingPuntsTotals;
                System.out.println("Ranking per número de punts totals:");
                break;
            case 2:
                ranking = rankingPartidesJugades;
                System.out.println("Ranking per número de partides jugades totals:");
                break;
            case 3:
                ranking = rankingPartidesGuanyades;
                System.out.println("Ranking per número de partides guanyades totals:");
                break;
            case 4:
                ranking = rankingRecordPersonal;
                System.out.println("Ranking per récord personal totals:");
                break;
            default:
                System.out.println("Número de ranking no vàlid.");
                return;
        }

        for (int i = 0; i < ranking.size(); i++) {
            Persona persona = ranking.get(i);
            System.out.println((i + 1) + ". " + persona.getNom() + " - " + persona.getValorEstaditiques(num));
        }
    }

    /**
     * Obté el rànquing ordenat per puntuació total.
     * 
     * @return llista de jugadors ordenada per puntuació total (descendent)
     */
    public List<Persona> getRankingPuntsTotals() {
        return rankingPuntsTotals;
    }

    /**
     * Obté el rànquing ordenat per nombre de partides jugades.
     * 
     * @return llista de jugadors ordenada per partides jugades (descendent)
     */
    public List<Persona> getRankingPartidesJugades() {
        return rankingPartidesJugades;
    }

    /**
     * Obté el rànquing ordenat per nombre de partides guanyades.
     * 
     * @return llista de jugadors ordenada per partides guanyades (descendent)
     */
    public List<Persona> getRankingPartidesGuanyades() {
        return rankingPartidesGuanyades;
    }

    /**
     * Obté el rànquing ordenat per rècord personal.
     * 
     * @return llista de jugadors ordenada per rècord personal (descendent)
     */
    public List<Persona> getRankingRecordPersonal() {
        return rankingRecordPersonal;
    }

}
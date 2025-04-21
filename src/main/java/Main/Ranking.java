package Main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ranking {
    private List<Persona> rankingPuntsTotals;
    private List<Persona> rankingPartidesJugades;
    private List<Persona> rankingPartidesGuanyades;
    private List<Persona> rankingRecordPersonal;
    private List<Persona> rankingParaulesTotals;

    public Ranking() {
        this.rankingPuntsTotals = new ArrayList<>();
        this.rankingPartidesJugades = new ArrayList<>();
        this.rankingPartidesGuanyades = new ArrayList<>();
        this.rankingRecordPersonal = new ArrayList<>();
        this.rankingParaulesTotals = new ArrayList<>();
    }

    public void afegirPersona(Persona persona) {
        if (!rankingPuntsTotals.contains(persona)) {
            rankingPuntsTotals.add(persona);
            rankingPartidesJugades.add(persona);
            rankingPartidesGuanyades.add(persona);
            rankingRecordPersonal.add(persona);
            rankingParaulesTotals.add(persona);
        }
        actualitzarRanking();
    }

    public void esborrarPersona(Persona persona) {
        rankingPuntsTotals.remove(persona);
        rankingPartidesJugades.remove(persona);
        rankingPartidesGuanyades.remove(persona);
        rankingRecordPersonal.remove(persona);
        rankingParaulesTotals.remove(persona);
    }

    public void actualitzarRanking() {
        this.rankingPuntsTotals.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(1)));
        this.rankingPartidesJugades.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(2)));
        this.rankingPartidesGuanyades.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(3)));
        this.rankingRecordPersonal.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(4)));
        this.rankingParaulesTotals.sort(Comparator.comparingInt(p -> -p.getValorEstaditiques(5)));
    }

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
            case 5:
                ranking = rankingParaulesTotals;
                System.out.println("Ranking per número de paraules totals:");
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

    public List<Persona> getRankingPuntsTotals() {
        return rankingPuntsTotals;
    }
    public List<Persona> getRankingPartidesJugades() {
        return rankingPartidesJugades;
    }
    public List<Persona> getRankingPartidesGuanyades() {
        return rankingPartidesGuanyades;
    }
    public List<Persona> getRankingRecordPersonal() {
        return rankingRecordPersonal;
    }
    public List<Persona> getRankingParaulesTotals() {
        return rankingParaulesTotals;
    }

}
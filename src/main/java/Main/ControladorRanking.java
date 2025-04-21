package Main;

import java.util.List;

public class ControladorRanking {
    Ranking ranking;

    public ControladorRanking() {
        this.ranking = new Ranking();
    }

    public void afegirNouUsuari(Persona persona) {
        ranking.afegirPersona(persona);
    }

    public void eliminarUsuari(Persona persona) {
        ranking.esborrarPersona(persona);
    }

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
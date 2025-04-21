package Main;

public class Estadistiques {
    private int paraulesTotals;
    private int puntTotal;
    private int partidesJugades;
    private int partidesGuanyades;
    private int recordPersonal;


    public Estadistiques() {
        this.paraulesTotals = 0;
        this.puntTotal = 0;
        this.partidesJugades = 0;
        this.partidesGuanyades = 0;
        this.recordPersonal = 0;
    }

    public void incrementarParaulesCreades() {
        this.paraulesTotals++;
    }

    public void incrementarPuntTotal(int punts) {
        this.puntTotal += punts;
    }

    public void incrementarPartidesJugades() {
        this.partidesJugades++;
    }

    public void incrementarPartidesGuanyades() {
        this.partidesGuanyades++;
    }

    public void actualitzarRecordPersonal(int punts) {
        if (punts > this.recordPersonal) {
            this.recordPersonal = punts;
        }
    }

    public int getParaulesTotals() {
        return paraulesTotals;
    }

    public int getPuntTotal() {
        return puntTotal;
    }

    public int getRecordPersonal() {
        return recordPersonal;
    }

    public int getPartidesJugades() {
        return partidesJugades;
    }

    public int getPartidesGuanyades() {
        return partidesGuanyades;
    }

    // Add the missing methods
    public int getPartidesPerdudes() {
        return partidesJugades - partidesGuanyades;
    }

    public int getPuntuacioTotal() {
        return puntTotal;
    }


    public double getPercentatgeVictories() {
        if (partidesJugades == 0) {
            return 0.0;
        }
        return (double) partidesGuanyades / partidesJugades * 100;
    }

    public String getNivellRanking() {
        // Calculate level based on points and win percentage
        double winPercentage = getPercentatgeVictories();
        int totalPoints = puntTotal;

        if (partidesJugades < 5) {
            return "Novell";
        } else if (winPercentage > 70 && totalPoints > 1000) {
            return "Expert";
        } else if (winPercentage > 50 && totalPoints > 500) {
            return "Avançat";
        } else if (winPercentage > 30 && totalPoints > 200) {
            return "Intermedi";
        } else {
            return "Principiant";
        }
    }
}


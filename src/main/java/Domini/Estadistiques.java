package Domini;

/**
 * Classe que representa les estadístiques d'un jugador d'Scrabble.
 * Emmagatzema i gestiona informació sobre el rendiment del jugador com les paraules creades,
 * puntuacions, partides jugades i guanyades, i el rècord personal.
 */
public class Estadistiques {
    /** Nombre total de paraules creades pel jugador */
    private int paraulesTotals;
    /** Puntuació total acumulada pel jugador en totes les partides */
    private int puntTotal;
    /** Nombre de partides jugades pel jugador */
    private int partidesJugades;
    /** Nombre de partides guanyades pel jugador */
    private int partidesGuanyades;
    /** Puntuació màxima aconseguida pel jugador en una partida */
    private int recordPersonal;

    /**
     * Constructor per defecte.
     * Inicialitza totes les estadístiques a zero.
     */
    public Estadistiques() {
        this.paraulesTotals = 0;
        this.puntTotal = 0;
        this.partidesJugades = 0;
        this.partidesGuanyades = 0;
        this.recordPersonal = 0;
    }

    /**
     * Incrementa el comptador de paraules creades en una unitat.
     */
    public void incrementarParaulesCreades() {
        this.paraulesTotals++;
    }

    /**
     * Incrementa la puntuació total del jugador.
     * 
     * @param punts puntuació a afegir al total
     */
    public void incrementarPuntTotal(int punts) {
        this.puntTotal += punts;
    }

    /**
     * Incrementa el comptador de partides jugades en una unitat.
     */
    public void incrementarPartidesJugades() {
        this.partidesJugades++;
    }

    /**
     * Incrementa el comptador de partides guanyades en una unitat.
     */
    public void incrementarPartidesGuanyades() {
        this.partidesGuanyades++;
    }

    /**
     * Actualitza el rècord personal del jugador si la puntuació proporcionada és superior.
     * 
     * @param punts nova puntuació a comparar amb el rècord actual
     */
    public void actualitzarRecordPersonal(int punts) {
        if (punts > this.recordPersonal) {
            this.recordPersonal = punts;
        }
    }

    /**
     * Obté el nombre total de paraules creades pel jugador.
     * 
     * @return total de paraules creades
     */
    public int getParaulesTotals() {
        return paraulesTotals;
    }

    /**
     * Obté la puntuació total acumulada pel jugador.
     * 
     * @return total de punts acumulats
     */
    public int getPuntTotal() {
        return puntTotal;
    }

    /**
     * Obté la puntuació màxima aconseguida pel jugador en una partida.
     * 
     * @return rècord personal de puntuació
     */
    public int getRecordPersonal() {
        return recordPersonal;
    }

    /**
     * Obté el nombre total de partides jugades pel jugador.
     * 
     * @return total de partides jugades
     */
    public int getPartidesJugades() {
        return partidesJugades;
    }

    /**
     * Obté el nombre total de partides guanyades pel jugador.
     * 
     * @return total de partides guanyades
     */
    public int getPartidesGuanyades() {
        return partidesGuanyades;
    }

    /**
     * Calcula i retorna el nombre de partides perdudes pel jugador.
     * Es calcula com la diferència entre les partides jugades i les partides guanyades.
     * 
     * @return total de partides perdudes
     */
    public int getPartidesPerdudes() {
        return partidesJugades - partidesGuanyades;
    }

    /**
     * Obté la puntuació total acumulada pel jugador.
     * Aquest mètode és equivalent a getPuntTotal().
     * 
     * @return total de punts acumulats
     */
    public int getPuntuacioTotal() {
        return puntTotal;
    }

    /**
     * Calcula i retorna el percentatge de victòries del jugador.
     * Es calcula com el nombre de partides guanyades dividit pel nombre de partides jugades, multiplicat per 100.
     * 
     * @return percentatge de victòries (0 si no s'ha jugat cap partida)
     */
    public double getPercentatgeVictories() {
        if (partidesJugades == 0) {
            return 0.0;
        }
        return (double) partidesGuanyades / partidesJugades * 100;
    }

    /**
     * Determina i retorna el nivell del jugador en el rànquing basant-se en les seves estadístiques.
     * El nivell es determina segons el percentatge de victòries, la puntuació total i el nombre de partides jugades.
     * 
     * @return nivell del jugador: "Novell", "Principiant", "Intermedi", "Avançat" o "Expert"
     */
    public String getNivellRanking() {
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
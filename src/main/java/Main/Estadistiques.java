package Main;

public class Estadistiques {
    int partidesGuanyades;
    int partidesPerdudes;
    int partidesJugades;
    int puntuacioTotal;
    int puntuacioPromig;
    int nivellRanking;
    
    //al crear unes estadistiques d'un usuari te tot a 0 per defecte;
    public Estadistiques() {
        this.partidesGuanyades = 0;
        this.partidesPerdudes = 0;
        this.partidesJugades = 0;
        this.puntuacioTotal = 0;
        this.puntuacioPromig = 0;
        this.nivellRanking = 0; //ultim? cal calcular
    }

    public int getPartidesGuanyades() {
        return this.partidesGuanyades;
    }

    public int getPartidesPerdudes() {
        return this.partidesPerdudes;
    }

    public int getPartidesJugades() {
        return this.partidesJugades;
    }

    public int getPuntuacioTotal() {
        return this.puntuacioTotal;
    }

    public int getPuntuacioPromig() {
        return this.puntuacioPromig;
    }

    public int getNivellRanking() {
        return this.nivellRanking;
    }

    
}

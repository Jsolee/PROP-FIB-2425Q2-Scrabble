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
}

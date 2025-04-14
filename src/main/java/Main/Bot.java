package Main;

import java.util.ArrayList;

// Aquesta és una clase singleton, ja que només hi haurà una instància del bot al llarg de l'execució del programa.
public class Bot extends Usuari{
    // Variable que conté l'única instancia del bot.
    private static Bot instancia;

    private Bot() {
    }

    public static Bot getInstancia() {
        if (instancia == null) {
            instancia = new Bot();
        }
        return instancia;
    }

//    public void getMillorJugada(Taulell taulell, Diccionari diccionari, ArrayList<Fitxa> atril, ArrayList<>) {
//
//    }

}

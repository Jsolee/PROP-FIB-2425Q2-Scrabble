package Main;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BotTest {

    @Test
    public void getInstanceRetornaElMateixBot() {
        Bot instance1 = Bot.getInstance();
        Bot instance2 = Bot.getInstance();

        assertSame("getInstance ha de retornar sempre la mateixa instància", instance1, instance2);
    }

    @Test
    public void botEsInstanciaDUsuari() {
        Bot bot = Bot.getInstance();

        assertTrue("Bot ha de ser una instancia d'usuari", bot instanceof Usuari);
    }

    @Test
    public void botNomEsCorrecte() {
        Bot bot = Bot.getInstance();

        assertEquals("Bot ha de dir-se 'bot'", "bot", bot.getNom());
    }

    @Test
    public void getMillorJugadaEnTaulellBuit() {
        System.out.println("getMillorJugadaEnTaulellBuit");

        Bot bot = Bot.getInstance();

        Taulell taulell = new Taulell();
        Diccionari diccionari = new Diccionari("castellano");
        ArrayList<Fitxa> atril = new ArrayList<>();
        Bossa bossa = new Bossa("castellano");

        atril.add(new Fitxa("P", 1));
        atril.add(new Fitxa("R", 1));
        atril.add(new Fitxa("U", 1));
        atril.add(new Fitxa("A", 1));
        atril.add(new Fitxa("E", 1));
        atril.add(new Fitxa("B", 1));


        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> result = bot.getMillorJugada(taulell, diccionari, atril, bossa.getAlfabet());

        for (Map.Entry<int[], Fitxa> entry : result.getKey().entrySet()) {
            int[] pos = entry.getKey();
            Fitxa fitxa = entry.getValue();
            System.out.println("Posició: " + pos[0] + ", " + pos[1] + " - Fitxa: " + fitxa);
        }
        System.out.println("-------------------------------------------");

    }

    /*/
    @Test
    public void getMillorJugadaAcrossEnTaulellNoBuit() {

        System.out.println("getMillorJugadaAcrossEnTaulellNoBuit");
        Bot bot = Bot.getInstance();

        Taulell taulell = new Taulell();
        Diccionari diccionari = new Diccionari("castellano");
        ArrayList<Fitxa> atril = new ArrayList<>();
        Bossa bossa = new Bossa("castellano");

        // Primera jugada: colocar "ES" en el tablero
        taulell.colocarFitxa(7, 7, new Fitxa("E", 1));
        taulell.colocarFitxa(7, 8, new Fitxa("S", 1));

        // Fichas del bot para la segunda jugada
        atril.add(new Fitxa("O", 1));

        // El bot realiza su jugada
        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> result = bot.getMillorJugada(taulell, diccionari, atril, bossa.getAlfabet());

        for (Map.Entry<int[], Fitxa> entry : result.getKey().entrySet()) {
            int[] pos = entry.getKey();
            Fitxa fitxa = entry.getValue();
            System.out.println("Posició: " + pos[0] + ", " + pos[1] + " - Fitxa: " + fitxa);
        }
        System.out.println("-------------------------------------------");

    }

    @Test
    public void getMillorJugadaDownEnTaulellNoBuit() {
        System.out.println("getMillorJugadaDownEnTaulellNoBuit");
        Bot bot = Bot.getInstance();

        Taulell taulell = new Taulell();
        Diccionari diccionari = new Diccionari("castellano");
        ArrayList<Fitxa> atril = new ArrayList<>();
        Bossa bossa = new Bossa("castellano");

        // Primera jugada: colocar "ES" en el tablero
        taulell.colocarFitxa(7, 7, new Fitxa("E", 1));
        taulell.colocarFitxa(8, 7, new Fitxa("S", 1));

        // Fichas del bot para la segunda jugada
        atril.add(new Fitxa("O", 1));

        // El bot realiza su jugada
        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> result = bot.getMillorJugada(taulell, diccionari, atril, bossa.getAlfabet());

        for (Map.Entry<int[], Fitxa> entry : result.getKey().entrySet()) {
            int[] pos = entry.getKey();
            Fitxa fitxa = entry.getValue();
            System.out.println("Posició: " + pos[0] + ", " + pos[1] + " - Fitxa: " + fitxa);
        }
        System.out.println("-------------------------------------------");

    }*/
}
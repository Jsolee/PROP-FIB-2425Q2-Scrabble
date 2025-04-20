package Main;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BotTest {

    @Test
    public void getInstanceReturnsSameInstance() {
        Bot instance1 = Bot.getInstance();
        Bot instance2 = Bot.getInstance();

        assertSame("getInstance should always return the same instance", instance1, instance2);
    }

    @Test
    public void botIsInstanceOfUsuari() {
        Bot bot = Bot.getInstance();

        assertTrue("Bot should be an instance of Usuari", bot instanceof Usuari);
    }

    @Test
    public void botNameIsSetCorrectly() {
        Bot bot = Bot.getInstance();

        assertEquals("Bot should be initialized with name 'bot'", "bot", bot.getNom());
    }

    @Test
    public void getMillorJugadaEnTaulellVacio() {
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


    }
}
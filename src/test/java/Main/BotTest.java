package Main;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BotTest {

    private Bot bot;
    private Taulell taulell;
    private Diccionari diccionari;
    private ArrayList<Fitxa> atril;
    private Bossa bossa;

    @Before
    public void setUp() {
        bot = Bot.getInstance();
        taulell = new Taulell();
        diccionari = new Diccionari("castellano");
        bossa = new Bossa("castellano");
        atril = new ArrayList<>();
    }

    @Test
    public void getInstanceRetornaElMateixBot() {
        Bot instance2 = Bot.getInstance();

        assertSame("getInstance ha de retornar sempre la mateixa instància", bot, instance2);
    }


    @Test
    public void botEsInstanciaDUsuari() {

        assertTrue("Bot ha de ser una instancia d'usuari", bot instanceof Usuari);
    }

    @Test
    public void botNomEsCorrecte() {

        assertEquals("Bot ha de dir-se 'bot'", "bot", bot.getNom());
    }

    // test per debuggar que no fa cap assert
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

    @Test
    public void tornaJugadaValidaAmbFixesSimples() {
        // Dona fitxes al bot per formar una paraula vàlida
        atril.add(new Fitxa("C", 3));
        atril.add(new Fitxa("A", 1));
        atril.add(new Fitxa("S", 1));
        atril.add(new Fitxa("A", 1));

        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> resultat = bot.getMillorJugada(taulell, diccionari, atril, bossa.getAlfabet());

        assertNotNull("El bot hauria de trobar una jugada", resultat);
        assertFalse("La jugada no hauria de ser buida", resultat.getKey().isEmpty());
    }

    @Test
    public void gestionaFixaComodí() {
        // Dona fitxes normals i un comodí
        atril.add(new Fitxa("C", 3));
        atril.add(new Fitxa("A", 1));
        atril.add(new Fitxa("S", 1));
        atril.add(new Fitxa("#", 0)); // Comodí

        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> resultat = bot.getMillorJugada(taulell, diccionari, atril, bossa.getAlfabet());

        // Comprovar que el bot troba una jugada
        assertNotNull("El bot hauria de trobar una jugada amb un comodí", resultat);
        assertFalse("La jugada amb comodí no hauria de ser buida", resultat.getKey().isEmpty());
    }

    @Test
    public void retornaJugadaBuidaQuanNoHiHaOpcions() {
        // Dona fitxes que no formen cap paraula coneguda
        atril.add(new Fitxa("X", 8));
        atril.add(new Fitxa("Z", 10));
        atril.add(new Fitxa("Q", 8));
        atril.add(new Fitxa("Y", 4));

        Map.Entry<LinkedHashMap<int[], Fitxa>, Boolean> resultat = bot.getMillorJugada(taulell, diccionari, atril, bossa.getAlfabet());

        // Al primer moviment sempre hauria de trobar alguna opció, però amb aquest test validem
        // que retorna una estructura de dades correcta encara que no hi hagi jugades
        assertNotNull("El resultat no hauria de ser nul", resultat);
    }
}

package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EstadistiquesTest {

    private Estadistiques estadistiques;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves d'Estadistiques...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves d'Estadistiques.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        estadistiques = new Estadistiques();
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        estadistiques = null;
    }

    @Test
    public void testConstructor() {
        // Verifiquem que els valors per defecte s'estableixen correctament
        assertEquals(0, estadistiques.getParaulesTotals());
        assertEquals(0, estadistiques.getPuntTotal());
        assertEquals(0, estadistiques.getPartidesJugades());
        assertEquals(0, estadistiques.getPartidesGuanyades());
        assertEquals(0, estadistiques.getPartidesPerdudes());
    }

    @Test
    public void testIncrementarParaulesCreades() {
        estadistiques.incrementarParaulesCreades();
        assertEquals(1, estadistiques.getParaulesTotals());

        estadistiques.incrementarParaulesCreades();
        assertEquals(2, estadistiques.getParaulesTotals());
    }

    @Test
    public void testIncrementarPuntTotal() {
        estadistiques.incrementarPuntTotal(5);
        assertEquals(5, estadistiques.getPuntTotal());
        assertEquals(5, estadistiques.getPuntuacioTotal());

        estadistiques.incrementarPuntTotal(10);
        assertEquals(15, estadistiques.getPuntTotal());
        assertEquals(15, estadistiques.getPuntuacioTotal());
    }

    @Test
    public void testIncrementarPartidesJugades() {
        estadistiques.incrementarPartidesJugades();
        assertEquals(1, estadistiques.getPartidesJugades());

        estadistiques.incrementarPartidesJugades();
        assertEquals(2, estadistiques.getPartidesJugades());
    }

    @Test
    public void testIncrementarPartidesGuanyades() {
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        assertEquals(1, estadistiques.getPartidesGuanyades());
        assertEquals(0, estadistiques.getPartidesPerdudes());

        estadistiques.incrementarPartidesJugades();
        assertEquals(1, estadistiques.getPartidesGuanyades());
        assertEquals(1, estadistiques.getPartidesPerdudes());
    }

    @Test
    public void testGetPartidesPerdudes() {
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        assertEquals(1, estadistiques.getPartidesGuanyades());
        assertEquals(1, estadistiques.getPartidesPerdudes());

        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        assertEquals(2, estadistiques.getPartidesGuanyades());
        assertEquals(1, estadistiques.getPartidesPerdudes());
    }

    @Test
    public void testGetPuntuacioPromig() {
        // Sense partides jugades hauria de retornar 0
        assertEquals(0.0, estadistiques.getPuntuacioPromig(), 0.001);

        // Afegim punts i partides
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPuntTotal(100);
        assertEquals(100.0, estadistiques.getPuntuacioPromig(), 0.001);

        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPuntTotal(200);
        assertEquals(150.0, estadistiques.getPuntuacioPromig(), 0.001);
    }

    @Test
    public void testGetPercentatgeVictories() {
        // Sense partides jugades hauria de retornar 0
        assertEquals(0.0, estadistiques.getPercentatgeVictories(), 0.001);

        // 1 partida, 1 victòria = 100%
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        assertEquals(100.0, estadistiques.getPercentatgeVictories(), 0.001);

        // 2 partides, 1 victòria = 50%
        estadistiques.incrementarPartidesJugades();
        assertEquals(50.0, estadistiques.getPercentatgeVictories(), 0.001);

        // 4 partides, 3 victòries = 75%
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        assertEquals(75.0, estadistiques.getPercentatgeVictories(), 0.001);
    }

    @Test
    public void testGetNivellRankingNovell() {
        // Menys de 5 partides = Novell
        assertEquals("Novell", estadistiques.getNivellRanking());

        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        estadistiques.incrementarPuntTotal(1500);
        assertEquals("Novell", estadistiques.getNivellRanking());

        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        estadistiques.incrementarPartidesJugades();
        estadistiques.incrementarPartidesGuanyades();
        estadistiques.incrementarPartidesJugades();
        assertEquals("Novell", estadistiques.getNivellRanking());
    }

    @Test
    public void testGetNivellRankingPrincipiante() {
        // 5 partides, 1 victòria (20%), 100 punts = Principiant
        for (int i = 0; i < 5; i++) {
            estadistiques.incrementarPartidesJugades();
        }
        estadistiques.incrementarPartidesGuanyades();
        estadistiques.incrementarPuntTotal(100);

        assertEquals("Principiant", estadistiques.getNivellRanking());
    }

    @Test
    public void testGetNivellRankingIntermedio() {
        // 10 partides, 4 victòries (40%), 300 punts = Intermedi
        for (int i = 0; i < 10; i++) {
            estadistiques.incrementarPartidesJugades();
        }
        for (int i = 0; i < 4; i++) {
            estadistiques.incrementarPartidesGuanyades();
        }
        estadistiques.incrementarPuntTotal(300);

        assertEquals("Intermedi", estadistiques.getNivellRanking());
    }

    @Test
    public void testGetNivellRankingAvanzado() {
        // 10 partides, 6 victòries (60%), 600 punts = Avançat
        for (int i = 0; i < 10; i++) {
            estadistiques.incrementarPartidesJugades();
        }
        for (int i = 0; i < 6; i++) {
            estadistiques.incrementarPartidesGuanyades();
        }
        estadistiques.incrementarPuntTotal(600);

        assertEquals("Avançat", estadistiques.getNivellRanking());
    }

    @Test
    public void testGetNivellRankingExperto() {
        // 10 partides, 8 victòries (80%), 1200 punts = Expert
        for (int i = 0; i < 10; i++) {
            estadistiques.incrementarPartidesJugades();
        }
        for (int i = 0; i < 8; i++) {
            estadistiques.incrementarPartidesGuanyades();
        }
        estadistiques.incrementarPuntTotal(1200);

        assertEquals("Expert", estadistiques.getNivellRanking());
    }
}
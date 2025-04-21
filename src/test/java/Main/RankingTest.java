/*package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class RankingTest {

    private Ranking ranking;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de Ranking...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de Ranking.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        ranking = new Ranking();
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        ranking = null;
    }

    @Test
    public void testConstructor() {
        assertEquals(0, ranking.getRecordPersonal());
        assertEquals(0, ranking.getPartidesJugades());
        assertEquals(0.0, ranking.getMitjana(), 0.001);
        assertTrue(ranking.getTotesPuntuacions().isEmpty());
    }

    @Test
    public void testAfegirPuntuacio() {
        ranking.afegirPuntuacio(100);

        assertEquals(1, ranking.getPartidesJugades());
        assertEquals(100, ranking.getRecordPersonal());
        assertEquals(100.0, ranking.getMitjana(), 0.001);
        assertEquals(1, ranking.getTotesPuntuacions().size());
        assertEquals(Integer.valueOf(100), ranking.getTotesPuntuacions().get(0));
    }

    @Test
    public void testAfegirMultiplesPuntuacions() {
        ranking.afegirPuntuacio(100);
        ranking.afegirPuntuacio(200);
        ranking.afegirPuntuacio(150);

        assertEquals(3, ranking.getPartidesJugades());
        assertEquals(200, ranking.getRecordPersonal());
        assertEquals(150.0, ranking.getMitjana(), 0.001);
        assertEquals(3, ranking.getTotesPuntuacions().size());
    }

    @Test
    public void testRecordPersonal() {
        ranking.afegirPuntuacio(100);
        assertEquals(100, ranking.getRecordPersonal());

        ranking.afegirPuntuacio(50);
        assertEquals(100, ranking.getRecordPersonal()); // Encara 100, no s'actualitza

        ranking.afegirPuntuacio(200);
        assertEquals(200, ranking.getRecordPersonal()); // Actualitzat a 200
    }

    @Test
    public void testGetUltimsResultats() {
        // Afegim 5 puntuacions
        ranking.afegirPuntuacio(10);
        ranking.afegirPuntuacio(20);
        ranking.afegirPuntuacio(30);
        ranking.afegirPuntuacio(40);
        ranking.afegirPuntuacio(50);

        // Obtenim les últimes 3 puntuacions
        List<Integer> ultims = ranking.getUltimsResultats(3);
        assertEquals(3, ultims.size());
        assertEquals(Integer.valueOf(30), ultims.get(0));
        assertEquals(Integer.valueOf(40), ultims.get(1));
        assertEquals(Integer.valueOf(50), ultims.get(2));

        // Provem quan es demanen més resultats dels disponibles
        List<Integer> tots = ranking.getUltimsResultats(10);
        assertEquals(5, tots.size()); // Hauria de retornar les 5 puntuacions
    }

    @Test
    public void testGetUltimsResultatsEmpty() {
        List<Integer> resultats = ranking.getUltimsResultats(5);
        assertTrue(resultats.isEmpty());
    }

    @Test
    public void testGetTotesPuntuacions() {
        ranking.afegirPuntuacio(100);
        ranking.afegirPuntuacio(200);

        List<Integer> puntuacions = ranking.getTotesPuntuacions();
        assertEquals(2, puntuacions.size());
        assertEquals(Integer.valueOf(100), puntuacions.get(0));
        assertEquals(Integer.valueOf(200), puntuacions.get(1));

        // Verifiquem que la llista retornada és una còpia
        puntuacions.add(300);
        assertEquals(2, ranking.getTotesPuntuacions().size()); // Llista original sense canvis
    }

    @Test
    public void testMitjanaCalculation() {
        ranking.afegirPuntuacio(100);
        assertEquals(100.0, ranking.getMitjana(), 0.001);

        ranking.afegirPuntuacio(200);
        assertEquals(150.0, ranking.getMitjana(), 0.001);

        ranking.afegirPuntuacio(150);
        assertEquals(150.0, ranking.getMitjana(), 0.001);

        ranking.afegirPuntuacio(50);
        assertEquals(125.0, ranking.getMitjana(), 0.001);
    }

    @Test
    public void testToString() {
        ranking.afegirPuntuacio(100);
        ranking.afegirPuntuacio(200);

        String expected = "Ranking [partidesJugades=2, recordPersonal=200, mitjana=150,00]";
        assertEquals(expected, ranking.toString());
    }
}*/
package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class RankingTest {

    private Ranking ranking;
    private Persona persona1;
    private Persona persona2;
    private Persona persona3;

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
        ranking = new Ranking();
        persona1 = new Persona("Marta", "marta@example.com", "password123", "25", "Spain");
        persona2 = new Persona("Joan", "joan@example.com", "securepass", "30", "Spain");
        persona3 = new Persona("Laura", "laura@example.com", "mypassword", "28", "Spain");

        // Configurar estadístiques per persona1
        Estadistiques est1 = persona1.getEstadistiques();
        est1.incrementarPuntTotal(100);
        for (int i = 0; i < 10; i++) est1.incrementarPartidesJugades();
        for (int i = 0; i < 7; i++) est1.incrementarPartidesGuanyades();
        est1.actualitzarRecordPersonal(30);
        for (int i = 0; i < 50; i++) est1.incrementarParaulesCreades();

        // Configurar estadístiques per persona2
        Estadistiques est2 = persona2.getEstadistiques();
        est2.incrementarPuntTotal(150);
        for (int i = 0; i < 15; i++) est2.incrementarPartidesJugades();
        for (int i = 0; i < 10; i++) est2.incrementarPartidesGuanyades();
        est2.actualitzarRecordPersonal(40);
        for (int i = 0; i < 75; i++) est2.incrementarParaulesCreades();

        // Configurar estadístiques per persona3
        Estadistiques est3 = persona3.getEstadistiques();
        est3.incrementarPuntTotal(120);
        for (int i = 0; i < 12; i++) est3.incrementarPartidesJugades();
        for (int i = 0; i < 8; i++) est3.incrementarPartidesGuanyades();
        est3.actualitzarRecordPersonal(35);
        for (int i = 0; i < 60; i++) est3.incrementarParaulesCreades();
    }

    @After
    public void tearDown() {
        ranking = null;
        persona1 = null;
        persona2 = null;
        persona3 = null;
    }

    @Test
    public void constructorInicialitzaLlistesCorrectes() {
        assertTrue(ranking.getRankingPuntsTotals().isEmpty());
        assertTrue(ranking.getRankingPartidesJugades().isEmpty());
        assertTrue(ranking.getRankingPartidesGuanyades().isEmpty());
        assertTrue(ranking.getRankingRecordPersonal().isEmpty());
        assertTrue(ranking.getRankingParaulesTotals().isEmpty());
    }

    @Test
    public void afegirPersonaAfegeixATotsElsRankings() {
        ranking.afegirPersona(persona1);

        assertEquals(1, ranking.getRankingPuntsTotals().size());
        assertEquals(1, ranking.getRankingPartidesJugades().size());
        assertEquals(1, ranking.getRankingPartidesGuanyades().size());
        assertEquals(1, ranking.getRankingRecordPersonal().size());
        assertEquals(1, ranking.getRankingParaulesTotals().size());

        assertEquals(persona1, ranking.getRankingPuntsTotals().get(0));
    }

    @Test
    public void afegirPersonaNoAfegeixDuplicats() {
        ranking.afegirPersona(persona1);
        ranking.afegirPersona(persona1);

        assertEquals(1, ranking.getRankingPuntsTotals().size());
    }

    @Test
    public void actualitzarRankingOrdenaCorrectament() {
        ranking.afegirPersona(persona1);
        ranking.afegirPersona(persona2);
        ranking.afegirPersona(persona3);

        List<Persona> puntsTotals = ranking.getRankingPuntsTotals();
        assertEquals("Joan", puntsTotals.get(0).getNom());
        assertEquals("Laura", puntsTotals.get(1).getNom());
        assertEquals("Marta", puntsTotals.get(2).getNom());

        List<Persona> recordPersonal = ranking.getRankingRecordPersonal();
        assertEquals("Joan", recordPersonal.get(0).getNom());
        assertEquals("Laura", recordPersonal.get(1).getNom());
        assertEquals("Marta", recordPersonal.get(2).getNom());
    }

    @Test
    public void esborrarPersonaEliminaDeTotsElsRankings() {
        ranking.afegirPersona(persona1);
        ranking.afegirPersona(persona2);
        ranking.afegirPersona(persona3);

        ranking.esborrarPersona(persona2);

        assertEquals(2, ranking.getRankingPuntsTotals().size());
        assertFalse(ranking.getRankingPuntsTotals().contains(persona2));
        assertFalse(ranking.getRankingPartidesJugades().contains(persona2));
        assertFalse(ranking.getRankingPartidesGuanyades().contains(persona2));
        assertFalse(ranking.getRankingRecordPersonal().contains(persona2));
        assertFalse(ranking.getRankingParaulesTotals().contains(persona2));
    }

    @Test
    public void actualitzarRankingsDespresDeModificarEstadistiques() {
        ranking.afegirPersona(persona1);
        ranking.afegirPersona(persona2);
        ranking.afegirPersona(persona3);

        // Modificar estadístiques de persona1
        Estadistiques est1 = persona1.getEstadistiques();
        est1.incrementarPuntTotal(100); // Ara té 200 punts, més que ningú

        ranking.actualitzarRanking();

        assertEquals("Marta", ranking.getRankingPuntsTotals().get(0).getNom());
    }

    @Test
    public void imprimirRankingMostraResultatsCorrectes() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        ranking.afegirPersona(persona1);
        ranking.afegirPersona(persona2);

        ranking.imprimirRanking(1);  // Punts totals

        System.setOut(originalOut);

        String resultat = outContent.toString();
        assertTrue(resultat.contains("Ranking per número de punts totals"));
        assertTrue(resultat.contains("Joan"));
        assertTrue(resultat.contains("Marta"));
    }

    @Test
    public void getRankingsRetornaReferencies() {
        ranking.afegirPersona(persona1);

        List<Persona> puntsTotals = ranking.getRankingPuntsTotals();
        List<Persona> partidesJugades = ranking.getRankingPartidesJugades();

        assertEquals(1, puntsTotals.size());
        assertEquals(1, partidesJugades.size());

        assertEquals(persona1, puntsTotals.get(0));
        assertEquals(persona1, partidesJugades.get(0));
    }

    @Test
    public void imprimirRankingNumeroInvalid() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        ranking.imprimirRanking(99);  // Número invàlid

        System.setOut(originalOut);

        String resultat = outContent.toString();
        assertTrue(resultat.contains("Número de ranking no vàlid"));
    }
}
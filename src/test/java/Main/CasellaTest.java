package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CasellaTest {

    private Casella casella;
    private Fitxa fitxa;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de Casella...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de Casella.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        casella = new Casella(7, 7, 1, 1);
        fitxa = new Fitxa("A", 1);
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        casella = null;
        fitxa = null;
    }

    @Test
    public void testConstructor() {
        assertEquals(7, casella.getX());
        assertEquals(7, casella.getY());
        assertEquals(1, casella.getMultiplicadorLetra());
        assertEquals(1, casella.getMultiplicadorParaula());
        assertFalse(casella.isOcupada());
        assertNull(casella.getFitxa());
        assertFalse(casella.isEsCasellaInicial());
    }

    @Test
    public void testSetEsCasellaInicial() {
        assertFalse(casella.isEsCasellaInicial());
        casella.setEsCasellaInicial(true);
        assertTrue(casella.isEsCasellaInicial());
    }

    @Test
    public void testIsDobleLetra() {
        Casella casellaDobleLetra = new Casella(0, 0, 2, 1);
        assertTrue(casellaDobleLetra.isDobleLetra());
        assertFalse(casellaDobleLetra.isTripleLetra());
        assertFalse(casellaDobleLetra.isDobleParaula());
        assertFalse(casellaDobleLetra.isTripleParaula());
    }

    @Test
    public void testIsTripleLetra() {
        Casella casellaTripleLetra = new Casella(0, 0, 3, 1);
        assertTrue(casellaTripleLetra.isTripleLetra());
        assertFalse(casellaTripleLetra.isDobleLetra());
        assertFalse(casellaTripleLetra.isDobleParaula());
        assertFalse(casellaTripleLetra.isTripleParaula());
    }

    @Test
    public void testIsDobleParaula() {
        Casella casellaDobleParaula = new Casella(0, 0, 1, 2);
        assertTrue(casellaDobleParaula.isDobleParaula());
        assertFalse(casellaDobleParaula.isTripleParaula());
        assertFalse(casellaDobleParaula.isDobleLetra());
        assertFalse(casellaDobleParaula.isTripleLetra());
    }

    @Test
    public void testIsTripleParaula() {
        Casella casellaTripleParaula = new Casella(0, 0, 1, 3);
        assertTrue(casellaTripleParaula.isTripleParaula());
        assertFalse(casellaTripleParaula.isDobleParaula());
        assertFalse(casellaTripleParaula.isDobleLetra());
        assertFalse(casellaTripleParaula.isTripleLetra());
    }

    @Test
    public void testColocarFitxa() {
        // Prova col·locar una fitxa en una casella buida
        assertTrue(casella.colocarFitxa(fitxa));
        assertTrue(casella.isOcupada());
        assertEquals(fitxa, casella.getFitxa());

        // Prova col·locar una fitxa en una casella ocupada
        Fitxa altraFitxa = new Fitxa("B", 2);
        assertFalse(casella.colocarFitxa(altraFitxa));
        assertEquals(fitxa, casella.getFitxa()); // La primera fitxa hauria de seguir allà
    }

    @Test
    public void testRetirarFitxa() {
        // Prova retirar una fitxa d'una casella buida
        assertNull(casella.retirarFitxa());

        // Prova retirar una fitxa d'una casella ocupada
        casella.colocarFitxa(fitxa);
        Fitxa fitxaRetirada = casella.retirarFitxa();
        assertEquals(fitxa, fitxaRetirada);
        assertFalse(casella.isOcupada());
        assertNull(casella.getFitxa());
    }

    @Test
    public void testToString() {
        // Prova toString per a una casella normal buida
        assertEquals("[  ]", casella.toString());

        // Prova toString per a una casella de doble lletra
        Casella casellaDobleLetra = new Casella(0, 0, 2, 1);
        assertEquals("[DL]", casellaDobleLetra.toString());

        // Prova toString per a una casella de triple lletra
        Casella casellaTripleLetra = new Casella(0, 0, 3, 1);
        assertEquals("[TL]", casellaTripleLetra.toString());

        // Prova toString per a una casella de doble paraula
        Casella casellaDobleParaula = new Casella(0, 0, 1, 2);
        assertEquals("[DP]", casellaDobleParaula.toString());

        // Prova toString per a una casella de triple paraula
        Casella casellaTripleParaula = new Casella(0, 0, 1, 3);
        assertEquals("[TP]", casellaTripleParaula.toString());

        // Prova toString per a una casella inicial
        casella.setEsCasellaInicial(true);
        assertEquals("[★ ]", casella.toString());

        // Prova toString per a una casella ocupada
        casella.colocarFitxa(fitxa);
        assertEquals("[A ]", casella.toString());
    }
}
package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FitxaTest {

    private Fitxa fitxa;
    private Fitxa fitxaBlank;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de Fitxa...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de Fitxa.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        fitxa = new Fitxa("A", 1);
        fitxaBlank = new Fitxa("#", 0);
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        fitxa = null;
        fitxaBlank = null;
    }

    @Test
    public void testConstructor() {
        // Prova fitxa normal
        assertEquals("A", fitxa.getLletra());
        assertEquals(1, fitxa.getValor());
        assertFalse(fitxa.isBlank());

        // Prova fitxa comodí
        assertEquals("#", fitxaBlank.getLletra());
        assertEquals(0, fitxaBlank.getValor());
        assertTrue(fitxaBlank.isBlank());
    }

    @Test
    public void testGetLletra() {
        assertEquals("A", fitxa.getLletra());
        assertEquals("#", fitxaBlank.getLletra());
    }

    @Test
    public void testGetValorLletra() {
        assertEquals("A", fitxa.getValorLletra());
        assertEquals("#", fitxaBlank.getValorLletra());
    }

    @Test
    public void testGetValor() {
        assertEquals(1, fitxa.getValor());
        assertEquals(0, fitxaBlank.getValor());
    }

    @Test
    public void testIsBlank() {
        assertFalse(fitxa.isBlank());
        assertTrue(fitxaBlank.isBlank());
    }

    @Test
    public void testSetLletra() {
        fitxa.setLletra("B");
        assertEquals("B", fitxa.getLletra());
        assertEquals("B", fitxa.getValorLletra());

        fitxaBlank.setLletra("C");
        assertEquals("C", fitxaBlank.getLletra());
        assertEquals("C", fitxaBlank.getValorLletra());
        // Nota: isBlank() es basa en el valor de lletra actual, no en un indicador
        assertFalse(fitxaBlank.isBlank());
    }

    @Test
    public void testToString() {
        assertEquals("A(1)", fitxa.toString());

        // Prova fitxa comodí
        assertEquals("_", fitxaBlank.toString());

        // Prova de canviar una fitxa comodí
        fitxaBlank.setLletra("C");
        assertEquals("C(0)", fitxaBlank.toString());
    }

    @Test
    public void testSpecialCharacters() {
        Fitxa specialFitxa = new Fitxa("Ñ", 8);
        assertEquals("Ñ", specialFitxa.getLletra());
        assertEquals(8, specialFitxa.getValor());
        assertFalse(specialFitxa.isBlank());
        assertEquals("Ñ(8)", specialFitxa.toString());
    }

    @Test
    public void testMultiCharacterTile() {
        // Crear una fitxa amb múltiples caràcters (si és permès)
        Fitxa multiCharFitxa = new Fitxa("CH", 5);
        assertEquals("CH", multiCharFitxa.getLletra());
        assertEquals(5, multiCharFitxa.getValor());
        assertFalse(multiCharFitxa.isBlank());
        assertEquals("CH(5)", multiCharFitxa.toString());
    }
}
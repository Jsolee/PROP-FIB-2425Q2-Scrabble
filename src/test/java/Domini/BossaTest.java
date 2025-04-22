package Domini;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;

public class BossaTest {
    private static Bossa bossa;

    // això s'executa una vegada abans de tots els tests
    @Before
    public void setUp() {
        // Se ejecuta una vez antes de todos los tests
        System.out.println("Iniciando pruebas de Bossa...");
        String nom_bossa = "castellano";
        System.out.println("Crear la bossa " + nom_bossa + ".");
        bossa = new Bossa(nom_bossa);
    }

    @Test
    public void testAlfabet() {
        Set<String> alfabet = bossa.getAlfabet();
        assertNotNull(alfabet);
        assertFalse(alfabet.isEmpty());
        System.out.println("Alfabet: " + alfabet);
    }


    @Test
    public void testAgafarFitxa() {
        Fitxa fitxa = bossa.agafarFitxa();
        assertNotNull(fitxa);
        assertFalse(bossa.esBuida());
    }

    @Test
    public void testRetornarFitxa() {
        // Agafem una fitxa de la bossa
        Fitxa fitxa = bossa.agafarFitxa();
        assertNotNull(fitxa);
        bossa.retornarFitxa(fitxa);
        assertFalse(bossa.esBuida());
    }

    @Test
    public void testConstructor() {
        assertEquals("castellano", bossa.getNom());
        assertFalse(bossa.getFitxes().isEmpty());
        assertTrue(bossa.getQuantitatFitxes() > 0);
        System.out.println("La bossa s'ha inicialitzat correctament amb " + bossa.getQuantitatFitxes() + " fitxes.");
    }

    @Test
    public void testAgafarFitxesMultiples() {
        int quantitatInicial = bossa.getQuantitatFitxes();
        int quantitatAgafar = 7; // Número estándar de fitxes per a un atril

        ArrayList<Fitxa> fitxesAgafades = bossa.agafarFitxes(quantitatAgafar);

        assertEquals(quantitatAgafar, fitxesAgafades.size());
        assertEquals(quantitatInicial - quantitatAgafar, bossa.getQuantitatFitxes());
        System.out.println("S'han agafat " + quantitatAgafar + " fitxes correctament.");
    }

    @Test
    public void testAgafarMesFitxesQueLesDisponibles() {
        int quantitatTotal = bossa.getQuantitatFitxes();

        var fitxesAgafades = bossa.agafarFitxes(quantitatTotal + 10);

        assertEquals(quantitatTotal, fitxesAgafades.size());
        assertTrue(bossa.esBuida());
        System.out.println("La bossa s'ha buidat completament en intentar agafar més fitxes de les disponibles.");
    }

    @Test
    public void testEsBuida() {
        assertFalse("La bossa no hauria d'estar buida inicialment", bossa.esBuida());

        // Buidem la bossa
        int quantitatTotal = bossa.getQuantitatFitxes();
        bossa.agafarFitxes(quantitatTotal);

        assertTrue("La bossa hauria d'estar buida després d'agafar totes les fitxes", bossa.esBuida());

        // Retornem una fitxa
        bossa.retornarFitxa(new Fitxa("A", 1));
        assertFalse("La bossa no hauria d'estar buida després de retornar una fitxa", bossa.esBuida());
    }
}

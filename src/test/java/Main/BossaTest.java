package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class BossaTest {
    private static Bossa bossa;

    // això s'executa una vegada abans de tots els tests
    @BeforeClass
    public static void setUpClass() {
        // Se ejecuta una vez antes de todos los tests
        System.out.println("Iniciando pruebas de Bossa...");
        String nom_bossa = "english";
        System.out.println("Crear la bossa " + nom_bossa + ".");
        bossa = new Bossa(nom_bossa);
    }

    // això s'executa una vegada després de tots els tests
    @AfterClass
    public static void tearDownClass() {
        // Se ejecuta una vez al terminar todos los tests
        System.out.println("Finalizadas las pruebas de Bossa.");
    }

    @Test
    public void testVacio() {
        LinkedList<Fitxa> fitxes = bossa.getFitxes();
        for (Fitxa fitxa : fitxes) {
            System.out.println(fitxa);
        }
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
}

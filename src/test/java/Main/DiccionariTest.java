package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DiccionariTest {

    private Diccionari dic;

    // això s'executa una vegada abans de tots els tests
    @BeforeClass
    public static void setUpClass() {
        // Se ejecuta una vez antes de todos los tests
        System.out.println("Iniciando pruebas de Diccionari...");
    }

    // això s'executa una vegada després de tots els tests
    @AfterClass
    public static void tearDownClass() {
        // Se ejecuta una vez al terminar todos los tests
        System.out.println("Finalizadas las pruebas de Diccionari.");
    }

    // aixo s'executa abans de cada test
    @Before
    public void setUp() {
        // Se ejecuta antes de cada test.
        // Configura el entorno de prueba.
        dic = new Diccionari("english");
    }

    // aixo s'executa després de cada test
    @After
    public void tearDown() {
        // Se ejecuta después de cada test.
        // Se pueden liberar recursos aquí.
        dic = null;
    }

    //tests

    @Test
    public void testGetNom() {
        // Comprueba que el nombre se ha asignado correctamente
        assertEquals("english", dic.getNom());
    }

}


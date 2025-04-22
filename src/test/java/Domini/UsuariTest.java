package Domini;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UsuariTest {

    // Implementació concreta per proves
    private static class UsuariConcret extends Usuari {
        public UsuariConcret(String nom) {
            super(nom);
        }
    }

    private Usuari usuari;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves d'Usuari...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves d'Usuari.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        usuari = new UsuariConcret("Test User");
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        usuari = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("Test User", usuari.getNom());
    }

    @Test
    public void testGetNom() {
        assertEquals("Test User", usuari.getNom());
    }

    @Test
    public void testSetNom() {
        usuari.setNom("New Name");
        assertEquals("New Name", usuari.getNom());
    }

    @Test
    public void testSetNomEmpty() {
        usuari.setNom("");
        assertEquals("", usuari.getNom());
    }

    @Test
    public void testSetNomNull() {
        usuari.setNom(null);
        assertNull(usuari.getNom());
    }
}
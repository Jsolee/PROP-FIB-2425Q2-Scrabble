package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class PersonaTest {

    private Persona persona;
    private Partida partida;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de Persona...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de Persona.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        persona = new Persona("Test User", "test@example.com", "password123", "25", "Spain");
        partida = new Partida("Test Game", "catalan");
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        persona = null;
        partida = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("Test User", persona.getNom());
        assertEquals("test@example.com", persona.getCorreu());
        assertEquals("password123", persona.getContrasenya());
        assertTrue(persona.teSessioIniciada());
        assertNotNull(persona.getPartidesEnCurs());
        assertTrue(persona.getPartidesEnCurs().isEmpty());
        assertNotNull(persona.getEstadistiques());
    }

    @Test
    public void testGetCorreu() {
        assertEquals("test@example.com", persona.getCorreu());
    }

    @Test
    public void testGetContrasenya() {
        assertEquals("password123", persona.getContrasenya());
    }

    @Test
    public void testSetContrasenya() {
        assertTrue(persona.setContrasenya("newPassword456"));
        assertEquals("newPassword456", persona.getContrasenya());
    }

    @Test
    public void testTeSessioIniciada() {
        assertTrue(persona.teSessioIniciada());

        persona.tancarSessio();
        assertFalse(persona.teSessioIniciada());

        persona.iniciarSessio();
        assertTrue(persona.teSessioIniciada());
    }

    @Test
    public void testTancarSessio() {
        assertTrue(persona.teSessioIniciada());

        persona.tancarSessio();
        assertFalse(persona.teSessioIniciada());
    }

    @Test
    public void testIniciarSessio() {
        persona.tancarSessio();
        assertFalse(persona.teSessioIniciada());

        persona.iniciarSessio();
        assertTrue(persona.teSessioIniciada());
    }

    @Test
    public void testGetPartidesEnCurs() {
        ArrayList<Partida> partides = persona.getPartidesEnCurs();
        assertNotNull(partides);
        assertTrue(partides.isEmpty());
    }

    @Test
    public void testBorrarPartidaEnCurs() {
        // Afegim una partida primer
        persona.getPartidesEnCurs().add(partida);
        assertEquals(1, persona.getPartidesEnCurs().size());

        // Després l'eliminem
        persona.borrarPartidaEnCurs(partida);
        assertEquals(0, persona.getPartidesEnCurs().size());
    }

    @Test
    public void testGetEstadistiques() {
        Estadistiques stats = persona.getEstadistiques();
        assertNotNull(stats);
        assertEquals(0, stats.getParaulesTotals());
        assertEquals(0, stats.getPuntTotal());
        assertEquals(0, stats.getPartidesJugades());
    }

    @Test
    public void testInheritanceFromUsuari() {
        // Comprovem que Persona hereta correctament d'Usuari
        assertTrue(persona instanceof Usuari);
        assertEquals("Test User", ((Usuari)persona).getNom());
    }
}
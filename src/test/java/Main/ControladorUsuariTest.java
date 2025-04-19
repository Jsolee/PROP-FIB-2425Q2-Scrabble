package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ControladorUsuariTest {

    private ControladorUsuari controladorUsuari;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de ControladorUsuari...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de ControladorUsuari.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        controladorUsuari = new ControladorUsuari();
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        controladorUsuari = null;
    }

    @Test
    public void testRegistrarPersona() {
        // Registrem un nou usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Verifiquem que l'usuari s'ha afegit al mapa
        assertNotNull(persona);
        assertEquals("John Doe", persona.getNom());
        assertEquals("john@example.com", persona.getCorreu());
        assertEquals("password123", persona.getContrasenya());
        assertTrue(persona.teSessioIniciada());

        // Verifiquem que podem recuperar l'usuari
        assertTrue(controladorUsuari.getUsuaris().containsKey("john@example.com"));
        assertEquals(persona, controladorUsuari.getUsuaris().get("john@example.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarPersonaWithExistingUsername() {
        // Primer registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Intentem registrar un altre usuari amb el mateix nom d'usuari
        controladorUsuari.registrarPersona("Another John", "john@example.com", "different_password");
    }

    @Test
    public void testExisteixUsuari() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Verifiquem que existeixUsuari retorna l'usuari correcte
        Usuari foundUser = controladorUsuari.existeixUsuari("john@example.com");
        assertEquals(persona, foundUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExisteixUsuariNonExistent() {
        // Intentem trobar un usuari inexistent
        controladorUsuari.existeixUsuari("nonexistent@example.com");
    }

    @Test
    public void testIniciarSessio() {
        // Registrem un usuari i tanquem la seva sessió
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");
        persona.tancarSessio();
        assertFalse(persona.teSessioIniciada());

        // Iniciem sessió
        boolean result = controladorUsuari.iniciarSessio("john@example.com", "password123");

        // Verifiquem que l'inici de sessió ha estat exitós
        assertTrue(result);
        assertTrue(persona.teSessioIniciada());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIniciarSessioWrongPassword() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Intentem iniciar sessió amb una contrasenya incorrecta
        controladorUsuari.iniciarSessio("john@example.com", "wrong_password");
    }

    @Test
    public void testEliminarCompte() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Eliminem el compte
        controladorUsuari.eliminarCompte("john@example.com");

        // Verifiquem que el compte s'ha eliminat
        assertFalse(controladorUsuari.getUsuaris().containsKey("john@example.com"));
    }

    @Test
    public void testTancarSessio() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");
        assertTrue(persona.teSessioIniciada());

        // Tanquem la sessió
        controladorUsuari.tancarSessio("john@example.com");

        // Verifiquem que la sessió s'ha tancat
        assertFalse(persona.teSessioIniciada());
    }

    @Test
    public void testRestablirContrasenya() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Canviem la contrasenya
        boolean result = controladorUsuari.restablirContrasenya("john@example.com", "password123", "new_password");

        // Verifiquem que la contrasenya s'ha canviat
        assertTrue(result);
        assertEquals("new_password", persona.getContrasenya());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestablirContrasenyaWrongPassword() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Intentem canviar la contrasenya amb una contrasenya actual incorrecta
        controladorUsuari.restablirContrasenya("john@example.com", "wrong_password", "new_password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestablirContrasenyaSamePassword() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Intentem canviar a la mateixa contrasenya
        controladorUsuari.restablirContrasenya("john@example.com", "password123", "password123");
    }

    @Test
    public void testGetPartides() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Obtenim les partides (haurien d'estar buides inicialment)
        List<Partida> partides = controladorUsuari.getPartides(persona);
        assertNotNull(partides);
        assertTrue(partides.isEmpty());
    }

    @Test
    public void testVeurePerfil() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123");

        // Veiem el perfil
        boolean result = controladorUsuari.veurePerfil("john@example.com");
        assertTrue(result);
    }
}
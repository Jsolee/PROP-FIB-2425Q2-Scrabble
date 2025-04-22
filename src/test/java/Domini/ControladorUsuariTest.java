package Domini;

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
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Verifiquem que l'usuari s'ha afegit al mapa
        assertNotNull(persona);
        assertEquals("John Doe", persona.getNom());
        assertEquals("john@example.com", persona.getCorreu());
        assertEquals("password123", persona.getContrasenya());
        assertTrue(persona.teSessioIniciada());

        // Verifiquem que podem recuperar l'usuari
        assertTrue(controladorUsuari.getUsuaris().containsKey("John Doe"));
        assertEquals(persona, controladorUsuari.getUsuaris().get("John Doe"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarPersonaWithExistingUsername() {
        // Primer registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Intentem registrar un altre usuari amb el mateix nom d'usuari
        controladorUsuari.registrarPersona("John Doe", "john2@example.com", "different_password", "30", "USA");
    }

    @Test
    public void testExisteixUsuari() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Verifiquem que existeixUsuari retorna l'usuari correcte
        Usuari foundUser = controladorUsuari.existeixUsuari("John Doe");
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
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");
        persona.tancarSessio();
        assertFalse(persona.teSessioIniciada());

        // Iniciem sessió
        boolean result = controladorUsuari.iniciarSessio("John Doe", "password123");

        // Verifiquem que l'inici de sessió ha estat exitós
        assertTrue(result);
        assertTrue(persona.teSessioIniciada());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIniciarSessioWrongPassword() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Intentem iniciar sessió amb una contrasenya incorrecta
        controladorUsuari.iniciarSessio("john@example.com", "wrong_password");
    }

    @Test
    public void testEliminarCompte() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Eliminem el compte
        controladorUsuari.eliminarCompte("John Doe");

        // Verifiquem que el compte s'ha eliminat
        assertFalse(controladorUsuari.getUsuaris().containsKey("John Doe"));
    }

    @Test
    public void testTancarSessio() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");
        assertTrue(persona.teSessioIniciada());

        // Tanquem la sessió
        controladorUsuari.tancarSessio("John Doe");

        // Verifiquem que la sessió s'ha tancat
        assertFalse(persona.teSessioIniciada());
    }

    @Test
    public void testRestablirContrasenya() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Canviem la contrasenya
        boolean result = controladorUsuari.restablirContrasenya("John Doe", "password123", "new_password");

        // Verifiquem que la contrasenya s'ha canviat
        assertTrue(result);
        assertEquals("new_password", persona.getContrasenya());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestablirContrasenyaWrongPassword() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Intentem canviar la contrasenya amb una contrasenya actual incorrecta
        controladorUsuari.restablirContrasenya("John Doe", "wrong_password", "new_password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRestablirContrasenyaSamePassword() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Intentem canviar a la mateixa contrasenya
        controladorUsuari.restablirContrasenya("John Doe", "password123", "password123");
    }

    @Test
    public void testGetPartides() {
        // Registrem un usuari
        Persona persona = controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Obtenim les partides (haurien d'estar buides inicialment)
        List<Partida> partides = controladorUsuari.getPartides(persona);
        assertNotNull(partides);
        assertTrue(partides.isEmpty());
    }

    @Test
    public void testVeurePerfil() {
        // Registrem un usuari
        controladorUsuari.registrarPersona("John Doe", "john@example.com", "password123", "25", "Spain");

        // Veiem el perfil
        boolean result = controladorUsuari.veurePerfil("John Doe");
        assertTrue(result);
    }
}
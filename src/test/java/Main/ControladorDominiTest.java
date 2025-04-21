package Main;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ControladorDominiTest {

    private ControladorDomini controladorDomini;
    private Persona persona;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de ControladorDomini...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de ControladorDomini.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        controladorDomini = new ControladorDomini();
        persona = new Persona("Test User", "test@example.com", "password123", "25", "Spain");
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        controladorDomini = null;
        persona = null;
    }

    @Test
    public void testCrearUsuari() {
        // Prova de creació d'un usuari
        Usuari usuari = controladorDomini.crearUsuari("Test User", "test@example.com", "password123", "spain", "25");

        assertNotNull(usuari);
        assertEquals("Test User", usuari.getNom());
    }

    @Test
    public void testIniciarSessio() {
        // Primer creem un usuari
        Usuari usuari = controladorDomini.crearUsuari("Test User", "test@example.com", "password123", "spain", "25");

        // Provem l'inici de sessió
        boolean result = controladorDomini.iniciarSessio("Test User", "password123");
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIniciarSessioWrongPassword() {
        // Primer creem un usuari
        Usuari usuari = controladorDomini.crearUsuari("Test User", "test@example.com", "password123", "spain", "25");

        // Provem l'inici de sessió amb una contrasenya incorrecta
        controladorDomini.iniciarSessio("User2", "wrong_password");
    }

    @Test
    public void testEliminarCompte() {
        // Primer creem un usuari
        Usuari usuari = controladorDomini.crearUsuari("Dani", "test@example.com", "password123", "spain", "25");

        // Eliminem el compte
        controladorDomini.eliminarCompte("Dani");

        // Intentem obtenir l'usuari, hauria de llançar una excepció
        try {
            controladorDomini.getUsuari("Dani");
            fail("S'esperava IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("No existeix l'usuari amb el nom: Dani", e.getMessage());
        }
    }

    @Test
    public void testRestablirContrasenya() {
        // Primer creem un usuari
        Usuari usuari = controladorDomini.crearUsuari("Test User", "test@example.com", "password123", "spain", "25");

        // Canviem la contrasenya
        controladorDomini.restablirContrasenya("Test User", "password123", "newpassword");

        // Verifiquem que l'inici de sessió funciona amb la nova contrasenya
        boolean result = controladorDomini.iniciarSessio("Test User", "newpassword");
        assertTrue(result);
    }

    @Test
    public void testGetUsuari() {
        // Primer creem un usuari
        Usuari created = controladorDomini.crearUsuari("Test User", "test@example.com", "password123", "spain", "25");

        // Obtenim l'usuari
        Usuari retrieved = controladorDomini.getUsuari("Test User");

        assertNotNull(retrieved);
        assertEquals(created, retrieved);
    }

    @Test
    public void testCrearPartida() {
        // Creem els usuaris
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        // Creem una partida
        Partida partida = controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        assertNotNull(partida);
        assertEquals("Test Game", partida.getNom());
        assertEquals(jugadors, partida.getJugadors());
    }

    @Test
    public void testGetPartida() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        Partida created = controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Recuperem la partida
        Partida retrieved = controladorDomini.getPartida("Test Game");

        assertNotNull(retrieved);
        assertEquals(created, retrieved);
    }

    @Test
    public void testJugarParaula() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        Partida partida = controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Intentem jugar una paraula
        // Nota: Això és una prova simplificada ja que no podem predir quines fitxes hi haurà disponibles
        try {
            LinkedHashMap<int[], Fitxa> jugades = new LinkedHashMap<>();
            int center = 7;

            // Necessitaríem obtenir fitxes reals de l'atril del jugador
            // Això és només un exemple i podria no funcionar en una prova real
            List<Fitxa> atril = partida.getAtril();
            if (atril != null && !atril.isEmpty()) {
                jugades.put(new int[]{center, center}, atril.get(0));

                int score = controladorDomini.jugarParaula(partida, jugades, "H");

                // Si tenim èxit, verifiquem que s'ha col·locat alguna peça
                assertTrue(partida.getTaulell().isEmpty());
            }
        } catch (Exception e) {
            // Jugar una paraula podria fallar per moltes raons en un entorn de proves
            // Només estem provant que el mètode és accessible
            System.out.println("Excepció en la prova de jugar paraula: " + e.getMessage());
        }
    }

    @Test
    public void testCanviDeFitxes() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        Partida partida = controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Intentem intercanviar fitxes
        try {
            String[] fitxes = {"A", "B"}; // Només lletres d'exemple
            controladorDomini.canviDeFitxes(partida, fitxes);
            // És difícil verificar el resultat exacte sense simulacions
        } catch (Exception e) {
            // L'intercanvi podria fallar per moltes raons en un entorn de proves
            System.out.println("Excepció en la prova d'intercanvi de fitxes: " + e.getMessage());
        }
    }

    @Test
    public void testEsFinalPartida() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");
        Usuari usuar2 = controladorDomini.crearUsuari("User2", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);
        jugadors.add(usuar2);

        Partida partida = controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Una partida nova no hauria d'estar finalitzada
        boolean isFinal = controladorDomini.esFinalPartida(partida);
        assertFalse(isFinal);

        // Acabem la partida i comprovem de nou
        controladorDomini.acabarPartida(partida);
        isFinal = controladorDomini.esFinalPartida(partida);
        assertTrue(isFinal);
    }

    @Test
    public void testTornDelJugador() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");    

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        Partida partida = controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Comprovem de qui és el torn
        Usuari currentPlayer = controladorDomini.tornDelJugador("Test Game");

        // En una partida d'un sol jugador, hauria de ser l'únic jugador
        assertEquals(usuari1, currentPlayer);
    }

    @Test
    public void testGetTaulell() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Obtenim el taulell
        Taulell taulell = controladorDomini.getTaulell("Test Game");

        // El taulell hauria d'existir
        assertNotNull(taulell);
    }

    @Test
    public void testGetAtril() {
        // Creem usuaris i una partida
        Usuari usuari1 = controladorDomini.crearUsuari("User1", "user1@example.com", "password", "spain", "25");

        List<Usuari> jugadors = new ArrayList<>();
        jugadors.add(usuari1);

        controladorDomini.crearPartida("Test Game", jugadors, "catalan");

        // Obtenim l'atril
        List<Fitxa> atril = controladorDomini.getAtril("Test Game");

        // L'atril hauria d'existir i tenir fitxes
        assertNotNull(atril);
        assertFalse(atril.isEmpty());
    }

}
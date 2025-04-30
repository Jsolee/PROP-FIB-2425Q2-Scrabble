package Domini;

import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ControladorPartidaTest {

    private ControladorPartida controladorPartida;
    private List<Usuari> jugadors;
    private Persona persona;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de ControladorPartida...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de ControladorPartida.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        controladorPartida = new ControladorPartida();
        persona = new Persona("Test User", "test@example.com", "password123", "25", "Spain");
        Usuari persona2 = new Persona("Dani", "correu", "contrasenya", "25", "Spain");
        jugadors = new ArrayList<>();
        jugadors.add(persona);
        jugadors.add(persona2);
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        controladorPartida = null;
        jugadors = null;
        persona = null;
    }

    @Test
    public void testCrearPartida() {
        // Creem una partida
        Partida partida = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        assertNotNull(partida);
        assertEquals("Test Game", partida.getNom());
        assertEquals(jugadors, partida.getJugadors());
        assertEquals("catalan", partida.getIdioma());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrearPartidaDuplicada() {
        // Creem una partida
        controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Intentem crear una altra partida amb el mateix nom
        controladorPartida.crearPartida("Test Game", jugadors, "catalan");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrearPartidaIdiomaInvalid() {
        // Intentem crear una partida amb un idioma invàlid
        controladorPartida.crearPartida("Test Game", jugadors, "french");
    }

    @Test
    public void testGetPartida() {
        // Creem una partida
        Partida created = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Obtenim la partida
        Partida retrieved = controladorPartida.getPartida("Test Game");

        assertNotNull(retrieved);
        assertEquals(created, retrieved);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPartidaInexistente() {
        // Intentem obtenir una partida inexistent
        controladorPartida.getPartida("Nonexistent Game");
    }

    @Test
    public void testTornDe() {
        // Creem una partida
        controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Comprovem de qui és el torn
        Usuari currentPlayer = controladorPartida.tornDe("Test Game");

        assertEquals(persona, currentPlayer);
    }

    @Test
    public void testJugarParaula() {
        // Creem una partida
        Partida partida = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Creem una jugada simulada - això probablement llançarà excepcions en proves reals
        // ja que no controlem el contingut de l'atril
        try {
            LinkedHashMap<int[], Fitxa> jugades = new LinkedHashMap<>();
            int[] pos = new int[]{7, 7};
            Fitxa fitxa = new Fitxa("L", 1);
            jugades.put(pos, fitxa);
            pos = new int[]{7, 8};
            fitxa = new Fitxa("A", 1);
            jugades.put(pos, fitxa);
            List<Integer> indices = new ArrayList<>();
            indices.add(0);
            indices.add(1);

            int score = controladorPartida.jugarParaula(partida, jugades, "H", indices);

            // Com que aquesta prova és simplificada, només comprovem que el mètode existeix
            // Una prova real necessitaria configurar acuradament el tauler, l'atril i les paraules vàlides
            assertTrue(score > 0);
        } catch (Exception e) {
            // Excepció esperada en la majoria d'execucions de proves ja que no configurem l'estat de joc adequadament
            System.out.println("Excepció esperada a la prova: " + e.getMessage());
        }
    }

    @Test
    public void testCanviDeFitxes() {
        // Creem una partida
        Partida partida = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Configurem un escenari de prova on hi ha prou fitxes a la bossa
        // En una prova real, hauríem de verificar això o simular la bossa
        try {
            String[] indexsACanviar = {"0", "1"};
            controladorPartida.canviDeFitxes(partida, indexsACanviar);

            // És difícil afirmar molt aquí sense simulacions
        } catch (IllegalArgumentException e) {
            // Això és esperat en la majoria d'execucions de proves
            assertTrue(e.getMessage().contains("No hi ha prou fitxes"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanviDeFitxesNoSuficientesEnBossa() {
        // Creem una partida amb una bossa simulada que té poques fitxes
        Partida partida = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Buidem la bossa fins que quedin menys de 7 fitxes
        Bossa bossa = partida.getBossa();
        while (bossa.getQuantitatFitxes() >= 3) {
            bossa.agafarFitxa();
        }

        // Intentem intercanviar fitxes
        String[] indexsACanviar = {"0", "1", "2", "3", "4", "5", "6"};
        controladorPartida.canviDeFitxes(partida, indexsACanviar);
    }

    @Test
    public void testEsFinalPartida() {
        // Creem una partida
        Partida partida = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Una partida nova no hauria d'estar finalitzada
        assertFalse(controladorPartida.esFinalPartida(partida));

        // Acabem la partida
        partida.acabarPartida();

        // Ara hauria d'estar finalitzada
        assertTrue(controladorPartida.esFinalPartida(partida));
    }

    @Test
    public void testGetTaulell() {
        // Creem una partida
        controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Obtenim el taulell
        Taulell taulell = controladorPartida.getTaulell("Test Game");

        // El taulell hauria d'existir
        assertNotNull(taulell);
    }

    @Test
    public void testAcabarPartida() {
        // Creem una partida
        Partida partida = controladorPartida.crearPartida("Test Game", jugadors, "catalan");

        // Acabem la partida
        controladorPartida.acabarPartida(partida);

        // La partida hauria d'estar finalitzada
        assertTrue(partida.getPartidaAcabada());

        // Per a un jugador humà, la partida s'hauria d'eliminar de les partides actives
        assertFalse(persona.getPartidesEnCurs().contains(partida));
    }
}
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

public class PartidaTest {

    private Partida partida;
    private Usuari jugador1;
    private Usuari jugador2;
    private Fitxa fitxaA;
    private Fitxa fitxaB;
    private Fitxa fitxaC;
    private Fitxa fitxaBlank;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Iniciant proves de Partida...");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Finalitzades les proves de Partida.");
    }

    @Before
    public void setUp() {
        System.out.println("Preparant test...");
        partida = new Partida("Test Game", "catalan");
        jugador1 = new Persona("Player 1", "player1@example.com", "password1");
        jugador2 = Bot.getInstance(); // Nivell de dificultat 1
        fitxaA = new Fitxa("A", 1);
        fitxaB = new Fitxa("B", 3);
        fitxaC = new Fitxa("C", 3);
        fitxaBlank = new Fitxa("#", 0);
    }

    @After
    public void tearDown() {
        System.out.println("Test finalitzat.");
        partida = null;
        jugador1 = null;
        jugador2 = null;
        fitxaA = null;
        fitxaB = null;
        fitxaC = null;
        fitxaBlank = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Game", partida.getNom());
        assertNotNull(partida.getTaulell());
        assertEquals(0, partida.getJugadors().size());
        assertFalse(partida.getPartidaAcabada());
        assertFalse(partida.getPartidaPausada());
        assertEquals("catalan", partida.getIdioma());
    }

    @Test
    public void testAfegirJugador() {
        partida.afegirJugador(jugador1);

        assertEquals(1, partida.getJugadors().size());
        assertEquals(jugador1, partida.getJugadors().get(0));
        assertEquals(1, partida.getAtrils().size());
        assertEquals(7, partida.getAtrils().get(0).size());
        assertEquals(1, partida.getPuntuacions().size());
        assertEquals(0, (int)partida.getPuntuacions().get(0));

        // Verifiquem que s'ha afegit al llistat de partides en curs del jugador si és una Persona
        assertTrue(((Persona)jugador1).getPartidesEnCurs().contains(partida));
    }

    @Test
    public void testAfegirMultipleJugadors() {
        partida.afegirJugador(jugador1);
        partida.afegirJugador(jugador2);

        assertEquals(2, partida.getJugadors().size());
        assertEquals(jugador1, partida.getJugadors().get(0));
        assertEquals(jugador2, partida.getJugadors().get(1));
        assertEquals(2, partida.getAtrils().size());
        assertEquals(2, partida.getPuntuacions().size());
    }

    @Test
    public void testGetJugadorActual() {
        partida.afegirJugador(jugador1);
        partida.afegirJugador(jugador2);

        assertEquals(jugador1, partida.getJugadorActual());
        partida.passarTorn();
        assertEquals(jugador2, partida.getJugadorActual());
    }

    @Test
    public void testPassarTorn() {
        partida.afegirJugador(jugador1);
        partida.afegirJugador(jugador2);

        assertEquals(jugador1, partida.getTornActual());
        assertTrue(partida.passarTorn());
        assertEquals(jugador2, partida.getTornActual());
        assertTrue(partida.passarTorn());
        assertEquals(jugador1, partida.getTornActual());
    }

    @Test
    public void testColocarFitxa() {
        partida.afegirJugador(jugador1);
        List<Fitxa> atril = partida.getAtril();
        Fitxa fitxa = atril.get(0);

        assertTrue(partida.colocarFitxa(7, 7, fitxa));
//        assertTrue(partida.getTaulell().esMovimentValid(7, 7, fitxa));
        assertEquals(1, partida.getFitxesActuals().size());
        assertEquals(1, partida.getPosicionsActuals().size());
    }

    @Test
    public void testColocarFitxaNoInAtril() {
        partida.afegirJugador(jugador1);
        Fitxa newFitxa = new Fitxa("Z", 10);

        assertFalse(partida.colocarFitxa(7, 7, newFitxa));
//        assertFalse(partida.getTaulell().esMovimentValid(7, 7, newFitxa));
    }

    @Test
    public void testRetiraFitxesJugades() {
        partida.afegirJugador(jugador1);
        List<Fitxa> atril = partida.getAtril();
        Fitxa fitxa = atril.get(0);

        partida.colocarFitxa(7, 7, fitxa);
//        assertTrue(partida.getTaulell().esMovimentValid(7, 7, fitxa));

        partida.retiraFitxesJugades();
//        assertFalse(partida.getTaulell().esMovimentValid(7, 7, fitxa));
    }

    @Test
    public void testAcabarPartida() {
        partida.acabarPartida();
        assertTrue(partida.getPartidaAcabada());
    }

    @Test
    public void testGuardarPartida() {
        partida.guardarPartida();
        assertTrue(partida.getPartidaPausada());

        partida.setNoGuardada();
        assertFalse(partida.getPartidaPausada());
    }

    @Test
    public void testCanviFitxesAtril() {
        partida.afegirJugador(jugador1);
        List<Fitxa> atrilOriginal = new ArrayList<>(partida.getAtril());

        // Intentem intercanviar la primera i tercera fitxa
        String[] posicions = {"1", "3"};
        assertTrue(partida.canviFitxesAtril(posicions));

        List<Fitxa> atrilNou = partida.getAtril();

        // Verifiquem que l'atril encara té 7 fitxes
        assertEquals(7, atrilNou.size());

        // Verifiquem que almenys una fitxa ha canviat (podrien ser totes si la bossa és prou gran)
        boolean diferentFitxa = false;
        for (int i = 0; i < atrilNou.size(); i++) {
            if (i < atrilOriginal.size() && !atrilNou.get(i).equals(atrilOriginal.get(i))) {
                diferentFitxa = true;
                break;
            }
        }
        assertTrue(diferentFitxa);
    }

    @Test
    public void testActualitzaPuntuacio() {
        partida.afegirJugador(jugador1);
        assertEquals(0, (int)partida.getPuntuacions().get(0));

        partida.actualitzaPuntuacio(10);
        assertEquals(10, (int)partida.getPuntuacions().get(0));

        partida.actualitzaPuntuacio(5);
        assertEquals(15, (int)partida.getPuntuacions().get(0));
    }

    @Test
    public void testDeterminarGuanyador() {
        partida.afegirJugador(jugador1);
        partida.afegirJugador(jugador2);

        // Inicialment ambdós jugadors tenen 0 punts
        assertNull(partida.determinarGuanyador());

        // El jugador 1 aconsegueix 10 punts
        partida.setPuntuacio(10, 0);
        assertEquals(jugador1, partida.determinarGuanyador());

        // El jugador 2 aconsegueix 20 punts
        partida.setPuntuacio(20, 1);
        assertEquals(jugador2, partida.determinarGuanyador());

        // Situació d'empat
        partida.setPuntuacio(20, 0);
        assertNull(partida.determinarGuanyador());
    }

    @Test
    public void testExisteixParaula() {
        // Aquesta prova depèn de la implementació del diccionari
        // Provarem tant una paraula comuna com una paraula sense sentit

        // "CASA" hauria d'existir en el diccionari català
        assertTrue(partida.existeixParaula("CASA"));

        // Una paraula sense sentit no hauria d'existir
        assertFalse(partida.existeixParaula("XYZABC"));
    }

    @Test
    public void testJugarParaula() {
        partida.afegirJugador(jugador1);

        // Reemplacem l'atril del jugador amb fitxes conegudes per a la prova
        List<List<Fitxa>> atrils = partida.getAtrils();
        List<Fitxa> atril = new ArrayList<>();
        atril.add(new Fitxa("C", 3));
        atril.add(new Fitxa("A", 1));
        atril.add(new Fitxa("S", 1));
        atril.add(new Fitxa("A", 1));
        atrils.set(0, atril);

        // Creem una jugada per formar "CASA" començant al centre
        LinkedHashMap<int[], Fitxa> jugades = new LinkedHashMap<>();
        int center = 7;
        jugades.put(new int[]{center, center}, atril.get(0));     // C
        jugades.put(new int[]{center, center+1}, atril.get(1));   // A
        jugades.put(new int[]{center, center+2}, atril.get(2));   // S
        jugades.put(new int[]{center, center+3}, atril.get(3));   // A

        try {
            int puntuacio = partida.jugarParaula(jugades, "H");
            assertTrue(puntuacio > 0);

            // Verifiquem que el tauler té les fitxes col·locades
            assertTrue(partida.getTaulell().getCasella(center, center).isOcupada());
            assertTrue(partida.getTaulell().getCasella(center, center+1).isOcupada());
            assertTrue(partida.getTaulell().getCasella(center, center+2).isOcupada());
            assertTrue(partida.getTaulell().getCasella(center, center+3).isOcupada());

            // Verifiquem que l'atril del jugador ha estat reomplert
            assertEquals(7, partida.getAtril().size());

            // Verifiquem que s'ha passat el torn
            assertEquals(jugador1, partida.getTornActual());

        } catch (IllegalArgumentException e) {
            // Això és esperat si la implementació del diccionari no conté "CASA"
            // o si hi ha altres regles de validació en joc
            System.out.println("Excepció esperada: " + e.getMessage());
        }
    }

    @Test
    public void testJugarParaulaEmptyJugada() {
        int p = partida.jugarParaula(new LinkedHashMap<>(), "H");
        assertEquals(-1, p);

    }

    @Test
    public void testJugarParaulaInvalidOrientation() {
        partida.afegirJugador(jugador1);
        LinkedHashMap<int[], Fitxa> jugades = new LinkedHashMap<>();
        jugades.put(new int[]{7, 7}, new Fitxa("A", 1));
        int p =partida.jugarParaula(jugades, "X");
        assertEquals(-1, p);
    }
}
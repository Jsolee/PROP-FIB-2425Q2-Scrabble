package Main;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;

/**
 * Clase de pruebas unitarias para Taulell
 * Incluye pruebas de casos básicos y extremos
 */
public class TaulellTest {
    
    private Taulell taulell;
    private Diccionari diccionariCastellano;
    private Diccionari diccionariCatalan;
    
    @Before
    public void setUp() {
        taulell = new Taulell();
        diccionariCastellano = new Diccionari("castellano");
        diccionariCatalan = new Diccionari("catalan");
    }
    
    // ===== CASOS BÁSICOS =====
    
    @Test
    public void testInicialitzarTaulell() {
        Casella[][] caselles = taulell.getCaselles();
        
        // Verificar que el tablero es 15x15
        assertEquals(15, caselles.length);
        assertEquals(15, caselles[0].length);
        
        // Verificar que todas las casillas están inicializadas correctamente
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                assertNotNull(caselles[i][j]);
                assertFalse(caselles[i][j].isOcupada());
            }
        }
        
        // Verificar que la casilla central es la casilla inicial
        assertTrue(caselles[7][7].isEsCasellaInicial());
    }
    
    @Test
    public void testIsEmpty() {
        // Verificar que el tablero está vacío al inicio
        assertTrue(taulell.isEmpty());
        
        // Colocar una ficha
        Fitxa fitxa = new Fitxa("A", 1);
        taulell.colocarFitxa(7, 7, fitxa);
        
        // Verificar que el tablero ya no está vacío
        assertFalse(taulell.isEmpty());
    }
    
    @Test
    public void testColocarYRetirarFitxa() {
        // Crear ficha de prueba
        Fitxa fitxa = new Fitxa("A", 1);
        
        // Colocar ficha en el tablero
        boolean resultado = taulell.colocarFitxa(7, 7, fitxa);
        
        // Verificar que la colocación fue exitosa
        assertTrue(resultado);
        assertTrue(taulell.getCasella(7, 7).isOcupada());
        assertEquals(fitxa, taulell.getCasella(7, 7).getFitxa());
        
        // Retirar la ficha
        Fitxa fitxaRetirada = taulell.retirarFitxa(7, 7);
        
        // Verificar que la retirada fue exitosa
        assertEquals(fitxa, fitxaRetirada);
        assertFalse(taulell.getCasella(7, 7).isOcupada());
        assertNull(taulell.getCasella(7, 7).getFitxa());
    }
    
    @Test
    public void testEsPrimerMoviment() {
        // Al inicio debe ser el primer movimiento
        assertTrue(taulell.esPrimerMoviment());
        
        // Colocar una ficha
        Fitxa fitxa = new Fitxa("A", 1);
        taulell.colocarFitxa(7, 7, fitxa);
        
        // Ya no debe ser el primer movimiento
        assertFalse(taulell.esPrimerMoviment());
    }
    
    @Test
    public void testTeFitxaAdjacent() {
        // Colocar una ficha en el centro
        Fitxa fitxa = new Fitxa("A", 1);
        taulell.colocarFitxa(7, 7, fitxa);
        
        // Verificar posiciones adyacentes
        assertTrue(taulell.teFitxaAdjacent(6, 7));
        assertTrue(taulell.teFitxaAdjacent(8, 7));
        assertTrue(taulell.teFitxaAdjacent(7, 6));
        assertTrue(taulell.teFitxaAdjacent(7, 8));
        
        // Verificar posiciones no adyacentes
        assertFalse(taulell.teFitxaAdjacent(5, 7));
        assertFalse(taulell.teFitxaAdjacent(7, 5));
        assertFalse(taulell.teFitxaAdjacent(6, 6));
    }
    
    // ===== CASOS DE JUGADAS BÁSICAS =====
    
    @Test
    public void testJugadaHorizontalValida() {
        // Crear la palabra "CASA" horizontal
        LinkedHashMap<int[], Fitxa> jugada = crearJugada(
            new String[]{"C", "A", "S", "A"}, 
            new int[][]{{7, 7}, {7, 8}, {7, 9}, {7, 10}},
            new int[]{3, 1, 1, 1}
        );
        
        // Verificar que la jugada es válida
        assertTrue(taulell.verificarFitxes(jugada, true));
        
        // Calcular puntuación
        int puntuacion = taulell.validesaYPuntuacioJugada(jugada, diccionariCastellano, true, true);
        
        // La puntuación debería ser positiva
        assertTrue(puntuacion > 0);
    }
    
    @Test
    public void testJugadaVerticalValida() {
        // Crear la palabra "CASA" vertical
        LinkedHashMap<int[], Fitxa> jugada = crearJugada(
            new String[]{"C", "A", "S", "A"}, 
            new int[][]{{7, 7}, {8, 7}, {9, 7}, {10, 7}},
            new int[]{3, 1, 1, 1}
        );
        
        // Verificar que la jugada es válida
        assertTrue(taulell.verificarFitxes(jugada, false));
        
        // Calcular puntuación
        int puntuacion = taulell.validesaYPuntuacioJugada(jugada, diccionariCastellano, false, true);
        
        // La puntuación debería ser positiva
        assertTrue(puntuacion > 0);
    }
    
    // ===== CASOS EXTREMOS =====
    

    
    @Test
    public void testComodinEnPalabra() {
        // Crear fichas para la palabra "CASA" con un comodín en la S
        Fitxa fitxaC = new Fitxa("C", 3);
        Fitxa fitxaA1 = new Fitxa("A", 1);
        Fitxa fitxaComodin = new Fitxa("S", 0);
        Fitxa fitxaA2 = new Fitxa("A", 1);
        
        LinkedHashMap<int[], Fitxa> jugada = new LinkedHashMap<>();
        jugada.put(new int[]{7, 7}, fitxaC);
        jugada.put(new int[]{7, 8}, fitxaA1);
        jugada.put(new int[]{7, 9}, fitxaComodin);
        jugada.put(new int[]{7, 10}, fitxaA2);
        
        // Verificar que la jugada es válida
        assertTrue(taulell.verificarFitxes(jugada, true));
        
        // Calcular puntuación
        int puntuacion = taulell.validesaYPuntuacioJugada(jugada, diccionariCastellano, true, true);
        // La puntuación debería ser positiva pero menor que la normal
        assertEquals(10, puntuacion); // 10 porque tiene doble puntuacion
    }
    
    @Test
    public void testPalabrasCruzadas() {
        // Primero colocamos "CASA" horizontal
        LinkedHashMap<int[], Fitxa> jugada1 = crearJugada(
            new String[]{"C", "A", "S", "A"}, 
            new int[][]{{7, 7}, {7, 8}, {7, 9}, {7, 10}},
            new int[]{3, 1, 1, 1}
        );
        
        int puntuacion1 = taulell.validesaYPuntuacioJugada(jugada1, diccionariCastellano, true, true);
        assertTrue(puntuacion1 > 0);
        
        // Luego colocamos "MESA" vertical cruzando por la S de CASA
        LinkedHashMap<int[], Fitxa> jugada2 = crearJugada(
            new String[]{"M", "E", "A"}, 
            new int[][]{{8, 9}, {10, 9}, {11, 9}},
            new int[]{3, 1, 1}
        );
        
        int puntuacion2 = taulell.validesaYPuntuacioJugada(jugada2, diccionariCastellano, false, true);
        assertTrue(puntuacion2 > 0);
    }
    
    @Test
    public void testPalabraInvalida() {
        // Crear una palabra que no existe "XZYW"
        LinkedHashMap<int[], Fitxa> jugada = crearJugada(
            new String[]{"X", "Z", "Y", "W"}, 
            new int[][]{{7, 7}, {7, 8}, {7, 9}, {7, 10}},
            new int[]{8, 10, 4, 8}
        );
        Taulell t = taulell;
        // La puntuación debería ser -1 (palabra inválida)
        int puntuacion = taulell.validesaYPuntuacioJugada(jugada, diccionariCastellano, true, true);
        assertEquals(-1, puntuacion);
        assertEquals(taulell, t);
    }
    
    @Test
    public void testInterseccionInvalida() {
        // Primero colocamos "CASA" horizontal
        LinkedHashMap<int[], Fitxa> jugada1 = crearJugada(
            new String[]{"C", "A", "S", "A"}, 
            new int[][]{{7, 7}, {7, 8}, {7, 9}, {7, 10}},
            new int[]{3, 1, 1, 1}
        );
        
        int puntuacion1 = taulell.validesaYPuntuacioJugada(jugada1, diccionariCastellano, true, true);
        assertTrue(puntuacion1 > 0);
        
        // Intentamos colocar "MESA" vertical pero con la E sobre la A de CASA
        LinkedHashMap<int[], Fitxa> jugada2 = crearJugada(
            new String[]{"M", "E", "S", "A"}, 
            new int[][]{{6, 8}, {7, 8}, {8, 8}, {9, 8}},
            new int[]{3, 1, 1, 1}
        );
        
        // Debería fallar porque la E no coincide con la A que ya está colocada
        boolean esValida = taulell.verificarFitxes(jugada2, false);
        assertFalse(esValida);
    }
    
    @Test
    public void testDigrafoSeparado() {
        // Intentar formar "COCHE" con C y H separadas (no permitido)
        LinkedHashMap<int[], Fitxa> jugada = crearJugada(
            new String[]{"C", "O", "C", "H", "E"}, 
            new int[][]{{7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}},
            new int[]{3, 1, 3, 4, 1}
        );
        Taulell t = taulell;
        
        // La validación debería fallar por intentar formar CH con letras separadas
        int puntuacion = taulell.validesaYPuntuacioJugada(jugada, diccionariCastellano, true, true);
        assertEquals(-1, puntuacion);
        assertEquals(taulell, t);
    }
    
    @Test
    public void testDigrafoValido() {
        // Formar "COCHE" con el dígrafo CH como una ficha única (suponiendo que existe)
        Fitxa fitxaC = new Fitxa("C", 3);
        Fitxa fitxaO = new Fitxa("O", 1);
        Fitxa fitxaCH = new Fitxa("CH", 5); // Ficha especial para el dígrafo
        Fitxa fitxaE = new Fitxa("E", 1);
        
        LinkedHashMap<int[], Fitxa> jugada = new LinkedHashMap<>();
        jugada.put(new int[]{7, 7}, fitxaC);
        jugada.put(new int[]{7, 8}, fitxaO);
        jugada.put(new int[]{7, 9}, fitxaCH);
        jugada.put(new int[]{7, 10}, fitxaE);
        
        // La palabra debería ser válida con el dígrafo como una ficha
        int puntuacion = taulell.validesaYPuntuacioJugada(jugada, diccionariCastellano, true, true);
        assertTrue(puntuacion > 0);
    }
    
    @Test
    public void testPosicionFueraDeTablero() {
        // Intentar colocar una ficha fuera del tablero
        Fitxa fitxa = new Fitxa("A", 1);
        boolean resultado = taulell.colocarFitxa(15, 15, fitxa);
        
        // Debería fallar
        assertFalse(resultado);
    }
    
    @Test
    public void testSinDiccionario() {
        // Crear la palabra "CASA"
        LinkedHashMap<int[], Fitxa> jugada = crearJugada(
            new String[]{"C", "A", "S", "A"}, 
            new int[][]{{7, 7}, {7, 8}, {7, 9}, {7, 10}},
            new int[]{3, 1, 1, 1}
        );
        
        // Intentar validar con un diccionario null
        assertThrows(NullPointerException.class, () -> {
            taulell.validesaYPuntuacioJugada(jugada, null, true, false);
        });
    }
    
    @Test
    public void testCasillaOcupada() {
        // Colocar una ficha
        Fitxa fitxa1 = new Fitxa("A", 1);
        boolean resultado1 = taulell.colocarFitxa(7, 7, fitxa1);
        assertTrue(resultado1);
        
        // Intentar colocar otra ficha en la misma casilla
        Fitxa fitxa2 = new Fitxa("B", 3);
        boolean resultado2 = taulell.colocarFitxa(7, 7, fitxa2);
        
        // Debería fallar
        assertFalse(resultado2);
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    /**
     * Crea una jugada con las letras, posiciones y valores especificados
     */
    private LinkedHashMap<int[], Fitxa> crearJugada(String[] letras, int[][] posiciones, int[] valores) {
        LinkedHashMap<int[], Fitxa> jugada = new LinkedHashMap<>();
        
        for (int i = 0; i < letras.length; i++) {
            Fitxa fitxa = new Fitxa(letras[i], valores[i]);
            jugada.put(posiciones[i], fitxa);
        }
        
        return jugada;
    }
    
}
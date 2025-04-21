package Main;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DAWGnodeTest {

    private DAWGnode node;

    @Before
    public void setUp() {
        node = new DAWGnode();
    }

    @Test
    public void testConstructor() {
        assertFalse("Un nou node no hauria de ser paraula", node.getEsParaula());
        assertTrue("Les transicions haurien d'estar buides inicialment", node.getTransicions().isEmpty());
    }

    @Test
    public void testSetGetEsParaula() {
        // Valor inicial
        assertFalse(node.getEsParaula());

        // Canviar a cert
        node.setEsParaula(true);
        assertTrue(node.getEsParaula());

        // Tornar a fals
        node.setEsParaula(false);
        assertFalse(node.getEsParaula());
    }

    @Test
    public void testAfegirTransicio() {
        DAWGnode fill = new DAWGnode();
        node.afegirTransicio('A', fill);

        assertEquals("El node fill hauria de ser accessible per la clau 'A'", fill, node.getTransicio('A'));
        assertEquals("Hauria d'haver-hi una transició", 1, node.getTransicions().size());
    }

    @Test
    public void testTeFills() {
        // Inicialment no té fills
        assertFalse("Un nou node no hauria de tenir fills", node.teFills());

        // Afegir un fill
        node.afegirTransicio('B', new DAWGnode());
        assertTrue("Després d'afegir una transició, hauria de tenir fills", node.teFills());
    }

    @Test
    public void testGetTransicioInexistent() {
        assertNull("getTransicio per una clau inexistent hauria de retornar null", node.getTransicio('Z'));
    }

    @Test
    public void testMultiplesTransicions() {
        DAWGnode fill1 = new DAWGnode();
        DAWGnode fill2 = new DAWGnode();
        DAWGnode fill3 = new DAWGnode();

        node.afegirTransicio('A', fill1);
        node.afegirTransicio('B', fill2);
        node.afegirTransicio('C', fill3);

        assertEquals(fill1, node.getTransicio('A'));
        assertEquals(fill2, node.getTransicio('B'));
        assertEquals(fill3, node.getTransicio('C'));
        assertEquals(3, node.getTransicions().size());
    }

    @Test
    public void testCanviarTransicio() {
        DAWGnode fill1 = new DAWGnode();
        DAWGnode fill2 = new DAWGnode();

        // Afegir transició
        node.afegirTransicio('D', fill1);
        assertEquals(fill1, node.getTransicio('D'));

        // Canviar la transició
        node.canviarTransicio('D', fill2);
        assertEquals(fill2, node.getTransicio('D'));
    }

    @Test
    public void testGetTransicioMesGran() {
        // Amb node buit
        assertNull(node.getTransicioMesGran());

        // Amb diverses transicions
        DAWGnode fillA = new DAWGnode();
        DAWGnode fillB = new DAWGnode();
        DAWGnode fillZ = new DAWGnode();

        node.afegirTransicio('A', fillA);
        node.afegirTransicio('B', fillB);
        node.afegirTransicio('Z', fillZ);

        assertEquals('Z', node.getTransicioMesGran().getKey().charValue());
        assertEquals(fillZ, node.getTransicioMesGran().getValue());
    }

    @Test
    public void testEquals() {
        // Dos nodes buits haurien de ser iguals
        DAWGnode node1 = new DAWGnode();
        DAWGnode node2 = new DAWGnode();

        assertTrue("Dos nodes buits haurien de ser iguals", node1.equals(node2));
        assertEquals("Els hashCodes haurien de ser iguals", node1.hashCode(), node2.hashCode());

        // Si canviem esParaula, ja no són iguals
        node1.setEsParaula(true);
        assertFalse("Nodes amb valors diferents d'esParaula no haurien de ser iguals", node1.equals(node2));
    }

    @Test
    public void testEqualsAmbTransicions() {
        DAWGnode node1 = new DAWGnode();
        DAWGnode node2 = new DAWGnode();

        node1.afegirTransicio('X', new DAWGnode());

        // Un node amb transicions no és igual a un sense transicions
        assertFalse("Nodes amb diferents transicions no haurien de ser iguals", node1.equals(node2));

        // Si afegim la mateixa transició, haurien de ser iguals
        DAWGnode commonChild = new DAWGnode();
        node1 = new DAWGnode();
        node2 = new DAWGnode();

        node1.afegirTransicio('X', commonChild);
        node2.afegirTransicio('X', commonChild);

        assertTrue("Nodes amb les mateixes transicions haurien de ser iguals", node1.equals(node2));
    }
}
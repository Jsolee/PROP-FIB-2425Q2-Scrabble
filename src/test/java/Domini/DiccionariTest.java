package Domini;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class DiccionariTest {

    private static Diccionari dic;

    // això s'executa una vegada abans de tots els tests
    @BeforeClass
    public static void setUpClass() {
        // Se ejecuta una vez antes de todos los tests
        System.out.println("Iniciando pruebas de Diccionari...");
        String nom_dic = "castellano";
        System.out.println("Crear el diccionari " + nom_dic + ".");
        dic = new Diccionari(nom_dic);
    }


    @Test
    public void testQuantsEstats () {
        int estats = dic.getNumeroNodes();
        System.out.println("El diccionari " + dic.getNom() + " conté " + estats + " estats.");
        assertTrue(estats > 0);
    }

    @Test
    public void testConteParaules() {
        String nom = dic.getNom();
        String ruta = "src/main/resources/" + nom + "/" + nom + ".txt";
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String paraula;
            while ((paraula = br.readLine()) != null) {
                assertTrue(dic.esParaula(paraula));
                count = count + 1;
            }
            System.out.println("El diccionari " + nom + " conté " + count + " paraules.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testParaulaQueNoEsta () {
        String paraula = "noexisteix"; // els diccionaris estan en mayuscules, per tant aixo no existeix
        assertFalse(dic.esParaula(paraula));
        System.out.println("La paraula " + paraula + " no existeix al diccionari.");
    }
}


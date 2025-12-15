package jeudedame;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Point2DTest {

    @AfterEach
    public void tearDown() {
        // Méthode volontairement vide :
        // ces tests ne créent aucune ressource externe (fichier, socket, DB)
        // et n'ont donc rien à nettoyer après chaque test.
    }

    /**
     * Test of equals method, of class Point2D.
     */
    @Test
    public void testEquals() {
        Point2D A = null;
        Point2D instance = new Point2D();
        boolean expResult = false;
        boolean result = instance.equals(A);
        assertEquals(expResult, result);

        Point2D B = new Point2D(1, 2);
        Point2D C = new Point2D(B);
        Point2D D = new Point2D(2, 3);
        Point2D E = new Point2D(1, 2);

        assertTrue(B.equals(C));
        assertFalse(B.equals(D));
        assertTrue(C.equals(E));
    }

    /**
     * Test of distance method, of class Point2D.
     */
    @Test
    public void testDistance() {
        // Test si un point est null
        Point2D p = null;
        Point2D instance = new Point2D(1, 2);
        double expResult = -1.0;
        double result = instance.distance(p);
        assertEquals(expResult, result, 0);

        // Test si 2 points sont au même endroit
        Point2D p2 = new Point2D(1, 2);
        Point2D instance2 = new Point2D(p2);
        double expResult2 = 0.0;
        double result2 = instance2.distance(p2);
        assertEquals(expResult2, result2, 0);

        // Test pour 2 points quelconques
        Point2D p3 = new Point2D(4, 6);
        Point2D instance3 = new Point2D(1, 2);
        double expResult3 = 5.0;
        double result3 = instance3.distance(p3);
        assertEquals(expResult3, result3, 0);
    }
}

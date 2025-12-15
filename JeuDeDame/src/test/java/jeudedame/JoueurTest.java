package jeudedame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;

public class JoueurTest {

    @Test
    void testCreationJoueurBlanc() {
        Joueur j1 = new Joueur("Alice", false);

        assertEquals("Alice", j1.getNom());
        assertFalse(j1.isNoir());
    }

    @Test
    void testCreationJoueurNoir() {
        Joueur j2 = new Joueur("Bob", true);

        assertEquals("Bob", j2.getNom());
        assertTrue(j2.isNoir());
    }

    @Test
    void testToStringAffichageNoir() {
        Joueur j2 = new Joueur("Bob", true);

        assertEquals("Bob [Noir]", j2.toString());
    }

    @Test
    void testJoueurEstSerializable() {
        Joueur j1 = new Joueur("Alice", false);

        assertTrue(j1 instanceof Serializable);
    }
}

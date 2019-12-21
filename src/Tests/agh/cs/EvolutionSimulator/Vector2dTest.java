package agh.cs.EvolutionSimulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    private Vector2d v_1_1 = new Vector2d(1,1);
    private Vector2d v_2_3= new Vector2d(2,3);
    private Vector2d v_1_4= new Vector2d(1,4);

    @Test
    void testToString() {
        assertEquals("(1,4)", v_1_4.toString());
    }

    @Test
    void testPrecedes() {
        assertTrue(v_1_1.precedes(new Vector2d (2,2)));
    }

    @Test
    void testFollows() {
        assertTrue(v_2_3.follows(v_1_1));
    }

    @Test
    void testEquals() {
        assertEquals("(1,1)",v_1_1.toString());
    }

    @Test
    void testUpperRight() {
        assertEquals(v_2_3, v_2_3.upperRight(v_1_1));
    }

    @Test
    void testLowerLeft() {
        assertEquals(new Vector2d(1,3),v_1_4.lowerLeft(v_2_3));
    }

    @Test
    void testAdd() {
        assertEquals(new Vector2d(3,7),v_1_4.add(v_2_3));
    }

    @Test
    void testSubtract() {
        assertEquals(new Vector2d(0,-3), v_1_1.subtract(v_1_4));
    }

    @Test
    void testOpposite() {
        assertEquals(v_1_4, new Vector2d(-1,-4).opposite());
    }


}
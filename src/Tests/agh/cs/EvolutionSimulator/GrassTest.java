package agh.cs.EvolutionSimulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassTest {
    private Grass g_1_1_3= new Grass(new Vector2d(1,1),3);
    private Grass g_5_3_4= new Grass(new Vector2d(5,3),4);
    private Grass g_2_1_9= new Grass(new Vector2d(2,1),9);

    @Test
    void testToString() {
        assertEquals(g_1_1_3.toString(),"*");
        assertEquals(g_5_3_4.toString(),"*");
        assertEquals(g_2_1_9.toString(),"*");
    }

    @Test
    void getPosition() {
        assertEquals(g_1_1_3.getPosition(),new Vector2d(1,1));
        assertEquals(g_5_3_4.getPosition(),new Vector2d(5,3));
        assertNotEquals(g_2_1_9.getPosition(), new Vector2d(1,2));
    }

}
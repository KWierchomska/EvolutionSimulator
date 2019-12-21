package agh.cs.EvolutionSimulator;

import org.junit.jupiter.api.Test;

import static agh.cs.EvolutionSimulator.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    @Test
    void testToString() {
        assertEquals(NORTH.toString(), "↑");
        assertEquals(SOUTH.toString(), "↓");
        assertEquals(WEST.toString(),"←" );
        assertEquals(EAST.toString(), "→");
    }

    @Test
    void toUnitVector() {
        assertEquals(NORTH.toUnitVector(), new Vector2d(0,1));
        assertEquals(NORTHEAST.toUnitVector(), new Vector2d(1,1));
        assertEquals(EAST.toUnitVector(), new Vector2d(1,0));
        assertEquals(SOUTHEAST.toUnitVector(), new Vector2d(1,-1));
        assertEquals(SOUTH.toUnitVector(), new Vector2d(0,-1));
        assertEquals(SOUTHWEST.toUnitVector(), new Vector2d(-1,-1));
        assertEquals(WEST.toUnitVector(), new Vector2d(-1,0));
        assertEquals(NORTHWEST.toUnitVector(), new Vector2d(-1,1));
    }

    @Test
    void testRotation() {
        assertEquals(EAST.rotation(0), EAST);
        assertEquals(EAST.rotation(1), SOUTHEAST);
        assertEquals(EAST.rotation(2), SOUTH);
        assertEquals(EAST.rotation(3), SOUTHWEST);
        assertEquals(EAST.rotation(4), WEST);
        assertEquals(EAST.rotation(5), NORTHWEST);
        assertEquals(EAST.rotation(6), NORTH);
        assertEquals(EAST.rotation(7), NORTHEAST);
        assertThrows(IllegalArgumentException.class, () -> {
            EAST.rotation(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EAST.rotation(8);
        });
    }

}
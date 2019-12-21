package agh.cs.EvolutionSimulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    private WorldMap map=new WorldMap(10,5,0.5,2,1,10, 2);
    @Test
    void testMove() {
        ((WorldMap) map).placeRandomAnimals();
        Vector2d position;
        for(Animal animal : ((WorldMap)map).animals){
            position=animal.getPosition();
            animal.move();
            position = map.correctPosition(position.add(animal.getDirection().toUnitVector()));
            assertEquals(animal.getPosition(),position);
            assertEquals(animal.getEnergy(),((WorldMap) map).getStartEnergy()-1);
        }
    }

    @Test
    void testConsume() {
        Animal animal1 = ((WorldMap) map).animals.get(0);
        Animal animal2 = ((WorldMap) map).animals.get(1);
        Animal animal3 = new Animal(map.animals.get(1).getPosition(),map.getStartEnergy(),map);
        Grass grass1 = new Grass (animal1.getPosition(), 1);
        Grass grass2 = new Grass (animal2.getPosition(), 8);
        map.place(grass1);
        map.place(animal3);
        map.place(grass2);
        animal1.consume(grass1);
        animal2.consume(grass2);
        assertEquals(animal1.getEnergy(),11); // check
        assertEquals(animal2.getEnergy(),14);
    }

    @Test
    void reproduce() {
        Animal mother = new Animal(new Vector2d(2,1), map.getStartEnergy(), map );
        Animal father = new Animal(new Vector2d(2,1), map.getStartEnergy(), map );
        map.place(mother);
        map.place(father);
        mother.reproduce(father);
        assertEquals(mother.getEnergy(), (int)(0.75*map.getStartEnergy()));
        assertEquals(father.getEnergy(), (int)(0.75*map.getStartEnergy()));
        assertEquals(father.getNumberOfChildren(),1);
        assertEquals(mother.getNumberOfChildren(),1);
        assertEquals(this.map.animals.get(4).getNumberOfChildren(),0);
        assertEquals(map.elementsMap.size(),5);
    }


    @Test
    void testIsDead() {
        Animal animal = new Animal(new Vector2d(1,1), map.getStartEnergy(), map );
        assertFalse(animal.isDead());
    }

    @Test
    void testCanReproduce() {
        Animal animal = new Animal(new Vector2d(2,1), map.getStartEnergy(), map );
        assertTrue(animal.canReproduce());
    }

    @Test
    void testGetPosition() {
        Animal animal = new Animal(new Vector2d(3,1), map.getStartEnergy(), map );
        assertEquals(animal.getPosition(),new Vector2d(3,1));
    }



}
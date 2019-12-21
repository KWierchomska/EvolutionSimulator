package agh.cs.EvolutionSimulator;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {
    private WorldMap map=new WorldMap(10,10,0.5,2,1,10, 0);

    @Test
    void correctPosition() {
        Vector2d v_0_10=new Vector2d(map.getUpperRight().x + 1, map.getUpperRight().y);
        Vector2d v_10_0=new Vector2d(map.getUpperRight().x, map.getUpperRight().y+1);
        Vector2d v_10_4=new Vector2d(map.getLowerLeft().x-1,4);
        v_0_10=map.correctPosition(v_0_10);
        v_10_0=map.correctPosition(v_10_0);
        v_10_4=map.correctPosition(v_10_4);
        assertEquals(v_0_10, new Vector2d(0,10));
        assertEquals(v_10_0, new Vector2d(10,0));
        assertEquals(v_10_4, new Vector2d(10,4));
    }

    @Test
    void getStrongestAnimals() {
        Animal mother = new Animal(new Vector2d(2,2), map.getStartEnergy(), map);
        Animal father = new Animal(new Vector2d(2,2), map.getStartEnergy(), map);
        Animal candidateForFather=new Animal(new Vector2d(2,2), 9, map);
        map.place(mother);
        map.place(father);
        map.place(candidateForFather);
        List<Animal> strongestAnimals=map.getStrongestAnimals(map.animals);
        for(Animal strongAnimal : strongestAnimals){
            assertTrue(strongAnimal.getEnergy()==10);
        }
    }

    @Test
    void getOnlyAnimals() {
        Animal mother = new Animal(new Vector2d(2,2), map.getStartEnergy(), map);
        Animal father = new Animal(new Vector2d(2,2), map.getStartEnergy(), map);
        Animal candidateForFather=new Animal(new Vector2d(2,2), 9, map);
        Grass grass= new Grass(new Vector2d(2,2),5);
        map.place(mother);
        map.place(father);
        map.place(candidateForFather);
        map.place(grass);
        List <Animal> animals=map.getOnlyAnimals(map.objectsAt(grass.getPosition()));
        for(Animal animal: animals){
            assertTrue(animal instanceof Animal);
        }
    }

    @Test
    void consumeGrass() {
        Grass g_1_1= new Grass(new Vector2d(1,1), map.getPlantEnergy());
        Vector2d grassPosition=g_1_1.getPosition();
        Animal animal1 = new Animal(g_1_1.getPosition(), map.getStartEnergy(), map);
        Animal animal2 = new Animal(g_1_1.getPosition(), map.getStartEnergy(), map);
        map.place(g_1_1);
        map.place(animal1);
        map.place(animal2);
        map.consumeGrass();
        assertEquals(animal1.getEnergy(), 11);
        assertEquals(animal2.getEnergy(), 11);

        List <IMapElement> elements = map.objectsAt(grassPosition);
        assertEquals(elements.size(), 2);
    }

    @Test
    void placeRandomAnimals() {
        map.placeRandomAnimals();
        int numberOfAnimals=map.elementsMap.size();
        assertEquals(numberOfAnimals,0);
    }

    @Test
    void procreate() {
        Animal mother = new Animal(new Vector2d(2,2), map.getStartEnergy(), map);
        Animal father = new Animal(new Vector2d(2,2), map.getStartEnergy(), map);
        Animal candidateForFather=new Animal(new Vector2d(2,2), 9, map);
        map.place(mother);
        map.place(father);
        map.place(candidateForFather);
        map.procreate();
        assertEquals(mother.getEnergy(), (int)(0.75*map.getStartEnergy()));
        assertEquals(father.getEnergy(), (int)(0.75*map.getStartEnergy()));
        assertEquals(candidateForFather.getEnergy(),9);
        assertEquals(map.elementsMap.size(), 4);
    }

    @Test
    void placeGrass() {
        map.placeGrass();
        for (int i = 0; i <= 10; i++){
            for (int j=0; j<= 10; j++){
                Vector2d position = new Vector2d(i,j);
                if (map.isOccupied(position)){
                    assertTrue(map.elementsMap.containsKey(position));
                    assertTrue(map.elementsMap.get(position).get(0) instanceof Grass);
                }
            }
        }
    }


    @Test
    void place() {
        Animal a_2_3 = new Animal(new Vector2d(2,3), map.getStartEnergy(), map);
        Animal a_8_7 = new Animal(new Vector2d(8,7), map.getStartEnergy(), map);
        Animal a_4_5 = new Animal(new Vector2d(4,5), map.getStartEnergy(), map);
        Grass g_6_3 = new Grass(new Vector2d(6,3), map.getPlantEnergy());
        Grass g_1_9 = new Grass(new Vector2d(1,9), map.getPlantEnergy());
        Animal animalOutOfBounds = new Animal(new Vector2d(-4,-5), map.getStartEnergy(), map);
        map.place(a_2_3);
        map.place(a_4_5);
        map.place(a_8_7);
        map.place(g_1_9);
        map.place(g_6_3);
        assertThrows(IllegalArgumentException.class, () ->{
            map.place(animalOutOfBounds);
        } );
        assertTrue(map.animals.contains(a_2_3));
        assertTrue(map.animals.contains(a_4_5));
        assertTrue(map.animals.contains(a_8_7));
        assertFalse(map.animals.contains(animalOutOfBounds));
        assertTrue(map.elementsMap.containsValue(g_6_3));
        assertTrue(map.elementsMap.containsValue(g_1_9));
    }

    @Test
    void isOccupied() {
        Animal a_1_2 = new Animal(new Vector2d(1,2), map.getStartEnergy(), map);
        Animal a_3_2 = new Animal(new Vector2d(3,2), map.getStartEnergy(),map);
        Animal a_6_7 = new Animal(new Vector2d(6,7), map.getStartEnergy(), map);
        map.place(a_1_2);
        map.place(a_3_2);
        map.place(a_6_7);
        for (int i = 0; i <= 10; i++){
            for (int j=0; j<= 10; j++){
                if ((i==1 && j==2) || (i==3 && j==2) || (i==6 && j==7)){
                    assertTrue(map.isOccupied(new Vector2d(i,j)));
                }
                else {
                    assertFalse(map.isOccupied(new Vector2d(i,j)));
                }
            }
        }
    }

    @Test
    void objectsAt() {
        Animal a_1_2 = new Animal(new Vector2d(1,2), map.getStartEnergy(), map);
        Animal a_3_2 = new Animal(new Vector2d(3,2), map.getStartEnergy(),map);
        Animal a_6_7 = new Animal(new Vector2d(6,7), map.getStartEnergy(), map);
        Grass g_1_2 = new Grass(new Vector2d(1,2), map.getPlantEnergy());
        map.place(a_1_2);
        map.place(a_3_2);
        map.place(a_6_7);
        map.place(g_1_2);
        List <IMapElement> elements;
        for (int i = 0; i <= 10; i++){
            for (int j=0; j<= 10; j++){
                if ((i==1 && j==2) || (i==3 && j==2) || (i==6 && j==7)){
                    elements=map.objectsAt(new Vector2d(i,j));
                    assertFalse(elements.isEmpty());
                }
            }
        }

    }



}
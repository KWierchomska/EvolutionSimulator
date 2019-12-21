package agh.cs.EvolutionSimulator;

import java.util.List;

public interface IWorldMap {
    boolean place(IMapElement element);
    boolean isOccupied(Vector2d position);
    List<IMapElement> objectsAt(Vector2d position);
    Vector2d getLowerLeft();
    Vector2d getUpperRight();
    int getMoveEnergy();
    void placeRandomAnimals();
    List<Animal> getStrongestAnimals(List<Animal> animals);
    List<Animal> getOnlyAnimals(List<IMapElement> objectsAt);
    void procreate();
    void placeGrass();
    void consumeGrass();
    Vector2d correctPosition(Vector2d position);

    int getStartEnergy();
}

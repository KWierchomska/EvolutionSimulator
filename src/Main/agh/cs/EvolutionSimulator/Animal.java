package agh.cs.EvolutionSimulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import java.awt.*;

public class Animal implements IMapElement {
    private MapDirection direction;
    private Vector2d position;
    private Genes genes;
    private int energy;
    private int energyToReproduce;
    private int moveEnergy;
    private int numberOfChildren;
    private IWorldMap map;
    private List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(Vector2d position, int startEnergy, IWorldMap map){
        this.direction=MapDirection.setRandomDirection();
        this.position=position;
        this.energy=startEnergy;
        this.moveEnergy=map.getMoveEnergy();
        this.energyToReproduce= (int) map.getStartEnergy()/2;
        this.genes= new Genes();
        this.numberOfChildren=0;
        this.map=map;
    }

    public Animal (IWorldMap map, Animal mother, Animal father){
        this.direction=MapDirection.setRandomDirection();
        this.position=childPosition(mother);
        this.energy=(int) (0.25 * mother.energy) + (int) (0.25 * father.energy);
        this.moveEnergy=map.getMoveEnergy();
        this.energyToReproduce=mother.energyToReproduce;
        this.genes=new Genes(mother.genes, father.genes);
        this.numberOfChildren=0;
        this.map=map;
    }


    public void move (){
        int index=new Random().nextInt(32);
        this.direction = this.direction.rotation(this.genes.getSetOfGenes()[index]);
        Vector2d newPosition = this.position.add(this.direction.toUnitVector());
        newPosition=this.map.correctPosition(newPosition);
        this.reduceEnergy();
        Vector2d oldPosition= this.getPosition();
        this.position=newPosition;
        this.positionChanged(oldPosition, newPosition);
    }

    protected boolean consume(Grass grass){
        List<Animal> strongestAnimals = map.getStrongestAnimals(map.getOnlyAnimals(map.objectsAt(grass.getPosition())));
        if(strongestAnimals.size() == 0) return false;

        int proteinToShare = grass.getGrassProtein() / strongestAnimals.size();
        for (Animal animal : strongestAnimals){
            animal.energy += proteinToShare;
            System.out.println(animal.energy);
        }
        return true;
    }

    protected void reproduce (Animal father){
        if(this.canReproduce() && father.canReproduce()){
            Animal child = new Animal(map,this,father);
            this.energy=(int) (0.75 * this.energy);
            father.energy=(int) (0.75 * father.energy);
            this.addChild();
            father.addChild();
            this.map.place(child);
        }

    }

    public Vector2d childPosition(Animal mother){
        int stop=9;
        MapDirection direction;
        Vector2d position;
        do{
            direction=MapDirection.setRandomDirection();
            position = mother.getPosition().add(direction.toUnitVector());
            position=mother.map.correctPosition(position);
            if(stop -- < 0) break;
        }
        while(mother.map.isOccupied(position));
        return position;
    }

    static class SortByEnergy implements Comparator<Animal> {

        @Override
        public int compare(Animal a, Animal b) {
            return b.energy-a.energy;
        }
    }

    public void addObserver (IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver (IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    private void positionChanged (Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : this.observers){
            observer.positionChanged(oldPosition, newPosition);
        }
    }


    private void reduceEnergy(){
        this.energy-=this.moveEnergy;
    }

    public boolean isDead(){
        return this.energy <=0;
    }

    public boolean canReproduce(){
        return energy>=energyToReproduce;
    }

    public int addChild(){
        return this.numberOfChildren++;
    }

    public Color toColor() {
        if (energy == 0) return new Color(222, 221, 224);
        if (energy < 0.2 * map.getStartEnergy()) return new Color(224, 179, 173);
        if (energy < 0.4 * map.getStartEnergy()) return new Color(224, 142, 127);
        if (energy < 0.6 * map.getStartEnergy()) return new Color(201, 124, 110);
        if (energy < 0.8 * map.getStartEnergy()) return new Color(182, 105, 91);
        if (energy < map.getStartEnergy()) return new Color(164, 92, 82);
        if (energy < 2 * map.getStartEnergy()) return new Color(146, 82, 73);
        if (energy < 4 * map.getStartEnergy()) return new Color(128, 72, 64);
        if (energy < 6 * map.getStartEnergy()) return new Color(119, 67, 59);
        if (energy < 8 * map.getStartEnergy()) return new Color(88, 50, 44);
        if (energy < 10 * map.getStartEnergy()) return new Color(74, 42, 37);
        return new Color(55, 31, 27);
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
        return this.energy;
    }
    public int getNumberOfChildren(){
        return this.numberOfChildren;
    }

    public MapDirection getDirection(){
        return this.direction;
    }

    @Override
    public String toString() {
        return this.energy == 0 ? "X" : this.direction.toString();
    }

}

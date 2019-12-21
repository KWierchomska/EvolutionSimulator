package agh.cs.EvolutionSimulator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.max;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    protected List<Animal> animals = new ArrayList<>();
    protected ListMultimap<Vector2d, IMapElement> elementsMap = ArrayListMultimap.create();

    private MapVisualizer mapVisualizer = new MapVisualizer(this);
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final int plantEnergy;
    private final int moveEnergy;
    private final int startEnergy;
    private int startNumberOfAnimals;
    private int numberOfGrass;
    private int numberOfDeadAnimals;
    private int totalNumberOfEraForDeadAnimals;
    private int numberOfEra;
    private double avgEnergy;
    private double avgNumberOfChildren;


    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public WorldMap(int width, int height, double jungleRatio, int plantEnergy, int moveEnergy, int startEnergy, int startNumberOfAnimals) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);

        int jungleWidth = (int) ((double) width * jungleRatio);
        int jungleHeight = (int) ((double) height * jungleRatio);
        this.jungleLowerLeft = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        this.jungleUpperRight = new Vector2d(this.jungleLowerLeft.x + jungleWidth, this.jungleLowerLeft.y + jungleHeight);

        this.plantEnergy = plantEnergy;
        this.moveEnergy = moveEnergy;
        this.startEnergy = startEnergy;

        this.startNumberOfAnimals = startNumberOfAnimals;

        this.numberOfDeadAnimals = 0;
        this.numberOfGrass = 0;
        this.totalNumberOfEraForDeadAnimals = 0;
        this.numberOfEra = 0;
        this.avgEnergy = 0;
        this.avgNumberOfChildren = 0;

        if (!lowerLeft.precedes(jungleLowerLeft)) {
            throw new IllegalArgumentException("Jungle lower left corner can not precede map lower left corner");
        }
        if (!upperRight.follows(jungleUpperRight)) {
            throw new IllegalArgumentException("Jungle upper right corner can not follow map upper right corner");
        }

        this.placeRandomAnimals();
    }

    @Override
    public Vector2d correctPosition(Vector2d position) {
        int x, y;
        if (position.x > this.upperRight.x) x = 0;
        else if (position.x < this.lowerLeft.x) x = this.upperRight.x;
        else x = position.x;
        if (position.y > this.upperRight.y) y = 0;
        else if (position.y < this.lowerLeft.y) y = this.upperRight.y;
        else y = position.y;
        Vector2d newPosition = new Vector2d(x, y);
        return newPosition;
    }

    private void removeAnimal(Animal animal) {
        this.animals.remove(animal);
        this.elementsMap.remove(animal.getPosition(), animal);
    }

    private void removeDeadAnimals() {
        Iterator iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = (Animal) iterator.next();
            if (animal.isDead()) {
                this.totalNumberOfEraForDeadAnimals += this.numberOfEra;
                this.numberOfDeadAnimals++;
                iterator.remove();
                this.elementsMap.remove(animal.getPosition(), animal);
            }
        }
    }

    private void removeGrass(Grass grass) {
        this.elementsMap.remove(grass.getPosition(), grass);
        this.numberOfGrass--;
    }

    @Override
    public List<Animal> getStrongestAnimals(List<Animal> animals) {
        int maxEnergy = -1;
        for (Animal animal : animals) maxEnergy = max(maxEnergy, animal.getEnergy());
        List<Animal> strongestAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() == maxEnergy) strongestAnimals.add(animal);
        }
        return strongestAnimals;
    }

    @Override
    public List<Animal> getOnlyAnimals(List<IMapElement> elements) {
        List<Animal> animals = new ArrayList<>();
        for (IMapElement element : elements) {
            if (element instanceof Animal) {
                animals.add((Animal) element);
            }
        }
        return animals;
    }

    @Override
    public void consumeGrass() {
        for (Animal animal : this.animals) {
            List<IMapElement> elements = this.objectsAt(animal.getPosition());
            if (elements.get(0) instanceof Grass) {
                if (animal.consume((Grass) elements.get(0))) {
                    removeGrass((Grass) elements.get(0));
                }
            }
        }
    }

    public void move() {
        for (Animal animal : this.animals) {
            animal.move();
        }
    }

    @Override
    public void placeRandomAnimals() {
        for (int i = 0; i < this.startNumberOfAnimals; i++) {
            int x, y;
            Vector2d position;
            do {
                x = new Random().nextInt(this.upperRight.x);
                y = new Random().nextInt(this.upperRight.y);
                position = new Vector2d(x, y);
                this.correctPosition(position);
            }
            while (isOccupied(position));
            Animal animal = new Animal(position, this.startEnergy, this);
            animal.addObserver((IPositionChangeObserver) this);
            this.animals.add(animal);
            this.elementsMap.put(position, animal);
        }
    }

    @Override
    public void procreate() {
        List<Vector2d> positions = new ArrayList<>();
        for (Animal animal : this.animals) {
            if (!positions.contains(animal.getPosition())) {
                positions.add(animal.getPosition());
            }
        }

        for (Vector2d position : positions) {
            List<Animal> animals = this.getOnlyAnimals(objectsAt(position));
            if (animals.size() >= 2) {
                Collections.sort(animals, new Animal.SortByEnergy());
                animals.get(0).reproduce(animals.get(1));
            }
        }
    }

    @Override
    public void placeGrass() {
        int xSavanna, ySavanna;
        int savannaArea = (upperRight.x - lowerLeft.x) * (upperRight.y - lowerLeft.y);
        Vector2d savannaGrass;
        do {
            xSavanna = new Random().nextInt(upperRight.x + 1);
            ySavanna = new Random().nextInt(upperRight.y + 1);
            savannaGrass = new Vector2d(xSavanna, ySavanna);
            savannaArea--;
            if (savannaArea < 0) break;
        } while (this.isOccupied(savannaGrass) || this.insideJungle(savannaGrass));
        this.elementsMap.put(savannaGrass, new Grass(savannaGrass, plantEnergy));
        this.numberOfGrass++;

        int jungleArea = (jungleUpperRight.x - jungleLowerLeft.x) * (jungleUpperRight.y - jungleLowerLeft.y);
        int xJungle, yJungle;
        Vector2d jungleGrass;

        do {
            xJungle = new Random().nextInt(jungleUpperRight.x - jungleLowerLeft.x + 1) + jungleLowerLeft.x;
            yJungle = new Random().nextInt(jungleUpperRight.y - jungleLowerLeft.y + 1) + jungleLowerLeft.y;
            jungleGrass = new Vector2d(xJungle, yJungle);
            jungleArea--;
            if (jungleArea < 0) break;
        } while (this.isOccupied(jungleGrass));
        this.elementsMap.put(jungleGrass, new Grass(jungleGrass, plantEnergy));
        this.numberOfGrass++;
    }


    public void nextDay() {
        move();
        removeDeadAnimals();
        consumeGrass();
        procreate();
        placeGrass();
        this.numberOfEra++;
        countStatistics();
    }


    /*
    public boolean canPlace(Vector2d position){
        return objectsAt(position).size()<3;
    }
     */

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        List<IMapElement> animals = this.elementsMap.get(oldPosition);
        Animal animalToDelete = null;
        boolean delete = false;
        for (IMapElement animal : animals) {
            if (animal.getPosition().equals(newPosition)) {
                animalToDelete = (Animal) animal;
                delete = true;
                break;
            }
        }
        if (delete) {
            animals.remove(animalToDelete);
            elementsMap.put(newPosition, animalToDelete);
        }
    }


    @Override
    public boolean place(IMapElement element) {
        if (!(lowerLeft.precedes(element.getPosition()) && upperRight.follows(element.getPosition()))) {
            throw new IllegalArgumentException(element.getPosition().toString() + " is out of map bounds");
        }
        //if(!canPlace(animal.getPosition())) throw new IllegalArgumentException(animal.getPosition().toString() + "is full");
        if (element instanceof Animal) {
            this.animals.add((Animal) element);
            this.elementsMap.put(element.getPosition(), (Animal) element);
            ((Animal) element).addObserver(this);
        }
        else this.elementsMap.put(element.getPosition(), (Grass) element );
        return true;
    }

    public boolean insideJungle(Vector2d position) {
        return position.follows(this.jungleLowerLeft) && position.precedes(this.jungleUpperRight);
    }

    public String getStatistics() {
        String result = "Statystyki:\n";
        result += "Era: " + this.numberOfEra + "\n";
        result += "Ilość żywych zwierząt: " + this.animals.size() + "\n";
        result += "Ilość roślin: " + setNumberOfGrass() + "\n";
        result += "Srednia energia żyjących zwierząt: " + df2.format(this.avgEnergy) + "\n";
        result += "Srednia liczba dzieci żyjących zwierząt: " + df2.format(this.avgNumberOfChildren) + "\n";
        result += "Srednia długość życia martwych zwierząt: " + df2.format(setAvgLengthOfLife()) + "\n";
        // result += "Dominujący gen: " + statistics.dominantGene() +  "\n";
        return result;
    }

    public int setNumberOfGrass() {
        return this.numberOfGrass < 0 ? 0 : this.numberOfGrass;
    }

    public double setAvgLengthOfLife (){
        return this.numberOfDeadAnimals==0 ? 0 : this.totalNumberOfEraForDeadAnimals/this.numberOfDeadAnimals;
    }


    public void countStatistics() {
        int sumEnergy = 0;
        int sumChildren = 0;
        if (!animals.isEmpty()) {
            for (Animal animal : animals) {
                sumEnergy += animal.getEnergy();
                sumChildren += animal.getNumberOfChildren();
            }
            this.avgEnergy = sumEnergy / animals.size();
            this.avgNumberOfChildren = sumChildren / animals.size();
        } else {
            this.avgNumberOfChildren = 0;
            this.avgEnergy = 0;
        }
    }


    public boolean areAnimalsAlive(){
        return this.animals.size()>0;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return elementsMap.containsKey(position);
    }

    @Override
    public List <IMapElement> objectsAt(Vector2d position) {
        if (this.elementsMap.containsKey(position)){
            return this.elementsMap.get(position);
        }

        return null;
    }
    @Override
    public String toString(){
        return getStatistics() +  mapVisualizer.draw(this.lowerLeft, this.upperRight);
    }

    @Override
    public Vector2d getLowerLeft() {
        return this.lowerLeft;
    }

    @Override
    public Vector2d getUpperRight() {
        return this.upperRight;
    }

    @Override
    public int getMoveEnergy() {
        return this.moveEnergy;
    }

    public int getPlantEnergy(){return this.plantEnergy;}

    public int getStartEnergy(){
        return this.startEnergy;
    }

    public double getAvgEnergy (){ return this.avgEnergy; }

    public double getAvgNumberOfChildren() { return avgNumberOfChildren; }


}

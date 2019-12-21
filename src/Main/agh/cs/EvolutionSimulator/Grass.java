package agh.cs.EvolutionSimulator;

public class Grass implements IMapElement {
    private Vector2d position;
    private final int grassProtein;

    public Grass (Vector2d position, int grassProtein){
        this.position=position;
        this.grassProtein = grassProtein;
    }

    public String toString(){
        return "*";
    }

    public int getGrassProtein() {
        return this.grassProtein;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

}

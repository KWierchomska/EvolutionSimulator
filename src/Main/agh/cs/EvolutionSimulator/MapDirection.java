package agh.cs.EvolutionSimulator;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString(){
        switch (this) {
            case NORTH:
                return "↑";
            case SOUTH:
                return "↓";
            case WEST:
                return "←";
            case EAST:
                return "→";
            case NORTHWEST:
                return "↖";
            case SOUTHWEST:
                return "↙";
            case NORTHEAST:
                return "↗";
            case SOUTHEAST:
                return "↘";
        }
        return null;
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case EAST:
                return new Vector2d(1, 0);
            case NORTHWEST:
                return new Vector2d(-1, 1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case SOUTHEAST:
                return new Vector2d(1, -1);
        }
        return null;
    }

    public MapDirection rotation (int rotationDirection){
        if(rotationDirection < 0 || rotationDirection > 7) throw new IllegalArgumentException("Wrong rotation direction");
        return MapDirection.values()[(this.ordinal() + rotationDirection) % MapDirection.values().length];
    }

    public static MapDirection setRandomDirection(){
        return MapDirection.values()[new Random().nextInt(MapDirection.values().length)];
    }
}

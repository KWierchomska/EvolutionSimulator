package agh.cs.EvolutionSimulator;

public interface IPositionChangeObserver {
    void positionChanged (Vector2d oldPosition, Vector2d newPosition);
}

package game;

public abstract class Obstacle extends Entity {
    public enum ObstacleType {
        MINE, STEEL_WALL, WOODEN_WALL
    }

    private ObstacleType type;

    public Obstacle(int x, int y, ObstacleType type) {
        super(x, y);
        this.type = type;
    }

    public ObstacleType getType() {
        return type;
    }

    public abstract void applyEffect(Robot robot);
}
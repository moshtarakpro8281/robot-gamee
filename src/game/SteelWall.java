package game;

public class SteelWall extends Obstacle {
    public SteelWall(int x, int y) {
        super(x, y, ObstacleType.STEEL_WALL);
    }

    @Override
    public void applyEffect(Robot robot) {
        // دیوار فولادی آسیب نمی‌زنه، فقط مانع حرکت می‌شه
        System.out.println("ربات به دیوار فولادی در (" + getX() + ", " + getY() + ") برخورد کرد!");
    }
}
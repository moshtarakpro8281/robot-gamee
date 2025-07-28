package game;

public class WoodenWall extends Obstacle {
    public WoodenWall(int x, int y) {
        super(x, y, ObstacleType.WOODEN_WALL);
    }

    @Override
    public void applyEffect(Robot robot) {
        // دیوار چوبی آسیب کمتری می‌زنه (مثلاً 10%)
        if (robot.isAlive()) {
            robot.takeDamage(10);
            System.out.println("ربات به دیوار چوبی در (" + getX() + ", " + getY() + ") برخورد کرد و 10% سلامتش کم شد!");
        }
    }
}
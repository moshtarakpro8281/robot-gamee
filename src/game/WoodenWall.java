package game;

public class WoodenWall extends Obstacle {
    public WoodenWall(int x, int y) {
        super(x, y, ObstacleType.WOODEN_WALL);
    }

    @Override
    public void applyEffect(Robot robot) {
        if (robot.isAlive()) {
            robot.takeDamage(10); // آسیب 10% فقط برای برخورد (نه شلیک)
            System.out.println("ربات به دیوار چوبی در (" + getX() + ", " + getY() + ") برخورد کرد و 10% سلامتش کم شد!");
        }
    }
}
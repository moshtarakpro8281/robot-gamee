package game;

public class Mine extends Obstacle {
    public Mine(int x, int y, ObstacleType type) {
        super(x, y, type);
    }

    @Override
    public void applyEffect(Robot robot) {
        if (robot.isAlive()) {
            robot.takeDamage(33); // کاهش 33% سلامت
            System.out.println("ربات در (" + getX() + ", " + getY() + ") با مین برخورد کرد و 33% سلامتش کم شد!");
        }
    }
}
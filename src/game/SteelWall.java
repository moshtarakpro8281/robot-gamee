package game;

public class SteelWall extends Obstacle {
    private int hitCount = 0;

    public SteelWall(int x, int y) {
        super(x, y, ObstacleType.STEEL_WALL);
    }

    @Override
    public void applyEffect(Robot robot) {
        if (robot.isAlive()) {
            robot.takeDamage(10); // آسیب 10% برای برخورد
            System.out.println("ربات به دیوار فولادی در (" + getX() + ", " + getY() + ") برخورد کرد و 10% سلامتش کم شد!");
        }
    }

    public void hit() {
        hitCount++;
        System.out.println("دیوار فولادی در (" + getX() + ", " + getY() + ") شلیک خورد. تعداد شلیک‌ها: " + hitCount);
    }

    public int getHitCount() {
        return hitCount;
    }

    public boolean isDestroyed() {
        return hitCount >= 3; // نابود می‌شه بعد از 3 شلیک
    }
}
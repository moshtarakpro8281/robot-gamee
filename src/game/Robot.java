package game;

public class Robot extends Entity {
    private int health = 100;
    private boolean isAlive = true;

    public Robot(int x, int y) {
        super(x, y);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }
}
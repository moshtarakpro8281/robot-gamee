package game;

public class Robot extends Entity {
    private int health = 100;
    private boolean isAlive = true;
    private int x;

    public Robot(int x, int y) {
        super(x, y);
    }

    public int getHealth() { return health; }
    public boolean isAlive() { return isAlive; }
    public void setAlive(boolean alive) { this.isAlive = alive; }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
package game;

public class Robot extends Entity {
    private int health = 100;

    public Robot(int x, int y) {
        super(x, y);
    }

    @Override
    public char getSymbol() {
        return 'R';
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }

    // اگر خواستی آسیب ثابت 20 رو هم با متد بدون پارامتر صدا بزنی:
    public void takeDamage() {
        takeDamage(20);
    }

    public int getHealth() {
        return health;
    }
}

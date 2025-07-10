package game;

import javafx.scene.paint.Color;

public class WoodenWall extends Entity {
    private int health = 1;
    private static final Color COLOR = Color.BROWN;

    public WoodenWall(int x, int y) {
        super(x, y);
    }

    public void takeDamage() {
        if (health > 0) {
            health--;
            if (health == 0) {
                System.out.println("Wooden wall at (" + x + ", " + y + ") destroyed!");
            }
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public Color getColor() {
        return COLOR;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public char getSymbol() {
        return 'W';
    }
}

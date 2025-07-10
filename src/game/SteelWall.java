package game;

import javafx.scene.paint.Color;

/**
 * Represents a steel wall that is destroyed after 3 hits.
 * @author SADR
 */
public class SteelWall extends Entity {
    private int health = 3;
    private static final Color COLOR = Color.GRAY;

    public SteelWall(int x, int y) {
        super(x, y);
    }

    public void takeDamage() {
        if (health > 0) {
            health--;
            if (health == 0) {
                System.out.println("Steel wall at (" + x + ", " + y + ") destroyed!");
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
        return 'S';
    }
}


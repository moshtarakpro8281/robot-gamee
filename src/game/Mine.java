package game;

import javafx.scene.paint.Color;

/**
 * Represents a mine entity in the game that causes damage and is destroyed upon impact.
 * @author SADR
 */
public class Mine extends Entity {
    private boolean isActive = true;
    private static final Color COLOR = Color.RED;
    private static final int DAMAGE = 50; // آسیب مین به ربات

    public Mine(int x, int y) {
        super(x, y);
    }

    /**
     * Activates the mine, dealing damage to a robot and deactivating itself.
     * @param robot The robot that triggers the mine.
     */
    public void trigger(Robot robot) {
        if (isActive) {
            robot.takeDamage(DAMAGE);
            isActive = false;
            System.out.println("Mine at (" + x + ", " + y + ") triggered, dealing " + DAMAGE + " damage!");
        }
    }

    /**
     * Checks if the mine is still active.
     * @return true if the mine is active, false if it has been triggered.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the color of the mine for rendering.
     * @return The color of the mine (red).
     */
    public Color getColor() {
        return COLOR;
    }

    @Override
    public char getSymbol() {
        return 'M';
    }
}


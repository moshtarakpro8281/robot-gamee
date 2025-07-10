package game;

/**
 * کلاس پایه برای تمام موجودیت‌های بازی (ربات، دیوار، مین و ...)
 */
public abstract class Entity {
    protected int x, y;

    /**
     * Constructor to initialize the entity's position.
     * @param x The x-coordinate of the entity.
     * @param y The y-coordinate of the entity.
     */
    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate of the entity.
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the entity.
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the position of the entity.
     * @param x New x-coordinate.
     * @param y New y-coordinate.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * نماد موجودیت برای نمایش در نقشه یا رابط گرافیکی.
     * هر زیرکلاس باید آن را پیاده‌سازی کند.
     * @return نماد کاراکتری موجودیت
     */
    public abstract char getSymbol();
}

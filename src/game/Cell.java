package game;

public class Cell {
    private final int x, y;
    private Entity entity; // می‌تونه Robot، Wall یا Mine باشه

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.entity = null;
    }

    public boolean isEmpty() {
        return entity == null;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void removeEntity() {
        this.entity = null;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean hasRobot() {
        return entity instanceof Robot;
    }

    public Robot getRobot() {
        if (entity instanceof Robot)
            return (Robot) entity;
        return null;
    }

    public boolean hasWall() {
        return entity instanceof SteelWall || entity instanceof WoodenWall;
    }

    public Entity getWall() {
        if (hasWall())
            return entity;
        return null;
    }

    public boolean hasMine() {
        return entity instanceof Mine;
    }

    public Mine getMine() {
        if (entity instanceof Mine)
            return (Mine) entity;
        return null;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    /**
     * متد جدید برای برگرداندن نوع سلول بر اساس موجودیت داخل آن
     * @return نوع سلول CellType
     */
    public CellType getType() {
        if (entity == null)
            return CellType.EMPTY;
        if (entity instanceof Robot)
            return CellType.ROBOT;
        if (entity instanceof SteelWall || entity instanceof WoodenWall)
            return CellType.WALL;
        if (entity instanceof Mine)
            return CellType.MINE;

        // اگر موجودیت ناشناخته بود، می‌تونی این خط رو تغییر بدی
        return CellType.EMPTY;
    }
}

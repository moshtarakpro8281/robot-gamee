package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class Board {

    private final int width;
    private final int height;
    private final Cell[][] grid;
    private final List<Robot> robots = new ArrayList<>();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width]; // [row][col]
        initializeGrid();
    }

    private void initializeGrid() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Cell(x, y);
            }
        }
    }

    public boolean addRobot(Robot robot, int x, int y) {
        if (isInBounds(x, y) && grid[y][x].isEmpty()) {
            grid[y][x].setEntity(robot);
            robot.setPosition(x, y);
            robots.add(robot);
            return true;
        }
        return false;
    }

    public boolean moveRobot(Robot robot, int newX, int newY) {
        if (!isInBounds(newX, newY)) return false;

        int oldX = robot.getX();
        int oldY = robot.getY();

        if (grid[newY][newX].isEmpty()) {
            grid[oldY][oldX].removeEntity();
            grid[newY][newX].setEntity(robot);
            robot.setPosition(newX, newY);
            return true;
        }

        return false;
    }

    public boolean shoot(Robot shooter, int targetX, int targetY) {
        if (!isInBounds(targetX, targetY)) return false;

        Cell targetCell = grid[targetY][targetX];
        if (targetCell.hasRobot()) {
            targetCell.getRobot().takeDamage(); // این متد بدون پارامتر تعریف شده
            return true;
        }

        return false;
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Cell getCell(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return grid[y][x];
    }

    public Entity getEntityAt(int x, int y) {
        if (!isInBounds(x, y)) return null;

        Cell cell = grid[y][x];

        if (cell.hasRobot()) return cell.getRobot();
        if (cell.hasWall()) return cell.getWall();
        if (cell.hasMine()) return cell.getMine();

        return null;
    }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    /**
     * برمی‌گرداند نوع سلول در مختصات مشخص.
     * @param x مختصات ستون
     * @param y مختصات سطر
     * @return نوع سلول CellType یا null اگر خارج از محدوده باشد
     */
    public CellType getCellType(int x, int y) {
        if (!isInBounds(x, y)) return null;
        return grid[y][x].getType();
    }

    /**
     * برمی‌گرداند رنگ مناسب برای نوع سلول در مختصات مشخص.
     * @param x مختصات ستون
     * @param y مختصات سطر
     * @return رنگ رنگ‌آمیزی سلول
     */
    public Color getCellColor(int x, int y) {
        CellType type = getCellType(x, y);
        if (type == null) return Color.BLACK;

        switch (type) {
            case EMPTY:
                return Color.WHITE;
            case WALL:
                return Color.DARKGRAY;
            case ROBOT:
                return Color.BLUE;
            case MINE:
                return Color.RED;
            default:
                return Color.LIGHTGRAY;
        }
    }

    /**
     * متد نمونه برای ایجاد یک نقشه تستی با چند دیوار، مین و ربات.
     */
    public void generateTestMap() {
        addRobot(new Robot(1, 1), 1, 1);
        addRobot(new Robot(3, 3), 3, 3);

        grid[0][0].setEntity(new WoodenWall(0, 0));
        grid[2][2].setEntity(new SteelWall(2, 2));
        grid[4][4].setEntity(new Mine(4, 4));
    }
}

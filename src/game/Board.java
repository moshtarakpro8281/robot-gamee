package game;

import java.util.Random;

public class Board {
    private Cell[][] grid;
    private int width;
    private int height;
    private Random random = new Random();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Cell(j, i);
            }
        }
    }

    public void addObstacles(GameConfig config) {
        // افزودن 2 دیوار چوبی
        for (int i = 0; i < 2; i++) {
            int x, y;
            do {
                x = random.nextInt(config.getMapWidth());
                y = random.nextInt(config.getMapHeight());
            } while (grid[y][x].getEntity() != null);
            grid[y][x].setEntity(new WoodenWall(x, y));
        }
        // افزودن 2 دیوار فولادی
        for (int i = 0; i < 2; i++) {
            int x, y;
            do {
                x = random.nextInt(config.getMapWidth());
                y = random.nextInt(config.getMapHeight());
            } while (grid[y][x].getEntity() != null);
            grid[y][x].setEntity(new SteelWall(x, y));
        }
        // افزودن 1 دیوار معمولی به‌عنوان مانع
        int x, y;
        do {
            x = random.nextInt(config.getMapWidth());
            y = random.nextInt(config.getMapHeight());
        } while (grid[y][x].getEntity() != null);
        grid[y][x].setEntity(new NormalWall(x, y));
        // افزودن مین‌های مخفی
        for (int i = 0; i < config.getMineCount(); i++) {
            int x1, y1;
            do {
                x1 = random.nextInt(config.getMapWidth());
                y1 = random.nextInt(config.getMapHeight());
            } while (grid[y1][x1].getEntity() != null);
            grid[y1][x1].setEntity(new HiddenMine(x1, y1));
        }
        // افزودن مین‌های آشکار
        for (int i = 0; i < config.getMineCount() / 2; i++) {
            int x1, y1;
            do {
                x1 = random.nextInt(config.getMapWidth());
                y1 = random.nextInt(config.getMapHeight());
            } while (grid[y1][x1].getEntity() != null || grid[y1][x1].getEntity() instanceof VisibleMine);
            grid[y1][x1].setEntity(new VisibleMine(x1, y1));
        }
    }

    public void generateTestMap() {
        // اضافه کردن چند مانع برای تست
        grid[1][1].setEntity(new SteelWall(1, 1));
        grid[2][2].setEntity(new WoodenWall(2, 2));
        grid[3][3].setEntity(new SteelWall(3, 3));
        grid[4][4].setEntity(new WoodenWall(4, 4));
        grid[5][5].setEntity(new NormalWall(5, 5)); // دیوار معمولی به‌عنوان مانع
        grid[6][6].setEntity(new HiddenMine(6, 6)); // مین مخفی
        grid[7][7].setEntity(new VisibleMine(7, 7)); // مین آشکار
    }

    public Cell getCell(int x, int y) {
        if (y >= 0 && y < height && x >= 0 && x < width) {
            return grid[y][x];
        }
        return null;
    }

    public Entity getEntityAt(int x, int y) {
        Cell cell = getCell(x, y);
        return cell != null ? cell.getEntity() : null;
    }

    public boolean moveRobot(Robot robot, int newX, int newY) {
        Cell currentCell = getCell(robot.getX(), robot.getY());
        Cell newCell = getCell(newX, newY);
        if (newCell != null && newCell.getEntity() == null) {
            currentCell.removeEntity();
            newCell.setEntity(robot);
            robot.setPosition(newX, newY);
            return true;
        }
        return false;
    }

    public boolean shoot(Robot shooter, int targetX, int targetY) {
        Entity target = getEntityAt(targetX, targetY);
        if (target != null) {
            if (target instanceof Obstacle) {
                ((Obstacle) target).applyEffect(shooter);
                if (target instanceof WoodenWall) {
                    getCell(targetX, targetY).removeEntity();
                    return true;
                } else if (target instanceof SteelWall) {
                    SteelWall steelWall = (SteelWall) target;
                    steelWall.hit();
                    if (steelWall.isDestroyed()) {
                        getCell(targetX, targetY).removeEntity();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public static class Cell {
        private int x, y;
        private Entity entity;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Entity getEntity() { return entity; }
        public void setEntity(Entity entity) { this.entity = entity; }
        public void removeEntity() { this.entity = null; }
    }
}
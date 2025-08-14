package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleRobots {
    private Board board;
    private List<Robot> robots;
    private Player player;
    private GameManager gameManager;
    private GameUI gameUI;
    private GameConfig config;
    private Random random;

    public BattleRobots() {
        // 1. تنظیم اولیه با خواندن GameConfig
        config = new GameConfig(); // خواندن تنظیمات مثل اندازه نقشه و تعداد ربات‌ها
        board = new Board(config.getMapWidth(), config.getMapHeight()); // ساخت نقشه 10x10 (از config می‌خونه)
        board.addObstacles(config); // اضافه کردن موانع مثل مین‌ها
        board.generateTestMap(); // تنظیم نقشه تست

        // 2. ایجاد و قرار دادن ربات‌ها
        robots = new ArrayList<>();
        random = new Random();
        initializeRobots(); // ربات‌ها رو به‌صورت تصادفی روی نقشه قرار می‌ده

        // 3. ایجاد بازیکن
        player = new Player(robots); // فرض می‌کنیم Player با لیست ربات‌ها کار می‌کنه

        // 4. تنظیم GameManager برای مدیریت بازی
        gameManager = new GameManager(board, player); // GameManager مسئول اجرای منطق بازی

        // 5. فعال کردن UI اگه تنظیمات اجازه بده
        if (config.isUIEnabled()) {
            gameUI = new GameUI(board, player, gameManager); // UI برای نمایش گرافیکی
            gameUI.show(); // نمایش رابط کاربری
        }

        // 6. شروع بازی
        gameManager.startGame(); // اجرای بازی از طریق GameManager
    }

    private void initializeRobots() {
        // قرار دادن ربات‌ها به‌صورت تصادفی روی نقشه 10x10
        int robotCount = config.getRobotCount(); // تعداد ربات‌ها از config
        for (int i = 0; i < robotCount; i++) {
            int x, y;
            do {
                x = random.nextInt(10); // مختصات تصادفی بین 0 تا 9
                y = random.nextInt(10);
            } while (board.getCell(x, y).getEntity() != null); // چک کن که سلول خالی باشه
            Robot robot = new Robot(x, y);
            board.getCell(x, y).setEntity(robot);
            robots.add(robot);
        }
    }

    public static void main(String[] args) {
        new BattleRobots(); // نقطه شروع اجرای کل پروژه
    }
}
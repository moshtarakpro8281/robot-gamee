package game;

public class GameManager {
    private Board board;
    private Player player;

    public GameManager(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    public void startGame() {
        System.out.println("بازی شروع شد! نقشه: " + board.getWidth() + "x" + board.getHeight());
        // منطق ساده: ربات فعال رو یک قدم به راست حرکت بده
        Robot activeRobot = player.getActiveRobot();
        if (activeRobot != null) {
            int newX = activeRobot.getX() + 1;
            int newY = activeRobot.getY();
            if (newX < board.getWidth() && board.moveRobot(activeRobot, newX, newY)) {
                System.out.println("ربات فعال به (" + newX + ", " + newY + ") حرکت کرد!");
            } else {
                System.out.println("حرکت ممکن نیست! (موانع یا خارج از محدوده)");
            }
        } else {
            System.out.println("ربات فعال پیدا نشد!");
        }
    }
}
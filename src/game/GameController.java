package game;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameController {

    @FXML
    private GridPane mapGrid;

    private Board board; // شی‌ء اصلی بازی

    private final int CELL_SIZE = 30; // اندازه هر سلول روی صفحه

    public void initialize() {
        // مقداردهی اولیه نقشه؛ در آینده از تنظیمات دریافت کن
        board = new Board(15, 15);
        board.generateTestMap(); // برای تست: چند مانع و ربات قرار بده
        renderBoard();
    }

    // تابع رسم نقشه روی GridPane
    private void renderBoard() {
        mapGrid.getChildren().clear(); // پاک‌سازی قبلی

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Cell cell = board.getCell(col, row);  // دقت کن: x=col و y=row
                CellType cellType = (cell != null) ? cell.getType() : null;

                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);

                if (cellType != null) {
                    switch (cellType) {
                        case EMPTY:
                            rect.setFill(Color.WHITE);
                            break;
                        case WALL:
                            rect.setFill(Color.DARKGRAY);
                            break;
                        case ROBOT:
                            rect.setFill(Color.BLUE);
                            break;
                        case MINE:
                            rect.setFill(Color.RED);
                            break;
                        default:
                            rect.setFill(Color.LIGHTGRAY);
                    }
                } else {
                    rect.setFill(Color.BLACK); // برای سلول‌های نامعتبر
                }

                rect.setStroke(Color.BLACK); // خطوط بین خانه‌ها
                mapGrid.add(rect, col, row); // اضافه کردن به grid
            }
        }
    }

    // دکمه‌های کنترلی اختیاری (اگر در FXML هستن)
    @FXML
    private void handleStart() {
        System.out.println("بازی شروع شد!");
    }

    @FXML
    private void handleSettings() {
        System.out.println("تنظیمات کلیک شد.");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}

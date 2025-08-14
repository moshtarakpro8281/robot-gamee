package game;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;

public class RenderFixer {
    private boolean isRendering = false;
    private boolean gameStarted = false;

    public void safeRender(GameController controller) {
        if (isRendering || controller.getMapGrid() == null) {
            System.out.println("رندر در جریان است یا mapGrid null است.");
            return;
        }
        isRendering = true;

        Platform.runLater(() -> {
            try {
                controller.getMapGrid().getChildren().clear();
                controller.renderBoard();
                System.out.println("نقشه رندر شد. تعداد عناصر: " + controller.getMapGrid().getChildren().size());
            } catch (Exception e) {
                System.err.println("خطا در رندر: " + e.getMessage());
            } finally {
                isRendering = false;
            }
        });
    }

    public void moveRobot(GameController controller, int deltaX, int deltaY) {
        if (!gameStarted) {
            System.out.println("بازی هنوز شروع نشده! دکمه شروع رو بزن.");
            return;
        }

        if (controller.currentRobotIndex >= 0 && controller.currentRobotIndex < controller.selectedRobots.size()) {
            Robot robot = controller.selectedRobots.get(controller.currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX() + deltaX;
                int newY = robot.getY() + deltaY;
                if (newX >= 0 && newX < controller.board.getWidth() && newY >= 0 && newY < controller.board.getHeight() && controller.board.moveRobot(robot, newX, newY)) {
                    controller.handleCollision(robot, newX, newY);
                    controller.getMapGrid().getChildren().clear();
                    controller.renderBoard();
                    controller.updateAmmoLabel();
                    controller.updateHealthDisplay();
                    if (deltaX == -1) controller.lastDirection = 3;
                    else if (deltaX == 1) controller.lastDirection = 0;
                    else if (deltaY == -1) controller.lastDirection = 1;
                    else if (deltaY == 1) controller.lastDirection = 2;
                    System.out.println("ربات " + (controller.currentRobotIndex + 1) + " حرکت کرد به (" + newX + ", " + newY + ")!");
                } else {
                    System.out.println("حرکت ممکن نیست! (موانع یا خارج از محدوده)");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    public void startGame() {
        gameStarted = true;
        System.out.println("بازی شروع شد!");
    }

    public void pauseGame() {
        gameStarted = false;
        System.out.println("بازی توقف شد!");
    }
}
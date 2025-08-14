package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML
    private GridPane mapGrid;

    @FXML
    private Button moveLeftButton, moveUpButton, moveDownButton, moveRightButton;
    @FXML
    private Button selectRobot1Button, selectRobot2Button, selectRobot3Button, selectRobot4Button;
    @FXML
    private Button shootButton, shootUpButton, shootDownButton, shootLeftButton, shootRightButton;

    @FXML
    private Label ammoLabel;
    @FXML
    private Label scoreLabel1;
    @FXML
    private Label scoreLabel2;
    @FXML
    private Label scoreLabel3;
    @FXML
    private Label scoreLabel4;
    @FXML
    private VBox scoreVBox;

    @FXML
    private HBox ammoHBox;
    @FXML
    private VBox healthVBox;

    @FXML
    private HBox healthRow1, healthRow2, healthRow3, healthRow4;

    private Pane health1Pane, health2Pane, health3Pane, health4Pane;
    private Rectangle[] health1 = new Rectangle[3];
    private Rectangle[] health2 = new Rectangle[3];
    private Rectangle[] health3 = new Rectangle[3];
    private Rectangle[] health4 = new Rectangle[3];

    private int[] hitCounts = new int[4];
    private int[] ammoCounts = new int[4];
    private int[] robotScores = new int[4]; // آرایه برای امتیاز هر ربات

    public Board board;
    public List<Robot> selectedRobots = new ArrayList<>();
    public int currentRobotIndex = -1;
    public int lastDirection = 0;

    private GameConfig gameConfig = new GameConfig();
    private Random random = new Random();

    public int CELL_SIZE = 30;

    private MediaPlayer shootSound;

    public RenderFixer renderFixer = new RenderFixer();

    public int getCurrentRobotIndex() {
        return currentRobotIndex;
    }

    public List<Robot> getSelectedRobots() {
        return selectedRobots;
    }

    public void initialize() {
        board = new Board(gameConfig.getMapWidth(), gameConfig.getMapHeight());
        board.addObstacles(gameConfig);
        board.generateTestMap();

        Robot robot1 = new Robot(1, 1);
        Robot robot2 = new Robot(3, 3);
        Robot robot3 = new Robot(5, 5);
        Robot robot4 = new Robot(7, 7);

        board.getCell(1, 1).setEntity(robot1);
        board.getCell(3, 3).setEntity(robot2);
        board.getCell(5, 5).setEntity(robot3);
        board.getCell(7, 7).setEntity(robot4);

        selectedRobots.add(robot1);
        selectedRobots.add(robot2);
        selectedRobots.add(robot3);
        selectedRobots.add(robot4);

        for (int i = 0; i < 4; i++) {
            ammoCounts[i] = 10;
            hitCounts[i] = 0;
            robotScores[i] = 0; // امتیاز اولیه همه ربات‌ها صفر
        }

        if (!selectedRobots.isEmpty()) {
            currentRobotIndex = 0;
            System.out.println("ربات پیش‌فرض (" + selectedRobots.get(0).getX() + ", " + selectedRobots.get(0).getY() + ") انتخاب شد!");
        }

        URL soundFile = getClass().getResource("/sound/shoot.mp3");
        if (soundFile != null) {
            shootSound = new MediaPlayer(new Media(soundFile.toString()));
            System.out.println("فایل صوتی با موفقیت لود شد: " + soundFile);
        } else {
            System.err.println("فایل صوتی shoot.mp3 یافت نشد! لطفاً چک کنید.");
        }

        try {
            health1Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            health2Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            health3Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            health4Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));

            if (health1Pane != null) healthRow1.getChildren().add(health1Pane);
            if (health2Pane != null) healthRow2.getChildren().add(health2Pane);
            if (health3Pane != null) healthRow3.getChildren().add(health3Pane);
            if (health4Pane != null) healthRow4.getChildren().add(health4Pane);

            if (health1Pane != null) {
                health1[0] = (Rectangle) ((HBox) health1Pane).getChildren().get(0);
                health1[1] = (Rectangle) ((HBox) health1Pane).getChildren().get(1);
                health1[2] = (Rectangle) ((HBox) health1Pane).getChildren().get(2);
            }
            if (health2Pane != null) {
                health2[0] = (Rectangle) ((HBox) health2Pane).getChildren().get(0);
                health2[1] = (Rectangle) ((HBox) health2Pane).getChildren().get(1);
                health2[2] = (Rectangle) ((HBox) health2Pane).getChildren().get(2);
            }
            if (health3Pane != null) {
                health3[0] = (Rectangle) ((HBox) health3Pane).getChildren().get(0);
                health3[1] = (Rectangle) ((HBox) health3Pane).getChildren().get(1);
                health3[2] = (Rectangle) ((HBox) health3Pane).getChildren().get(2);
            }
            if (health4Pane != null) {
                health4[0] = (Rectangle) ((HBox) health4Pane).getChildren().get(0);
                health4[1] = (Rectangle) ((HBox) health4Pane).getChildren().get(1);
                health4[2] = (Rectangle) ((HBox) health4Pane).getChildren().get(2);
            }

            if (!healthVBox.getChildren().contains(healthRow1)) {
                healthVBox.getChildren().addAll(healthRow1, healthRow2, healthRow3, healthRow4);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("خطا در لود HealthDisplay.fxml: " + e.getMessage());
        }

        updateAmmoLabel();
        updateScoreLabels();
        updateHealthDisplay();
        renderFixer.safeRender(this);
    }

    public void renderBoard() {
        mapGrid.getChildren().clear();

        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();

        for (int i = 0; i < board.getWidth(); i++) {
            mapGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(CELL_SIZE));
        }
        for (int i = 0; i < board.getHeight(); i++) {
            mapGrid.getRowConstraints().add(new javafx.scene.layout.RowConstraints(CELL_SIZE));
        }

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setFill(Color.WHITE);

                Entity entity = board.getEntityAt(col, row);
                if (entity instanceof Mine) {
                    if (entity instanceof HiddenMine) {
                        Rectangle point = new Rectangle(CELL_SIZE / 4, CELL_SIZE / 4);
                        point.setFill(Color.web("#D8BFD8")); // بنفش کم‌رنگ برای مخفی
                        point.setX(col * CELL_SIZE + CELL_SIZE / 2 - point.getWidth() / 2);
                        point.setY(row * CELL_SIZE + CELL_SIZE / 2 - point.getHeight() / 2);
                        mapGrid.add(point, col, row);
                    } else if (entity instanceof VisibleMine) {
                        Circle circle = new Circle(CELL_SIZE / 2.5);
                        circle.setFill(Color.PURPLE); // بنفش پررنگ برای آشکار
                        circle.setCenterX(col * CELL_SIZE + CELL_SIZE / 2);
                        circle.setCenterY(row * CELL_SIZE + CELL_SIZE / 2);
                        mapGrid.add(circle, col, row);
                    }
                } else if (entity instanceof SteelWall) {
                    cell.setFill(Color.SILVER);
                } else if (entity instanceof WoodenWall) {
                    cell.setFill(Color.BROWN);
                } else if (entity instanceof NormalWall) {
                    cell.setFill(Color.ORANGE); // رنگ نارنجی برای دیوار معمولی
                }

                mapGrid.add(cell, col, row);
            }
        }

        for (int i = 0; i < selectedRobots.size(); i++) {
            Robot robot = selectedRobots.get(i);
            int col = robot.getX();
            int row = robot.getY();
            if (col >= 0 && col < board.getWidth() && row >= 0 && row < board.getHeight()) {
                Rectangle robotRect = new Rectangle(CELL_SIZE, CELL_SIZE);
                robotRect.setStroke(Color.BLACK);
                robotRect.setFill(i == currentRobotIndex && robot.isAlive() ? Color.YELLOW : Color.BLUE);
                Text number = new Text(String.valueOf(i + 1));
                number.setFill(Color.WHITE);
                number.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                number.setX(col * CELL_SIZE + CELL_SIZE / 2 - 5);
                number.setY(row * CELL_SIZE + CELL_SIZE / 2 + 5);
                if (!robot.isAlive()) {
                    robotRect.setFill(Color.RED);
                }
                mapGrid.add(robotRect, col, row);
                mapGrid.add(number, col, row);
            }
        }
    }

    public void updateAmmoLabel() {
        if (ammoLabel != null && currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            String robotName = "Robot " + (currentRobotIndex + 1);
            ammoLabel.setText("Ammo (" + robotName + "): " + ammoCounts[currentRobotIndex]);
            System.out.println("Updated ammoLabel to: " + ammoLabel.getText());
        } else {
            System.out.println("ammoLabel null است یا currentRobotIndex نامعتبر است!");
        }
    }

    private void updateScoreLabels() {
        if (scoreLabel1 != null) scoreLabel1.setText("Robot 1: " + robotScores[0]);
        if (scoreLabel2 != null) scoreLabel2.setText("Robot 2: " + robotScores[1]);
        if (scoreLabel3 != null) scoreLabel3.setText("Robot 3: " + robotScores[2]);
        if (scoreLabel4 != null) scoreLabel4.setText("Robot 4: " + robotScores[3]);
        System.out.println("امتیازها به‌روزرسانی شدند: " + robotScores[0] + ", " + robotScores[1] + ", " + robotScores[2] + ", " + robotScores[3]);
    }

    public void updateHealthDisplay() {
        for (int i = 0; i < selectedRobots.size(); i++) {
            Robot robot = selectedRobots.get(i);
            if (robot != null) {
                int healthLevel = robot.getHealth() / 33;
                Rectangle[] healthArray = (i == 0) ? health1 : (i == 1) ? health2 : (i == 2) ? health3 : health4;
                for (int j = 0; j < 3; j++) {
                    if (healthArray[j] != null) {
                        healthArray[j].setVisible(j < healthLevel);
                        healthArray[j].setFill(j < healthLevel ? Color.BLUE : Color.WHITE);
                    } else {
                        System.out.println("healthArray[" + j + "] برای ربات " + (i + 1) + " null است!");
                    }
                }
                System.out.println("Updated health for Robot " + (i + 1) + " to " + robot.getHealth() + "%");
            }
        }
    }

    @FXML
    private void moveRobotLeft() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX() - 1;
                int newY = robot.getY();
                if (newX >= 0 && board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderFixer.safeRender(this);
                    updateAmmoLabel();
                    updateHealthDisplay();
                    lastDirection = 3;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به چپ حرکت کرد به (" + newX + ", " + newY + ")!");
                } else {
                    System.out.println("حرکت به چپ ممکن نیست! (موانع یا خارج از محدوده)");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void moveRobotUp() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX();
                int newY = robot.getY() - 1;
                if (newY >= 0 && board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderFixer.safeRender(this);
                    updateAmmoLabel();
                    updateHealthDisplay();
                    lastDirection = 1;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به بالا حرکت کرد به (" + newX + ", " + newY + ")!");
                } else {
                    System.out.println("حرکت به بالا ممکن نیست! (موانع یا خارج از محدوده)");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void moveRobotDown() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX();
                int newY = robot.getY() + 1;
                if (newY < board.getHeight() && board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderFixer.safeRender(this);
                    updateAmmoLabel();
                    updateHealthDisplay();
                    lastDirection = 2;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به پایین حرکت کرد به (" + newX + ", " + newY + ")!");
                } else {
                    System.out.println("حرکت به پایین ممکن نیست! (موانع یا خارج از محدوده)");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void moveRobotRight() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX() + 1;
                int newY = robot.getY();
                if (newX < board.getWidth() && board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderFixer.safeRender(this);
                    updateAmmoLabel();
                    updateHealthDisplay();
                    lastDirection = 0;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به راست حرکت کرد به (" + newX + ", " + newY + ")!");
                } else {
                    System.out.println("حرکت به راست ممکن نیست! (موانع یا خارج از محدوده)");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void selectRobot1() { selectRobot(0); }
    @FXML
    private void selectRobot2() { selectRobot(1); }
    @FXML
    private void selectRobot3() { selectRobot(2); }
    @FXML
    private void selectRobot4() { selectRobot(3); }

    private void selectRobot(int index) {
        if (index >= 0 && index < selectedRobots.size()) {
            currentRobotIndex = index;
            updateHealthDisplay();
            updateAmmoLabel();
            updateScoreLabels();
            System.out.println("ربات " + (index + 1) + " تو (" + selectedRobots.get(index).getX() + ", " + selectedRobots.get(index).getY() + ") انتخاب شد!");
            renderFixer.safeRender(this);
        } else {
            System.out.println("ربات " + (index + 1) + " یافت نشد!");
        }
    }

    @FXML
    private void handleStart() {
        System.out.println("بازی شروع شد!");
        renderFixer.safeRender(this);
    }

    @FXML
    private void handleSettings() {
        renderFixer.pauseGame();
        System.out.println("بازی توقف شد! (تنظیمات کلیک شد)");
    }

    @FXML
    private void handleExit() {
        System.out.println("خروج از برنامه!");
        System.exit(0);
    }

    @FXML
    private void shoot() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            shootInDirection(lastDirection);
        }
    }

    @FXML
    private void shootUp() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            shootInDirection(1);
        }
    }

    @FXML
    private void shootDown() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            shootInDirection(2);
        }
    }

    @FXML
    private void shootLeft() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            shootInDirection(3);
        }
    }

    @FXML
    private void shootRight() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            shootInDirection(0);
        }
    }

    private void shootInDirection(int direction) {
        Robot shooter = selectedRobots.get(currentRobotIndex);
        if (shooter != null && !shooter.isAlive()) {
            System.out.println("ربات مرده و نمی‌تونه شلیک کنه!");
            return;
        }
        if (ammoCounts[currentRobotIndex] <= 0) {
            System.out.println("گلوله کافی نیست!");
            return;
        }

        int targetX = shooter.getX();
        int targetY = shooter.getY();
        boolean hit = false;

        switch (direction) {
            case 0: // راست
                targetX += 1;
                if (targetX < board.getWidth() && board.getEntityAt(targetX, targetY) != null) {
                    hit = true;
                }
                break;
            case 1: // بالا
                targetY -= 1;
                if (targetY >= 0 && board.getEntityAt(targetX, targetY) != null) {
                    hit = true;
                }
                break;
            case 2: // پایین
                targetY += 1;
                if (targetY < board.getHeight() && board.getEntityAt(targetX, targetY) != null) {
                    hit = true;
                }
                break;
            case 3: // چپ
                targetX -= 1;
                if (targetX >= 0 && board.getEntityAt(targetX, targetY) != null) {
                    hit = true;
                }
                break;
        }

        ammoCounts[currentRobotIndex]--; // کاهش گلوله با هر شلیک
        updateAmmoLabel();

        if (hit || (targetX >= 0 && targetX < board.getWidth() && targetY >= 0 && targetY < board.getHeight())) {
            if (board.shoot(shooter, targetX, targetY)) {
                System.out.println("شلیک موفق به (" + targetX + ", " + targetY + ")!");
                updateHealthDisplay();
                handleRobotHit(targetX, targetY);
            } else {
                System.out.println("شلیک به هدف ناموفق بود!");
            }

            showBulletEffect(shooter.getX(), shooter.getY(), targetX, targetY);

            renderFixer.safeRender(this);
            Entity target = board.getEntityAt(targetX, targetY);
            if (target instanceof Robot && !((Robot) target).isAlive()) {
                selectNextAliveRobot();
            }

            if (shootSound != null) {
                shootSound.stop();
                shootSound.play();
            }
        } else {
            System.out.println("شلیک خارج از محدوده!");
        }
        lastDirection = direction;
    }

    private void addAmmoAfterShot() {
        if (ammoCounts[currentRobotIndex] < 10) {
            ammoCounts[currentRobotIndex] = 10;
            updateAmmoLabel();
            System.out.println("10 گلوله دریافت شد!");
        }
    }

    public void handleCollision(Robot robot, int x, int y) {
        Entity entity = board.getEntityAt(x, y);
        if (entity instanceof Obstacle) {
            ((Obstacle) entity).applyEffect(robot);
            if (entity instanceof SteelWall) {
                ((SteelWall) entity).hit();
                if (((SteelWall) entity).getHitCount() >= 3) {
                    board.getCell(x, y).removeEntity();
                    if (currentRobotIndex >= 0 && currentRobotIndex < 4) {
                        robotScores[currentRobotIndex] += 3; // 3 امتیاز برای دیوار فولادی
                        updateScoreLabels();
                    }
                }
            } else if (entity instanceof WoodenWall) {
                board.getCell(x, y).removeEntity();
                if (currentRobotIndex >= 0 && currentRobotIndex < 4) {
                    robotScores[currentRobotIndex] += 1; // 1 امتیاز برای دیوار چوبی
                    updateScoreLabels();
                }
            } else if (entity instanceof NormalWall) {
                // دیوار معمولی از بین نمی‌ره
            }
            updateHealthDisplay();
        } else if (entity instanceof Robot) {
            Robot target = (Robot) entity;
            int targetIndex = selectedRobots.indexOf(target);
            if (targetIndex >= 0 && target != robot) {
                hitCounts[targetIndex]++;
                handleRobotHit(x, y);
            }
        }
        renderFixer.safeRender(this);
    }

    private void handleRobotHit(int x, int y) {
        Entity entity = board.getEntityAt(x, y);
        if (entity instanceof Robot) {
            Robot target = (Robot) entity;
            int targetIndex = selectedRobots.indexOf(target);
            if (targetIndex >= 0) {
                hitCounts[targetIndex]++;
                System.out.println("تعداد شلیک به ربات " + (targetIndex + 1) + ": " + hitCounts[targetIndex]);
                if (hitCounts[targetIndex] % 3 == 0) {
                    target.takeDamage(33);
                    System.out.println("آسیب 33 به ربات " + (targetIndex + 1) + " وارد شد. سلامت جدید: " + target.getHealth());
                    updateHealthDisplay();
                    if (!target.isAlive()) {
                        target.setAlive(false);
                        System.out.println("ربات " + (targetIndex + 1) + " نابود شد!");
                        renderFixer.safeRender(this);
                    }
                }
            }
        }
    }

    private void showBulletEffect(int startX, int startY, int targetX, int targetY) {
        Rectangle bullet = new Rectangle(CELL_SIZE / 3, CELL_SIZE / 3);
        bullet.setFill(Color.RED);

        int gridX = startX;
        int gridY = startY;
        switch (lastDirection) {
            case 0: gridX = targetX; break;
            case 1: gridY = targetY; break;
            case 2: gridY = targetY; break;
            case 3: gridX = targetX; break;
        }

        mapGrid.add(bullet, gridX, gridY);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            mapGrid.getChildren().remove(bullet);
        }));
        timeline.play();
    }

    private void selectNextAliveRobot() {
        for (int i = 1; i <= selectedRobots.size(); i++) {
            int nextIndex = (currentRobotIndex + i) % selectedRobots.size();
            Robot nextRobot = selectedRobots.get(nextIndex);
            if (nextRobot.isAlive()) {
                selectRobot(nextIndex);
                return;
            }
        }
        System.out.println("هیچ ربات زنده‌ای موجود نیست.");
    }
    public GridPane getMapGrid() {
        return mapGrid;
    }

}

/*package game;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    @FXML
    private GridPane mapGrid;

    @FXML
    private Button moveLeftButton;
    @FXML
    private Button moveUpButton;
    @FXML
    private Button moveDownButton;
    @FXML
    private Button moveRightButton;
    @FXML
    private Button selectRobot1Button;
    @FXML
    private Button selectRobot2Button;
    @FXML
    private Button selectRobot3Button;
    @FXML
    private Button selectRobot4Button;
    @FXML
    private Button shootButton;

    @FXML
    private Label ammoLabel;
    @FXML
    private HBox ammoHBox;
    @FXML
    private VBox healthVBox;

    @FXML
    private HBox healthRow1;
    @FXML
    private HBox healthRow2;
    @FXML
    private HBox healthRow3;
    @FXML
    private HBox healthRow4;

    private Pane health1Pane, health2Pane, health3Pane, health4Pane;
    private Rectangle[] health1 = new Rectangle[3];
    private Rectangle[] health2 = new Rectangle[3];
    private Rectangle[] health3 = new Rectangle[3];
    private Rectangle[] health4 = new Rectangle[3];
    private int[] hitCounts = new int[4]; // شمارش گلوله‌ها برای هر ربات
    private int[] ammoCounts = new int[4]; // گلوله برای هر ربات

    private Board board;
    private List<Robot> selectedRobots = new ArrayList<>();
    private int currentRobotIndex = -1;
    private int lastDirection = 0;
    private GameConfig gameConfig = new GameConfig();
    private Random random = new Random();

    private final int CELL_SIZE = 30;
    private MediaPlayer shootSound;

    public void initialize() {
        board = new Board(gameConfig.getMapWidth(), gameConfig.getMapHeight());
        board.addObstacles(gameConfig); // اضافه کردن مین‌ها
        board.generateTestMap();

        // اضافه کردن ربات‌ها
        Robot robot1 = new Robot(1, 1);
        Robot robot2 = new Robot(3, 3);
        Robot robot3 = new Robot(5, 5);
        Robot robot4 = new Robot(7, 7);
        board.getCell(1, 1).setEntity(robot1);
        board.getCell(3, 3).setEntity(robot2);
        board.getCell(5, 5).setEntity(robot3);
        board.getCell(7, 7).setEntity(robot4);
        selectedRobots.add(robot1);
        selectedRobots.add(robot2);
        selectedRobots.add(robot3);
        selectedRobots.add(robot4);

        // تنظیم اولیه گلوله‌ها
        for (int i = 0; i < 4; i++) {
            ammoCounts[i] = 10;
            hitCounts[i] = 0;
        }

        if (!selectedRobots.isEmpty()) {
            currentRobotIndex = 0;
            System.out.println("ربات پیش‌فرض (" + selectedRobots.get(0).getX() + ", " + selectedRobots.get(0).getY() + ") انتخاب شد!");
        }

        // تنظیم صدا
        URL soundFile = getClass().getResource("/sound/shoot.mp3");
        if (soundFile != null) {
            shootSound = new MediaPlayer(new Media(soundFile.toString()));
            System.out.println("فایل صوتی با موفقیت لود شد: " + soundFile);
        } else {
            System.err.println("فایل صوتی shoot.mp3 یافت نشد! لطفاً چک کنید.");
        }

        try {
            health1Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            health2Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            health3Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            health4Pane = FXMLLoader.load(getClass().getResource("/game/HealthDisplay.fxml"));
            System.out.println("HealthDisplay.fxml لود شد!");

            if (health1Pane != null) {
                health1[0] = (Rectangle) health1Pane.lookup("#health1");
                health1[1] = (Rectangle) health1Pane.lookup("#health2");
                health1[2] = (Rectangle) health1Pane.lookup("#health3");
            }
            if (health2Pane != null) {
                health2[0] = (Rectangle) health2Pane.lookup("#health1");
                health2[1] = (Rectangle) health2Pane.lookup("#health2");
                health2[2] = (Rectangle) health2Pane.lookup("#health3");
            }
            if (health3Pane != null) {
                health3[0] = (Rectangle) health3Pane.lookup("#health1");
                health3[1] = (Rectangle) health3Pane.lookup("#health2");
                health3[2] = (Rectangle) health3Pane.lookup("#health3");
            }
            if (health4Pane != null) {
                health4[0] = (Rectangle) health4Pane.lookup("#health1");
                health4[1] = (Rectangle) health4Pane.lookup("#health2");
                health4[2] = (Rectangle) health4Pane.lookup("#health3");
            }

            if (health1[0] != null && health1[1] != null && health1[2] != null) healthRow1.getChildren().add(health1Pane);
            if (health2[0] != null && health2[1] != null && health2[2] != null) healthRow2.getChildren().add(health2Pane);
            if (health3[0] != null && health3[1] != null && health3[2] != null) healthRow3.getChildren().add(health3Pane);
            if (health4[0] != null && health4[1] != null && health4[2] != null) healthRow4.getChildren().add(health4Pane);
            if (!healthVBox.getChildren().contains(healthRow1)) {
                healthVBox.getChildren().addAll(healthRow1, healthRow2, healthRow3, healthRow4);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("خطا در لود HealthDisplay.fxml: " + e.getMessage());
        }

        updateAmmoLabel();
        updateHealthDisplay();
        renderBoard();
    }

    private void renderBoard() {
        mapGrid.getChildren().clear();

        // تنظیم اندازه GridPane
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
        for (int i = 0; i < board.getWidth(); i++) {
            mapGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(CELL_SIZE));
        }
        for (int i = 0; i < board.getHeight(); i++) {
            mapGrid.getRowConstraints().add(new javafx.scene.layout.RowConstraints(CELL_SIZE));
        }

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setFill(Color.WHITE);

                Entity entity = board.getEntityAt(col, row);
                if (entity instanceof Robot) {
                    Robot robot = (Robot) entity;
                    int index = selectedRobots.indexOf(robot);
                    if (index >= 0) {
                        Rectangle robotRect = new Rectangle(CELL_SIZE, CELL_SIZE);
                        robotRect.setStroke(Color.BLACK);
                        robotRect.setFill(index == currentRobotIndex && robot.isAlive() ? Color.YELLOW : Color.BLUE); // آبی و زرد
                        Text number = new Text(String.valueOf(index + 1));
                        number.setFill(Color.WHITE);
                        number.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                        number.setX(col * CELL_SIZE + CELL_SIZE / 2 - 5);
                        number.setY(row * CELL_SIZE + CELL_SIZE / 2 + 5);
                        if (!robot.isAlive()) {
                            robotRect.setFill(Color.RED);
                        }
                        mapGrid.add(robotRect, col, row);
                        mapGrid.add(number, col, row);
                    }
                } else if (entity instanceof Mine) {
                    cell.setFill(Color.ORANGE); // مین نارنجی
                }

                mapGrid.add(cell, col, row);
            }
        }
    }

    private void updateAmmoLabel() {
        if (ammoLabel != null && currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            String robotName = "Robot " + (currentRobotIndex + 1);
            ammoLabel.setText("Ammo (" + robotName + "): " + ammoCounts[currentRobotIndex]);
            System.out.println("Updated ammoLabel to: " + ammoLabel.getText());
        }
    }

    private void updateHealthDisplay() {
        for (int i = 0; i < selectedRobots.size(); i++) {
            Robot robot = selectedRobots.get(i);
            if (robot != null) {
                int healthLevel = robot.getHealth() / 33; // هر 33% یک خانه
                Rectangle[] healthArray = (i == 0) ? health1 : (i == 1) ? health2 : (i == 2) ? health3 : health4;
                for (int j = 0; j < 3; j++) {
                    if (healthArray[j] != null) {
                        healthArray[j].setVisible(j < healthLevel);
                        healthArray[j].setFill(j < healthLevel ? Color.BLUE : Color.WHITE);
                    }
                }
                System.out.println("Updated health for Robot " + (i + 1) + " at (" + robot.getX() + ", " + robot.getY() + ") to " + robot.getHealth() + "%");
            }
        }
    }

    @FXML
    private void moveRobotLeft() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX() - 1;
                int newY = robot.getY();
                if (board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderBoard();
                    addAmmoAfterShot();
                    lastDirection = 3;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به چپ حرکت کرد!");
                } else {
                    System.out.println("حرکت به چپ ممکن نیست!");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void moveRobotUp() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX();
                int newY = robot.getY() - 1;
                if (board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderBoard();
                    addAmmoAfterShot();
                    lastDirection = 1;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به بالا حرکت کرد!");
                } else {
                    System.out.println("حرکت به بالا ممکن نیست!");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void moveRobotDown() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX();
                int newY = robot.getY() + 1;
                if (board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderBoard();
                    addAmmoAfterShot();
                    lastDirection = 2;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به پایین حرکت کرد!");
                } else {
                    System.out.println("حرکت به پایین ممکن نیست!");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void moveRobotRight() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot robot = selectedRobots.get(currentRobotIndex);
            if (robot != null && robot.isAlive()) {
                int newX = robot.getX() + 1;
                int newY = robot.getY();
                if (board.moveRobot(robot, newX, newY)) {
                    handleCollision(robot, newX, newY);
                    renderBoard();
                    addAmmoAfterShot();
                    lastDirection = 0;
                    System.out.println("ربات " + (currentRobotIndex + 1) + " به راست حرکت کرد!");
                } else {
                    System.out.println("حرکت به راست ممکن نیست!");
                }
            } else {
                System.out.println("ربات مرده یا ناموجود است!");
            }
        }
    }

    @FXML
    private void selectRobot1() {
        selectRobot(0);
    }

    @FXML
    private void selectRobot2() {
        selectRobot(1);
    }

    @FXML
    private void selectRobot3() {
        selectRobot(2);
    }

    @FXML
    private void selectRobot4() {
        selectRobot(3);
    }

    private void selectRobot(int index) {
        if (index >= 0 && index < selectedRobots.size()) {
            currentRobotIndex = index;
            updateHealthDisplay();
            updateAmmoLabel();
            System.out.println("ربات " + (index + 1) + " تو (" + selectedRobots.get(index).getX() + ", " + selectedRobots.get(index).getY() + ") انتخاب شد!");
            renderBoard();
        } else {
            System.out.println("ربات " + (index + 1) + " یافت نشد!");
        }
    }

    @FXML
    private void handleStart() {
        System.out.println("بازی شروع شد!");
        renderBoard();
    }

    @FXML
    private void handleSettings() {
        System.out.println("تنظیمات کلیک شد!");
    }

    @FXML
    private void handleExit() {
        System.out.println("خروج از برنامه!");
        System.exit(0);
    }

    @FXML
    private void shoot() {
        if (currentRobotIndex >= 0 && currentRobotIndex < selectedRobots.size()) {
            Robot shooter = selectedRobots.get(currentRobotIndex);
            if (shooter != null && !shooter.isAlive()) {
                System.out.println("ربات مرده و نمی‌تونه شلیک کنه!");
                return;
            }
            if (ammoCounts[currentRobotIndex] <= 0) {
                System.out.println("گلوله کافی نیست!");
                return;
            }
            int targetX = shooter.getX();
            int targetY = shooter.getY();
            switch (lastDirection) {
                case 0: targetX += 1; break;
                case 1: targetY -= 1; break;
                case 2: targetY += 1; break;
                case 3: targetX -= 1; break;
            }
            if (board.shoot(shooter, targetX, targetY)) {
                ammoCounts[currentRobotIndex]--;
                updateAmmoLabel();
                updateHealthDisplay();
                handleRobotHit(targetX, targetY);
                System.out.println("شلیک موفق به (" + targetX + ", " + targetY + ")!");
                renderBoard();
                if (shootSound != null) {
                    shootSound.stop();
                    shootSound.play();
                } else {
                    System.err.println("فایل صوتی لود نشده است!");
                }
            } else {
                System.out.println("شلیک ناموفق! (هدف یا خارج از محدوده)");
            }
        } else {
            System.out.println("هیچ رباتی انتخاب نشده!");
        }
    }

    private void addAmmoAfterShot() {
        if (ammoCounts[currentRobotIndex] < 10) {
            ammoCounts[currentRobotIndex] = 10;
            updateAmmoLabel();
            System.out.println("10 گلوله دریافت شد!");
        }
    }

    private void handleCollision(Robot robot, int x, int y) {
        Entity entity = board.getEntityAt(x, y);
        if (entity instanceof Obstacle) {
            ((Obstacle) entity).applyEffect(robot); // اعمال اثر مین
            board.getCell(x, y).removeEntity(); // حذف مین بعد از برخورد
            updateHealthDisplay();
        } else if (entity instanceof Robot) {
            Robot target = (Robot) entity;
            int targetIndex = selectedRobots.indexOf(target);
            if (targetIndex >= 0 && target != robot) {
                hitCounts[targetIndex]++;
                handleRobotHit(x, y);
            }
        }
        renderBoard();
    }

    private void handleRobotHit(int x, int y) {
        Entity entity = board.getEntityAt(x, y);
        if (entity instanceof Robot) {
            Robot target = (Robot) entity;
            int targetIndex = selectedRobots.indexOf(target);
            if (targetIndex >= 0) {
                hitCounts[targetIndex]++;
                if (hitCounts[targetIndex] % 3 == 0) {
                    target.takeDamage(33); // 33% آسیب برای هر 3 گلوله
                    updateHealthDisplay();
                }
                if (!target.isAlive()) {
                    target.setAlive(false);
                    renderBoard();
                }
            }
        }
    }
}*/
package game;


import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class GameUI extends Application {
    private Board board;
    private Player player;
    private GameManager gameManager;
    private Stage primaryStage;

    public GameUI(Board board, Player player, GameManager gameManager) {
        this.board = board;
        this.player = player;
        this.gameManager = gameManager;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // صفحه خوش‌آمدگویی
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));

        LinearGradient backgroundFill = new LinearGradient(0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#FFF5E1")), // کرمی
                new Stop(1, Color.web("#D2B48C"))); // قهوه‌ای روشن
        welcomeLayout.setBackground(new Background(new BackgroundFill(backgroundFill, CornerRadii.EMPTY, Insets.EMPTY)));

        Label welcomeText = new Label("خوش آمدید :)");
        welcomeText.setStyle("-fx-font-size: 36px; -fx-text-fill: black; -fx-highlight-fill: #D8BFD8;"); // هایلایت بنفش کم‌رنگ
        welcomeText.setAlignment(Pos.CENTER);

        Button startButton = new Button("شروع");
        startButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: white; -fx-font-size: 24px;");
        startButton.setOnAction(e -> showGameScreen());

        Button settingsButton = new Button("تنظیمات");
        settingsButton.setStyle("-fx-background-color: #ADD8E6; -fx-text-fill: black; -fx-font-size: 24px;");
        settingsButton.setOnAction(e -> showSettingsScreen());

        welcomeLayout.getChildren().addAll(welcomeText, startButton, settingsButton);
        Scene welcomeScene = new Scene(welcomeLayout, 400, 300);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Battle Robots");
        primaryStage.show();
    }

    private void showGameScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/GameController.fxml"));
            BorderPane gameRoot = loader.load();
            Scene gameScene = new Scene(gameRoot, 600, 400);
            primaryStage.setScene(gameScene);
            GameController controller = loader.getController();
            controller.initialize(); // رندر نقشه فعلی
            controller.renderFixer.startGame(); // شروع بازی
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSettingsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/SettingsController.fxml"));
            VBox settingsRoot = loader.load();
            Scene settingsScene = new Scene(settingsRoot, 400, 300);
            Stage settingsStage = new Stage();
            settingsStage.setScene(settingsScene);
            settingsStage.setTitle("تنظیمات");
            settingsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        launch(); // شروع UI
    }
}

/*package game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameUI extends Application {
    private Board board;
    private Player player;
    private GameManager gameManager;
    private Stage primaryStage;

    public GameUI(Board board, Player player, GameManager gameManager) {
        this.board = board;
        this.player = player;
        this.gameManager = gameManager;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // صفحه خوش‌آمدگویی
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(20));

        // بک‌گراند هاشوری کرمی-قهوه‌ای
        LinearGradient backgroundFill = new LinearGradient(0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#FFF5E1")), // کرمی
                new Stop(1, Color.web("#D2B48C"))); // قهوه‌ای روشن
        welcomeLayout.setBackground(new Background(new BackgroundFill(backgroundFill, CornerRadii.EMPTY, Insets.EMPTY)));

        Label welcomeText = new Label("خوش آمدید :)");
        welcomeText.setStyle("-fx-font-size: 36px; -fx-text-fill: black; -fx-highlight-fill: #D8BFD8;"); // هایلایت بنفش کم‌رنگ
        welcomeText.setAlignment(Pos.CENTER);

        Button playButton = new Button();
        playButton.setText("▶"); // مثلث Play
        playButton.setStyle("-fx-background-color: #00FF00; -fx-text-fill: white; -fx-font-size: 24px;"); // سبز روشن
        playButton.setOnAction(e -> showGameScreen());

        welcomeLayout.getChildren().addAll(welcomeText, playButton);
        Scene welcomeScene = new Scene(welcomeLayout, 400, 300);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Battle Robots");
        primaryStage.show();
    }

    private void showGameScreen() {
        // صفحه اصلی بازی
        BorderPane gameLayout = new BorderPane();

        // نقشه در مرکز
        GridPane grid = new GridPane();
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Rectangle cell = new Rectangle(30, 30);
                cell.setStroke(Color.BLACK);
                cell.setFill(Color.WHITE);

                Entity entity = board.getEntityAt(x, y);
                if (entity instanceof Mine) {
                    if (entity instanceof HiddenMine) {
                        Rectangle point = new Rectangle(7.5, 7.5); // نقطه کوچک
                        point.setFill(Color.RED);
                        point.setX(x * 30 + 11.25);
                        point.setY(y * 30 + 11.25);
                        grid.add(point, x, y);
                    } else if (entity instanceof VisibleMine) {
                        Circle circle = new Circle(6); // دایره کوچک
                        circle.setFill(Color.RED);
                        circle.setCenterX(x * 30 + 15);
                        circle.setCenterY(y * 30 + 15);
                        grid.add(circle, x, y);
                    }
                } else if (entity instanceof SteelWall) {
                    cell.setFill(Color.SILVER);
                } else if (entity instanceof WoodenWall) {
                    cell.setFill(Color.BROWN);
                } else if (entity instanceof NormalWall) {
                    cell.setFill(Color.LIGHTGRAY);
                } else if (entity instanceof Robot) {
                    cell.setFill(Color.BLUE); // ربات‌ها آبی
                }

                grid.add(cell, x, y);
            }
        }
        gameLayout.setCenter(grid);

        // راهنما در سمت راست
        VBox guide = new VBox(10);
        guide.setPadding(new Insets(10));
        guide.getChildren().addAll(
                new Label("راهنمای نقشه:"),
                new Label("R - ربات"),
                new Label("H - مین مخفی"),
                new Label("V - مین آشکار"),
                new Label("S - دیوار فولادی"),
                new Label("W - دیوار چوبی"),
                new Label("N - دیوار معمولی")
        );
        gameLayout.setRight(guide);

        // اطلاعات رنگی در سمت چپ
        VBox legend = new VBox(10);
        legend.setPadding(new Insets(10));
        legend.getChildren().addAll(
                new Label("اطلاعات رنگی:"),
                createLegendItem("ربات", Color.BLUE),
                createLegendItem("مین مخفی", Color.RED),
                createLegendItem("مین آشکار", Color.RED),
                createLegendItem("دیوار فولادی", Color.SILVER),
                createLegendItem("دیوار چوبی", Color.BROWN),
                createLegendItem("دیوار معمولی", Color.LIGHTGRAY)
        );
        gameLayout.setLeft(legend);

        Scene gameScene = new Scene(gameLayout, 600, 400);
        primaryStage.setScene(gameScene);
    }

    private HBox createLegendItem(String text, Color color) {
        HBox item = new HBox(5);
        Rectangle square = new Rectangle(20, 20);
        square.setFill(color);
        item.getChildren().addAll(square, new Label(text));
        return item;
    }

    public void show() {
        launch(); // شروع UI
    }
}*/
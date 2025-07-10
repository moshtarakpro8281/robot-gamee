package game;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class SettingsController {

    private ToggleGroup gameTypeGroup; // ساخته می‌شود در initialize()

    @FXML
    private RadioButton pvpRadio, pvAIRadio, aiVsAiRadio;

    @FXML
    private Spinner<Integer> widthSpinner;
    @FXML
    private Spinner<Integer> heightSpinner;

    private Stage dialogStage;

    private String selectedGameType = "pvp"; // مقدار پیش‌فرض
    private int boardWidth = 15;
    private int boardHeight = 15;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        // ایجاد ToggleGroup و اتصال رادیوها
        gameTypeGroup = new ToggleGroup();
        pvpRadio.setToggleGroup(gameTypeGroup);
        pvAIRadio.setToggleGroup(gameTypeGroup);
        aiVsAiRadio.setToggleGroup(gameTypeGroup);

        // مقداردهی اولیه اسپینرها
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, boardWidth));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, boardHeight));
    }

    @FXML
    private void handleConfirm() {
        RadioButton selectedRadio = (RadioButton) gameTypeGroup.getSelectedToggle();
        if (selectedRadio == pvpRadio) {
            selectedGameType = "pvp";
        } else if (selectedRadio == pvAIRadio) {
            selectedGameType = "pvAI";
        } else if (selectedRadio == aiVsAiRadio) {
            selectedGameType = "aiVsAi";
        }

        boardWidth = widthSpinner.getValue();
        boardHeight = heightSpinner.getValue();

        System.out.println("نوع بازی انتخاب شده: " + selectedGameType);
        System.out.println("ابعاد نقشه: " + boardWidth + " × " + boardHeight);

        dialogStage.close();
    }

    public String getSelectedGameType() {
        return selectedGameType;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardWidth(int width) {
        this.boardWidth = width;
        if (widthSpinner != null) {
            widthSpinner.getValueFactory().setValue(width);
        }
    }

    public void setBoardHeight(int height) {
        this.boardHeight = height;
        if (heightSpinner != null) {
            heightSpinner.getValueFactory().setValue(height);
        }
    }
}

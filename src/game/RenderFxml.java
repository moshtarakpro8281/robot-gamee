package game;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;

public class RenderFxml {

    @FXML
    private RadioButton pvpRadio, pvAIRadio, aiVsAiRadio;

    @FXML
    private Spinner<Integer> widthSpinner, heightSpinner;

    private ToggleGroup gameTypeGroup = new ToggleGroup();

    @FXML
    private void initialize() {
        pvpRadio.setToggleGroup(gameTypeGroup);
        pvAIRadio.setToggleGroup(gameTypeGroup);
        aiVsAiRadio.setToggleGroup(gameTypeGroup);

        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, 10));
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, 10));
    }

    @FXML
    private void handleConfirm() {
        RadioButton selectedRadio = (RadioButton) gameTypeGroup.getSelectedToggle();
        String gameType = selectedRadio.getText();

        int width = widthSpinner.getValue();
        int height = heightSpinner.getValue();

        System.out.println("نوع بازی: " + gameType);
        System.out.println("ابعاد نقشه: " + width + " x " + height);

        // ارسال تنظیمات به GameController
        GameController controller = new GameController(); // این یه نمونه موقت هست
        controller.board = new Board(width, height); // ابعاد جدید رو به نقشه اعمال کن
        controller.initialize(); // نقشه رو با تنظیمات جدید رندر کن
        controller.renderFixer.safeRender(controller); // رندر مجدد
    }
}
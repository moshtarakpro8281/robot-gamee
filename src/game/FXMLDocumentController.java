package game;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;       // اضافه شده
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label; // اگر میخوای میتونی برای نمایش پیام استفاده کنی

    private String selectedGameType = "pvp"; // مقدار پیش‌فرض نوع بازی

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: اینجا میتونی مقداردهی اولیه انجام بدی
    }    

    @FXML
    private void handleStartGame(ActionEvent event) {
        // اینجا می‌تونی براساس selectedGameType بازی رو شروع کنی
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("شروع بازی");
        alert.setHeaderText(null);
        alert.setContentText("شروع بازی انجام شد! نوع بازی: " + selectedGameType);
        alert.showAndWait();

        if (label != null) {
            label.setText("شروع بازی انتخاب شد: " + selectedGameType);
        }
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
            Parent root = loader.load();

            // پنجره جدید تنظیمات
            Stage stage = new Stage();
            stage.setTitle("تنظیمات بازی");
            stage.initModality(Modality.APPLICATION_MODAL); // جلوگیری از کار با پنجره اصلی
            stage.setScene(new Scene(root));
            
            // گرفتن کنترلر پنجره تنظیمات
            SettingsController controller = loader.getController();
            controller.setDialogStage(stage);

            stage.showAndWait(); // منتظر بمان تا پنجره بسته شود

            // دریافت نوع بازی انتخاب شده بعد از بسته شدن پنجره تنظیمات
            selectedGameType = controller.getSelectedGameType();

            if (label != null) {
                label.setText("نوع بازی انتخاب شده: " + selectedGameType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}


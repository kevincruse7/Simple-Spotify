package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class ChangePassword extends Controller {
    @FXML private PasswordField currentPassword;
    @FXML private TextField newPassword;
    @FXML private Button submit;

    private Stage stage;

    @FXML
    private void handleSubmit(ActionEvent event) {
        String currentPassword = this.currentPassword.getText();
        String newPassword = this.newPassword.getText();

        if (this.getMain().getDatabase().changePassword(currentPassword, newPassword)) {
            super.alert(this.stage, AlertType.INFORMATION, "Password updated!");
            this.stage.close();
        }
        else {
            super.alert(this.stage, AlertType.ERROR, "Current password incorrect!");
        }

        this.currentPassword.setText("");
        this.newPassword.setText("");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

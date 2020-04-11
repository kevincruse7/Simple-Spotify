package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    void handleSubmit(ActionEvent event) {
        
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

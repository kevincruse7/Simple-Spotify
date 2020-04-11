package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class ChangePassword extends Controller {
    @FXML private PasswordField currentPassword;
    @FXML private TextField newPassword;
    @FXML private Button submit;

    @FXML
    void handleSubmit(ActionEvent event) {

    }
}

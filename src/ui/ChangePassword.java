package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.stage.Stage;

/**
 * Controller class for change password interface
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class ChangePassword extends Controller {
    @FXML private PasswordField currentPassword;
    @FXML private TextField newPassword;
    @FXML private Button submit;

    // Reference to stage this interface belongs to
    private Stage stage;

    /**
     * Sets the change password interface stage to the passed value
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    // Handle password change request
    private void handleSubmit(ActionEvent event) {
        String currentPassword = this.currentPassword.getText();
        String newPassword = this.newPassword.getText();

        if (this.getMain().getDatabase().changePassword(currentPassword, newPassword)) {
            // If password update succeeds, inform user and close
            this.alert(this.stage, AlertType.INFORMATION, "Password updated!");
            this.stage.close();
        }
        else {
            // Otherwise, inform user that current password is incorrect
            this.alert(this.stage, AlertType.ERROR, "Current password incorrect!");
        }

        // Clear text after each attempt
        this.currentPassword.setText("");
        this.newPassword.setText("");
    }
}

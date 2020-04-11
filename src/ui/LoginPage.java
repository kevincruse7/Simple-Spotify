package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller for login page interface
 */
public class LoginPage extends Controller {
    // Interface element references
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;
    
    @FXML
    // Login to the database when the login button is pressed
    private void handleLogin(ActionEvent event) {
        Stage stage = super.getMain().getStage();
        Scene mainInterfaceScene = super.getMain().getMainInterface().getScene();
        
        // Get user credentials
        String username = this.username.getText();
        String password = this.password.getText();

        // Check credentials with database
        String credentialCheck = super.getMain().getDatabase().login(username, password);
        if (credentialCheck.equals("MATCH")) {
            // If credentials match, display the main interface
            stage.setScene(mainInterfaceScene);
        }
        else if (credentialCheck.equals("NEW")) {
            // If credentials indicate a new user, notify such and display the main interface
            super.alert(AlertType.INFORMATION, "User \'" + username + "\' created!");
            stage.setScene(mainInterfaceScene);
        }
        else {
            // Otherwise, if credentials don't match, notify the user
            super.alert(AlertType.ERROR, "Incorrect password!");
        }
    }
}
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
 * Controller class for login page interface
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class LoginPage extends Controller {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;
    
    @FXML
    // Handle database login request
    private void handleLogin(ActionEvent event) {
        Stage stage = this.getMain().getStage();
        Scene mainInterfaceScene = this.getMain().getMainInterface().getScene();
        
        // Get user credentials
        String username = this.username.getText();
        String password = this.password.getText();

        // Check credentials with database
        String credentialCheck = this.getMain().getDatabase().login(username, password);
        if (credentialCheck.equals("MATCH")) {
            // If credentials match, display the main interface
            stage.setScene(mainInterfaceScene);
        }
        else if (credentialCheck.equals("NEW")) {
            // If credentials indicate a new user, notify such and display the main interface
            this.alert(AlertType.INFORMATION, "User \'" + username + "\' created!");
            stage.setScene(mainInterfaceScene);
        }
        else {
            // Otherwise, if credentials don't match, notify the user
            this.alert(AlertType.ERROR, "Incorrect password!");
        }
    }
}
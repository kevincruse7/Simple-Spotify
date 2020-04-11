package ui;

import application.Database;

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
        // Application references
        Database database = super.getMain().getDatabase();
        
        // User interface references
        Stage stage = super.getMain().getStage();
        Scene mainInterfaceScene = super.getMain().getMainInterface().getScene();
        
        // Get text entered by user
        String username = this.username.getText();
        String password = this.password.getText();

        // Check if the given user exists in the database
        if (database.userExists(username)) {
            // If the user exists, check if the password is correct
            if (database.isValidCredentials(username, password)) {
                // If the password is correct, set the user and login
                database.setUser(username, password);
                stage.setScene(mainInterfaceScene);
            }
            else {
                // If the password is incorrect, notify the user
                super.alert(AlertType.ERROR, "Incorrect password!");
            }
        }
        else {
            // If the user does not exist in the database, create a new user and login
            database.addUser(username, password);
            super.alert(AlertType.INFORMATION, "User \'" + username + "\' created!");
            stage.setScene(mainInterfaceScene);
        }
    }
}
package ui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginPage {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button login;
    @FXML private Text loginFailed;

    @FXML
    void handleLogin(ActionEvent event) {
        // public static void setUser(String username, String password) throws IllegalArgumentException
    }
}
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
        // public boolean isValidCredentials(String username, String password)
        // public void setUser(String username, String password) throws IllegalArgumentException
        // public void addUser(String username, String password)
    }
}
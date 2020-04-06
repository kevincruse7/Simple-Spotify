package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginPage {
  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private Button login;
  @FXML private Text loginFailed;
}
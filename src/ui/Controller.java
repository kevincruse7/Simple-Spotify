package ui;

import application.Main;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 * Generalized interface controller that holds references to the main class and interface scene
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Controller {
    private Main main;
    private Scene scene;

    /**
     * Display a dialog on the given stage with the given alert type and message. Returns the user's selection if a confirmation
     * dialog, false otherwise
     */
    public boolean alert(Stage stage, AlertType alertType, String message) {
        Alert alert = new Alert(alertType);

        // Set dialog title based on alert type
        switch (alertType) {
            case CONFIRMATION:
                alert.setTitle("Confirmation Dialog");
                break;
            case ERROR:
                alert.setTitle("Error Dialog");
                break;
            case INFORMATION:
                alert.setTitle("Information Dialog");
                break;
            case NONE:
                alert.setTitle("Dialog");
                break;
            case WARNING:
                alert.setTitle("Warning Dialog");
        }

        // Set dialog content and parent window
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(stage);
        
        // Display dialog and return result
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Display a dialog on the main application stage with the given alert type and message
     */
    public boolean alert(AlertType alertType, String message) {
        return this.alert(this.getMain().getStage(), alertType, message);
    }
    
    /**
     * Sets the main application reference to the given value
     */
    public void setMain(Main main) {
        this.main = main;
    }
    
    /**
     * Sets the interface scene to the given value
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Returns a reference to the main application
     */
    public Main getMain() {
        return this.main;
    }
    
    /**
     * Returns a reference to the interface scene
     */
    public Scene getScene() {
        return this.scene;
    }
}
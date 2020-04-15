package ui;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

/**
 * Controller class for the create playlist interface
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class CreatePlaylist extends Controller {
    @FXML private TextField playlistTitle;
    @FXML private Button submit;

    // Reference to the stage this interface belongs to
    private Stage stage;

    /**
     * Sets the create playlist interface stage to the passed value
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    // Handle create playlist request
    private void handleSubmit(ActionEvent event) {
        String playlistTitle = this.playlistTitle.getText();
        
        if (playlistTitle.equals("")) {
            // If the user didn't enter a title, inform them
            this.alert(this.stage, AlertType.ERROR, "No title entered!");
        } else if (this.getMain().getDatabase().createPlaylist(playlistTitle).equals("PLAYLIST ALREADY EXISTS")) {
            // If the playlist already exists in the database, inform the user
            this.alert(this.stage, AlertType.ERROR, "Playlist already exists!");
        } else {
            // If the playlist was successfully created, inform the user and close
            this.alert(this.stage, AlertType.INFORMATION, "Playlist created!");
            this.stage.close();
        }

        // Clear inputs between attempts
        this.playlistTitle.setText("");
    }
}
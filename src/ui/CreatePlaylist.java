package ui;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class CreatePlaylist extends Controller {
    @FXML private TextField playlistTitle;
    @FXML private Button submit;

    private Stage stage;

    @FXML
    void handleSubmit(ActionEvent event) {
        String playlistTitle = this.playlistTitle.getText();
        
        if (playlistTitle.equals("")) {
            super.alert(this.stage, AlertType.ERROR, "No title entered!");
        } else if (super.getMain().getDatabase().createPlaylist(playlistTitle).equals("PLAYLIST ALREADY EXISTS")) {
            super.alert(this.stage, AlertType.ERROR, "Playlist already exists!");
        } else {
            super.alert(this.stage, AlertType.INFORMATION, "Playlist created!");
            this.stage.close();
        }

        this.playlistTitle.setText("");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
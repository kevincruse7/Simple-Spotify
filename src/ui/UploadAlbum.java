package ui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UploadAlbum {
    @FXML private TextField albumTitle;
    @FXML private Button uploadSongs;
    @FXML private Button uploadCoverArt;
    @FXML private Button submit;

    @FXML
    void handleSubmit(ActionEvent event) {
        // Database
        // public staic void uploadAlbum(Album album)
    }

    @FXML
    void handleUploadCoverArt(ActionEvent event) {

    }

    @FXML
    void handleUploadSongs(ActionEvent event) {

    }
}
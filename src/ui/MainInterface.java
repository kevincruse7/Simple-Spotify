package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MainInterface extends Controller {
    @FXML private TextField search;
    @FXML private Button changePassword;
    @FXML private Button uploadAlbum;
    @FXML private Button createPlaylist;
    @FXML private ListView<?> searchedSongs;
    @FXML private MenuItem addToPlaylist;
    @FXML private ListView<?> searchedArtists;
    @FXML private ListView<?> searchedAlbums;
    @FXML private ListView<?> searchedPlaylists;
    @FXML private ImageView cover;
    @FXML private Text title;
    @FXML private Text subtitle;
    @FXML private Button deletePlaylist;
    @FXML private ListView<?> viewedSongs;
    @FXML private MenuItem deleteFromPlaylist;
    @FXML private Button playPause;
    @FXML private Text songTime;

    @FXML
    void handleAddToPlaylist(ActionEvent event) {
        // Database
        // public void addToPlaylist(Song song)
    }

    @FXML
    void handleChangePassword(ActionEvent event) {

    }

    @FXML
    void handleCreatePlaylist(ActionEvent event) {
        // Display create playlist dialog
    }

    @FXML
    void handleDeleteFromPlaylist(ActionEvent event) {

    }

    @FXML
    void handleDeletePlaylist(ActionEvent event) {

    }

    @FXML
    void handlePlayPause(ActionEvent event) {
        // SongPlayer
        // public void togglePause()
    }

    @FXML
    void handleSearch(ActionEvent event) {
        // Database
        // public SearchResult search(String query)
    }

    @FXML
    void handleSearchedAlbumSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Album album)
    }

    @FXML
    void handleSearchedArtistSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Artist artist)
    }

    @FXML
    void handleSearchedPlaylistSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Playlist playlist)
    }

    @FXML
    void handleSearchedSongSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Song song)
    }

    @FXML
    void handleUploadAlbum(ActionEvent event) {
        // Display album dialog
    }

    @FXML
    void handleViewedSongSelected(MouseEvent event) {
        // SongPlayer
        // public void setQueue(List<Song> songs)
    }
}

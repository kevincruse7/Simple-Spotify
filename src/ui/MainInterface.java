package ui;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MainInterface {
    @FXML private TextField searchBar;
    @FXML private ListView<String> songResults;
    @FXML private MenuItem addToPlaylist;
    @FXML private ListView<String> albumResults;
    @FXML private ListView<String> artistResults;
    @FXML private Button createPlaylist;
    @FXML private Button uploadAlbum;
    @FXML private ListView<String> playlistResults;
    @FXML private ImageView albumCover;
    @FXML private Text title;
    @FXML private Text info;
    @FXML private ListView<String> songList;
    @FXML private MenuItem removeFromPlaylist;
    @FXML private Button playPause;
    @FXML private Text songTime;

    @FXML
    void handleAddToPlaylist(ActionEvent event) {
        // Database
        // public static void addToPlaylist(String songTitle)
    }

    @FXML
    void handleCreatePlaylist(ActionEvent event) {
        // Display create playlist dialog
    }

    @FXML
    void handlePlayPause(ActionEvent event) {
        // SongPlayer
        // public static void togglePause()
    }

    @FXML
    void handleSearch(ActionEvent event) {
        // Database
        // public static SearchResult search(String query)
    }

    @FXML
    void handleSearchedAlbumSelected(MouseEvent event) {
        // Database
        // public static void populateWithData(Album album)
    }

    @FXML
    void handleSearchedArtistSelected(MouseEvent event) {
        // Database
        // public static void populateWithData(Artist artist)
    }

    @FXML
    void handleSearchedPlaylistSelected(MouseEvent event) {
        // Database
        // public static void populateWithData(Playlist playlist)
    }

    @FXML
    void handleSearchedSongSelected(MouseEvent event) {
        // Database
        // public static void populateWithData(Song song)
    }

    @FXML
    void handleUploadAlbum(ActionEvent event) {
        // Display album dialog
    }

    @FXML
    void handleViewedSongSelected(MouseEvent event) {
        // SongPlayer
        // public static void setQueue(List<Song> songs)
    }
}
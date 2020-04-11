package ui;

import data.Album;
import data.Artist;
import data.Playlist;
import data.SearchResult;
import data.Song;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainInterface extends Controller {
    @FXML private TextField search;
    @FXML private Button changePassword;
    @FXML private Button uploadAlbum;

    @FXML private Button createPlaylist;
    @FXML private Button deletePlaylist;
    @FXML private MenuItem addToPlaylist;
    @FXML private MenuItem deleteFromPlaylist;

    @FXML private ListView<Song> searchedSongs;
    @FXML private ListView<Artist> searchedArtists;
    @FXML private ListView<Album> searchedAlbums;
    @FXML private ListView<Playlist> searchedPlaylists;
    
    @FXML private ImageView cover;
    @FXML private Text title;
    @FXML private Text subtitle;
    @FXML private ListView<Song> viewedSongs;

    @FXML private Button playPause;
    @FXML private Text songTime;

    @FXML
    void handleSearch(ActionEvent event) {
        // Send query to database
        String query = this.search.getText();
        SearchResult result = super.getMain().getDatabase().search(query);

        // Pull data from search result
        ObservableList<Song> songs = FXCollections.observableArrayList(result.getSongs());
        ObservableList<Artist> artists = FXCollections.observableArrayList(result.getArtists());
        ObservableList<Album> albums = FXCollections.observableArrayList(result.getAlbums());
        ObservableList<Playlist> playlists = FXCollections.observableArrayList(result.getPlaylists());

        // Push data to list views
        searchedSongs.setItems(songs);
        searchedArtists.setItems(artists);
        searchedAlbums.setItems(albums);
        searchedPlaylists.setItems(playlists);
    }

    @FXML
    void handleChangePassword(ActionEvent event) {
        Stage stage = new Stage();
        
        // Initialize change password interface stage
        stage.initOwner(super.getMain().getStage());
        stage.setScene(super.getMain().getChangePassword().getScene());
        stage.setTitle("Change Password");
        stage.setResizable(false);
        stage.show();

        // Pass stage reference to change password interface controller
        super.getMain().getChangePassword().setStage(stage);
    }

    @FXML
    void handleUploadAlbum(ActionEvent event) {
        // Display album dialog
    }

    @FXML
    void handleCreatePlaylist(ActionEvent event) {
        // Display create playlist dialog
    }

    @FXML
    void handleDeletePlaylist(ActionEvent event) {

    }

    @FXML
    void handleAddToPlaylist(ActionEvent event) {
        // Database
        // public void addToPlaylist(Song song)
    }
    
    @FXML
    void handleDeleteFromPlaylist(ActionEvent event) {

    }

    @FXML
    void handleSearchedSongSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Song song)
    }

    @FXML
    void handleSearchedArtistSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Artist artist)
    }
    
    @FXML
    void handleSearchedAlbumSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Album album)
    }

    @FXML
    void handleSearchedPlaylistSelected(MouseEvent event) {
        // Database
        // public void populateWithData(Playlist playlist)
    }

    @FXML
    void handleViewedSongSelected(MouseEvent event) {
        // SongPlayer
        // public void setQueue(List<Song> songs)
    }

    @FXML
    void handlePlayPause(ActionEvent event) {
        // SongPlayer
        // public void togglePause()
    }
}

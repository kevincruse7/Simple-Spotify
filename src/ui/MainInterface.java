package ui;

import data.Album;
import data.Artist;
import data.Playlist;
import data.SearchResult;
import data.Song;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import javafx.stage.Stage;

/**
 * Controller class for main interface
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
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

    /**
     * Enables the play button
     */
    public void enablePlayPause() {
        this.playPause.disableProperty().set(false);
    }

    /**
     * Disables the play button
     */
    public void disablePlayPause() {
        this.playPause.disableProperty().set(true);
        this.playPause.setText("Play");
    }

    /**
     * Sets the current song time to the passed value
     */
    public void setSongTime(String songTime) {
        this.songTime.setText(songTime);
    }
    
    @FXML
    // Handle search request
    private void handleSearch(ActionEvent event) {
        // Send query to database
        String query = this.search.getText();
        SearchResult result = this.getMain().getDatabase().search(query);

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
    // Handle change password request
    private void handleChangePassword(ActionEvent event) {
        Stage stage = new Stage();
        
        // Initialize change password interface stage
        stage.initOwner(this.getMain().getStage());
        stage.setScene(this.getMain().getChangePassword().getScene());
        stage.setTitle("Change Password");
        stage.setResizable(false);
        stage.show();

        // Pass stage reference to change password interface controller
        this.getMain().getChangePassword().setStage(stage);
    }

    @FXML
    // Handle upload album request
    private void handleUploadAlbum(ActionEvent event) {
        Stage stage = new Stage();

        // Initialize upload album interface stage
        stage.initOwner(this.getMain().getStage());
        stage.setScene(this.getMain().getUploadAlbum().getScene());
        stage.setTitle("Upload Album");
        stage.setResizable(false);
        stage.show();

        // Pass stage reference to upload album interface controller
        this.getMain().getUploadAlbum().setStage(stage);
    }

    @FXML
    // Handle create playlist request
    private void handleCreatePlaylist(ActionEvent event) {
        Stage stage = new Stage();

        // Initialize create playlist interface stage
        stage.initOwner(this.getMain().getStage());
        stage.setScene(this.getMain().getCreatePlaylist().getScene());
        stage.setTitle("Create Playlist");
        stage.setResizable(false);
        stage.show();

        // Pass stage reference to create playlist interface controller
        this.getMain().getCreatePlaylist().setStage(stage);
    }

    @FXML
    // Handle delete playlist request
    private void handleDeletePlaylist(ActionEvent event) {
        if (this.alert(AlertType.CONFIRMATION, "Are you sure?")) {
            // If user confirms, remove playlist from database and song queue
            Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();
            this.getMain().getDatabase().deletePlaylist(selectedPlaylist);
            this.getMain().getSongPlayer().setSongQueue(null);
            
            // Remove playlist from viewing pane and search results
            this.title.setText("Artist/Album/Playlist");
            this.subtitle.setText("Creators/Release Year");
            this.deletePlaylist.disableProperty().set(true);
            this.viewedSongs.setItems(null);
            this.searchedPlaylists.getItems().remove(selectedPlaylist);
        }
    }

    @FXML
    // Handle add to playlist request
    private void handleAddToPlaylist(ActionEvent event) {
        Song selectedSong = this.searchedSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();

        if (this.getMain().getDatabase().addToPlaylist(selectedSong, selectedPlaylist).equals("CONNECTION CREATED")) {
            // Get song data from database
            this.getMain().getDatabase().populateWithData(selectedSong);
            
            // Set song queue to revised playlist
            if (selectedPlaylist.getSongs() == null) {
                selectedPlaylist.setSongs(new ArrayList<Song>());
            }
            selectedPlaylist.getSongs().add(selectedSong);
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedPlaylist.getSongs());
            this.getMain().getSongPlayer().setSongQueue(songQueue);
            
            // Add song to viewing pane
            this.viewedSongs.getItems().add(selectedSong);
            this.playPause.setText("Play");
        } else {
            this.alert(AlertType.ERROR, "Song already exists in this playlist!");
        }
    }
    
    @FXML
    // Handle delete from playlist request
    private void handleDeleteFromPlaylist(ActionEvent event) {
        Song selectedSong = this.viewedSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();

        if (selectedSong != null && this.getMain().getDatabase().removeFromPlaylist(selectedSong, selectedPlaylist)
                .equals("CONNECTION DELETED")) {
            // Remove song from viewing pane
            this.viewedSongs.getItems().remove(selectedSong);
            this.playPause.setText("Play");
            
            // Set song queue to revised playlist
            selectedPlaylist.getSongs().remove(selectedSong);
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedPlaylist.getSongs());
            this.getMain().getSongPlayer().setSongQueue(songQueue);
        }
    }

    @FXML
    // Handle song selection in search pane
    private void handleSearchedSongSelected(MouseEvent event) {
        Song selectedSong = this.searchedSongs.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedSong != null) {
            // Get song data from database
            this.getMain().getDatabase().populateWithData(selectedSong);
            
            // Set song queue to the selected song
            ArrayList<Song> songQueue = new ArrayList<Song>();
            songQueue.add(selectedSong);
            this.getMain().getSongPlayer().setSongQueue(songQueue);
        }
    }

    @FXML
    // Handle artist selection in search pane
    private void handleSearchedArtistSelected(MouseEvent event) {
        Artist selectedArtist = this.searchedArtists.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedArtist != null) {
            // Clear other search selections
            this.searchedSongs.getSelectionModel().clearSelection();
            this.searchedAlbums.getSelectionModel().clearSelection();
            this.searchedPlaylists.getSelectionModel().clearSelection();
            
            // Disable playlist controls
            this.deletePlaylist.disableProperty().set(true);
            this.addToPlaylist.disableProperty().set(true);
            this.deleteFromPlaylist.disableProperty().set(true);
            
            // Get artist data from database
            this.getMain().getDatabase().populateWithData(selectedArtist);
            
            // Set artist song list as song queue
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedArtist.getSongs());
            this.getMain().getSongPlayer().setSongQueue(songQueue);

            // Display artist in viewing pane
            this.title.setText(selectedArtist.getName());
            String genres = selectedArtist.getGenres().toString();
            genres = genres.substring(1, genres.length() - 1);
            this.subtitle.setText(genres);
            URI blank = null;
            try {
                 blank = this.getClass().getResource("/resources/blank.png").toURI();
            } catch (URISyntaxException e) {
                this.getMain().exitWithException(e, "reading in blank cover art");
            }
            this.cover.setImage(new Image(blank.toString()));
            this.viewedSongs.setItems(FXCollections.observableArrayList(selectedArtist.getSongs()));
        }
    }
    
    @FXML
    // Handle album selection in search pane
    private void handleSearchedAlbumSelected(MouseEvent event) {
        Album selectedAlbum = this.searchedAlbums.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedAlbum != null) {
            // Clear other search selections
            this.searchedSongs.getSelectionModel().clearSelection();
            this.searchedArtists.getSelectionModel().clearSelection();
            this.searchedPlaylists.getSelectionModel().clearSelection();
            
            // Disable playlist controls
            this.deletePlaylist.disableProperty().set(true);
            this.addToPlaylist.disableProperty().set(true);
            this.deleteFromPlaylist.disableProperty().set(true);
            
            // Get album data from database
            this.getMain().getDatabase().populateWithData(selectedAlbum);
            
            // Set album as song queue
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedAlbum.getSongs());
            this.getMain().getSongPlayer().setSongQueue(songQueue);

            // Display album in viewing pane
            this.title.setText(selectedAlbum.getTitle());
            String artists = selectedAlbum.getArtists().toString();
            artists = artists.substring(1, artists.length() - 1);
            String genres = selectedAlbum.getGenres().toString();
            genres = genres.substring(1, genres.length() - 1);
            this.subtitle.setText(artists + " - " + genres + " - " + Integer.toString(selectedAlbum.getReleaseYear()));
            this.viewedSongs.setItems(FXCollections.observableArrayList(selectedAlbum.getSongs()));

            // Display album cover art
            try {
                FileInputStream albumCover = new FileInputStream(selectedAlbum.getCover());
                this.cover.setImage(new Image(albumCover));
                albumCover.close();
            } catch (IOException e) {
                this.getMain().exitWithException(e, "loading cover art");
            }
        }
    }

    @FXML
    // Handle playlist selection in search pane
    private void handleSearchedPlaylistSelected(MouseEvent event) {
        Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedPlaylist != null) {
            // Clear other search selections
            this.searchedSongs.getSelectionModel().clearSelection();
            this.searchedArtists.getSelectionModel().clearSelection();
            this.searchedAlbums.getSelectionModel().clearSelection();
            
            if (this.getMain().getDatabase().getUser().equals(selectedPlaylist.getCreator())) {
                // If the user is the playlist creator, enable playlist controls
                this.deletePlaylist.disableProperty().set(false);
                this.addToPlaylist.disableProperty().set(false);
                this.deleteFromPlaylist.disableProperty().set(false);
            } else {
                // Otherwise, disable them
                this.deletePlaylist.disableProperty().set(true);
                this.addToPlaylist.disableProperty().set(true);
                this.deleteFromPlaylist.disableProperty().set(true);
            }
            
            // Get playlist data from database
            this.getMain().getDatabase().populateWithData(selectedPlaylist);
            
            // Set playlist as song queue
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedPlaylist.getSongs());
            this.getMain().getSongPlayer().setSongQueue(songQueue);

            // Display playlist in viewing pane
            this.title.setText(selectedPlaylist.getTitle());
            this.subtitle.setText(selectedPlaylist.getCreator());
            URI blank = null;
            try {
                 blank = this.getClass().getResource("/resources/blank.png").toURI();
            } catch (URISyntaxException e) {
                this.getMain().exitWithException(e, "reading in blank cover art");
            }
            this.cover.setImage(new Image(blank.toString()));
            this.viewedSongs.setItems(FXCollections.observableArrayList(selectedPlaylist.getSongs()));
        }
    }

    @FXML
    // Handle play/pause request
    private void handlePlayPause(ActionEvent event) {
        if (this.playPause.getText().equals("Play")) {
            // If play was pressed, then play song and toggle text
            this.getMain().getSongPlayer().play();
            this.playPause.setText("Pause");
        } else {
            // Otherwise, if pause was pressed, pause song and toggle text
            this.getMain().getSongPlayer().pause();
            this.playPause.setText("Play");
        }
    }
}
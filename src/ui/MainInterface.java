package ui;

import data.Album;
import data.Artist;
import data.Playlist;
import data.SearchResult;
import data.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    
    public void setSongTime(String songTime) {
        this.songTime.setText(songTime);
    }

    public void enablePlayPause() {
        this.playPause.disableProperty().set(false);
    }

    public void disablePlayPause() {
        this.playPause.disableProperty().set(true);
        this.playPause.setText("Play");
    }
    
    @FXML
    private void handleSearch(ActionEvent event) {
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
    private void handleChangePassword(ActionEvent event) {
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
    private void handleUploadAlbum(ActionEvent event) {
        Stage stage = new Stage();

        // Initialize upload album interface stage
        stage.initOwner(super.getMain().getStage());
        stage.setScene(super.getMain().getUploadAlbum().getScene());
        stage.setTitle("Upload Album");
        stage.setResizable(false);
        stage.show();

        // Pass stage reference to upload album interface controller
        super.getMain().getUploadAlbum().setStage(stage);
    }

    @FXML
    private void handleCreatePlaylist(ActionEvent event) {
        Stage stage = new Stage();

        // Initialize create playlist interface stage
        stage.initOwner(super.getMain().getStage());
        stage.setScene(super.getMain().getCreatePlaylist().getScene());
        stage.setTitle("Create Playlist");
        stage.setResizable(false);
        stage.show();

        // Pass stage reference to create playlist interface controller
        super.getMain().getCreatePlaylist().setStage(stage);
    }

    @FXML
    private void handleDeletePlaylist(ActionEvent event) {
        if (super.alert(AlertType.CONFIRMATION, "Are you sure?")) {
            Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();
            super.getMain().getDatabase().deletePlaylist(selectedPlaylist);

            this.deletePlaylist.disableProperty().set(true);
            this.searchedPlaylists.getItems().remove(selectedPlaylist);
            this.title.setText("Artist/Album/Playlist");
            this.subtitle.setText("Creators/Release Year");
        }
    }

    @FXML
    private void handleAddToPlaylist(ActionEvent event) {
        Song selectedSong = this.searchedSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();

        if (super.getMain().getDatabase().addToPlaylist(selectedSong, selectedPlaylist).equals("CONNECTION CREATED")) {
            this.viewedSongs.getItems().add(selectedSong);
        }
    }
    
    @FXML
    private void handleDeleteFromPlaylist(ActionEvent event) {
        Song selectedSong = this.viewedSongs.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();

        if (super.getMain().getDatabase().removeFromPlaylist(selectedSong, selectedPlaylist).equals("CONNECTION DELETED")) {
            selectedPlaylist.getSongs().remove(selectedSong);
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedPlaylist.getSongs());
            this.getMain().getSongPlayer().setSongQueue(songQueue);

            this.viewedSongs.getItems().remove(selectedSong);
        }
    }

    @FXML
    private void handleSearchedSongSelected(MouseEvent event) {
        Song selectedSong = this.searchedSongs.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedSong != null) {
            
            super.getMain().getDatabase().populateWithData(selectedSong);
            
            ArrayList<Song> songQueue = new ArrayList<Song>();
            songQueue.add(selectedSong);
            super.getMain().getSongPlayer().setSongQueue(songQueue);
        }
    }

    @FXML
    private void handleSearchedArtistSelected(MouseEvent event) {
        Artist selectedArtist = this.searchedArtists.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedArtist != null) {
            this.searchedSongs.getSelectionModel().clearSelection();
            this.searchedAlbums.getSelectionModel().clearSelection();
            this.searchedPlaylists.getSelectionModel().clearSelection();
            
            this.deletePlaylist.disableProperty().set(true);
            this.addToPlaylist.disableProperty().set(true);
            this.deleteFromPlaylist.disableProperty().set(true);
            
            super.getMain().getDatabase().populateWithData(selectedArtist);
            
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedArtist.getSongs());
            super.getMain().getSongPlayer().setSongQueue(songQueue);

            this.title.setText(selectedArtist.getName());
            String genres = selectedArtist.getGenres().toString();
            genres = genres.substring(1, genres.length() - 1);
            this.subtitle.setText(genres);
            this.cover.setImage(new Image(new File("resources/blank.png").getAbsoluteFile().toURI().toString()));

            this.viewedSongs.setItems(FXCollections.observableArrayList(selectedArtist.getSongs()));
        }
    }
    
    @FXML
    private void handleSearchedAlbumSelected(MouseEvent event) {
        Album selectedAlbum = this.searchedAlbums.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedAlbum != null) {
            this.searchedSongs.getSelectionModel().clearSelection();
            this.searchedArtists.getSelectionModel().clearSelection();
            this.searchedPlaylists.getSelectionModel().clearSelection();
            
            this.deletePlaylist.disableProperty().set(true);
            this.addToPlaylist.disableProperty().set(true);
            this.deleteFromPlaylist.disableProperty().set(true);
            
            super.getMain().getDatabase().populateWithData(selectedAlbum);
            
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedAlbum.getSongs());
            super.getMain().getSongPlayer().setSongQueue(songQueue);

            this.title.setText(selectedAlbum.getTitle());
            String artists = selectedAlbum.getArtists().toString();
            artists = artists.substring(1, artists.length() - 1);
            String genres = selectedAlbum.getGenres().toString();
            genres = genres.substring(1, genres.length() - 1);
            this.subtitle.setText(artists + " - " + genres + " - " + Integer.toString(selectedAlbum.getReleaseYear()));

            this.viewedSongs.setItems(FXCollections.observableArrayList(selectedAlbum.getSongs()));

            try {
                this.cover.setImage(new Image(new FileInputStream(selectedAlbum.getCover())));
            } catch (FileNotFoundException e) {
                super.getMain().exitWithException(e, "loading cover art");
            }
        }
    }

    @FXML
    private void handleSearchedPlaylistSelected(MouseEvent event) {
        Playlist selectedPlaylist = this.searchedPlaylists.getSelectionModel().getSelectedItem();
        if (event.getButton() == MouseButton.PRIMARY && selectedPlaylist != null) {
            this.searchedSongs.getSelectionModel().clearSelection();
            this.searchedArtists.getSelectionModel().clearSelection();
            this.searchedAlbums.getSelectionModel().clearSelection();
            
            if (super.getMain().getDatabase().getUser().equals(selectedPlaylist.getCreator())) {
                this.deletePlaylist.disableProperty().set(false);
                this.addToPlaylist.disableProperty().set(false);
                this.deleteFromPlaylist.disableProperty().set(false);
            } else {
                this.deletePlaylist.disableProperty().set(true);
                this.addToPlaylist.disableProperty().set(true);
                this.deleteFromPlaylist.disableProperty().set(true);
            }
            
            super.getMain().getDatabase().populateWithData(selectedPlaylist);
            
            ArrayList<Song> songQueue = new ArrayList<Song>(selectedPlaylist.getSongs());
            super.getMain().getSongPlayer().setSongQueue(songQueue);

            this.title.setText(selectedPlaylist.getTitle());
            this.subtitle.setText(selectedPlaylist.getCreator());
            this.cover.setImage(new Image(new File("resources/blank.png").getAbsoluteFile().toURI().toString()));
            this.viewedSongs.setItems(FXCollections.observableArrayList(selectedPlaylist.getSongs()));
        }
    }

    @FXML
    private void handlePlayPause(ActionEvent event) {
        if (this.playPause.getText().equals("Play")) {
            super.getMain().getSongPlayer().play();
            this.playPause.setText("Pause");
        } else {
            super.getMain().getSongPlayer().pause();
            this.playPause.setText("Play");
        }
    }
}

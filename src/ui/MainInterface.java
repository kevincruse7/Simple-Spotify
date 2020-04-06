package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class MainInterface {
    @FXML private TextField searchBar;
    @FXML private ListView<?> songResults;
    @FXML private MenuItem addToPlaylist;
    @FXML private ListView<?> albumResults;
    @FXML private ListView<?> artistResults;
    @FXML private Button createPlaylist;
    @FXML private Button uploadAlbum;
    @FXML private ListView<?> playlistResults;
    @FXML private ImageView albumCover;
    @FXML private Text title;
    @FXML private Text info;
    @FXML private ListView<?> songList;
    @FXML private MenuItem removeFromPlaylist;
    @FXML private Button playPause;
    @FXML private Text songTime;
}
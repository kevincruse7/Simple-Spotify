package ui;

import data.Album;
import data.Artist;
import data.Song;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Controller class for upload album interface
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class UploadAlbum extends Controller {
    @FXML private TextField albumTitle;
    @FXML private Button uploadSongs;
    @FXML private Button uploadCoverArt;
    @FXML private Button submit;

    // Reference to the stage this interface belongs to
    private Stage stage;

    private List<String> genres;
    private File cover;
    private List<Song> songs;

    /**
     * Sets the upload album interface stage to the passed value
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    // Handle album upload request
    private void handleUploadSongs(ActionEvent event) {
        // Initialize album metadata lists
        this.genres = new ArrayList<String>();
        this.songs = new ArrayList<Song>();
        
        // Get list of song files from user
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("MP3 Files", "mp3"));
        chooser.setMultiSelectionEnabled(true);

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Process each song selected by user
            File[] songFiles = chooser.getSelectedFiles();
            for (File songFile : songFiles) {
                // Read in song metadata
                AudioFile songData = null;
                try {
                    songData = AudioFileIO.read(songFile);
                } catch (Exception e) {
                    this.getMain().exitWithException(e, "reading in MP3 metadata");
                }

                // Initialize song object and metadata types
                Song song = new Song();
                Tag songTag = songData.getTag();
                AudioHeader songHeader = songData.getAudioHeader();

                // Initialize base metadata
                song.setID(-1);
                song.setTitle(songTag.getFirst(FieldKey.TITLE));
                song.setTrack(Integer.parseInt(songTag.getFirst(FieldKey.TRACK)));
                song.setReleaseYear(Integer.parseInt(songTag.getFirst(FieldKey.YEAR)));
                song.setLength(songHeader.getTrackLength());
                song.setFile(songFile);
                
                // Convert artist string to list of artist objects that is set to song
                List<String> artistNames = Arrays.asList(songTag.getFirst(FieldKey.ARTIST).split(", "));
                List<Artist> artists = new ArrayList<Artist>();
                for (String artistName : artistNames) {
                    Artist artist = new Artist();
                    artist.setName(artistName);
                    artists.add(artist);
                }
                song.setArtists(artists);
                
                // Set song genre and add to list of album genres
                String genre = songTag.getFirst(FieldKey.GENRE);
                if (!this.genres.contains(genre)) {
                    this.genres.add(genre);
                }
                song.setGenre(genre);

                // Add song to list of songs
                songs.add(song);
            }
        }
    }

    @FXML
    // Handle cover art upload request
    private void handleUploadCoverArt(ActionEvent event) {
        // Get cover art from user
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PNG Files", "png"));

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Store selected cover art
            this.cover = chooser.getSelectedFile();
        }
    }
    
    @FXML
    // Handle submit album request
    private void handleSubmit(ActionEvent event) {
        String albumTitle = this.albumTitle.getText();
        
        // Check if user forgot to enter any fields. If not, upload album to the database
        if (albumTitle.equals("")) {
            this.alert(this.stage, AlertType.ERROR, "No title entered!");
        } else if (this.songs == null || this.songs.size() == 0) {
            this.alert(this.stage, AlertType.ERROR, "No songs selected!");
        } else if (this.cover == null) {
            this.alert(this.stage, AlertType.ERROR, "No cover art selected!");
        } else {
            // Initialize the album
            Album album = new Album();
            album.setID(-1);
            album.setTitle(albumTitle);
            album.setGenres(this.genres);
            album.setReleaseYear(this.songs.get(0).getReleaseYear());
            album.setCover(this.cover);
            album.setSongs(this.songs);

            // Push album to the database
            if (this.getMain().getDatabase().uploadAlbum(album) != null) {
                this.alert(AlertType.ERROR, "Album already exists!");
            } else {
                this.alert(AlertType.INFORMATION, "Album uploaded!");

                // Close the window if album upload succeeds
                this.stage.close();
            }

            // Clear all input fields
            this.albumTitle.setText("");
            this.genres = null;
            this.cover = null;
            this.songs = null;
        }
    }
}
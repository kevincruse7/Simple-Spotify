package application;

import ui.ChangePassword;
import ui.Controller;
import ui.CreatePlaylist;
import ui.LoginPage;
import ui.MainInterface;
import ui.UploadAlbum;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entrypoint for JavaFX application that holds references to application and user interface elements
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Main extends Application {
    // Application references
    private Database database;
    private SongPlayer songPlayer;

    // User interface references
    private Stage stage;
    private LoginPage loginPage;
    private MainInterface mainInterface;
    private ChangePassword changePassword;
    private UploadAlbum uploadAlbum;
    private CreatePlaylist createPlaylist;
    
    /**
     * Launches JavaFX application
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    /**
     * Initializes JavaFX application
     */
    public void start(Stage stage) {
        // Initialize application references
        this.database = new Database(this);
        this.songPlayer = new SongPlayer(this);

        // Initialize user interface references
        this.stage = stage;
        this.loginPage = this.loadInterface("/resources/LoginPage.fxml");
        this.mainInterface = this.loadInterface("/resources/MainInterface.fxml");
        this.changePassword = this.loadInterface("/resources/ChangePassword.fxml");
        this.uploadAlbum = this.loadInterface("/resources/UploadAlbum.fxml");
        this.createPlaylist = this.loadInterface("/resources/CreatePlaylist.fxml");
        
        // Start separate thread to keep track of current song time
        this.songPlayer.startSongTimer();

        // Create cache folder for songs and covers retrieved from the database, if it does not already exist
        File cache = new File("cache/");
        if (!cache.exists()) {
            cache.mkdir();
        }

        // Initialize JavaFX stage
        stage.setScene(this.getLoginPage().getScene());
        stage.setTitle("Simple Spotify");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    /**
     * Stops the JavaFX application
     */
    public void stop() {
        // Close connection to database and stop song timer thread
        this.database.close();
        this.songPlayer.stopSongTimer();

        // Drop all references and clean them up with garbage collection to help with cache deletion
        this.database = null;
        this.songPlayer = null;
        this.stage = null;
        this.loginPage = null;
        this.mainInterface = null;
        this.changePassword = null;
        this.uploadAlbum = null;
        this.createPlaylist = null;
        System.gc();
        
        // Delete all files in the cache
        File[] files = new File("cache/").listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    /**
     * Runs the given runnable on the JavaFX application thread
     */
    public void runLater(Runnable toRun) {
        Platform.runLater(toRun);
    }

    /**
     * Exits program with an error code, printing given exception stack trace and action being performed at time of exception
     */
    public void exitWithException(Exception e, String action) {
        System.err.println("Exception when " + action + ":");
        e.printStackTrace();
        System.exit(1);
    }

    /**
     * Returns a reference to the database controller
     */
    public Database getDatabase() {
        return this.database;
    }

    /**
     * Returns a reference to the song player
     */
    public SongPlayer getSongPlayer() {
        return this.songPlayer;
    }

    /**
     * Returns a reference to the JavaFX stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Returns a refernce to the login page controller
     */
    public LoginPage getLoginPage() {
        return this.loginPage;
    }

    /**
     * Returns a reference to the main interface controller
     */
    public MainInterface getMainInterface() {
        return this.mainInterface;
    }

    /**
     * Returns a reference to the change password controller
     */
    public ChangePassword getChangePassword() {
        return this.changePassword;
    }

    /**
     * Returns a reference to the upload album controller
     */
    public UploadAlbum getUploadAlbum() {
        return this.uploadAlbum;
    }

    /**
     * Returns a reference to the create playlist controller
     */
    public CreatePlaylist getCreatePlaylist() {
        return this.createPlaylist;
    }
    
    // Loads in the interface controller and scene at the given FXML file path
    private <T extends Controller> T loadInterface(String path) {
        // Open stream to file path
        InputStream stream = this.getClass().getResourceAsStream(path);

        // Load FXML file and get interface scene
        FXMLLoader loader = new FXMLLoader();
        Scene scene = null;
        try {
            scene = new Scene(loader.load(stream));
        }
        catch (IOException e) {
            this.exitWithException(e, "loading FXML file at " + path);
        }

        // Close stream to file path
        try {
            stream.close();
        }
        catch (IOException e) {
            this.exitWithException(e, "closing FXML file stream at " + path);
        }

        // Initialize interface controller
        T controller = loader.getController();
        controller.setMain(this);
        controller.setScene(scene);

        return controller;
    }
}
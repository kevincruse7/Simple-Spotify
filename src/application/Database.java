package application;

import data.Album;
import data.Artist;
import data.Playlist;
import data.SearchResult;
import data.Song;

/**
 * Database interaction handler for Simple Spotify client.
 */
public class Database {
    private Main main;
    private String user;

    public Database(Main main) {
        this.main = main;
    }
    
    /**
     * Determines if the given user exists in the database.
     */
    public boolean userExists(String username) {
        return false;
    }

    /**
     * Adds the given login credentials to the database.
     */
    public void addUser(String username, String password) {

    }
    
    /**
     * Determines if the given login credentials exist in the database.
     */
    public boolean isValidCredentials(String username, String password) {
        return false;
    }
    
    /**
     * Sets the session user to the one associated with the given login credentials. If the credentials are invalid, an
     * IllegalArgumentException is thrown.
     */
    public void setUser(String username, String password) throws IllegalArgumentException {
        if (this.isValidCredentials(username, password)) {
            this.user = username;
        }
        else {
            throw new IllegalArgumentException("Invalid login credentials");
        }
    }

    /**
     * Gets the current session user.
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Uploads the given album to the database.
     */
    public void uploadAlbum(Album album) {
        
    }

    /**
     * Creates a playlist by the current user with the given title.
     */
    public void createPlaylist(String title) {

    }
    
    /**
     * Adds the given song to the user's current playlist.
     */
    public void addToPlaylist(Playlist playlist, Song song) {

    }

    /**
     * Searches the database with the given query and returns any matches.
     */
    public SearchResult search(String query) {
        return null;
    }

    /**
     * Populates the given song with stream data from the database.
     */
    public void populateWithData(Song song) {

    }
    
    /**
     * Populates the given artist with song data from the database.
     */
    public void populateWithData(Artist artist) {

    }
    
    /**
     * Populates the given album with song data from the database.
     */
    public void populateWithData(Album album) {
        
    }

    /**
     * Populates the given playlist with song data from the database.
     */
    public void populateWithData(Playlist playlist) {

    }
}
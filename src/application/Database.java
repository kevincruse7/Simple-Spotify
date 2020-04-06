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
    private static String user;

    /**
     * Sets the session user to the one associated with the given login credentials. If the credentials are invalid, an
     * IllegalArgumentException is thrown.
     */
    public static void setUser(String username, String password) throws IllegalArgumentException {
        Database.user = username;
    }

    /**
     * Gets the current session user.
     */
    public static String getUser() {
        return Database.user;
    }

    /**
     * Adds the song indicated by the given song title to the user's current playlist.
     */
    public static void addToPlaylist(String songTitle) {

    }

    /**
     * Creates a playlist by the current user with the given title.
     */
    public static void createPlaylist(String title) {

    }

    /**
     * Searches the database with the given query and returns any matches.
     */
    public SearchResult search(String query) {
        return null;
    }

    /**
     * Populates the given album with song data from the database.
     */
    public static void populateWithData(Album album) {
        
    }

    /**
     * Populates the given artist with song data from the database.
     */
    public static void populateWithData(Artist artist) {

    }

    /**
     * Populates the given playlist with song data from the database.
     */
    public static void populateWithData(Playlist playlist) {

    }

    /**
     * Populates the given song with stream data from the database.
     */
    public static void populateWithData(Song song) {

    }

    /**
     * Uploads the given album to the database.
     */
    public static void uploadAlbum(Album album) {
        
    }
}
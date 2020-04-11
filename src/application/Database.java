package application;

import data.Album;
import data.Artist;
import data.Playlist;
import data.SearchResult;
import data.Song;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Database interaction handler for Simple Spotify client.
 */
public class Database {
    private static final String SERVER_NAME = "localhost";
    private static final int PORT_NUMBER = 3306;
    private static final String DB_NAME = "simple_spotify";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    
    private Main main;
    private String user;
    private Connection conn;

    public Database(Main main) {
        this.main = main;
        this.user = null;

        // Specify the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            this.main.exitWithException(e, "specifying the MySQL JDBC driver");
        }

        // Connect to the database
        String url = "jdbc:mysql://" + Database.SERVER_NAME + ":" + Database.PORT_NUMBER + "/" + Database.DB_NAME
                + "?allowPublicKeyRetrieval=true&characterEncoding=UTF-8&useSSL=false";
        try {
            this.conn = DriverManager.getConnection(url, Database.DB_USERNAME, Database.DB_PASSWORD);
        }
        catch (SQLException e) {
            this.main.exitWithException(e, "connecting to the database");
        }
    }

    /**
     * Closes the connection to the database
     */
    public void close() {
        if (this.conn != null) {
            try {
                this.conn.close();
            }
            catch (SQLException e) {
                this.main.exitWithException(e, "closing the database connection");
            }
        }
    }

    /**
     * Logs in using the given credentials. If the user exists in the database and the password matches, the user is set as the
     * sesion user and "MATCH" is returned. If the user exists in the database but the password does not match, "NO MATCH" is
     * returned. If the user does not exist in the database, the user is created and set as the sesion user.
     */
    public String login(String username, String password) {
        String result = "NO MATCH";
        
        try {
            // Prepare call to check_credentials database procedure
            CallableStatement checkCredentials = this.conn.prepareCall("{call check_credentials(?, ?)}");
            checkCredentials.setString(1, username);
            checkCredentials.setString(2, password);
            
            // Call procedure and get result string
            ResultSet resultSet = checkCredentials.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (SQLException e) {
            this.main.exitWithException(e, "checking user credentials with the database");
        }

        // If credentials are valid, set the given user as the session user
        if (result.equals("MATCH") || result.equals("NEW")) {
            this.user = username;
        }

        return result;
    }

    /**
     * Searches the database with the given query and returns any matches.
     */
    public SearchResult search(String query) {
        return null;
    }

    /**
     * Changes the user's password to the given new password. Returns true and changes the password if the given current
     * password matches the actual current password. Otherwise, returns false
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        return false;
    }

    /**
     * Uploads the given album to the database
     */
    public void uploadAlbum(Album album) {

    }
    
    /**
     * Creates a user playlist with the given title
     */
    public void createPlaylist(String title) {

    }

    /**
     * Deletes the given playlist from the database
     */
    public void deletePlaylist(Playlist playlist) {

    }

    /**
     * Adds the given song to the given playlist
     */
    public void addToPlaylist(Song song, Playlist playlist) {

    }

    /**
     * Removes the given song from the given playlist
     */
    public void removeFromPlaylist(Song song, Playlist playlist) {

    }

    /**
     * Populates the given song with stream data from the database
     */
    public void populateWithData(Song song) {

    }

    /**
     * Populates the given artist with song data from the database
     */
    public void populateWithData(Artist artist) {

    }

    /**
     * Populates the given album with song data from the database
     */
    public void populateWithData(Album album) {

    }

    /**
     * Populates the given playlist with song data from the database
     */
    public void populateWithData(Playlist playlist) {

    }

    /**
     * Returns the current session user
     */
    public String getUser() {
        return this.user;
    }
}
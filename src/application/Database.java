package application;

import data.Album;
import data.Artist;
import data.Playlist;
import data.SearchResult;
import data.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Database interaction handler
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Database {
    // Server connection properties
    private static final String SERVER_NAME = "localhost";
    private static final int PORT_NUMBER = 3306;
    private static final String DB_NAME = "simple_spotify";
    private static final String DB_USERNAME = "simplespotify";
    private static final String DB_PASSWORD = "";
    
    // Main reference for access to central application elements
    private Main main;

    // Database connection reference and current session user
    private Connection conn;
    private String user;

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
     * Closes connection to the database
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
     * returned. If the user does not exist in the database, the user is created and set as the sesion user, and "NEW" is
     * returned
     */
    public String login(String username, String password) {
        String result = null;
        
        try {
            // Prepare call to check_credentials database procedure
            CallableStatement checkCredentials = this.conn.prepareCall("{call check_credentials(?, ?)}");
            checkCredentials.setString(1, username);
            checkCredentials.setString(2, password);
            
            // Call procedure and get result string
            ResultSet resultSet = checkCredentials.executeQuery();
            resultSet.next();
            result = resultSet.getString(1);

            // Close result set and procedure call
            resultSet.close();
            checkCredentials.close();
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
     * Searches the database with the given query and returns any matches as a SearchResult
     */
    public SearchResult search(String query) {
        SearchResult result = null;
        
        try {
            // Call search database procedure
            CallableStatement searchDB = this.conn.prepareCall("{call search(?)}");
            searchDB.setString(1, query);
            searchDB.execute();

            // Process song results
            // Uses HashMap to allow multiple tuples to be associated with a single song due to inner joins
            HashMap<Integer, Song> songMap = new HashMap<Integer, Song>();
            ResultSet songsResultSet = searchDB.getResultSet();
            while (songsResultSet.next()) {
                int songID = songsResultSet.getInt("song_id");
                Song song = songMap.get(songID);
                
                if (song == null) {
                    // If song does not exist in map, create new song and initialize with metadata
                    song = new Song();
                    song.setID(songID);
                    song.setTitle(songsResultSet.getString("songs.title"));
                    song.setTrack(songsResultSet.getInt("track"));
                    song.setGenre(songsResultSet.getString("genre_name"));
                    song.setReleaseYear(songsResultSet.getInt("release_year"));
                    song.setLength(songsResultSet.getInt("length"));

                    // Set song artist
                    song.setArtists(new ArrayList<Artist>());
                    Artist artist = new Artist();
                    artist.setName(songsResultSet.getString("artist_name"));
                    song.getArtists().add(artist);
                    
                    // Set song album
                    Album album = new Album();
                    album.setID(songsResultSet.getInt("album_id"));
                    album.setTitle(songsResultSet.getString("albums.title"));
                    song.setAlbum(album);

                    // Put song on map
                    songMap.put(songID, song);
                } else {
                    // Otherwise, song has multiple artists, so add new artist
                    Artist artist = new Artist();
                    artist.setName(songsResultSet.getString("artist_name"));
                    song.getArtists().add(artist);
                }
            }
            songsResultSet.close();
            
            // Final song list
            ArrayList<Song> songs = new ArrayList<Song>(songMap.values());

            // Process artist results
            searchDB.getMoreResults();
            HashMap<String, Artist> artistMap = new HashMap<String, Artist>();
            ResultSet artistsResultSet = searchDB.getResultSet();
            while (artistsResultSet.next()) {
                String artistName = artistsResultSet.getString("artist_name");
                Artist artist = artistMap.get(artistName);

                if (artist == null) {
                    // If artist does not exist in map, create new artist and initialize with metadata
                    artist = new Artist();
                    artist.setName(artistName);

                    // Set artist genre
                    artist.setGenres(new ArrayList<String>());
                    artist.getGenres().add(artistsResultSet.getString("genre_name"));

                    // Put artist on map
                    artistMap.put(artistName, artist);
                } else {
                    // Otherwise, artist has multiple genres, so add new genre
                    artist.getGenres().add(artistsResultSet.getString("genre_name"));
                }
            }
            artistsResultSet.close();
            
            // Final artist list
            ArrayList<Artist> artists = new ArrayList<Artist>(artistMap.values());

            // Process album results
            searchDB.getMoreResults();
            HashMap<Integer, Album> albumMap = new HashMap<Integer, Album>();
            ResultSet albumsResultSet = searchDB.getResultSet();
            while (albumsResultSet.next()) {
                int albumID = albumsResultSet.getInt("album_id");
                Album album = albumMap.get(albumID);

                if (album == null) {
                    // If album does not exist in map, initialize with base metadata
                    album = new Album();
                    album.setID(albumID);
                    album.setTitle(albumsResultSet.getString("title"));
                    album.setReleaseYear(albumsResultSet.getInt("release_year"));

                    // Set album artist
                    album.setArtists(new ArrayList<Artist>());
                    Artist artist = new Artist();
                    artist.setName(albumsResultSet.getString("artist_name"));
                    album.getArtists().add(artist);

                    // Set album genre
                    album.setGenres(new ArrayList<String>());
                    album.getGenres().add(albumsResultSet.getString("genre_name"));

                    // Put album on map
                    albumMap.put(albumID, album);
                } else {
                    // Otherwise, album either has multiple artists or multiple genres, so add new entries
                    String genre = albumsResultSet.getString("genre_name");
                    Artist artist = new Artist();
                    artist.setName(albumsResultSet.getString("artist_name"));

                    // Ensure not to add duplicate genres or artists
                    if (!album.getGenres().contains(genre)) {
                        album.getGenres().add(genre);
                    }
                    if (!album.getArtists().contains(artist)) {
                        album.getArtists().add(artist);
                    }
                }
            }
            albumsResultSet.close();

            // Final album list
            ArrayList<Album> albums = new ArrayList<Album>(albumMap.values());

            // Process playlist results
            // Playlist metadata does not have any joins, so only one tuple per playlist
            searchDB.getMoreResults();
            ArrayList<Playlist> playlists = new ArrayList<Playlist>();
            ResultSet playlistsResultSet = searchDB.getResultSet();
            while (playlistsResultSet.next()) {
                Playlist playlist = new Playlist();
                playlist.setID(playlistsResultSet.getInt("playlist_id"));
                playlist.setTitle(playlistsResultSet.getString("title"));
                playlist.setCreator(playlistsResultSet.getString("creator"));

                playlists.add(playlist);
            }
            playlistsResultSet.close();

            // Close procedure call and aggregate results
            searchDB.close();
            result = new SearchResult(songs, albums, artists, playlists);
        } catch (SQLException e) {
            this.main.exitWithException(e, "searching the database");
        }
        
        return result;
    }

    /**
     * Changes the user's password to the given new password. Returns true and changes the password if the given current
     * password matches the actual current password. Otherwise, returns false
     */
    public boolean changePassword(String currentPassword, String newPassword) {
        boolean result = false;

        try {
            // Prepare call to change_password database proceedure
            CallableStatement chgPswd = this.conn.prepareCall("{call change_password(?, ?, ?)}");
            chgPswd.setString(1, this.user);
            chgPswd.setString(2, currentPassword);
            chgPswd.setString(3, newPassword);

            // Call procedure and get result
            ResultSet resultSet = chgPswd.executeQuery();
            resultSet.next();
            String resultString = resultSet.getString(1);
            result = resultString.equals("MATCH");

            // Close result set and procedure call
            resultSet.close();
            chgPswd.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "changing user password");
        }
        
        return result;
    }

    /**
     * Uploads the given album to the database. Returns "ALBUM ALREADY EXISTS" if the album already exists in the database.
     * Otherwise, returns null
     */
    public String uploadAlbum(Album album) {
        String result = null;

        try {
            // Prepare call to add_album database procedure
            CallableStatement addAlbum = this.conn.prepareCall("{call add_album(?, ?, ?)}");
            addAlbum.setString(1, album.getTitle());
            addAlbum.setInt(2, album.getReleaseYear());
            addAlbum.setBinaryStream(3, new FileInputStream(album.getCover()));

            // Call procedure and get result set
            ResultSet resultSet = addAlbum.executeQuery();
            resultSet.next();
            String addAlbumResult = resultSet.getString(1);
            
            // Close result set and procedure call
            resultSet.close();
            addAlbum.close();

            if (addAlbumResult.equals("ALBUM ALREADY EXISTS")) {
                // If the album already exists, stop upload process and return result
                result = addAlbumResult;
            } else {
                // If the add operation was successful, store the ID of the album
                album.setID(Integer.parseInt(addAlbumResult));

                // Add every genre in the album's genre list to the database
                for (String genre : album.getGenres()) {
                    CallableStatement addGenre = this.conn.prepareCall("{call add_genre(?)}");
                    addGenre.setString(1, genre);
                    addGenre.execute();
                    addGenre.close();

                    CallableStatement connectAlbumGenre = this.conn.prepareCall("{call connect_album_genre(?, ?)}");
                    connectAlbumGenre.setInt(1, album.getID());
                    connectAlbumGenre.setString(2, genre);
                    connectAlbumGenre.execute();
                    connectAlbumGenre.close();
                }

                // Add every song in the album's song list to the database
                for (Song song : album.getSongs()) {
                    CallableStatement addSong = this.conn.prepareCall("{call add_song(?, ?, ?, ?, ?, ?)}");
                    addSong.setString(1, song.getTitle());
                    addSong.setInt(2, song.getTrack());
                    addSong.setInt(3, song.getLength());
                    addSong.setBinaryStream(4, new FileInputStream(song.getFile()));
                    addSong.setInt(5, album.getID());
                    addSong.setString(6, song.getGenre());
                    
                    // Store returned song ID
                    resultSet = addSong.executeQuery();
                    resultSet.next();
                    song.setID(Integer.parseInt(resultSet.getString(1)));

                    addSong.close();
                    
                    // Add every artist in the song's artist list to the database
                    for (Artist artist : song.getArtists()) {
                        CallableStatement addArtist = this.conn.prepareCall("{call add_artist(?)}");
                        addArtist.setString(1, artist.getName());
                        addArtist.execute();
                        addArtist.close();

                        CallableStatement connectArtistSong = this.conn.prepareCall("{call connect_artist_song(?, ?)}");
                        connectArtistSong.setString(1, artist.getName());
                        connectArtistSong.setInt(2, song.getID());
                        connectArtistSong.execute();
                        connectArtistSong.close();

                        CallableStatement connectArtistGenre = this.conn.prepareCall("{call connect_artist_genre(?, ?)}");
                        connectArtistGenre.setString(1, artist.getName());
                        connectArtistGenre.setString(2, song.getGenre());
                        connectArtistGenre.execute();
                        connectArtistGenre.close();
                    }
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            this.main.exitWithException(e, "uploading album to the database");
        }

        return result;
    }
    
    /**
     * Creates a user playlist with the given title and returns "PLAYLIST ADDED" if successful. If playlist already exists,
     * returns "PLAYLIST ALREADY EXISTS"
     */
    public String createPlaylist(String title) {
        String result = null;
        
        try {
            CallableStatement addPlaylist = this.conn.prepareCall("{call add_playlist(?, ?)}");
            addPlaylist.setString(1, title);
            addPlaylist.setString(2, this.user);

            ResultSet addPlaylistResultSet = addPlaylist.executeQuery();
            addPlaylistResultSet.next();
            result = addPlaylistResultSet.getString(1);

            addPlaylistResultSet.close();
            addPlaylist.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "creating new playlist");
        }

        return result;
    }

    /**
     * Deletes the given playlist from the database and returns "PLAYLIST DELETED" if successful. If the playlist doesn't exist
     * in the database, returns "PLAYLIST DOESN'T EXIST"
     */
    public String deletePlaylist(Playlist playlist) {
        String deletePlaylistResult = null;

        try {
            CallableStatement deletePlaylist = this.conn.prepareCall("{call delete_playlist(?)}");
            deletePlaylist.setInt(1, playlist.getID());
            
            ResultSet deletePlaylistResultSet = deletePlaylist.executeQuery();
            deletePlaylistResultSet.next();
            deletePlaylistResult = deletePlaylistResultSet.getString(1);

            deletePlaylistResultSet.close();
            deletePlaylist.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "deleting playlist");
        }

        return deletePlaylistResult;
    }

    /**
     * Adds the given song to the given playlist and returns "CONNECTION CREATED" if successful. If the song already exists in
     * the playlist, returns "CONNECTION ALREADY EXISTS". If either the given song or the given playlist doesn't exist in the
     * database, returns "MISSING DATA"
     */
    public String addToPlaylist(Song song, Playlist playlist) {
        String addToPlaylistResult = null;

        try {
            CallableStatement connectSongPlaylist = this.conn.prepareCall("{call connect_song_playlist(?, ?)}");
            connectSongPlaylist.setInt(1, song.getID());
            connectSongPlaylist.setInt(2, playlist.getID());

            ResultSet connectSongPlaylistResultSet = connectSongPlaylist.executeQuery();
            connectSongPlaylistResultSet.next();
            addToPlaylistResult = connectSongPlaylistResultSet.getString(1);

            connectSongPlaylistResultSet.close();
            connectSongPlaylist.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "adding a song to playlist");
        }

        return addToPlaylistResult;
    }

    /**
     * Removes the given song from the given playlist and returns "CONNECTION DELETED" if successful. If the song doesn't exist
     * in the playlist, returns "CONNECTION DOESN'T EXIST"
     */
    public String removeFromPlaylist(Song song, Playlist playlist) {
        String removeFromPlaylistResult = null;

        try {
            CallableStatement deleteSongPlaylistConnection = this.conn.prepareCall("{call delete_song_playlist_connection(?, ?)}");
            deleteSongPlaylistConnection.setInt(1, song.getID());
            deleteSongPlaylistConnection.setInt(2, playlist.getID());

            ResultSet deleteSongPlaylistConnectionResultSet = deleteSongPlaylistConnection.executeQuery();
            deleteSongPlaylistConnectionResultSet.next();
            removeFromPlaylistResult = deleteSongPlaylistConnectionResultSet.getString(1);

            deleteSongPlaylistConnectionResultSet.close();
            deleteSongPlaylistConnection.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "removing a song from playlist");
        }

        return removeFromPlaylistResult;
    }

    /**
     * Populates the given song with metadata from the database and stores MP3 file to cache
     */
    public void populateWithData(Song song) {
        // If the song already has file in the cache, only request metadata from the database to save processing time
        String query;
        File outputFile = new File("cache/" + Integer.toString(song.getID()) + ".mp3");
        if (outputFile.exists()) {
            query = "SELECT DISTINCT "
                        + "songs.title, "
                        + "songs.track, "
                        + "songs.length, "
                        + "songs.genre_name, "
                        + "artists_songs.artist_name, "
                        + "albums.title, "
                        + "albums.release_year "
                    + "FROM songs "
                    + "JOIN artists_songs ON songs.song_id = artists_songs.song_id "
                    + "JOIN albums ON songs.album_id = albums.album_id "
                    + "WHERE songs.song_id = " + Integer.toString(song.getID());
        } else {
            query = "SELECT DISTINCT "
                        + "songs.title, "
                        + "songs.track, "
                        + "songs.length, "
                        + "songs.genre_name, "
                        + "songs.stream, "
                        + "artists_songs.artist_name, "
                        + "albums.title, "
                        + "albums.release_year "
                    + "FROM songs "
                    + "JOIN artists_songs ON songs.song_id = artists_songs.song_id "
                    + "JOIN albums ON songs.album_id = albums.album_id "
                    + "WHERE songs.song_id = " + Integer.toString(song.getID());
        }
        
        try {
            Statement selectStream = this.conn.createStatement();
            ResultSet selectResultSet = selectStream.executeQuery(query);
            selectResultSet.next();
            
            // Initialize song with metadata
            song.setTitle(selectResultSet.getString("songs.title"));
            song.setTrack(selectResultSet.getInt("songs.track"));;
            song.setGenre(selectResultSet.getString("songs.genre_name"));
            song.setLength(selectResultSet.getInt("songs.length"));
            song.setReleaseYear(selectResultSet.getInt("albums.release_year"));
            
            // Set song album
            Album album = new Album();
            album.setTitle(selectResultSet.getString("albums.title"));
            song.setAlbum(album);

            // Write out file if not already present in the cache
            if (!outputFile.exists()) {
                // Read in stream data to buffer
                InputStream inputStream = selectResultSet.getBinaryStream("songs.stream");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();

                // Write out stream data to MP3 file
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(buffer);
                outputStream.close();
            }

            // Set song artists
            song.setArtists(new ArrayList<Artist>());
            Artist artist = new Artist();
            artist.setName(selectResultSet.getString("artists_songs.artist_name"));
            song.getArtists().add(artist);
            
            while (selectResultSet.next()) {
                artist = new Artist();
                artist.setName(selectResultSet.getString("artists_songs.artist_name"));
                song.getArtists().add(artist);
            }

            // Close database connections
            selectResultSet.close();
            selectStream.close();
        } catch (SQLException | IOException e) {
            this.main.exitWithException(e, "reading in song stream data");
        }

        // Set song file
        song.setFile(outputFile);
    }

    /**
     * Populates the given artist with song data from the database
     */
    public void populateWithData(Artist artist) {
        ArrayList<Song> songs = new ArrayList<Song>();

        try {
            // Select all corresponding song IDs
            Statement selectSongs = this.conn.createStatement();
            ResultSet selectSongsResultSet = selectSongs.executeQuery("SELECT song_id FROM artists_songs WHERE artist_name = \'"
                    + artist.getName() + "\'");
            
            // Covert song IDs to song objects and populate with file and metadata
            while (selectSongsResultSet.next()) {
                Song song = new Song();
                song.setID(selectSongsResultSet.getInt(1));
                this.populateWithData(song);
                songs.add(song);
            }

            selectSongsResultSet.close();
            selectSongs.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "populating artist data");
        }

        artist.setSongs(songs);
    }

    /**
     * Populates the given album with song data from the database
     */
    public void populateWithData(Album album) {
        ArrayList<Song> songs = new ArrayList<Song>();

        // If album already has cover image saved in the cache, only request metadata to save processing time
        String query;
        File outputFile = new File("cache/" + Integer.toString(album.getID()) + ".png");
        if (outputFile.exists()) {
            query = "SELECT songs.song_id FROM albums "
                    + "JOIN songs ON albums.album_id = songs.album_id "
                    + "WHERE albums.album_id = " + album.getID();
        } else {
            query = "SELECT songs.song_id, albums.cover FROM albums "
                    + "JOIN songs ON albums.album_id = songs.album_id "
                    + "WHERE albums.album_id = " + album.getID();
        }
        
        try {
            Statement selectSongs = this.conn.createStatement();
            ResultSet selectSongsResultSet = selectSongs.executeQuery(query);
            selectSongsResultSet.next();
            
            // Write out album cover file if it doesn't already exist in the cache
            if (!outputFile.exists()) {
                // Read in cover stream from database
                InputStream inputStream = selectSongsResultSet.getBinaryStream("albums.cover");
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();

                // Write out stream to PNG file
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(buffer);
                outputStream.close();
            }
                
            // Populate all corresponding songs with files and metadata
            Song song = new Song();
            song.setID(selectSongsResultSet.getInt("songs.song_id"));
            this.populateWithData(song);
            songs.add(song);
            
            while (selectSongsResultSet.next()) {
                song = new Song();
                song.setID(selectSongsResultSet.getInt(1));
                this.populateWithData(song);
                songs.add(song);
            }

            selectSongsResultSet.close();
            selectSongs.close();

            album.setCover(outputFile);
        } catch (SQLException |IOException e) {
            this.main.exitWithException(e, "populating album data");
        }

        Collections.sort(songs);
        album.setSongs(songs);
    }

    /**
     * Populates the given playlist with song data from the database
     */
    public void populateWithData(Playlist playlist) {
        ArrayList<Song> songs = new ArrayList<Song>();

        try {
            // Select all corresponding song IDs
            Statement selectSongs = this.conn.createStatement();
            ResultSet selectSongsResultSet = selectSongs.executeQuery("SELECT song_id FROM songs_playlists WHERE playlist_id = "
                    + playlist.getID());
            
            // Convert song IDs to objects and populate songs with files and metadata
            while (selectSongsResultSet.next()) {
                Song song = new Song();
                song.setID(selectSongsResultSet.getInt(1));
                this.populateWithData(song);
                songs.add(song);
            }

            selectSongsResultSet.close();
            selectSongs.close();
        } catch (SQLException e) {
            this.main.exitWithException(e, "populating playlist data");
        }

        playlist.setSongs(songs);
    }

    /**
     * Returns the current session user
     */
    public String getUser() {
        return this.user;
    }
}
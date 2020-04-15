package data;

import java.util.List;

/**
 * Database search result data class
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class SearchResult {
    private List<Song> songs;
    private List<Album> albums;
    private List<Artist> artists;
    private List<Playlist> playlists;

    public SearchResult(List<Song> songs, List<Album> albums, List<Artist> artists, List<Playlist> playlists) {
        this.songs = songs;
        this.albums = albums;
        this.artists = artists;
        this.playlists = playlists;
    }

    /**
     * Returns the search result song list
     */
    public List<Song> getSongs() {
        return this.songs;
    }

    /**
     * Returns the search result album list
     */
    public List<Album> getAlbums() {
        return this.albums;
    }

    /**
     * Returns the search result artist list
     */
    public List<Artist> getArtists() {
        return this.artists;
    }

    /**
     * Returns the search result playlist list
     */
    public List<Playlist> getPlaylists() {
        return this.playlists;
    }
}
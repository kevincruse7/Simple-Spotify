package data;

import java.util.List;

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

    public List<Song> getSongs() {
        return this.songs;
    }

    public List<Album> getAlbums() {
        return this.albums;
    }

    public List<Artist> getArtists() {
        return this.artists;
    }

    public List<Playlist> getPlaylists() {
        return this.playlists;
    }
}
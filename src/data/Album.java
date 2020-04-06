package data;

import java.io.InputStream;
import java.util.List;

public class Album {
    // Album metadata
    private String title;
    private List<Artist> artists;
    private List<String> genres;
    private int releaseYear;

    // Album data
    private InputStream cover;
    private List<Song> songs;

    public Album(String title, List<Artist> artists, List<String> genres, int releaseYear, InputStream cover, List<Song> songs) {
        this.title = title;
        this.artists = artists;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.setCover(cover);
        this.setSongs(songs);
    }

    public Album(String title, List<Artist> artists, List<String> genres, int releaseYear) {
        this(title, artists, genres, releaseYear, null, null);
    }

    public void setCover(InputStream cover) {
        this.cover = cover;
    }
    
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
    public String getTitle() {
        return this.title;
    }

    public List<Artist> getArtists() {
        return this.artists;
    }

    public List<String> getGenres() {
        return this.genres;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public InputStream getCover() {
        return this.cover;
    }
    
    public List<Song> getSongs() {
        return this.songs;
    }
}
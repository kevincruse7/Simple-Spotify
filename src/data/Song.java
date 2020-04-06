package data;

import java.io.InputStream;
import java.util.List;

public class Song {
    // Song metadata
    private String title;
    private Album album;
    private List<Artist> artists;
    private List<String> genres;
    private int releaseYear;
    private int length;
    
    // Song data
    private InputStream stream;

    public Song(String title, Album album, List<Artist> artists, List<String> genres, int releaseYear, int length, InputStream stream) {
        this.title = title;
        this.album = album;
        this.artists = artists;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.length = length;
        this.setStream(stream);
    }

    public Song(String title, Album album, List<Artist> artists, List<String> genres, int releaseYear, int length) {
        this(title, album, artists, genres, releaseYear, length, null);
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
    
    public String getTitle() {
        return this.title;
    }

    public Album getAlbum() {
        return this.album;
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

    public int getLength() {
        return this.length;
    }

    public InputStream getStream() {
        return this.stream;
    }
}
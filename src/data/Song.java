package data;

import java.io.InputStream;
import java.util.List;

public class Song {
    // Song metadata
    private int id;
    private String title;
    private Album album;
    private List<Artist> artists;
    private List<String> genres;
    private int releaseYear;
    private int length;
    
    // Song data
    private InputStream stream;

    public Song(int id, String title, Album album, List<Artist> artists, List<String> genres, int releaseYear, int length, InputStream stream) {
        this.setID(id);
        this.title = title;
        this.album = album;
        this.artists = artists;
        this.genres = genres;
        this.releaseYear = releaseYear;
        this.length = length;
        this.setStream(stream);
    }

    public Song(String title, Album album, List<Artist> artists, List<String> genres, int releaseYear, int length, InputStream stream) {
        this(-1, title, album, artists, genres, releaseYear, length, stream);
    }

    public Song(int id, String title, Album album, List<Artist> artists, List<String> genres, int releaseYear, int length) {
        this(id, title, album, artists, genres, releaseYear, length, null);
    }

    @Override
    public String toString() {
        return this.getTitle() + " - " + this.getArtists().toString() + " - " + this.getAlbum().getTitle() + " - "
                + Integer.toString(this.getReleaseYear());
    }

    public void setID(int id) {
        this.id = id;
    }
    
    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public int getID() {
        return this.id;
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
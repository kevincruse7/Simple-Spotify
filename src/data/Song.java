package data;

import java.io.File;
import java.util.List;

/**
 * Song data class
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Song implements Comparable<Song> {
    private int id;
    private String title;
    private List<Artist> artists;
    private Album album;
    private int track;
    private String genre;
    private int releaseYear;
    private int length;
    private File file;

    @Override
    /**
     * Compares songs by track number for sorting by album
     */
    public int compareTo(Song song) {
        return this.track - song.track;
    }
    
    @Override
    /**
     * Compares songs by ID for testing equality
     */
    public boolean equals(Object object) {
        return object instanceof Song && this.id == ((Song)object).id;
    }
    
    @Override
    /**
     * Returns a string with the song title, artist list, album title, release year, genre, and length
     */
    public String toString() {
        // Strip square brackets from the artist list string
        String artists = this.artists.toString();
        artists = artists.substring(1, artists.length() - 1);
        
        return this.title + " - "
                + artists + " - "
                + this.album.getTitle() + " - "
                + Integer.toString(this.releaseYear) + " - "
                + this.genre + " - "
                + Integer.toString(this.length / 60) + ":" + String.format("%02d", this.length % 60);
    }

    /**
     * Sets the song ID to the passed value
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Sets the song title to the passed value
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the song artist list to the passed value
     */
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    /**
     * Sets the song album list to the passed value
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * Sets the song track to the passed value
     */
    public void setTrack(int track) {
        this.track = track;
    }

    /**
     * Sets the song genre to the passed value
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Sets the song release year to the passed value
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the song length to the passed value
     */
    public void setLength(int length) {
        this.length = length;
    }
    
    /**
     * Sets the song file to the passed value
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Returns the song ID
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * Returns the song title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the song artist list
     */
    public List<Artist> getArtists() {
        return this.artists;
    }

    /**
     * Returns the song album
     */
    public Album getAlbum() {
        return this.album;
    }

    /**
     * Returns the song track
     */
    public int getTrack() {
        return this.track;
    }

    /**
     * Returns the song genre
     */
    public String getGenre() {
        return this.genre;
    }

    /**
     * Returns the song release year
     */
    public int getReleaseYear() {
        return this.releaseYear;
    }

    /**
     * Returns the song length
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Returns the song file
     */
    public File getFile() {
        return this.file;
    }
}
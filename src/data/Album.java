package data;

import java.io.File;
import java.util.List;

/**
 * Album data class
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Album implements Comparable<Album> {
    private int id;
    private String title;
    private List<Artist> artists;
    private List<String> genres;
    private int releaseYear;
    private File cover;
    private List<Song> songs;

    @Override
    /**
     * Compares albums by title for sorting
     */
    public int compareTo(Album album) {
        return this.title.compareTo(album.title);
    }
    
    @Override
    /**
     * Compares album IDs for testing equality
     */
    public boolean equals(Object object) {
        return object instanceof Album && this.id == ((Album)object).id;
    }
    
    @Override
    /**
     * Returns a string with the album title, artist list, and release year
     */
    public String toString() {
        String artists = this.artists.toString();
        artists = artists.substring(1, artists.length() - 1);
        
        return this.title + " - " + artists + " - " + Integer.toString(this.releaseYear);
    }
    
    /**
     * Sets the album ID to the passed value
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Sets the album title to the passed value
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the album artist list to the passed value
     */
    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    /**
     * Sets the album genre list to the passed value
     */
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    /**
     * Sets the album release year to the passed value
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Sets the album cover file to the passed value
     */
    public void setCover(File cover) {
        this.cover = cover;
    }
    
    /**
     * Sets the album song list to the passed value
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * Returns the album ID
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * Returns the album title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the album artist list
     */
    public List<Artist> getArtists() {
        return this.artists;
    }

    /**
     * Returns the album genre list
     */
    public List<String> getGenres() {
        return this.genres;
    }

    /**
     * Returns the album release year
     */
    public int getReleaseYear() {
        return this.releaseYear;
    }

    /**
     * Returns the album cover file
     */
    public File getCover() {
        return this.cover;
    }
    
    /**
     * Returns the album song list
     */
    public List<Song> getSongs() {
        return this.songs;
    }
}
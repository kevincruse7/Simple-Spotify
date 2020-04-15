package data;

import java.util.List;

/**
 * Artist data class
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Artist implements Comparable<Artist> {
    private String name;
    private List<String> genres;
    private List<Song> songs;

    @Override
    /**
     * Compares artists by name for sorting
     */
    public int compareTo(Artist artist) {
        return this.name.compareTo(artist.name);
    }
    
    @Override
    /**
     * Compares artists by name for testing equality
     */
    public boolean equals(Object object) {
        return object instanceof Artist && this.compareTo((Artist)object) == 0;
    }
    
    @Override
    /**
     * Returns a string with the artist name
     */
    public String toString() {
        return this.name;
    }

    /**
     * Sets the artist name to the passed value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the artist genre list to the passed value
     */
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    /**
     * Sets the artist song list to the passed value
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
    /**
     * Returns the artist name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the artist genre list
     */
    public List<String> getGenres() {
        return this.genres;
    }

    /**
     * Returns the artist song list
     */
    public List<Song> getSongs() {
        return this.songs;
    }
}
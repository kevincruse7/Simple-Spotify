package data;

import java.util.List;

/**
 * Playlist data class
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class Playlist implements Comparable<Playlist> {
    private int id;
    private String title;
    private String creator;
    private List<Song> songs;

    @Override
    /**
     * Compare playlists by creator and title for sorting
     */
    public int compareTo(Playlist playlist) {
        return (this.creator + this.title).compareTo(playlist.creator + playlist.title);
    }
    
    @Override
    /**
     * Compare playlists by ID for testing equality
     */
    public boolean equals(Object object) {
        return object instanceof Playlist && this.id == ((Playlist)object).id;
    }
    
    @Override
    /**
     * Returns a string with the playlist title and creator
     */
    public String toString() {
        return this.getTitle() + " - " + this.getCreator();
    }

    /**
     * Sets the playlist ID to the passed value
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Sets the playlist title to the given value
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the playlist creator to the given value
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    /**
     * Sets the playlist song list to the given value
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * Returns the playlist ID
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * Returns the playlist title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the playlist creator
     */
    public String getCreator() {
        return this.creator;
    }

    /**
     * Returns the playlist song list
     */
    public List<Song> getSongs() {
        return this.songs;
    }
}
package data;

import java.util.List;

public class Playlist {
    // Playlist metadata
    private int id;
    private String title;
    private String creator;

    // Playlist data
    private List<Song> songs;

    public Playlist(int id, String title, String creator, List<Song> songs) {
        this.setID(id);
        this.title = title;
        this.creator = creator;
        this.setSongs(songs);
    }

    public Playlist(String title, String creator, List<Song> songs) {
        this(-1, title, creator, songs);
    }

    public Playlist(int id, String title, String creator) {
        this(id, title, creator, null);
    }

    public void setID(int id) {
        this.id = id;
    }
    
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getID() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }

    public String getCreator() {
        return this.creator;
    }

    public List<Song> getSongs() {
        return this.songs;
    }
}
package data;

import java.util.List;

public class Playlist {
    // Playlist metadata
    private String title;
    private String creator;

    // Playlist data
    private List<Song> songs;

    public Playlist(String title, String creator, List<Song> songs) {
        this.title = title;
        this.creator = creator;
        this.setSongs(songs);
    }

    public Playlist(String title, String creator) {
        this(title, creator, null);
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
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
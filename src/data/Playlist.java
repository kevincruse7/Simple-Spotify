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
        this.songs = songs;
    }

    public Playlist(String title, String creator) {
        this(title, creator, null);
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

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
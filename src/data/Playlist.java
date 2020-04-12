package data;

import java.util.List;

public class Playlist {
    private int id;
    private String title;
    private String creator;
    private List<Song> songs;

    @Override
    public boolean equals(Object o) {
        return o instanceof Playlist && this.id == ((Playlist)o).getID();
    }
    
    @Override
    public String toString() {
        return this.getTitle() + " - " + this.getCreator();
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
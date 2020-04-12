package data;

import java.util.List;

public class Artist {
    private String name;
    private List<String> genres;
    private List<Song> songs;

    @Override
    public boolean equals(Object o) {
        return o instanceof Artist && this.name.equals(((Artist)o).getName());
    }
    
    @Override
    public String toString() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
    public String getName() {
        return this.name;
    }

    public List<String> getGenres() {
        return this.genres;
    }

    public List<Song> getSongs() {
        return this.songs;
    }
}
package data;

import java.util.List;

public class Artist {
    // Artist metadata
    private String name;
    private List<String> genres;

    // Artist data
    private List<Song> songs;

    public Artist(String name, List<String> genres, List<Song> songs) {
        this.name = name;
        this.genres = genres;
        this.setSongs(songs);
    }

    public Artist(String name, List<String> genres) {
        this(name, genres, null);
    }

    @Override
    public String toString() {
        return this.getName();
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
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
        this.songs = songs;
    }

    public Artist(String name, List<String> genres) {
        this(name, genres, null);
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

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
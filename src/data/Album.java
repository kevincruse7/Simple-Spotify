package data;

import java.io.File;

import java.util.List;

public class Album {
    private int id;
    private String title;
    private List<Artist> artists;
    private List<String> genres;
    private int releaseYear;
    private File cover;
    private List<Song> songs;

    @Override
    public boolean equals(Object o) {
        return o instanceof Album && this.id == ((Album)o).getID();
    }
    
    @Override
    public String toString() {
        String artists = this.artists.toString();
        artists = artists.substring(1, artists.length() - 1);
        
        return this.title + " - " + artists + " - " + Integer.toString(this.releaseYear);
    }
    
    public void setID(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setCover(File cover) {
        this.cover = cover;
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

    public List<Artist> getArtists() {
        return this.artists;
    }

    public List<String> getGenres() {
        return this.genres;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public File getCover() {
        return this.cover;
    }
    
    public List<Song> getSongs() {
        return this.songs;
    }
}
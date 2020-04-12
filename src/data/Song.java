package data;

import java.io.File;

import java.util.List;

public class Song implements Comparable<Song> {
    private int id;
    private String title;
    private List<Artist> artists;
    private Album album;
    private int track;
    private String genre;
    private int releaseYear;
    private int length;
    private File file;

    @Override
    public boolean equals(Object o) {
        return o instanceof Song && this.id == ((Song)o).getID();
    }

    @Override
    public int compareTo(Song s) {
        return this.track - s.getTrack();
    }
    
    @Override
    public String toString() {
        String artists = this.artists.toString();
        artists = artists.substring(1, artists.length() - 1);
        
        return this.title + " - "
                + artists + " - "
                + this.album.getTitle() + " - "
                + Integer.toString(this.releaseYear) + " - "
                + this.genre + " - "
                + Integer.toString(this.length / 60) + ":" + String.format("%02d", this.length % 60);
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

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
    public void setFile(File file) {
        this.file = file;
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

    public Album getAlbum() {
        return this.album;
    }

    public int getTrack() {
        return this.track;
    }

    public String getGenre() {
        return this.genre;
    }

    public int getReleaseYear() {
        return this.releaseYear;
    }

    public int getLength() {
        return this.length;
    }

    public File getFile() {
        return this.file;
    }
}
package application;

import data.Song;

import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Media player controller class
 * 
 * @author Kevin Cruse
 * @author Jacob Shell
 * @version 4/12/20
 */
public class SongPlayer {
    // Reference to main class for central application elements
    private Main main;
    
    // List of songs to play. The first song in this list is the currently playing song
    private List<Song> songQueue;

    private boolean songTimerEnabled;
    private MediaPlayer mediaPlayer;

    public SongPlayer(Main main) {
        this.main = main;
        this.songQueue = null;
        this.songTimerEnabled = false;
        this.mediaPlayer = null;
    }

    @Override
    /**
     * Returns a string with the current playing song, the current song time, and the total song duration. If the current time
     * has reached the total time, play the next song
     */
    public String toString() {
        String time = "0:00/0:00";
        
        if (this.mediaPlayer != null && this.songQueue != null && this.songQueue.size() > 0) {
            // If media player is initialized and is currently playing a song, get the song times
            double currentTime = this.mediaPlayer.getCurrentTime().toSeconds();
            double stopTime = this.mediaPlayer.getStopTime().toSeconds();

            time = this.songQueue.get(0).getTitle() + " - "
                    + Integer.toString((int)currentTime / 60) + ":" + String.format("%02d", (int)currentTime % 60) + "/"
                    + Integer.toString((int)stopTime / 60) + ":" + String.format("%02d", (int)stopTime % 60);
            
            // If the song has finished, play the next one
            if (currentTime >= stopTime) {
                this.nextSong();
            }
        }

        return time;
    }

    /**
     * Start a new thread to update the current song time every second
     */
    public void startSongTimer() {
        this.songTimerEnabled = true;
        
        new Thread(() -> {
            while (this.songTimerEnabled) {
                // Interface element updates must happen on JavaFX thread, so queue up task using Main's runLater method
                this.main.runLater(() -> {
                    this.main.getMainInterface().setSongTime(this.toString());
                });

                // Wait for one second
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    this.main.exitWithException(e, "running song timer");
                }
            }
        }).start();
    }

    /**
     * Stop the song timer thread and close the media player, if present
     */
    public void stopSongTimer() {
        this.songTimerEnabled = false;
        
        if (this.mediaPlayer != null) {
            this.mediaPlayer.dispose();
        }
    }

    /**
     * Play the current song, if available
     */
    public void play() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.play();
        }
    }

    /**
     * Pause the current song, if available
     */
    public void pause() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.pause();
        }
    }

    /**
     * Stops the current song queue and initializes a new one.
     * 
     * Note: This current implementation makes the program inherently multitasking-unfriendly. Perhaps a more dynamic
     * implementation can be added in the future
     */
    public void setSongQueue(List<Song> songQueue) {
        this.songQueue = songQueue;

        // Disposes of the current media player, if available
        if (this.mediaPlayer != null) {
            this.mediaPlayer.dispose();
        }
        
        if (this.songQueue != null && this.songQueue.size() > 0) {
            // If the passed song queue has songs in it, start playing them
            this.initMediaPlayer();
        } else {
            // Otherwise, disable the play button
            this.main.getMainInterface().disablePlayPause();
        }
    }

    // Initializes a new media player on the current song queue
    private void initMediaPlayer() {
        Media media = new Media(this.songQueue.get(0).getFile().getAbsoluteFile().toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);

        // Media player is initialized, enable the play button
        this.main.getMainInterface().enablePlayPause();
    }

    // Play the next song in the queue, if there is one
    private void nextSong() {
        // Disposes of the current media player
        this.mediaPlayer.dispose();
        
        if (this.songQueue.size() > 0) {
            // If the song queue is not already empty, remove the current song
            this.songQueue.remove(0);
            
            if (this.songQueue.size() > 0) {
                // If there is a next song, play it
                this.initMediaPlayer();
                this.play();
            } else {
                // Otherwise, disable the play button
                this.main.getMainInterface().disablePlayPause();
            }
        }
    }
}
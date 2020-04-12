package application;

import data.Song;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SongPlayer {
    private Main main;
    private List<Song> songQueue;
    private boolean songTimerEnabled;
    private MediaPlayer mediaPlayer;

    public SongPlayer(Main main) {
        this.main = main;
        this.songQueue = new ArrayList<Song>();
    }

    @Override
    public String toString() {
        if (this.mediaPlayer != null && this.songQueue.size() > 0) {
            double currentTime = this.mediaPlayer.getCurrentTime().toSeconds();
            double stopTime = this.mediaPlayer.getStopTime().toSeconds();

            if (currentTime >= stopTime) {
                this.nextSong();
            }
            
            if (this.songQueue.size() > 0) {
                return this.songQueue.get(0).getTitle() + " - "
                        + Integer.toString((int)currentTime / 60) + ":" + String.format("%02d", (int)currentTime % 60) + "/"
                        + Integer.toString((int)stopTime / 60) + ":" + String.format("%02d", (int)stopTime % 60);
            }
        }

        return "0:00/0:00";
    }

    public void startSongTimer() {
        this.songTimerEnabled = true;
        
        new Thread(() -> {
            while (this.songTimerEnabled) {
                this.main.runLater(() -> {
                    this.main.getMainInterface().setSongTime(this.toString());
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    this.main.exitWithException(e, "running song timer");
                }
            }
        }).start();
    }

    public void stopSongTimer() {
        this.songTimerEnabled = false;
        
        if (this.mediaPlayer != null) {
            this.mediaPlayer.dispose();
        }
    }

    public void play() {
        this.mediaPlayer.play();
    }

    public void pause() {
        this.mediaPlayer.pause();
    }

    public void setSongQueue(List<Song> songQueue) {
        this.songQueue = songQueue;

        if (this.mediaPlayer != null) {
            this.mediaPlayer.dispose();
        }
        
        if (this.songQueue.size() > 0) {
            this.initMediaPlayer();
        } else {
            this.main.getMainInterface().disablePlayPause();
        }
    }

    private void nextSong() {
        this.mediaPlayer.dispose();
        
        if (this.songQueue.size() > 0) {
            this.songQueue.remove(0);
            
            if (this.songQueue.size() > 0) {
                this.initMediaPlayer();
                this.play();
            } else {
                this.main.getMainInterface().disablePlayPause();
            }
        }
    }

    private void initMediaPlayer() {
        Media media = new Media(this.songQueue.get(0).getFile().getAbsoluteFile().toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
        this.main.getMainInterface().enablePlayPause();
    }
}
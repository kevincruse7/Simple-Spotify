DROP DATABASE IF EXISTS simple_spotify;
CREATE DATABASE simple_spotify;
USE simple_spotify;

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
	username VARCHAR(32) PRIMARY KEY,
    pass VARCHAR(32) NOT NULL
);

DROP TABLE IF EXISTS artists;
CREATE TABLE artists
(
	artist_id INT PRIMARY KEY AUTO_INCREMENT,
    artist_name VARCHAR(64) NOT NULL
);

DROP TABLE IF EXISTS songs;
CREATE TABLE songs
(
	song_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    length INT NOT NULL,
    mp3 LONGBLOB NOT NULL
);

DROP TABLE IF EXISTS albums;
CREATE TABLE albums
(
	album_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    release_year DATE NOT NULL,
    cover MEDIUMBLOB NOT NULL
);

DROP TABLE IF EXISTS playlists;
CREATE TABLE playlists
(
	playlist_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    creator VARCHAR(32) NOT NULL,
    
    CONSTRAINT playlist_creator
		FOREIGN KEY (creator)
        REFERENCES users (username)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
	genre_name VARCHAR(64) PRIMARY KEY
);

DROP TABLE IF EXISTS artists_songs;
CREATE TABLE artists_songs
(
	artist_id INT NOT NULL,
    song_id INT NOT NULL,
    
    CONSTRAINT ars_artist_id
		FOREIGN KEY (artist_id)
        REFERENCES artists (artist_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
	CONSTRAINT ars_song_id
		FOREIGN KEY (song_id)
        REFERENCES songs (song_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS songs_albums;
CREATE TABLE songs_albums
(
	song_id INT NOT NULL,
    album_id INT NOT NULL,
    
    CONSTRAINT sal_song_id
		FOREIGN KEY (song_id)
        REFERENCES songs (song_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT sal_album_id
		FOREIGN KEY (album_id)
        REFERENCES albums (album_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS songs_playlists;
CREATE TABLE songs_playlists
(
	song_id INT NOT NULL,
    playlist_id INT NOT NULL,
    
    CONSTRAINT sp_song_id
		FOREIGN KEY (song_id)
        REFERENCES songs (song_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT sp_playlist_id
		FOREIGN KEY (playlist_id)
        REFERENCES playlists (playlist_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS artists_genres;
CREATE TABLE artists_genres
(
	artist_id INT NOT NULL,
    genre_name VARCHAR(64) NOT NULL,
    
    CONSTRAINT arg_artist_id
		FOREIGN KEY (artist_id)
        REFERENCES artists (artist_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT arg_genre_name
		FOREIGN KEY (genre_name)
        REFERENCES genres (genre_name)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS songs_genres;
CREATE TABLE songs_genres
(
	song_id INT NOT NULL,
    genre_name VARCHAR(64) NOT NULL,
    
    CONSTRAINT sg_song_id
		FOREIGN KEY (song_id)
        REFERENCES songs (song_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT sg_genre_name
		FOREIGN KEY (genre_name)
        REFERENCES genres (genre_name)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS albums_genres;
CREATE TABLE albums_genres
(
	album_id INT NOT NULL,
    genre_name VARCHAR(64) NOT NULL,
    
    CONSTRAINT alg_album_id
		FOREIGN KEY (album_id)
        REFERENCES albums (album_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT alg_genre_name
		FOREIGN KEY (genre_name)
        REFERENCES genres (genre_name)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);
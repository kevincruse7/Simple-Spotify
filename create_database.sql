DROP DATABASE IF EXISTS simple_spotify;
CREATE DATABASE simple_spotify;
USE simple_spotify;

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    username VARCHAR(32) PRIMARY KEY,
    pass VARCHAR(32) NOT NULL
);

DROP TABLE IF EXISTS songs;
CREATE TABLE songs
(
    song_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    length INT NOT NULL,
    stream LONGBLOB NOT NULL
);

DROP TABLE IF EXISTS artists;
CREATE TABLE artists
(
    artist_name VARCHAR(64) PRIMARY KEY
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

DROP TABLE IF EXISTS songs_artists;
CREATE TABLE songs_artists
(
    song_id INT NOT NULL,
    artist_name VARCHAR(64) NOT NULL,
    
    CONSTRAINT sar_song_id
        FOREIGN KEY (song_id)
        REFERENCES songs (song_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT sar_artist_name
        FOREIGN KEY (artist_name)
        REFERENCES artists (artist_name)
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

DROP TABLE IF EXISTS artists_genres;
CREATE TABLE artists_genres
(
    artist_name VARCHAR(64) NOT NULL,
    genre_name VARCHAR(64) NOT NULL,
    
    CONSTRAINT arg_artist_name
        FOREIGN KEY (artist_name)
        REFERENCES artists (artist_name)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT arg_genre_name
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

-- If the username exists, returns "MATCH" if the passwords match and "NO MATCH" if they don't. If the username doesn't exist,
-- adds the credentials to the users table and returns "NEW"
DROP PROCEDURE IF EXISTS check_credentials;
DELIMITER //
CREATE PROCEDURE check_credentials
(
    entered_username VARCHAR(32),
    entered_pass VARCHAR(32)
)
BEGIN
    IF entered_username IN (SELECT username FROM users WHERE BINARY pass = entered_pass) THEN
        SELECT 'MATCH';
    ELSEIF entered_username NOT IN (SELECT username FROM users) THEN
        INSERT INTO users VALUES (entered_username, entered_pass);
        SELECT 'NEW';
    ELSE
        SELECT 'NO MATCH';
    END IF;
END//
DELIMITER ;

-- Selects all matching songs, artists, albums, and playlists to the given search query
DROP PROCEDURE IF EXISTS search;
DELIMITER //
CREATE PROCEDURE search
(
    search_query VARCHAR(64)
)
BEGIN
    -- Select matching songs
    SELECT * FROM songs
    JOIN songs_artists ON songs.song_id = songs_artists.song_id
    JOIN song_albums ON songs.song_id = songs_albums.song_id
    JOIN song_genres ON songs.song_id = songs_genres.song_id
    WHERE songs.title = search_query;
    
    -- Select matching artists
    SELECT * FROM artists
    JOIN artists_genres ON artists.artist_name = artists_genres.artist_name
    WHERE artists.artist_name = search_query;
    
    -- Select matching albums
    SELECT * FROM albums
    JOIN (
        SELECT albums.album_id, artists.artist_name, artists_genres.genre_name FROM albums
        JOIN songs_albums ON albums.album_id = songs_albums.album_id
        JOIN songs_artists ON songs_albums.song_id = songs_artists.song_id
        JOIN artists_genres ON songs_artists.artist_name = artists_genres.artist_name
    ) AS artists_albums ON albums.album_id = artists_albums.album_id
    JOIN albums_genres ON albums.album_id = albums_genres.album_id
    WHERE albums.title = search_query;
    
    -- Select matching playlists
    SELECT * FROM playlists
    WHERE playlists.title = search_query;
END//
DELIMITER ;
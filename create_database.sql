-- SQL script for creating Simple Spotify database

-- Kevin Cruse & Jacob Shell
-- 4/12/20

-- Uncomment and run this command if you get an error regarding maximum packet size
-- SET GLOBAL max_allowed_packet=1024*1024*1024;


DROP DATABASE IF EXISTS simple_spotify;
CREATE DATABASE simple_spotify;
USE simple_spotify;


DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    username VARCHAR(32) PRIMARY KEY,
    passwrd VARCHAR(32) NOT NULL
);


DROP TABLE IF EXISTS albums;
CREATE TABLE albums
(
    album_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    release_year YEAR NOT NULL,
    cover MEDIUMBLOB NOT NULL
);


DROP TABLE IF EXISTS artists;
CREATE TABLE artists
(
    artist_name VARCHAR(64) PRIMARY KEY
);


DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    genre_name VARCHAR(64) PRIMARY KEY
);


DROP TABLE IF EXISTS songs;
CREATE TABLE songs
(
    song_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(64) NOT NULL,
    track INT NOT NULL,
    length INT NOT NULL,
    stream LONGBLOB NOT NULL,
    album_id INT NOT NULL,
    genre_name VARCHAR(64) NOT NULL,
    
    CONSTRAINT song_album_id
        FOREIGN KEY (album_id)
        REFERENCES albums (album_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT song_genre_name
        FOREIGN KEY (genre_name)
        REFERENCES genres (genre_name)
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
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


DROP TABLE IF EXISTS artists_songs;
CREATE TABLE artists_songs
(
    artist_name VARCHAR(64) NOT NULL,
    song_id INT NOT NULL,
    
    CONSTRAINT as_artist_name
        FOREIGN KEY (artist_name)
        REFERENCES artists (artist_name)
        ON UPDATE RESTRICT
        ON DELETE CASCADE,
    CONSTRAINT as_song_id
        FOREIGN KEY (song_id)
        REFERENCES songs (song_id)
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


-- If the username exists, returns "MATCH" if the passwords match and "NO MATCH" if they don't. If the username doesn't exist,
-- adds the credentials to the users table and returns "NEW"
DROP PROCEDURE IF EXISTS check_credentials;
DELIMITER //
CREATE PROCEDURE check_credentials
(
    username VARCHAR(32),
    passwrd VARCHAR(32)
)
BEGIN
    IF username IN (SELECT users.username FROM users WHERE BINARY users.passwrd = passwrd) THEN
        SELECT 'MATCH';
    ELSEIF username NOT IN (SELECT users.username FROM users) THEN
        INSERT INTO users VALUES (username, passwrd);
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
    SELECT DISTINCT
        songs.song_id,
        songs.title,
        songs.track,
        songs.length,
        songs.album_id,
        albums.title,
        songs.genre_name,
        artists_songs.artist_name,
        albums.release_year
    FROM songs
    JOIN artists_songs ON songs.song_id = artists_songs.song_id
    JOIN albums ON songs.album_id = albums.album_id
    LEFT OUTER JOIN songs_playlists ON songs.song_id = songs_playlists.song_id
    LEFT OUTER JOIN playlists ON songs_playlists.playlist_id = playlists.playlist_id
    WHERE 
        INSTR(songs.title, search_query) > 0
        OR INSTR(artists_songs.artist_name, search_query) > 0
        OR INSTR(albums.title, search_query) > 0
        OR INSTR(playlists.title, search_query) > 0
        OR INSTR(playlists.creator, search_query) > 0;
    
    -- Select matching artists
    SELECT DISTINCT
        artists.artist_name,
        artists_genres.genre_name
    FROM artists
    JOIN artists_genres ON artists.artist_name = artists_genres.artist_name
    JOIN artists_songs ON artists.artist_name = artists_songs.artist_name
    JOIN songs ON artists_songs.song_id = songs.song_id
    JOIN albums ON songs.album_id = albums.album_id
    LEFT OUTER JOIN songs_playlists ON songs.song_id = songs_playlists.song_id
    LEFT OUTER JOIN playlists ON songs_playlists.playlist_id = playlists.playlist_id
    WHERE
        INSTR(artists.artist_name, search_query) > 0
        OR INSTR(songs.title, search_query) > 0
        OR INSTR(albums.title, search_query) > 0
        OR INSTR(playlists.title, search_query) > 0
        OR INSTR(playlists.creator, search_query) > 0;
    
    -- Select matching albums
    SELECT DISTINCT
        albums.album_id,
        albums.title,
        albums.release_year,
        albums_artists.artist_name,
        albums_genres.genre_name
    FROM albums
    JOIN (
        SELECT albums.album_id, artists_songs.artist_name FROM albums
        JOIN songs ON albums.album_id = songs.album_id
        JOIN artists_songs ON songs.song_id = artists_songs.song_id
    ) AS albums_artists ON albums.album_id = albums_artists.album_id
    JOIN albums_genres ON albums.album_id = albums_genres.album_id
    JOIN songs ON albums.album_id = songs.album_id
    LEFT OUTER JOIN songs_playlists ON songs.song_id = songs_playlists.song_id
    LEFT OUTER JOIN playlists ON songs_playlists.playlist_id = playlists.playlist_id
    WHERE
        INSTR(albums.title, search_query) > 0
        OR INSTR(songs.title, search_query) > 0
        OR INSTR(albums_artists.artist_name, search_query) > 0
        OR INSTR(playlists.title, search_query) > 0
        OR INSTR(playlists.creator, search_query) > 0;
    
    -- Select matching playlists
    SELECT DISTINCT
        playlists.playlist_id,
        playlists.title,
        playlists.creator
    FROM playlists
    LEFT OUTER JOIN songs_playlists ON playlists.playlist_id = songs_playlists.playlist_id
    LEFT OUTER JOIN songs ON songs_playlists.song_id = songs.song_id
    LEFT OUTER JOIN artists_songs ON songs.song_id = artists_songs.song_id
    LEFT OUTER JOIN albums ON songs.album_id = albums.album_id
    WHERE
        INSTR(playlists.title, search_query) > 0
        OR INSTR(playlists.creator, search_query) > 0
        OR INSTR(songs.title, search_query) > 0
        OR INSTR(artists_songs.artist_name, search_query) > 0
        OR INSTR(albums.title, search_query) > 0;
END//
DELIMITER ;


-- Changes given user's password if the given current password matches the actual current password. Selects 'MATCH' if the
-- current passwords match and the operation is successful. Selects 'NO MATCH' if the current passwords don't match and
-- 'NO USER' if the given username does not exist
DROP PROCEDURE IF EXISTS change_password;
DELIMITER //
CREATE PROCEDURE change_password
(
    username VARCHAR(32),
    current_password VARCHAR(32),
    new_password VARCHAR(32)
)
BEGIN
    IF username IN (SELECT users.username FROM users) THEN
        IF current_password = (SELECT BINARY passwrd FROM users WHERE users.username = username) THEN
            UPDATE users SET passwrd = new_password WHERE users.username = username;
            SELECT 'MATCH';
        ELSE
            SELECT 'NO MATCH';
        END IF;
    ELSE
        SELECT 'NO USER';
    END IF;
END//
DELIMITER ;


-- Adds the given album to the database and selects the internal ID for this album. If the album already exists, selects
-- 'ALBUM ALREADY EXISTS'
DROP PROCEDURE IF EXISTS add_album;
DELIMITER //
CREATE PROCEDURE add_album
(
    title VARCHAR(64),
    release_year YEAR,
    cover MEDIUMBLOB
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM albums
        WHERE albums.title = title
        AND albums.release_year = release_year
        AND albums.cover = cover
    ) THEN
        INSERT INTO albums (title, release_year, cover) VALUES (title, release_year, cover);
        SELECT album_id FROM albums
        WHERE albums.title = title
        AND albums.release_year = release_year
        AND albums.cover = cover;
    ELSE
        SELECT 'ALBUM ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Adds the given genre to the database and selects 'GENRE ADDED' if successful. If the genre already exists, selects
-- 'GENRE ALREADY EXISTS'
DROP PROCEDURE IF EXISTS add_genre;
DELIMITER //
CREATE PROCEDURE add_genre
(
    genre_name VARCHAR(64)
)
BEGIN
    IF NOT EXISTS (SELECT * FROM genres WHERE genres.genre_name = genre_name) THEN
        INSERT INTO genres VALUES (genre_name);
        SELECT 'GENRE ADDED';
    ELSE
        SELECT 'GENRE ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Connects the given album to the given genre and selects 'CONNECTION CREATED' if successful. If the connection already exists,
-- selects 'CONNECTION ALREADY EXISTS'. If either the album or genre doesn't exist, selects 'MISSING DATA'
DROP PROCEDURE IF EXISTS connect_album_genre;
DELIMITER //
CREATE PROCEDURE connect_album_genre
(
    album_id INT,
    genre_name VARCHAR(64)
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM albums_genres
        WHERE albums_genres.album_id = album_id
        AND albums_genres.genre_name = genre_name
    ) THEN
        IF
            EXISTS (SELECT * FROM albums WHERE albums.album_id = album_id)
            AND EXISTS (SELECT * FROM genres WHERE genres.genre_name = genre_name)
        THEN
            INSERT INTO albums_genres VALUES (album_id, genre_name);
            SELECT 'CONNECTION CREATED';
        ELSE
            SELECT 'MISSING DATA';
        END IF;
    ELSE
        SELECT 'CONNECTION ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Adds the given song to the database and selects the internal ID of the song if successful. If the song already exists,
-- selects 'SONG ALREADY EXISTS'. If either the referenced album or genre does not exist, selects 'MISSING DATA'
DROP PROCEDURE IF EXISTS add_song;
DELIMITER //
CREATE PROCEDURE add_song
(
    title VARCHAR(64),
    track INT,
    length INT,
    stream LONGBLOB,
    album_id INT,
    genre_name VARCHAR(64)
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM songs
        WHERE songs.title = title
        AND songs.track = track
        AND songs.length = length
        AND songs.stream = stream
        AND songs.album_id = album_id
        AND songs.genre_name = genre_name
    ) THEN
        IF
            EXISTS (SELECT * FROM albums WHERE albums.album_id = album_id)
            AND EXISTS (SELECT * FROM genres WHERE genres.genre_name = genre_name)
        THEN
            INSERT INTO songs (title, track, length, stream, album_id, genre_name)
            VALUES (title, track, length, stream, album_id, genre_name);
            
            SELECT song_id FROM songs
            WHERE songs.title = title
            AND songs.track = track
            AND songs.length = length
            AND songs.stream = stream
            AND songs.album_id = album_id
            AND songs.genre_name = genre_name;
        ELSE
            SELECT 'MISSING DATA';
        END IF;
    ELSE
        SELECT 'SONG ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Adds the given artist to the database and selects 'CONNECTION CREATED' if successful. If the artist already exists, selects
-- 'ARTIST ALREADY EXISTS'
DROP PROCEDURE IF EXISTS add_artist;
DELIMITER //
CREATE PROCEDURE add_artist
(
    artist_name VARCHAR(64)
)
BEGIN
    IF NOT EXISTS (SELECT * FROM artists WHERE artists.artist_name = artist_name) THEN
        INSERT INTO artists VALUES (artist_name);
        SELECT 'CONNECTION CREATED';
    ELSE
        SELECT 'ARTIST ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Connects the given artist to the given song and selects 'CONNECTION CREATED' if successful. If the connection already exists,
-- selects 'CONNECTION ALREADY EXISTS'. If either the artist or song doesn't exist, selects 'MISSING DATA'
DROP PROCEDURE IF EXISTS connect_artist_song;
DELIMITER //
CREATE PROCEDURE connect_artist_song
(
    artist_name VARCHAR(64),
    song_id INT
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM artists_songs
        WHERE artists_songs.artist_name = artist_name
        AND artists_songs.song_id = song_id
    ) THEN
        IF
            EXISTS (SELECT * FROM artists WHERE artists.artist_name = artist_name)
            AND EXISTS (SELECT * FROM songs WHERE songs.song_id = song_id)
        THEN
            INSERT INTO artists_songs VALUES (artist_name, song_id);
            SELECT 'CONNECTION CREATED';
        ELSE
            SELECT 'MISSING DATA';
        END IF;
    ELSE
        SELECT 'CONNECTION ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Connects the given artist to the given genre and selects 'CONNECTION CREATED' if successful. If the connection already
-- exists, selects 'CONNECTION ALREADY EXISTS'. If either the artist or genre doesn't exist, selects 'MISSING DATA'
DROP PROCEDURE IF EXISTS connect_artist_genre;
DELIMITER //
CREATE PROCEDURE connect_artist_genre
(
    artist_name VARCHAR(64),
    genre_name VARCHAR(64)
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM artists_genres
        WHERE artists_genres.artist_name = artist_name
        AND artists_genres.genre_name = genre_name
    ) THEN
        IF
            EXISTS (SELECT * FROM artists WHERE artists.artist_name = artist_name)
            AND EXISTS (SELECT * FROM genres WHERE genres.genre_name = genre_name)
        THEN
            INSERT INTO artists_genres VALUES (artist_name, genre_name);
            SELECT 'CONNECTION CREATED';
        ELSE
            SELECT 'MISSING DATA';
        END IF;
    ELSE
        SELECT 'CONNECTION ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Adds the given playlist to the database and selects 'PLAYLIST ADDED' if successful. If the playlist already exists, selects
-- 'PLAYLIST ALREADY EXISTS'
DROP PROCEDURE IF EXISTS add_playlist;
DELIMITER //
CREATE PROCEDURE add_playlist
(
    title VARCHAR(64),
    creator VARCHAR(32)
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM playlists
        WHERE playlists.title = title
        AND playlists.creator = creator
    ) THEN
        INSERT INTO playlists (title, creator) VALUES (title, creator);
        SELECT 'PLAYLIST ADDED';
    ELSE
        SELECT 'PLAYLIST ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Deletes the given playlist and selects 'PLAYLIST DELETED' if successful. If the playlist doesn't exist, selects
-- 'PLAYLIST DOESN'T EXIST'.
DROP PROCEDURE IF EXISTS delete_playlist;
DELIMITER //
CREATE PROCEDURE delete_playlist
(
    playlist_id INT
)
BEGIN
    IF EXISTS (SELECT * FROM playlists WHERE playlists.playlist_id = playlist_id) THEN
        DELETE FROM playlists WHERE playlists.playlist_id = playlist_id;
        DELETE FROM songs_playlists WHERE songs_playlists.playlist_id = playlist_id;
        SELECT 'PLAYLIST DELETED';
    ELSE
        SELECT 'PLAYLIST DOESN\'T EXIST';
    END IF;
END//
DELIMITER ;


-- Connects the given song to the given playlist and selects 'CONNECTION CREATED' if successful. If the connection already
-- exists, selects 'CONNECTION ALREADY EXISTS'. If either the song or playlist doesn't exist, selects 'MISSING DATA'
DROP PROCEDURE IF EXISTS connect_song_playlist;
DELIMITER //
CREATE PROCEDURE connect_song_playlist
(
    song_id INT,
    playlist_id INT
)
BEGIN
    IF NOT EXISTS (
        SELECT * FROM songs_playlists
        WHERE songs_playlists.song_id = song_id
        AND songs_playlists.playlist_id = playlist_id
    ) THEN
        IF 
            EXISTS (SELECT * FROM songs WHERE songs.song_id = song_id)
            AND EXISTS (SELECT * FROM playlists WHERE playlists.playlist_id = playlist_id)
        THEN
            INSERT INTO songs_playlists VALUES (song_id, playlist_id);
            SELECT 'CONNECTION CREATED';
        ELSE
            SELECT 'MISSING DATA';
        END IF;
    ELSE
        SELECT 'CONNECTION ALREADY EXISTS';
    END IF;
END//
DELIMITER ;


-- Deletes the connection between the given song and playlist and selects 'CONNECTION DELETED' if successful. If the connection
-- doesn't exist, selects 'CONNECTION DOESN'T EXIST'
DROP PROCEDURE IF EXISTS delete_song_playlist_connection;
DELIMITER //
CREATE PROCEDURE delete_song_playlist_connection
(
    song_id INT,
    playlist_id INT
)
BEGIN
    IF EXISTS (
        SELECT * FROM songs_playlists
        WHERE songs_playlists.song_id = song_id
        AND songs_playlists.playlist_id = playlist_id
    ) THEN
        DELETE FROM songs_playlists
        WHERE songs_playlists.song_id = song_id
        AND songs_playlists.playlist_id = playlist_id;
        
        SELECT 'CONNECTION DELETED';
    ELSE
        SELECT 'CONNECTION DOESN\'T EXIST';
    END IF;
END//
DELIMITER ;
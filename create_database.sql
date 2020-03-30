drop database if exists simple_spotify;
create database simple_spotify;
use simple_spotify;

drop table if exists users;
create table users
(
	username varchar(32) primary key,
    pass varchar(32) not null
);

drop table if exists playlists;
create table playlists
(
	playlist_id int primary key auto_increment,
    title varchar(64) not null,
    creator varchar(32) not null,
    
    constraint playlist_creator
		foreign key (creator)
        references users (username)
        on update restrict
        on delete cascade
);

drop table if exists genres;
create table genres
(
	genre_name varchar(64) primary key
);

drop table if exists artists;
create table artists
(
	artist_id int primary key auto_increment,
    artist_name varchar(64) not null,
    username varchar(32),
    
    constraint ar_username
		foreign key (username)
        references users (username)
        on update restrict
        on delete cascade
);

drop table if exists albums;
create table albums
(
	album_id int primary key auto_increment,
    title varchar(64) not null,
    release_year date not null,
    cover mediumblob not null
);

drop table if exists songs;
create table songs
(
	song_id int primary key auto_increment,
    title varchar(64) not null,
    length int not null,
    mp3 longblob not null
);

drop table if exists playlists_songs;
create table playlists_songs
(
	playlist_id int not null,
    song_id int not null,
    
    constraint ps_playlist_id
		foreign key (playlist_id)
        references playlists (playlist_id)
        on update restrict
        on delete cascade,
	constraint ps_song_id
		foreign key (song_id)
        references songs (song_id)
        on update restrict
        on delete cascade
);

drop table if exists albums_songs;
create table albums_songs
(
	album_id int not null,
    song_id int not null,
    
    constraint als_album_id
		foreign key (album_id)
        references albums (album_id)
        on update restrict
        on delete cascade,
	constraint als_song_id
		foreign key (song_id)
        references songs (song_id)
        on update restrict
        on delete cascade
);

drop table if exists artists_songs;
create table artists_songs
(
	artist_id int not null,
    song_id int not null,
    
    constraint ars_artist_id
		foreign key (artist_id)
        references artists (artist_id)
        on update restrict
        on delete cascade,
	constraint ars_song_id
		foreign key (song_id)
        references songs (song_id)
        on update restrict
        on delete cascade
);

drop table if exists genres_songs;
create table genres_songs
(
	genre_name varchar(64) not null,
    song_id int not null,
    
    constraint gs_genre_name
		foreign key (genre_name)
        references genres (genre_name)
        on update restrict
        on delete cascade,
	constraint gs_song_id
		foreign key (song_id)
        references songs (song_id)
        on update restrict
        on delete cascade
);

drop table if exists genres_albums;
create table genres_albums
(
	genre_name varchar(64) not null,
    album_id int not null,
    
    constraint gal_genre_name
		foreign key (genre_name)
        references genres (genre_name)
        on update restrict
        on delete cascade,
	constraint gal_album_id
		foreign key (album_id)
        references albums (album_id)
        on update restrict
        on delete cascade
);

drop table if exists genres_artists;
create table genres_artists
(
	genre_name varchar(64) not null,
    artist_id int not null,
    
    constraint gar_genre_name
		foreign key (genre_name)
        references genres (genre_name)
        on update restrict
        on delete cascade,
	constraint gar_artist_id
		foreign key (artist_id)
        references artists (artist_id)
        on update restrict
        on delete cascade
);
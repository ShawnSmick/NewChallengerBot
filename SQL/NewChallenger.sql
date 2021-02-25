BEGIN TRANSACTION;
DROP TABLE If EXISTS player_match;
CREATE TABLE player_match
(
        player_id int NOT NULL
        ,match_id int NOT NULL
        ,team int NOT NULL
);
ALTER TABLE player_match
ADD CONSTRAINT pk_match_player_id PRIMARY KEY (player_id,match_id);
COMMIT;
BEGIN TRANSACTION;
DROP TABLE If EXISTS player;
CREATE TABLE player
(
        id serial
        ,discord_ID bigint NOT NULL
        ,alias varchar(25)
        ,logo BYTEA
        ,opt_in boolean NOT NULL
);

ALTER TABLE player
ADD CONSTRAINT pk_player_id PRIMARY KEY (id);

ALTER TABLE player
ADD CONSTRAINT unique_discord_id UNIQUE (discord_id);
COMMIT;


BEGIN TRANSACTION;
DROP TABLE If EXISTS match;
CREATE TABLE match
(
        id serial
        ,winner int NOT NULL
        ,time_played timestamp NOT NULL
        ,time_ended timestamp NOT NULL
        ,game_id int
);

ALTER TABLE match
ADD CONSTRAINT pk_match_id PRIMARY KEY (id);

COMMIT;
BEGIN TRANSACTION;

ALTER TABLE player_match
ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES player (id);

ALTER TABLE player_match
ADD CONSTRAINT fk_match_id FOREIGN KEY (match_id) REFERENCES match (id);
COMMIT;

BEGIN TRANSACTION;
DROP TABLE IF EXISTS validChannels
CREATE TABLE validChannels
(
        id bigint NOT NULL
);
ALTER TABLE validChannels
ADD CONSTRAINT pk_channel_id PRIMARY KEY (id);
COMMIT;

BEGIN TRANSACTION;
DROP TABLE IF EXISTS game;
CREATE TABLE game
(
        id serial
        ,name varchar(30) NOT NULL
        ,code varchar(10) NOT NULL
        ,logo BYTEA
);
ALTER TABLE game
ADD CONSTRAINT pk_game_id PRIMARY KEY (id);
COMMIT;
BEGIN TRANSACTION;
ALTER TABLE match
ADD CONSTRAINT fk_match_game_id FOREIGN KEY (game_id) REFERENCES game (id) ;
COMMIT;     
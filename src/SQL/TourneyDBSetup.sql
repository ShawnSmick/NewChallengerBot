BEGIN TRANSACTION;
CREATE TABLE player
(
        id serial
        ,first_name varchar(25) NOT NULL
        ,last_name varchar(25) NOT NULL
        ,elo smallint NOT NULL
);
CREATE TABLE match
(
        id serial
        ,winning_team smallint --used to determine which player won
        ,start_time timestamp
        ,end_time timestamp
        ,best_of smallint
);
CREATE TABLE player_match
(
        player_id int
        ,match_id int
        ,elo_at_time smallint --This is the elo the player was at when the match occured
        ,team smallint
        ,wins smallint --games won in match
);
CREATE TABLE tournament
(
        id serial
        ,name varchar(50)
        
);
COMMIT;

BEGIN TRANSACTION;

ALTER TABLE player
ADD CONSTRAINT pk_player_id PRIMARY KEY (id);
ALTER TABLE match
ADD CONSTRAINT pk_match_id PRIMARY KEY (id);
ALTER TABLE player_match
ADD CONSTRAINT pk_player_match PRIMARY KEY (player_id,match_id);

COMMIT;

BEGIN TRANSACTION;

ALTER TABLE player_match
ADD CONSTRAINT fk_player_match_player_id FOREIGN KEY (player_id) REFERENCES player (id);

ALTER TABLE player_match
ADD CONSTRAINT fk_player_match_match_id FOREIGN KEY (match_id) REFERENCES match (id);

COMMIT;

BEGIN TRANSACTION;

INSERT INTO player (first_name,last_name,elo)
VALUES 
('Daniel','Mccaulley',1000),
('Moises','Castrovera',800);

COMMIT;
BEGIN TRANSACTION;

INSERT INTO match (winning_team,start_time,end_time,best_of)
VALUES 
(1,current_timestamp,current_timestamp,3);


COMMIT;
BEGIN TRANSACTION;

INSERT INTO player_match (  player_id ,match_id ,elo_at_time,team ,wins  )
VALUES 
(1,1,1000,1,2),
(2,1,800,2,0);


COMMIT;
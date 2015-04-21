-- do foreign keys in another file, after we add data

create database if not exists ESPN;

use ESPN;

create table Player (
player_name VARCHAR(64),
team_id     int NOT NULL,
player_role VARCHAR(64),
email       VARCHAR(64),
kdr         float, -- null -> no kills or deaths?
PRIMARY KEY (player_name)
)

create table Team (
team_id int,
name VARCHAR(64) NOT NULL,
rank int,
win_loss_ratio float,
PRIMARY KEY (team_id)
)

create table Game ( -- match is a keyword
game_number int,
tournament_name VARCHAR(64) NOT NULL,
winning_team_id int,
team1_id int,
team2_id int,
length int NOT NULL,
PRIMARY KEY (game_number)
)

create table Tournament (
tournament_name VARCHAR(64),
tournament_year YEAR,
winning_team_id int,
country VARCHAR(64) NOT NULL,
PRIMARY KEY (tournament_name, tournament_year)
)

create table PlayerMatchStat (
player_name VARCHAR(64),
game_number int,
champion VARCHAR(64) NOT NULL,
kills int,
deaths int,
assists int,
creep_score int,
PRIMARY KEY (player_name, game_number)
)

-- Create Table

CREATE TABLE user (
    name VARCHAR(12) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE game(
    id int NOT NULL AUTO_INCREMENT,
    game_name VARCHAR(100) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE GameUser(
    user_name VARCHAR(12) NOT NULL,
    game_id int NOT NULL,
    hand VARCHAR(100) NOT NULL,
    CONSTRAINT user_name_fk FOREIGN KEY(user_name) REFERENCES user(name),
    CONSTRAINT game_id_fk FOREIGN KEY(game_id) REFERENCES game(id),
    PRIMARY KEY(user_name, game_id)
);

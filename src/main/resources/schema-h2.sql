CREATE TABLE IF NOT EXISTS man (
    id INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    id_user INT,
    name VARCHAR (255),
    age INT,
    gender VARCHAR(255),
    country VARCHAR(255),
    city VARCHAR(255),
    occupation VARCHAR(255),
    looking_for VARCHAR(255),
    avatar_xxs VARCHAR (255),
    avatar_xl VARCHAR (255),
    avatar_large VARCHAR (255),
    avatar_small VARCHAR (255),
    is_online BIT,
    id_partner VARCHAR (255),
    last_visit DATE
);
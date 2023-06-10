CREATE DATABASE birthdaybot;

use `birthdaybot`;

CREATE TABLE pracownik(
id INT AUTO_INCREMENT PRIMARY KEY,
imie VARCHAR(50) NOT NULL,
nazwisko VARCHAR(50) NOT NULL,
data_urodzenia DATE NOT NULL,
email VARCHAR(100) NOT NULL
);

CREATE TABLE administrator(
id INT AUTO_INCREMENT PRIMARY KEY,
pracownik_id INT,
add_pracownik BOOLEAN DEFAULT 1,
edit_pracownik BOOLEAN DEFAULT 1,
delete_pracownik BOOLEAN DEFAULT 1,
add_zyczenie BOOLEAN DEFAULT 1,
edit_zyczenie BOOLEAN DEFAULT 1,
delete_zyczenie BOOLEAN DEFAULT 1,
add_przypomnienie BOOLEAN DEFAULT 1,
edit_przypomnienie BOOLEAN DEFAULT 1,
delete_przypomnienie BOOLEAN DEFAULT 1,
FOREIGN KEY (pracownik_id) REFERENCES pracownik(id)
);

CREATE TABLE zyczenia(
id INT AUTO_INCREMENT PRIMARY KEY,
tytul VARCHAR(50) NOT NULL,
tresc VARCHAR(1023) NOT NULL,
rok INT(4) UNIQUE NOT NULL,
obraz LONGBLOB
);

CREATE TABLE przypomnienie(
tytul VARCHAR(50) NOT NULL,
tresc VARCHAR(255) NOT NULL,
ile_dni_wyprzedzenia SMALLINT
);
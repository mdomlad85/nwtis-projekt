CREATE DATABASE nwtis_mdomladov_bp_1;

CREATE TABLE `nwtis_mdomladov_bp_1`.`uredjaji` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `naziv` VARCHAR(128) NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NULL,
  `status` INT(1) NOT NULL DEFAULT 0,
  `vrijeme_promjene` DATETIME NOT NULL,
  `vrijeme_kreiranja` DATETIME NULL,
  PRIMARY KEY (`id`))
COMMENT = 'IoT uređaji';

CREATE TABLE `nwtis_mdomladov_bp_1`.`meteo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `uredjaj_id` INT NOT NULL,
  `adresa_stanice` VARCHAR(255) NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `vrijeme` VARCHAR(255) NULL,
  `vrijeme_opis` VARCHAR(255) NULL,
  `temp` DOUBLE NULL,
  `temp_min` DOUBLE NULL,
  `temp_max` DOUBLE NULL,
  `vlaga` DOUBLE NULL,
  `tlak` DOUBLE NULL,
  `vjetar` DOUBLE NULL,
  `vjetar_smjer` DOUBLE NULL,
  PRIMARY KEY (`id`),
  `preuzeto` DATETIME NOT NULL,
  INDEX `fk_uredjaj_idx` (`uredjaj_id` ASC),
  CONSTRAINT `fk_uredjaj`
    FOREIGN KEY (`uredjaj_id`)
    REFERENCES `nwtis_mdomladov_bp_1`.`uredjaji` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Preuzeti meteo podaci za uređaj';

CREATE TABLE `nwtis_mdomladov_bp_1`.`korisnik` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `ime` VARCHAR(128) NOT NULL,
  `prezime` VARCHAR(128) NOT NULL,
  `korisnicko_ime` VARCHAR(64) NOT NULL,
  `lozinka` VARCHAR(128) NOT NULL COMMENT 'Ne bi trebala biti u plain textu ali radi jednostavnosti je na taj način',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `korisnicko_ime_UNIQUE` (`korisnicko_ime` ASC));


CREATE TABLE `nwtis_mdomladov_bp_1`.`dnevnik` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `korisnik` VARCHAR(64) NOT NULL,
  `url` VARCHAR(128) NOT NULL,
  `ip_adresa` VARCHAR(128) NOT NULL,
  `vrijeme` DATETIME NOT NULL,
  `trajanje` INT NOT NULL,
  `status` INT NOT NULL,
  `vrsta` ENUM('REST', 'SOAP', 'KOMANDA') NOT NULL,
  PRIMARY KEY (`id`));
  
  ALTER TABLE `nwtis_mdomladov_bp_1`.`dnevnik` 
CHANGE COLUMN `vrijeme` `vrijeme` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

ALTER TABLE `nwtis_mdomladov_bp_1`.`dnevnik` 
CHANGE COLUMN `vrsta` `vrsta` ENUM('WS', 'KOMANDA') NOT NULL ;

/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  barce
 * Created: Sep 8, 2025
 */
-- Create DB
CREATE DATABASE IF NOT EXISTS invmauricio
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE invmauricio;

-- 1) Datos del resguardante (owner/user of the equipment)
CREATE TABLE datos_resguardante (
  id                INT          NOT NULL AUTO_INCREMENT,
  nombre            VARCHAR(120) NOT NULL,
  departamento      VARCHAR(120) NOT NULL,
  direccion         VARCHAR(200) NULL,
  cargo             VARCHAR(120) NULL,
  ubicacion_fisica  VARCHAR(200) NULL,
  extension         VARCHAR(20)  NULL,
  correo            VARCHAR(180) NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- 2) Hardware – gabinete (main computer unit)
CREATE TABLE hardware_gabinete (
  id             INT          NOT NULL AUTO_INCREMENT,
  resguardante   INT          NULL,                    -- → datos_resguardante.id
  marca          VARCHAR(80)  NULL,
  modelo         VARCHAR(120) NULL,
  no_inventario  VARCHAR(60)  NULL,
  numero_serie   VARCHAR(120) NULL,
  componentes    VARCHAR(255) NULL,
  observaciones  TEXT         NULL,
  PRIMARY KEY (id),
  KEY idx_gabinete_resguardante (resguardante),
  CONSTRAINT fk_gabinete_resguardante
    FOREIGN KEY (resguardante)
    REFERENCES datos_resguardante(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3) Hardware – periféricos (monitor, teclado, etc.)
CREATE TABLE hardware_perifericos (
  id                 INT          NOT NULL AUTO_INCREMENT,
  resguardante       INT          NULL,                 -- → datos_resguardante.id
  monitor_marca      VARCHAR(80)  NULL,
  monitor_modelo     VARCHAR(120) NULL,
  monitor_serie      VARCHAR(120) NULL,
  teclado_marca      VARCHAR(80)  NULL,
  teclado_modelo     VARCHAR(120) NULL,
  teclado_serie      VARCHAR(120) NULL,
  mouse_marca        VARCHAR(80)  NULL,
  mouse_modelo       VARCHAR(120) NULL,
  mouse_serie        VARCHAR(120) NULL,
  impresora_marca    VARCHAR(80)  NULL,
  impresora_modelo   VARCHAR(120) NULL,
  impresora_serie    VARCHAR(120) NULL,
  bocinas_marca      VARCHAR(80)  NULL,
  webcam_marca       VARCHAR(80)  NULL,
  microfono_marca    VARCHAR(80)  NULL,
  regulador_marca    VARCHAR(80)  NULL,
  nobreak_marca      VARCHAR(80)  NULL,
  otros              TEXT         NULL,
  PRIMARY KEY (id),
  KEY idx_perif_resguardante (resguardante),
  CONSTRAINT fk_perif_resguardante
    FOREIGN KEY (resguardante)
    REFERENCES datos_resguardante(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
) ENGINE=InnoDB;

-- 4) Software (installed on a given hardware_gabinete)
CREATE TABLE software (
  id                  INT           NOT NULL AUTO_INCREMENT,
  hardware            INT           NULL,                 -- → hardware_gabinete.id
  so                  VARCHAR(120)  NULL,
  so_version          VARCHAR(120)  NULL,
  so_licencia         VARCHAR(160)  NULL,
  office              VARCHAR(120)  NULL,
  office_version      VARCHAR(120)  NULL,
  office_licencia     VARCHAR(160)  NULL,
  antivirus           VARCHAR(120)  NULL,
  antivirus_version   VARCHAR(120)  NULL,
  otras_aplicaciones  TEXT          NULL,
  observaciones       TEXT          NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_software_hardware (hardware),  -- one-to-one as implied by the diagram
  CONSTRAINT fk_software_hardware
    FOREIGN KEY (hardware)
    REFERENCES hardware_gabinete(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL
) ENGINE=InnoDB;



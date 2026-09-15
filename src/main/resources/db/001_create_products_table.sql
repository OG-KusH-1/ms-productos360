-- ============================================================
-- DDL Oracle - Tabla PRODUCTS
-- Microservicio: ms-productos360
-- Ejecutar como DBA o usuario ADMIN
-- ============================================================

-- Secuencia para ID autoincremental
CREATE SEQUENCE ADMIN.PRODUCTS_SEQ
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

-- Tabla de productos
CREATE TABLE ADMIN.PRODUCTS (
  ID           NUMBER(19,0)   DEFAULT ADMIN.PRODUCTS_SEQ.NEXTVAL NOT NULL,
  NOMBRE       VARCHAR2(150)  NOT NULL,
  DESCRIPCION  VARCHAR2(500),
  PRECIO       NUMBER(10,2)   NOT NULL,
  STOCK        NUMBER(10,0)   DEFAULT 0 NOT NULL,
  CATEGORIA    VARCHAR2(100)  NOT NULL,
  ESTADO       VARCHAR2(20)   DEFAULT ''ACTIVO'' NOT NULL,
  FECHA_CREACION TIMESTAMP    DEFAULT SYSTIMESTAMP,
  CONSTRAINT PK_PRODUCTS PRIMARY KEY (ID),
  CONSTRAINT CHK_ESTADO_PROD CHECK (ESTADO IN (''ACTIVO'', ''INACTIVO'', ''AGOTADO'')),
  CONSTRAINT CHK_PRECIO CHECK (PRECIO >= 0),
  CONSTRAINT CHK_STOCK CHECK (STOCK >= 0)
);

COMMENT ON TABLE  ADMIN.PRODUCTS              IS ''Catalogo de productos del sistema Pedidos360'';
COMMENT ON COLUMN ADMIN.PRODUCTS.ESTADO       IS ''Estado: ACTIVO, INACTIVO, AGOTADO'';
COMMENT ON COLUMN ADMIN.PRODUCTS.PRECIO       IS ''Precio unitario del producto'';

-- Datos de prueba
INSERT INTO ADMIN.PRODUCTS (NOMBRE, DESCRIPCION, PRECIO, STOCK, CATEGORIA)
VALUES (''Laptop Dell XPS 15'', ''Laptop profesional 15 pulgadas i7'', 1299990, 15, ''Informatica'');

INSERT INTO ADMIN.PRODUCTS (NOMBRE, DESCRIPCION, PRECIO, STOCK, CATEGORIA)
VALUES (''Monitor LG 27 4K'', ''Monitor UHD 4K para diseño y programacion'', 459990, 8, ''Monitores'');

INSERT INTO ADMIN.PRODUCTS (NOMBRE, DESCRIPCION, PRECIO, STOCK, CATEGORIA)
VALUES (''Teclado Mecanico RGB'', ''Teclado mecanico switches Cherry MX'', 89990, 30, ''Perifericos'');

INSERT INTO ADMIN.PRODUCTS (NOMBRE, DESCRIPCION, PRECIO, STOCK, CATEGORIA)
VALUES (''Mouse Logitech MX Master'', ''Mouse ergonomico inalambrico'', 79990, 25, ''Perifericos'');

INSERT INTO ADMIN.PRODUCTS (NOMBRE, DESCRIPCION, PRECIO, STOCK, CATEGORIA, ESTADO)
VALUES (''Impresora HP LaserJet'', ''Impresora laser monocromatica'', 199990, 0, ''Impresion'', ''AGOTADO'');

COMMIT;

SELECT * FROM ADMIN.PRODUCTS ORDER BY ID;

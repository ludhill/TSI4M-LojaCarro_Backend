
DELETE FROM carro;

ALTER TABLE carro ALTER COLUMN id RESTART WITH 1;

INSERT INTO carro (id, modelo, ano) VALUES (1, 'Corolla', 2020);
INSERT INTO carro (id, modelo, ano) VALUES (2, 'Civic', 2021);
INSERT INTO carro (id, modelo, ano) VALUES (3, 'Fusca', 1974);
INSERT INTO carro (id, modelo, ano) VALUES (4, 'Uno', 1998);
INSERT INTO carro (id, modelo, ano) VALUES (5, 'Palio', 2012);
INSERT INTO carro (id, modelo, ano) VALUES (6, 'Gol', 2015);
INSERT INTO carro (id, modelo, ano) VALUES (7, 'Onix', 2019);
INSERT INTO carro (id, modelo, ano) VALUES (8, 'HB20', 2018);
INSERT INTO carro (id, modelo, ano) VALUES (9, 'Sandero', 2014);
INSERT INTO carro (id, modelo, ano) VALUES (10, 'S10', 2016);

ALTER TABLE carro ALTER COLUMN id RESTART WITH 11;
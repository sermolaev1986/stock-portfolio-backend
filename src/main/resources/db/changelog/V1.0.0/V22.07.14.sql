--liquibase formatted sql
--changeset ermolaevs:V22.07.14

INSERT INTO stock (euSymbol, usSymbol, stockName)
VALUES ('4AB.BE', 'ABBV', 'AbbVie');

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
-- Sergei Tesla
('TL0', 2, '+', 702, 'Sergei', '2022-07-14T15:23:48.388'),
-- Sergei NVIDIA
('NVD', 10, '+', 152.5, 'Sergei', '2022-07-14T15:23:48.388'),
-- Sergei AbbVie
('4AB.BE', 9, '+', 149.8, 'Sergei', '2022-07-14T15:23:48.388'),
-- Olga AbbVie
('4AB.BE', 9, '+', 149.8, 'Olga', '2022-07-14T15:23:48.388');




--liquibase formatted sql
--changeset ermolaevs:V1.0.0

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
-- Sergei Easterly
('E05.F', 67, '+', 18.9, 'Sergei', '2022-04-19T15:23:48.388'),
-- Olga Easterly
('E05.F', 67, '+', 18.9, 'Olga', '2022-04-19T15:23:48.388'),
--        Sergei EPAM
('E3M', 5, '+', 206.6, 'Sergei', '2022-03-15T15:23:48.388'),
('E3M', 4, '+', 277.0, 'Sergei', '2022-04-19T15:23:48.388'),
--        Sergei Netflix
('NFC', 6, '+', 241.25, 'Sergei', '2022-04-20T15:23:48.388'),
-- Vanguard S&P 500 Distributing
('VUSA', 13.64, '+', 73.21, 'Sergei', '2022-03-15T15:23:48.388'),
('VUSA', 12.84, '+', 77.76, 'Sergei', '2022-04-19T15:23:48.388'),
-- Andrei Realty Income
('RY6', 10, '+', 59.48, 'Andrei', '2022-03-16T15:23:48.388')

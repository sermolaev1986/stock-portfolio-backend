--liquibase formatted sql
--changeset ermolaevs:V22.06.08

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
--        Sergei Home Depot
('HDI', 8, '+', 279.5, 'Sergei', '2022-06-08T15:23:48.388'),
--        Andrei Realty Income
('RY6', 8, '+', 63.01, 'Andrei', '2022-06-07T15:23:48.388'),
--        Andrei Stag
('SW6', 15, '+', 31.695, 'Andrei', '2022-06-07T15:23:48.388'),
--        Olga Realty Income
('RY6', 16, '+', 62.6, 'Olga', '2022-06-07T15:23:48.388'),
--        Olga Stag
('SW6', 32, '+', 31.575, 'Olga', '2022-06-07T15:23:48.388'),
--        Olga FRIT - Sell
('WX2', 37, '-', 114, 'Olga', '2022-04-22T15:23:48.388'),
--        Andrei FRIT - Sell
('WX2', 8, '-', 114, 'Andrei', '2022-04-22T15:23:48.388')



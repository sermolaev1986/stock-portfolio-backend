--liquibase formatted sql
--changeset ermolaevs:V22.07.15

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
-- Sergei Iron Mountain
('I5M', 21, '+', 45.1, 'Sergei', '2022-07-14T07:23:48.388'),
-- Olga Iron Mountain
('I5M', 35, '+', 45.1, 'Olga', '2022-07-14T07:23:48.388');




--liquibase formatted sql
--changeset ermolaevao:V22.03.14

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
-- HSBC MSCI Russia
('H4ZM.DE', 391, '+', 4.65, 'Sergei', '2022-02-28T15:23:48.388'),
-- Vanguard USD Corporate Bond
('VUCP', 36.085, '+', 27, 'Sergei', '2022-03-01T15:23:48.388'),
('VUCP', 36.085, '+', 38, 'Olga', '2022-03-01T15:23:48.388')

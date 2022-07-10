--liquibase formatted sql
--changeset ermolaevs:V22.07.10

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
--        Sergei AMD
('AMD', 16, '+', 73.55, 'Sergei', '2022-07-06T15:23:48.388')



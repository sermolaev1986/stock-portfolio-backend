--liquibase formatted sql
--changeset ermolaevs:V22.06.08

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
--        Sergei Gold Trust
('GQ9', 9, '+', 162.36, 'Sergei', '2022-06-15T01:00:00.000')



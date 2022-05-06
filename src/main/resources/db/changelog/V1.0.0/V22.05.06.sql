--liquibase formatted sql
--changeset ermolaevs:V22.05.06

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
-- Sergei Easterly
('SRB', 14, '+', 73.32, 'Sergei', '2022-04-21T15:23:48.388'),
('VGWD', 12.93, '+', 58.01, 'Sergei', '2022-04-21T15:23:48.388'),
('VGWD', 12.93, '+', 58.01, 'Olga', '2022-04-21T15:23:48.388')


--liquibase formatted sql
--changeset ermolaevs:V22.07.11

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
-- Olga WP Carey
('WPY', 23, '-', 79.65, 'Olga', '2022-07-11T15:23:48.388'),
-- Sergei WP Carey
('WPY', 15, '-', 79.65, 'Sergei', '2022-07-11T15:23:48.388'),
-- Sergei Easterly
('E05.F', 53, '-', 18.5, 'Sergei', '2022-07-11T15:23:48.388'),
-- Olga Easterly
('E05.F', 81, '-', 18.5, 'Olga', '2022-07-11T15:23:48.388'),
-- Sergei Iron Mountain
('I5M', 24, '-', 47.855, 'Sergei', '2022-07-11T15:23:48.388'),
-- Olga Iron Mountain
('I5M', 37, '-', 47.855, 'Olga', '2022-07-11T15:23:48.388'),
-- Olga Procter & Gamble
('PRG', 5, '+', 142.52, 'Olga', '2022-07-11T15:23:48.388'),
-- Sergei Procter & Gamble
('PRG', 6, '+', 142.52, 'Sergei', '2022-07-11T15:23:48.388')




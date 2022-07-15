--liquibase formatted sql
--changeset ermolaevs:V22.07.15-1

-- Olga Iron Mountain - fix transaction
DELETE FROM transaction WHERE owner = 'Olga' AND symbol = 'I5M' AND date = '2022-07-14T07:23:48.388';

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
('I5M', 35, '+', 45.1, 'Olga', '2022-07-14T07:23:48.388');




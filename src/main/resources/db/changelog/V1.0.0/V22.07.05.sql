--liquibase formatted sql
--changeset ermolaevs:V22.07.05

ALTER TABLE stock ADD COLUMN type VARCHAR(8) DEFAULT 'stock';

UPDATE stock
SET type = 'gold'
WHERE euSymbol IN ('GQ9');

UPDATE stock
SET type = 'bond'
WHERE euSymbol IN ('EUNW', 'SXRH', 'E15H.F', 'VUCP');

UPDATE stock
SET type = 'reit'
WHERE euSymbol IN ('RY6', 'SW6', 'WPY', 'E05.F', 'I5M');


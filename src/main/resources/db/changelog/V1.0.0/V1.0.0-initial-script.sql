--liquibase formatted sql
--changeset ermolaevao:V1.0.0

CREATE TABLE IF NOT EXISTS position
(
    id BIGSERIAL UNIQUE PRIMARY KEY,
    symbol VARCHAR(8) NOT NULL,
    stockCount INTEGER    NOT NULL,
    owner      VARCHAR(6) NOT NULL,
    buyDate    TIMESTAMP  NOT NULL,
    broker     VARCHAR(8) NOT NULL
    );

CREATE TABLE IF NOT EXISTS dividend
(
    id BIGSERIAL UNIQUE PRIMARY KEY,
    symbol VARCHAR(8) NOT NULL,
    amountPerShare NUMERIC(8,4) NOT NULL,
    exchangeRate   NUMERIC(8,4) NOT NULL,
    exDate         TIMESTAMP  NOT NULL,
    shareAmount    INTEGER    NOT NULL,
    owner          VARCHAR(6) NOT NULL
    );

CREATE TABLE IF NOT EXISTS transaction
(
    id BIGSERIAL UNIQUE PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    symbol VARCHAR(8) NOT NULL,
    owner    VARCHAR(6) NOT NULL,
    argument INTEGER    NOT NULL,
    operator VARCHAR(1) NOT NULL,
    price    NUMERIC(8,4)
    );


INSERT INTO position (symbol, stockCount, owner, buyDate, broker)
VALUES
    -- Apple
    ('APC', 15, 'Sergei', '2018-11-12T15:23:48.388', 'Flatex'),
    -- Microsoft
    ('MSF', 19, 'Sergei', '2018-11-13T15:23:48.388', 'Flatex'),
    -- Accenture
    ('CSA', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Severstal
    ('RTS2', 227, 'Olga', '2021-01-15T15:23:48.388', 'Dadat'),
    ('RTS2', 8, 'Andrei', '2021-08-31T15:23:48.388', 'Dadat'),
    ('RTS2', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
--        Coca Cola
    ('CCC3', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Broadcom
    ('1YD', 5, 'Olga', '2021-09-10T15:23:48.388', 'Dadat'),
    -- Home Depot
    ('HDI', 9, 'Olga', '2021-09-13T15:23:48.388', 'Dadat'),
    -- Exxon
    ('XONA', 91, 'Olga', '2020-12-22T15:23:48.388', 'Dadat'),
    -- AT&T
    ('SOBA', 80, 'Olga', '2020-12-22T15:23:48.388', 'Dadat'),
    ('SOBA', 0, 'Sergei', '2018-11-09T15:23:48.388', 'Flatex'),
    -- Federal Realty
    ('WX2', 37, 'Olga', '2020-12-11T15:23:48.388', 'Dadat'),
    ('WX2', 8, 'Andrei', '2020-12-11T15:23:48.388', 'Dadat'),
    -- Pepsi
    ('PEP', 24, 'Olga', '2020-12-28T15:23:48.388', 'Dadat'),
    -- Walt Disney
    ('WDP', 20, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Amazon
    ('AMZ', 1, 'Sergei', '2021-08-30T15:23:48.388', 'Flatex'),
    -- Lukoil
    ('LUK', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Pfizer
    ('PFE', 44, 'Sergei', '2020-03-20T15:23:48.388', 'Flatex'),
    -- Blizzard
    ('AIY', 20, 'Sergei', '2018-11-09T15:23:48.388', 'Flatex'),
    -- Boeing
    ('BCO', 3, 'Sergei', '2019-03-15T15:23:48.388', 'Flatex'),
    -- Canopy Growth
    ('11L1', 20, 'Sergei', '2018-11-05T15:23:48.388', 'Flatex'),
    -- Teva
    ('TEV', 25, 'Sergei', '2018-11-05T15:23:48.388', 'Flatex'),
    -- Procter & Gamble
    ('PRG', 19, 'Olga', '2021-09-17T15:23:48.388', 'Dadat'),
    -- AMD
    ('AMD', 22, 'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
    -- PayPal
    ('2PP', 8, 'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
    -- TCS Group
    ('13T1', 20, 'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
    -- EPAM
    ('E3M', 5, 'Sergei', '2021-10-04T15:23:48.388', 'Flatex'),
    -- Netflix
    ('NFC', 5, 'Sergei', '2021-10-04T15:23:48.388', 'Flatex'),
    -- Gold Trust
    ('GQ9', 13, 'Sergei', '2020-03-18T15:23:48.388', 'Flatex'),
    -- TODO fix fractional ETF shares
    -- Vanguard FTSE All-World
    ('VWCE', 34, 'Sergei', '2020-02-18T15:23:48.388', 'Flatex'),
    -- Vanguard S&P 500
    ('VUAA', 14, 'Sergei', '2021-08-16T15:23:48.388', 'Flatex'),
    -- Salesforce
    ('FOO', 5, 'Sergei', '2021-10-05T15:23:48.388', 'Flatex'),
    -- NVIDIA
    ('NVD', 7, 'Sergei', '2021-10-05T15:23:48.388', 'Flatex')
;

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES ('APC', 15, '+', 46.62, 'Sergei', '2018-11-12T15:23:48.388'),
       ('MSF', 19, '+', 95.53, 'Sergei', '2018-11-13T15:23:48.388'),
       ('GQ9', 13, '+', 129.4, 'Sergei', '2020-03-18T15:23:48.388'),
       ('RTS2', 235, '+', 15.5, 'Olga', '2021-01-15T15:23:48.388'),
--        Severstal 8 shares from Olga to Andrei
       ('RTS2', 8, '-', 19.5, 'Olga', '2021-08-31T15:23:48.388'),
       ('RTS2', 8, '+', 19.5, 'Andrei', '2021-08-31T15:23:48.388'),
       ('1YD', 5, '+', 421.1, 'Olga', '2021-09-10T15:23:48.388'),
       ('HDI', 9, '+', 283.5, 'Olga', '2021-09-13T15:23:48.388'),
       ('XONA', 91, '+', 37.04, 'Olga', '2020-12-22T15:23:48.388'),
       ('SOBA', 80, '+', 24.1, 'Olga', '2020-12-22T15:23:48.388'),
       ('WX2', 37, '+', 75.5, 'Olga', '2020-12-11T15:23:48.388'),
       ('WX2', 8, '+', 75.5, 'Andrei', '2020-12-11T15:23:48.388'),
       ('PEP', 24, '+', 119.12, 'Olga', '2020-12-28T15:23:48.388'),
       ('WDP', 20, '+', 138.38, 'Sergei', '2019-12-02T15:23:48.388'),
       ('AMZ', 1, '+', 2784, 'Sergei', '2021-08-30T15:23:48.388'),
       ('LUK', 30, '+', 87.28, 'Sergei', '2019-12-02T15:23:48.388'),
       ('PFE', 44, '+', 29.4, 'Sergei', '2020-03-20T15:23:48.388'),
       ('AIY', 20, '+', 50.64, 'Sergei', '2018-11-09T15:23:48.388'),
       ('BCO', 3, '+', 330.2, 'Sergei', '2019-03-15T15:23:48.388'),
       ('11L1', 20, '+', 32.98, 'Sergei', '2018-11-05T15:23:48.388'),
       ('TEV', 25, '+', 20.08, 'Sergei', '2018-11-05T15:23:48.388'),
       -- TODO fix fractional ETF shares
       ('VWCE', 5, '+', 95.67, 'Sergei', '2021-07-15T15:23:48.388'),
       ('VWCE', 5, '+', 91.14, 'Sergei', '2021-04-15T15:23:48.388'),
       ('VWCE', 8, '+', 75.42, 'Sergei', '2020-08-18T15:23:48.388'),
       ('VWCE', 8, '+', 75.43, 'Sergei', '2020-05-18T15:23:48.388'),
       ('VWCE', 7, '+', 75.43, 'Sergei', '2020-02-18T15:23:48.388'),
       ('VUAA', 14, '+', 68.876, 'Sergei', '2021-08-16T15:23:48.388'),
       ('PRG', 19, '+', 122.2, 'Olga', '2021-09-17T15:23:48.388'),
       ('AMD', 22, '+', 92.19, 'Sergei', '2021-09-07T15:23:48.388'),
       ('2PP', 8, '+', 247.2, 'Sergei', '2021-09-07T15:23:48.388'),
       ('13T1', 20, '+', 81, 'Sergei', '2021-09-07T15:23:48.388'),
       ('NFC', 5, '+', 526.3, 'Sergei', '2021-10-04T15:23:48.388'),
       ('E3M', 5, '+', 496.4, 'Sergei', '2021-10-04T15:23:48.388'),
       ('FOO', 5, '+', 234.85, 'Sergei', '2021-10-05T15:23:48.388'),
       ('NVD', 7, '+', 172.18, 'Sergei', '2021-10-05T15:23:48.388'),
--        Sergei Coca-Cola
       ('CCC3', 60, '+', 48.49, 'Sergei', '2019-09-02T15:23:48.388'),
       ('CCC3', 65, '+', 41.815, 'Sergei', '2020-05-05T15:23:48.388'),
       ('CCC3', 125, '-', 47.825, 'Sergei', '2021-09-07T15:23:48.388'),
--        Sergei Severstal
       ('RTS2', 200, '+', 12.55, 'Sergei', '2019-12-02T15:23:48.388'),
       ('RTS2', 200, '-', 14.7, 'Sergei', '2021-01-09T15:23:48.388'),
--        Sergei Lukoil
       ('LUK', 30, '+', 87.28, 'Sergei', '2019-12-02T15:23:48.388'),
       ('LUK', 30, '-', 82.7, 'Sergei', '2021-10-01T15:23:48.388'),
--        Sergei Accenture
       ('CSA', 16, '+', 183.0, 'Sergei', '2019-12-02T15:23:48.388'),
       ('CSA', 16, '-', 280.0, 'Sergei', '2021-10-01T15:23:48.388'),
--        Sergei AT&T
       ('SOBA', 35, '+', 27.29, 'Sergei', '2018-11-09T15:23:48.388'),
       ('SOBA', 35, '-', 23.41, 'Sergei', '2021-09-27T15:23:48.388'),
       ('SOBA', 100, '-', 23.135, 'Olga', '2021-09-13T15:23:48.388')
;

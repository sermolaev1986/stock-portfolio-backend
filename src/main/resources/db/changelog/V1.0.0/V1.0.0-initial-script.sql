--liquibase formatted sql
--changeset ermolaevao:V1.0.0

CREATE TABLE IF NOT EXISTS position
(
    id BIGSERIAL UNIQUE PRIMARY KEY,
    symbol VARCHAR(8) NOT NULL,
    stockName VARCHAR(35) NOT NULL,
    stockCount NUMERIC(8,4)    NOT NULL,
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
    shareAmount    NUMERIC(8,4)    NOT NULL,
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


INSERT INTO position (symbol, stockName, stockCount, owner, buyDate, broker)
VALUES
    -- Apple
    ('APC', 'Apple', 60, 'Sergei', '2018-11-12T15:23:48.388', 'Flatex'),
    -- Microsoft
    ('MSF', 'Microsoft', 19, 'Sergei', '2018-11-13T15:23:48.388', 'Flatex'),
    -- Accenture
    ('CSA', 'Accenture', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Severstal
    ('RTS2', 'Severstal', 227, 'Olga', '2021-01-15T15:23:48.388', 'Dadat'),
    ('RTS2', 'Severstal', 8, 'Andrei', '2021-08-31T15:23:48.388', 'Dadat'),
    ('RTS2', 'Severstal', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
--        Coca Cola
-- TODO fix non unique for the same symbol on different brokers
    ('CCC3', 'Coca-Cola', 31, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
--     ('CCC3', 'Coca-Cola', 31, 'Sergei', '2021-11-10T15:23:48.388', 'Dadat'),
    ('CCC3', 'Coca-Cola', 31, 'Olga', '2021-11-10T15:23:48.388', 'Dadat'),
    -- Broadcom
    ('1YD', 'Broadcom', 5, 'Olga', '2021-09-10T15:23:48.388', 'Dadat'),
    ('1YD', 'Broadcom', 3, 'Sergei', '2021-11-12T15:23:48.388', 'Dadat'),
    -- Home Depot
    ('HDI', 'Home Depot', 9, 'Olga', '2021-09-13T15:23:48.388', 'Dadat'),
    -- Exxon
    ('XONA', 'Exxon', 91, 'Olga', '2020-12-22T15:23:48.388', 'Dadat'),
    -- AT&T
    ('SOBA', 'AT&T', 80, 'Olga', '2020-12-22T15:23:48.388', 'Dadat'),
    ('SOBA', 'AT&T', 0, 'Sergei', '2018-11-09T15:23:48.388', 'Flatex'),
    -- Federal Realty
    ('WX2', 'FRIT', 37, 'Olga', '2020-12-11T15:23:48.388', 'Dadat'),
    ('WX2', 'FRIT', 8, 'Andrei', '2020-12-11T15:23:48.388', 'Dadat'),
    -- Pepsi
    ('PEP', 'Pepsi', 24, 'Olga', '2020-12-28T15:23:48.388', 'Dadat'),
    -- Walt Disney
    ('WDP', 'Walt Disney', 20, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Amazon
    ('AMZ', 'Amazon', 1, 'Sergei', '2021-08-30T15:23:48.388', 'Flatex'),
    -- Lukoil
    ('LUK', 'Lukoil', 0, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
    -- Pfizer
    ('PFE', 'Pfizer', 44, 'Sergei', '2020-03-20T15:23:48.388', 'Flatex'),
    -- Blizzard
    ('AIY', 'Blizzard', 0, 'Sergei', '2018-11-09T15:23:48.388', 'Flatex'),
    -- Boeing
    ('BCO', 'Boeing', 0, 'Sergei', '2019-03-15T15:23:48.388', 'Flatex'),
    -- Canopy Growth
    ('11L1', 'Canopy Growth', 20, 'Sergei', '2018-11-05T15:23:48.388', 'Flatex'),
    -- Teva
    ('TEV', 'Teva', 0, 'Sergei', '2018-11-05T15:23:48.388', 'Flatex'),
    -- Procter & Gamble
    ('PRG', 'P&G', 19, 'Olga', '2021-09-17T15:23:48.388', 'Dadat'),
    -- AMD
    ('AMD', 'AMD', 22, 'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
    -- PayPal
    ('2PP', 'PayPal', 8, 'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
    -- TCS Group
    ('13T1', 'Tinkoff', 20, 'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
    -- EPAM
    ('E3M', 'EPAM', 5, 'Sergei', '2021-10-04T15:23:48.388', 'Flatex'),
    -- Netflix
    ('NFC', 'Netflix', 5, 'Sergei', '2021-10-04T15:23:48.388', 'Flatex'),
    -- Gold Trust
    ('GQ9', 'Gold Trust', 13, 'Sergei', '2020-03-18T15:23:48.388', 'Flatex'),
    -- Salesforce
    ('FOO', 'Salesforce', 5, 'Sergei', '2021-10-05T15:23:48.388', 'Flatex'),
    -- NVIDIA
    ('NVD', 'NVIDIA', 7, 'Sergei', '2021-10-05T15:23:48.388', 'Flatex'),
    -- Mastercard
    ('M4I', 'Mastercard', 6, 'Sergei', '2021-11-12T15:23:48.388', 'Flatex'),
    -- Visa
    ('3V64', 'Visa', 11, 'Sergei', '2021-11-12T15:23:48.388', 'Flatex'),
    -- McDonalds
    ('MDO', 'McDonalds', 5, 'Sergei', '2021-11-16T15:23:48.388', 'Flatex'),
    -- Starbucks
    ('SRB', 'Starbucks', 16, 'Sergei', '2021-11-18T15:23:48.388', 'Flatex'),
    -- Tesla
    ('TL0', 'Tesla', 2, 'Sergei', '2021-11-08T15:23:48.388', 'Flatex'),
    -- Realty Income
    ('RY6', 'Realty Income', 17, 'Sergei', '2021-11-08T15:23:48.388', 'Flatex'),
    ('RY6', 'Realty Income', 24, 'Olga', '2021-11-08T15:23:48.388', 'Flatex'),
    -- Stag
    ('SW6', 'Stag', 28, 'Sergei', '2021-11-08T15:23:48.388', 'Flatex'),
    ('SW6', 'Stag', 41, 'Olga', '2021-11-08T15:23:48.388', 'Flatex'),
    -- WP Carey
    ('WPY', 'WP Carey', 15, 'Sergei', '2021-11-08T15:23:48.388', 'Dadat'),
    ('WPY', 'WP Carey', 23, 'Olga', '2021-11-08T15:23:48.388', 'Dadat'),
    -- Easterly
    ('E05.F', 'Easterly', 53, 'Sergei', '2021-11-08T15:23:48.388', 'Dadat'),
    ('E05.F', 'Easterly', 81, 'Olga', '2021-11-08T15:23:48.388', 'Dadat'),
    -- Iron Mountain
    ('I5M', 'Iron Mountain', 24, 'Sergei', '2021-11-10T15:23:48.388', 'Dadat'),
    ('I5M', 'Iron Mountain', 37, 'Olga', '2021-11-10T15:23:48.388', 'Dadat'),
    -- Altria Group
    ('PHM7', 'Altria', 70, 'Sergei', '2021-11-18T15:23:48.388', 'Dadat'),
    -- Cisco Systems
    ('CIS', 'Cisco', 30, 'Sergei', '2021-11-08T15:23:48.388', 'Dadat'),
    ('CIS', 'Cisco', 30, 'Olga', '2021-11-08T15:23:48.388', 'Dadat'),
    -- Mondelez
    ('KTF', 'Mondelez', 30, 'Sergei', '2021-11-12T15:23:48.388', 'Dadat'),
    ('KTF', 'Mondelez', 30, 'Olga', '2021-11-12T15:23:48.388', 'Dadat'),
    -- Johnson&Johnson
    ('JNJ', 'Johnson&Johnson', 23, 'Sergei', '2022-01-18T15:23:48.388', 'Dadat'),
    -- HSBC MSCI RUSSIA
    ('H4ZM', 'Russia ETF', 221.62, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('H4ZM', 'Russia ETF', 83.15, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- IShares Euro High Yield Corporate Bond
    ('EUNW', 'Euro High Yield Corporate Bond', 9.78, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('EUNW', 'Euro High Yield Corporate Bond', 24.47, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- IShares TIPS
    ('SXRH', 'TIPS', 544.07, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('SXRH', 'TIPS', 544.07, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- Lyxor Euro government inflation-linked bonds
    ('E15H.F', 'Euro Gov. Inflation-linked bond', 16.575, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('E15H.F', 'Euro Gov. Inflation-linked bond', 16.575, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- Vanguard FTSE 100
    ('VUKE', 'Great Britain stocks', 64.335, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('VUKE', 'Great Britain stocks', 64.335, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- Vanguard FTSE All-World High Dividend yield
    ('VGWD', 'FTSE All-World High Div. Yield', 39.15, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('VGWD', 'FTSE All-World High Div. Yield', 39.15, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- Vanguard FTSE All-World Distributing
    ('VGWL', 'FTSE All-World Dist.', 32.55, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('VGWL', 'FTSE All-World Dist.', 32.55, 'Olga', '2021-11-15T15:23:48.388', 'Flatex'),
    -- Vanguard FTSE All-World Accumulating
    ('VWCE', 'FTSE All-World Acc.', 34.72, 'Sergei', '2020-02-18T15:23:48.388', 'Flatex'),
    -- Vanguard S&P 500 Accumulating
    ('VUAA', 'S&P 500 Acc.', 35.78, 'Sergei', '2021-08-16T15:23:48.388', 'Flatex'),
    -- Vanguard S&P 500 Distributing
    ('VUSA', 'S&P 500 Dist.', 77.06, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    -- Vanguard USD Corporate Bond
    ('VUCP', 'USD Corporate Bond', 51.03, 'Sergei', '2021-11-15T15:23:48.388', 'Flatex'),
    ('VUCP', 'USD Corporate Bond', 51.03, 'Olga', '2021-11-15T15:23:48.388', 'Flatex')
;

INSERT INTO transaction (symbol, argument, operator, price, owner, date)
VALUES
--        Sergei Apple
       ('APC', 15, '+', 174.47, 'Sergei', '2018-11-12T15:23:48.388'),
--        Sergei Microsoft
       ('MSF', 19, '+', 95.53, 'Sergei', '2018-11-13T15:23:48.388'),
--        Sergei Gold
       ('GQ9', 13, '+', 129.4, 'Sergei', '2020-03-18T15:23:48.388'),
--        Olga Broadcom
       ('1YD', 5, '+', 421.1, 'Olga', '2021-09-10T15:23:48.388'),
--        Olga Home Depot
       ('HDI', 9, '+', 283.5, 'Olga', '2021-09-13T15:23:48.388'),
--        Olga Exxon
       ('XONA', 91, '+', 37.04, 'Olga', '2020-12-22T15:23:48.388'),
--        Olga FRIT
       ('WX2', 37, '+', 75.5, 'Olga', '2020-12-11T15:23:48.388'),
--        Andrei FRIT
       ('WX2', 8, '+', 75.5, 'Andrei', '2020-12-11T15:23:48.388'),
--        Olga Pepsi
       ('PEP', 24, '+', 119.12, 'Olga', '2020-12-28T15:23:48.388'),
       ('WDP', 20, '+', 138.38, 'Sergei', '2019-12-02T15:23:48.388'),

--        Sergei Amazon
       ('AMZ', 1, '+', 2784, 'Sergei', '2021-08-30T15:23:48.388'),
--        Sergei Pfizer
       ('PFE', 44, '+', 29.4, 'Sergei', '2020-03-20T15:23:48.388'),
--        Sergei Blizzard
       ('AIY', 20, '+', 50.64, 'Sergei', '2018-11-09T15:23:48.388'),
        ('AIY', 20, '-', 57.73, 'Sergei', '2021-11-08T15:23:48.388'),
--        Sergei Boeing
       ('BCO', 3, '+', 330.2, 'Sergei', '2019-03-15T15:23:48.388'),
        ('BCO', 3, '-', 192.34, 'Sergei', '2021-11-08T15:23:48.388'),
--        Sergei TEVA
       ('TEV', 25, '+', 20.08, 'Sergei', '2018-11-05T15:23:48.388'),
        ('TEV', 25, '-', 8.05, 'Sergei', '2021-11-08T15:23:48.388'),
--        Sergei Canopy Growth
        ('11L1', 20, '+', 32.98, 'Sergei', '2018-11-05T15:23:48.388'),
--        Olga Procter & Gamble
       ('PRG', 19, '+', 122.2, 'Olga', '2021-09-17T15:23:48.388'),
--        Sergei AMD
       ('AMD', 22, '+', 92.19, 'Sergei', '2021-09-07T15:23:48.388'),
--        Sergei Paypal
       ('2PP', 8, '+', 247.2, 'Sergei', '2021-09-07T15:23:48.388'),
--        Sergei TCS Group
       ('13T1', 20, '+', 81, 'Sergei', '2021-09-07T15:23:48.388'),
--        Sergei EPAM
       ('E3M', 5, '+', 496.4, 'Sergei', '2021-10-04T15:23:48.388'),
--        Sergei Salesforce
       ('FOO', 5, '+', 234.85, 'Sergei', '2021-10-05T15:23:48.388'),
--        Sergei NVIDIA
       ('NVD', 7, '+', 172.18, 'Sergei', '2021-10-05T15:23:48.388'),
--        Sergei Netflix
       ('NFC', 5, '+', 526.3, 'Sergei', '2021-10-04T15:23:48.388'),
--        Sergei Coca-Cola
       ('CCC3', 60, '+', 48.49, 'Sergei', '2019-09-02T15:23:48.388'),
       ('CCC3', 65, '+', 41.815, 'Sergei', '2020-05-05T15:23:48.388'),
       ('CCC3', 125, '-', 47.825, 'Sergei', '2021-09-07T15:23:48.388'),
        ('CCC3', 31, '+', 49.225, 'Sergei', '2021-11-10T15:23:48.388'),
--        Olga Coca-Cola
        ('CCC3', 31, '+', 49.225, 'Olga', '2021-11-10T15:23:48.388'),
--        Sergei Severstal
       ('RTS2', 200, '+', 12.55, 'Sergei', '2019-12-02T15:23:48.388'),
       ('RTS2', 200, '-', 14.7, 'Sergei', '2021-01-09T15:23:48.388'),
--        Olga Severstal
       ('RTS2', 235, '+', 15.5, 'Olga', '2021-01-15T15:23:48.388'),
--        Severstal 8 shares from Olga to Andrei
       ('RTS2', 8, '-', 19.5, 'Olga', '2021-08-31T15:23:48.388'),
       ('RTS2', 8, '+', 19.5, 'Andrei', '2021-08-31T15:23:48.388'),
--        Sergei Lukoil
       ('LUK', 30, '+', 87.28, 'Sergei', '2019-12-02T15:23:48.388'),
       ('LUK', 30, '-', 82.7, 'Sergei', '2021-10-01T15:23:48.388'),
--        Sergei Accenture
       ('CSA', 16, '+', 183.0, 'Sergei', '2019-12-02T15:23:48.388'),
       ('CSA', 16, '-', 280.0, 'Sergei', '2021-10-01T15:23:48.388'),
--        Sergei AT&T
       ('SOBA', 35, '+', 27.29, 'Sergei', '2018-11-09T15:23:48.388'),
       ('SOBA', 35, '-', 23.41, 'Sergei', '2021-09-27T15:23:48.388'),
--        Olga AT&T
        ('SOBA', 180, '+', 24.105, 'Olga', '2020-12-22T15:23:48.388'),
       ('SOBA', 100, '-', 23.135, 'Olga', '2021-09-13T15:23:48.388'),
        -- Sergei Mastercard
        ('M4I', 6, '+', 317.2, 'Sergei', '2021-11-12T15:23:48.388'),
        -- Sergei Visa
        ('3V64', 11, '+', 185.68, 'Sergei', '2021-11-12T15:23:48.388'),
        -- Sergei McDonalds
        ('MDO', 5, '+', 223.3, 'Sergei', '2021-11-16T15:23:48.388'),
        -- Sergei Starbucks
        ('SRB', 16, '+', 98.79, 'Sergei', '2021-11-18T15:23:48.388'),
        -- Sergei Tesla
        ('TL0', 2, '+', 1006.8, 'Sergei', '2021-11-08T15:23:48.388'),
        -- Sergei Realty Income
        ('RY6', 17, '+', 61.72, 'Sergei', '2021-11-08T15:23:48.388'),
        -- Olga Realty Income
        ('RY6', 24, '+', 61.72, 'Olga', '2021-11-08T15:23:48.388'),
        -- Sergei Stag
        ('SW6', 28, '+', 36.66, 'Sergei', '2021-11-08T15:23:48.388'),
        -- Olga Stag
        ('SW6', 41, '+', 36.66, 'Olga', '2021-11-08T15:23:48.388'),
        -- Olga WP Carey
        ('WPY', 23, '+', 67.3, 'Olga', '2021-11-08T15:23:48.388'),
        -- Sergei WP Carey
        ('WPY', 15, '+', 67.3, 'Sergei', '2021-11-08T15:23:48.388'),
        -- Sergei Easterly
        ('E05.F', 53, '+', 18.5, 'Sergei', '2021-11-08T15:23:48.388'),
        -- Olga Easterly
        ('E05.F', 81, '+', 18.5, 'Olga', '2021-11-08T15:23:48.388'),
        -- Sergei Iron Mountain
        ('I5M', 24, '+', 41.58, 'Sergei', '2021-11-10T15:23:48.388'),
        -- Olga Iron Mountain
        ('I5M', 37, '+', 41.58, 'Olga', '2021-11-10T15:23:48.388'),
        -- Sergei Altria Group
        ('PHM7', 70, '+', 39.19, 'Sergei', '2021-11-18T15:23:48.388'),
        -- Sergei Cisco
        ('CIS', 30, '+', 50.56, 'Sergei', '2021-11-10T15:23:48.388'),
        -- Olga Cisco
        ('CIS', 30, '+', 50.56, 'Olga', '2021-11-10T15:23:48.388'),
        -- Sergei Broadcom
        ('1YD', 3, '+', 489.45, 'Sergei', '2021-11-12T15:23:48.388'),
        -- Sergei Mondelez
        ('KTF', 30, '+', 54.82, 'Sergei', '2021-11-12T15:23:48.388'),
        -- Olga Mondelez
        ('KTF', 30, '+', 54.82, 'Olga', '2021-11-12T15:23:48.388'),
        -- Sergei Johnson&Johnson
        ('JNJ', 23, '+', 147.04, 'Sergei', '2022-01-18T15:23:48.388'),

        -- HSBC MSCI Russia
        ('H4ZM', 38.775, '+', 12.88, 'Sergei', '2021-11-15T15:23:48.388'),
        ('H4ZM', 38.775, '+', 12.88, 'Olga', '2021-11-15T15:23:48.388'),
        ('H4ZM', 44.375, '+', 11.25, 'Sergei', '2021-12-15T15:23:48.388'),
        ('H4ZM', 44.375, '+', 11.25, 'Olga', '2021-12-15T15:23:48.388'),
        ('H4ZM', 138.47, '+', 10.82, 'Sergei', '2022-01-17T15:23:48.388'),

        -- IShares Euro High Yield Corporate Bond
        ('EUNW', 4.89, '+', 102.08, 'Sergei', '2021-11-15T15:23:48.388'),
        ('EUNW', 4.89, '+', 102.08, 'Olga', '2021-11-15T15:23:48.388'),
        ('EUNW', 4.89, '+', 102.08, 'Sergei', '2021-12-15T15:23:48.388'),
        ('EUNW', 4.89, '+', 102.08, 'Olga', '2021-12-15T15:23:48.388'),
        ('EUNW', 14.69, '+', 102.01, 'Olga', '2022-01-17T15:23:48.388'),

        -- IShares TIPS
        ('SXRH', 108.45, '+', 4.6, 'Sergei', '2021-11-15T15:23:48.388'),
        ('SXRH', 108.45, '+', 4.6, 'Olga', '2021-11-15T15:23:48.388'),
        ('SXRH', 107.765, '+', 4.63, 'Sergei', '2021-12-15T15:23:48.388'),
        ('SXRH', 107.765, '+', 4.63, 'Olga', '2021-12-15T15:23:48.388'),
        ('SXRH', 327.855, '+', 4.57, 'Sergei', '2022-01-17T15:23:48.388'),
        ('SXRH', 327.855, '+', 4.57, 'Olga', '2022-01-17T15:23:48.388'),

        -- Lyxor Euro government inflation-linked bonds
        ('E15H.F', 3.28, '+', 152.15, 'Sergei', '2021-11-15T15:23:48.388'),
        ('E15H.F', 3.28, '+', 152.15, 'Olga', '2021-11-15T15:23:48.388'),
        ('E15H.F', 3.285, '+', 152, 'Sergei', '2021-12-15T15:23:48.388'),
        ('E15H.F', 3.285, '+', 152, 'Olga', '2021-12-15T15:23:48.388'),
        ('E15H.F', 10.01, '+', 149.79, 'Sergei', '2022-01-17T15:23:48.388'),
        ('E15H.F', 10.01, '+', 149.79, 'Olga', '2022-01-17T15:23:48.388'),

        -- Vanguard FTSE 100
        ('VUKE', 13.2, '+', 37.83, 'Sergei', '2021-11-15T15:23:48.388'),
        ('VUKE', 13.2, '+', 37.83, 'Olga', '2021-11-15T15:23:48.388'),
        ('VUKE', 13.475, '+', 26.95, 'Sergei', '2021-12-15T15:23:48.388'),
        ('VUKE', 13.475, '+', 26.95, 'Olga', '2021-12-15T15:23:48.388'),
        ('VUKE', 37.66, '+', 39.81, 'Sergei', '2022-01-17T15:23:48.388'),
        ('VUKE', 37.66, '+', 39.81, 'Olga', '2022-01-17T15:23:48.388'),

        -- Vanguard FTSE All-World High Dividend yield
        ('VGWD', 4.45, '+', 56.01, 'Sergei', '2021-11-15T15:23:48.388'),
        ('VGWD', 4.45, '+', 56.01, 'Olga', '2021-11-15T15:23:48.388'),
        ('VGWD', 8.98, '+', 55.59, 'Sergei', '2021-12-15T15:23:48.388'),
        ('VGWD', 8.98, '+', 55.59, 'Olga', '2021-12-15T15:23:48.388'),
        ('VGWD', 25.72, '+', 58.32, 'Sergei', '2022-01-17T15:23:48.388'),
        ('VGWD', 25.72, '+', 58.32, 'Olga', '2022-01-17T15:23:48.388'),

        -- Vanguard FTSE All-World Distributing
        ('VGWL', 4.58, '+', 108.96, 'Sergei', '2021-11-15T15:23:48.388'),
        ('VGWL', 4.58, '+', 108.96, 'Olga', '2021-11-15T15:23:48.388'),
        ('VGWL', 14.01, '+', 107.03, 'Sergei', '2021-12-15T15:23:48.388'),
        ('VGWL', 14.01, '+', 107.03, 'Olga', '2021-12-15T15:23:48.388'),
        ('VGWL', 13.96, '+', 107.39, 'Sergei', '2022-01-17T15:23:48.388'),
        ('VGWL', 13.96, '+', 107.39, 'Olga', '2022-01-17T15:23:48.388'),

        -- FTSE All-World accumulating
        ('VWCE', 7.35, '+', 75.43, 'Sergei', '2020-02-18T15:23:48.388'),
        ('VWCE', 8.61, '+', 75.43, 'Sergei', '2020-05-18T15:23:48.388'),
        ('VWCE', 8.08, '+', 75.42, 'Sergei', '2020-08-18T15:23:48.388'),
        ('VWCE', 5.47, '+', 91.14, 'Sergei', '2021-04-15T15:23:48.388'),
        ('VWCE', 5.21, '+', 95.67, 'Sergei', '2021-07-15T15:23:48.388'),

        -- Vanguard S&P 500 Accumulating
        ('VUAA', 14.5, '+', 71.55, 'Sergei', '2021-08-16T15:23:48.388'),
        ('VUAA', 21.28, '+', 71.55, 'Sergei', '2021-10-15T15:23:48.388'),

        -- Vanguard S&P 500 Distributing
        ('VUSA', 12.83, '+', 78.84, 'Sergei', '2021-11-15T15:23:48.388'),
        ('VUSA', 32.02, '+', 78.85, 'Sergei', '2021-12-15T15:23:48.388'),
        ('VUSA', 32.21, '+', 78.85, 'Sergei', '2022-01-17T15:23:48.388'),

        -- Vanguard USD Corporate Bond
        ('VUCP', 5.03, '+', 49.57, 'Sergei', '2021-11-15T15:23:48.388'),
        ('VUCP', 5.03, '+', 49.57, 'Olga', '2021-11-15T15:23:48.388'),
        ('VUCP', 9.915, '+', 50.35, 'Sergei', '2021-12-15T15:23:48.388'),
        ('VUCP', 9.915, '+', 50.35, 'Olga', '2021-12-15T15:23:48.388'),
        ('VUCP', 36.085, '+', 48.48, 'Sergei', '2022-01-17T15:23:48.388'),
        ('VUCP', 36.085, '+', 48.48, 'Olga', '2022-01-17T15:23:48.388')
;

--liquibase formatted sql
--changeset ermolaevao:V1.0.0

CREATE TABLE IF NOT EXISTS position (
                                      id BIGSERIAL UNIQUE PRIMARY KEY,
                                      symbol VARCHAR(8) NOT NULL,
                                      stockCount INTEGER NOT NULL,
                                      owner VARCHAR(6) NOT NULL,
                                      buyDate TIMESTAMP NOT NULL,
                                      broker VARCHAR(8) NOT NULL
                                      );

CREATE TABLE IF NOT EXISTS dividend (   id BIGSERIAL UNIQUE PRIMARY KEY,
                                        symbol VARCHAR(8) NOT NULL,
                                        dollarBruttoAmount FLOAT(8) NOT NULL,
                                        exchangeRate FLOAT(8) NOT NULL,
                                        exDate TIMESTAMP NOT NULL,
                                        shareAmount INTEGER NOT NULL,
                                        owner VARCHAR(6) NOT NULL);

CREATE TABLE IF NOT EXISTS transaction
                                        (id BIGSERIAL UNIQUE PRIMARY KEY,
                                        date TIMESTAMP NOT NULL,
                                        symbol VARCHAR(8) NOT NULL,
                                        owner VARCHAR(6) NOT NULL,
                                        argument INTEGER NOT NULL,
                                        operator VARCHAR(1) NOT NULL,
                                        price FLOAT(8));


INSERT INTO position (symbol, stockCount, owner, buyDate, broker) VALUES
                      ('APC', 15,  'Sergei', '2018-11-12T15:23:48.388', 'Flatex'),
                      ('MSF', 19,  'Sergei', '2018-11-13T15:23:48.388', 'Flatex'),
                      ('CSA', 16,  'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
                      ('RTS2', 227,  'Olga', '2021-01-15T15:23:48.388', 'Dadat'),
                      ('RTS2', 8,  'Andrei', '2021-08-31T15:23:48.388', 'Dadat'),
                      ('1YD', 5,  'Olga', '2021-09-10T15:23:48.388', 'Dadat'),
                      ('HDI', 9,  'Olga', '2021-09-13T15:23:48.388', 'Dadat'),
                      ('XONA', 91,  'Olga', '2020-12-22T15:23:48.388', 'Dadat'),
                      ('SOBA', 80,  'Olga', '2020-12-22T15:23:48.388', 'Dadat'),
                      ('SOBA', 35,  'Sergei', '2018-11-09T15:23:48.388', 'Flatex'),
                      ('WX2', 37,  'Olga', '2020-12-11T15:23:48.388', 'Dadat'),
                      ('WX2', 8,  'Andrei', '2020-12-11T15:23:48.388', 'Dadat'),
                      ('PEP', 24,  'Olga', '2020-12-28T15:23:48.388', 'Dadat'),
                      ('WDP', 20, 'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
                      ('AMZ', 1,  'Sergei', '2021-08-30T15:23:48.388', 'Flatex'),
                      ('LUK', 30,  'Sergei', '2019-12-02T15:23:48.388', 'Flatex'),
                      ('PFE', 44, 'Sergei', '2020-03-20T15:23:48.388', 'Flatex'),
                      ('AIY', 20,  'Sergei', '2018-11-09T15:23:48.388', 'Flatex'),
                      ('BCO', 3, 'Sergei', '2019-03-15T15:23:48.388', 'Flatex'),
                      ('11L1', 20, 'Sergei', '2018-11-05T15:23:48.388', 'Flatex'),
                      ('TEV', 25,  'Sergei', '2018-11-05T15:23:48.388', 'Flatex'),
                      ('PRG', 19, 'Olga', '2021-09-17T15:23:48.388', 'Dadat'),
                      ('AMD', 22,  'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
                      ('2PP', 8,  'Sergei', '2021-09-07T15:23:48.388', 'Flatex'),
                      ('13T1', 20,  'Sergei', '2021-09-07T15:23:48.388', 'Flatex');

INSERT INTO transaction (symbol, argument, operator, price, owner, date) VALUES
                      ('APC', 15, '+', 46.62, 'Sergei', '2018-11-12T15:23:48.388'),
                      ('MSF', 19, '+',95.53, 'Sergei', '2018-11-13T15:23:48.388'),
                      ('CSA', 16, '+',183, 'Sergei', '2019-12-02T15:23:48.388'),
                      ('RTS2', 227, '+',15.5, 'Olga', '2021-01-15T15:23:48.388'),
                      ('RTS2', 8, '+',19.5, 'Andrei', '2021-08-31T15:23:48.388'),
                      ('1YD', 5, '+',421.1, 'Olga', '2021-09-10T15:23:48.388'),
                      ('HDI', 9, '+',283.5, 'Olga', '2021-09-13T15:23:48.388'),
                      ('XONA', 91, '+',37.04, 'Olga', '2020-12-22T15:23:48.388'),
                      ('SOBA', 80, '+',24.1, 'Olga', '2020-12-22T15:23:48.388'),
                      ('SOBA', 35, '+',27.29, 'Sergei', '2018-11-09T15:23:48.388'),
                      ('WX2', 37, '+',75.5, 'Olga', '2020-12-11T15:23:48.388'),
                      ('WX2', 8, '+',75.5, 'Andrei', '2020-12-11T15:23:48.388'),
                      ('PEP', 24, '+',119.12, 'Olga', '2020-12-28T15:23:48.388'),
                      ('WDP', 20, '+',138.38, 'Sergei', '2019-12-02T15:23:48.388'),
                      ('AMZ', 1, '+',2784, 'Sergei', '2021-08-30T15:23:48.388'),
                      ('LUK', 30, '+',87.28, 'Sergei', '2019-12-02T15:23:48.388'),
                      ('PFE', 44, '+',29.4, 'Sergei', '2020-03-20T15:23:48.388'),
                      ('AIY', 20, '+',50.64, 'Sergei', '2018-11-09T15:23:48.388'),
                      ('BCO', 3, '+',330.2, 'Sergei', '2019-03-15T15:23:48.388'),
                      ('11L1', 20, '+',32.98, 'Sergei', '2018-11-05T15:23:48.388'),
                      ('TEV', 25, '+',20.08, 'Sergei', '2018-11-05T15:23:48.388'),
                      ('PRG', 19, '+',122.2, 'Olga', '2021-09-17T15:23:48.388'),
                      ('AMD', 22, '+',92.19, 'Sergei', '2021-09-07T15:23:48.388'),
                      ('2PP', 8, '+',247.2, 'Sergei', '2021-09-07T15:23:48.388'),
                      ('13T1', 20, '+',81, 'Sergei', '2021-09-07T15:23:48.388');

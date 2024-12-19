drop table stock

CREATE TABLE stock (
    stockno      NUMBER(10)    NOT NULL,
    symbol       VARCHAR(255)  NULL,
    name         VARCHAR(255)  NOT NULL,
    sector       VARCHAR(255)  NULL,
    industry     VARCHAR(255)  NULL,
    description  VARCHAR(255)  NULL,
    contentno    NUMBER(10)    NOT NULL,
    PRIMARY KEY (stockno)
);

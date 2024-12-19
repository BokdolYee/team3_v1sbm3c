drop table stockdata

CREATE TABLE stockdata (
	sdatano	NUMBER(10)	NOT NULL,
	rdate	date	NULL,
	open_price	NUMBER(30)	NULL,
	close_price	NUMBER(30)	NULL,
	volume	NUMBER(30)	NULL,
	change	NUMBER(30)	NULL,
	stockno	NUMBER(30)	NOT NULL,
    PRIMARY KEY(sdatano)
);
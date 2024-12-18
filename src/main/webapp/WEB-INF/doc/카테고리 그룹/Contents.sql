drop table contents

CREATE TABLE contents (
	contentno	Number(10)	NOT NULL,
	title	VARCHAR(255)	NULL,
	passwd	VARCHAR(50)	NULL,
	rdate	date	NULL,
	cnt	NUMBER(10)	NULL,
	all_cnt	NUMBER(30)	NULL,
	visible	CHAR(1)	NULL,
	cateno	Number(10)	NOT NULL,
    PRIMARY KEY (contentno)
);
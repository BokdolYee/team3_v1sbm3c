DROP TABLE ISSUE CASCADE CONSTRAINTS; 

CREATE TABLE ISSUE{
    issueno	NUMBER(10)	    NOT NULL    PRIMARY KEY,
	cnt	NUMBER(10)	        NULL,
	content	VARCHAR(300)	NOT NULL,
    rdate date              NULL,
    title VARCHAR(300)      NOT NULL,
}

ALTER TABLE issue ADD is_urgent CHAR(1) DEFAULT 'N';

CREATE SEQUENCE ISSUE_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지
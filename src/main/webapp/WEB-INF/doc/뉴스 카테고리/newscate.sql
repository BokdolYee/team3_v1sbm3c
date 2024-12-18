/**********************************/
/* Table Name: 뉴스 카테고리 */
/**********************************/
Drop table newscate
CREATE TABLE newscate(
    NEWCATENO                       NUMBER(10)     NOT NULL    PRIMARY KEY,
    NAME                              VARCHAR2(30)     NOT NULL,
    CNT                               NUMBER(7)    DEFAULT 0     NOT NULL,
    SEQNO                             NUMBER(5)    DEFAULT 1     NOT NULL,
    VISIBLE                           CHAR(1)    DEFAULT 'Y'     NOT NULL,
    RDATE                             DATE     NOT NULL
);

COMMENT ON TABLE newscate is '뉴스 카테고리';
COMMENT ON COLUMN newscate.NEWCATENO is '뉴스카테고리 번호';
COMMENT ON COLUMN newscate.NEWCATENO is '장르';
COMMENT ON COLUMN newscate.NAME is '카테고리 이름';
COMMENT ON COLUMN newscate.SEQNO is '출력 순서';
COMMENT ON COLUMN newscate.VISIBLE is '출력 모드';
COMMENT ON COLUMN newscate.RDATE is '등록일';

DROP SEQUENCE NEWCATE_SEQ;

 CREATE SEQUENCE NEWCATE_SEQ
   START WITH 1         -- 시작 번호
   INCREMENT BY 1       -- 증가값
   MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
   CACHE 2              -- 2번은 메모리에서만 계산
   NOCYCLE;             -- 다시 1부터 생성되는 것을 방지
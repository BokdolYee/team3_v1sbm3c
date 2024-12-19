DROP TABLE loginlog;
DROP TABLE loginlog CASCADE CONSTRAINTS;
DROP SEQUENCE loginlog_seq;
COMMIT;

CREATE TABLE loginlog (
 loginlogno   NUMBER(10)  NOT NULL, -- 로그인 기록 번호, 기본키
 loginsuccess VARCHAR(1)  NOT NULL, -- 로그인 성공 여부, T OR F, ORACLE DB는 BOOLEAN 타입 없어서 NUMBER나 VARCHAR로 해야 함
 ip           VARCHAR(10) NOT NULL, -- 로그인 시도 ip ex) 121.160.41.226
 ldate        DATE        NOT NULL, -- 로그인 성공 시 날짜 및 시간 ex) 2024-12-09 14:56:31
 memberno     NUMBER(7)   NOT NULL, -- 외래키
 FOREIGN KEY (memberno) REFERENCES member (memberno),
 PRIMARY KEY(loginlogno)
);

COMMENT ON TABLE LOGINLOG is '로그인 기록';
COMMENT ON COLUMN LOGINLOG.loginlogno is '로그인 기록 번호';
COMMENT ON COLUMN LOGINLOG.LOGINSUCCESS is '로그인 성공 여부';
COMMENT ON COLUMN LOGINLOG.IP is '로그인 ip';
COMMENT ON COLUMN LOGINLOG.LDATE is '로그인 날짜';
COMMENT ON COLUMN LOGINLOG.MEMBERNO is '회원 번호';

CREATE SEQUENCE loginlog_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

SELECT * FROM loginlog;
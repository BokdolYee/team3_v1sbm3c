DROP TABLE loginlog;
DROP TABLE loginlog CASCADE CONSTRAINTS;
DROP SEQUENCE loginlog_seq;
COMMIT;

CREATE TABLE loginlog (
 loginlogno   NUMBER(10)              NOT NULL, -- 로그인 기록 번호, 기본키
 id           VARCHAR(30)             NOT NULL, -- 로그인 시도한 id
 ip           VARCHAR(15)             NOT NULL, -- 로그인 시도 ip ex) 121.160.41.226
 result       VARCHAR(1) DEFAULT 'F'  NOT NULL, -- 로그인 성공 여부, T OR F, ORACLE DB는 BOOLEAN 타입 없어서 NUMBER나 VARCHAR로 해야 함
 ldate        DATE                    NOT NULL, -- 로그인 성공 시 날짜 및 시간 ex) 2024-12-09 14:56:31
 
 PRIMARY KEY(loginlogno)
);

COMMENT ON TABLE LOGINLOG is '로그인 기록';
COMMENT ON COLUMN LOGINLOG.LOGINLOGNO is '로그인 기록 번호';
COMMENT ON COLUMN LOGINLOG.ID is '로그인 시도 ID';
COMMENT ON COLUMN LOGINLOG.IP is '로그인 ip';
COMMENT ON COLUMN LOGINLOG.RESULT is '로그인 성공 여부';
COMMENT ON COLUMN LOGINLOG.LDATE is '로그인 날짜';

CREATE SEQUENCE loginlog_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

SELECT * FROM loginlog;
drop table contents CASCADE CONSTRAINTS;
DROP SEQUENCE contents_seq;

CREATE TABLE contents (
    contentno      NUMBER(10)       NOT NULL,
    title         VARCHAR(255)      NULL,
    passwd        VARCHAR(50)       NULL,
    rdate         DATE              NULL,
    cnt           NUMBER(10)        NULL,
    all_cnt       NUMBER(30)        NULL,
    visible       CHAR(1)           NULL,
    newscateno    NUMBER(10)        NOT NULL,
    stockno       NUMBER(10)        NOT NULL,
    newsno        NUMBER(10)        NOT NULL,
    recom         NUMBER(10)        NULL,
    file1         VARCHAR2(100 BYTE) NULL,  -- 메인 이미지
    file1saved    VARCHAR2(100 BYTE) NULL,  -- 실제 저장된 메인 이미지
    thumb1        VARCHAR2(100 BYTE) NULL,  -- 메인 이미지 Preview
    size1         NUMBER(10,0)     NULL,  -- 메인 이미지 크기
    PRIMARY KEY (contentno)
);

CREATE SEQUENCE contents_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
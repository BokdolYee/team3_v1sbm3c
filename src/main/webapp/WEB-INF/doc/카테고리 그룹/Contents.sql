drop table contents

CREATE TABLE contents (
    contentno      NUMBER(10)       NOT NULL,
    title         VARCHAR(255)      NULL,
    passwd        VARCHAR(50)       NULL,
    rdate         DATE              NULL,
    cnt           NUMBER(10)        NULL,
    all_cnt       NUMBER(30)        NULL,
    visible       CHAR(1)           NULL,
    newscateno    NUMBER(10)       NOT NULL,
    stockno       NUMBER(10)       NOT NULL,
    newsno        NUMBER(10)       NOT NULL,
    file1         VARCHAR2(100 BYTE) NULL,  -- 메인 이미지
    file1saved    VARCHAR2(100 BYTE) NULL,  -- 실제 저장된 메인 이미지
    thumb1        VARCHAR2(100 BYTE) NULL,  -- 메인 이미지 Preview
    size1         NUMBER(10,0)     NULL,  -- 메인 이미지 크기
    PRIMARY KEY (contentno)
);

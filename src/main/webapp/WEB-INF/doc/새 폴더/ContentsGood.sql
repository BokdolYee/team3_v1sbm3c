DROP TABLE CONTENTSGOOD CASCADE CONSTRAINTS;
DROP SEQUENCE CONTENTSGOOD_seq;

CREATE TABLE CONTENTSGOOD (
    CONTENTSGOODNO   NUMBER(10)       NOT NULL PRIMARY KEY,
    GOODBAD          VARCHAR2(10)     NULL,    -- VARCHAR2로 수정
    RDATE            DATE             NULL,            -- DATE 대문자로 수정
    CONTENTNO        NUMBER(10)       NOT NULL,
    MEMBERNO         NUMBER(10)       NOT NULL,
    FOREIGN KEY (CONTENTNO) REFERENCES CONTENTS(CONTENTNO), -- 컬럼 이름 확인
    FOREIGN KEY (MEMBERNO) REFERENCES MEMBER(MEMBERNO)      -- 컬럼 이름 확인
);

CREATE SEQUENCE CONTENTSGOOD_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
-- 데이터 삽입
INSERT INTO CONTENTSGOOD(CONTENTSGOODNO, rdate, contentno, memberno)
VALUES(CONTENTSGOOD_seq.nextval, sysdate, 1, 3);

-- 전체 조회
SELECT CONTENTSGOODNO, rdate, contentno, memberno
FROM CONTENTSGOOD
ORDER BY CONTENTSGOODNO DESC;

-- 조회
SELECT CONTENTSGOODNO, rdate, contentno, memberno
FROM CONTENTSGOOD
WHERE contentsgoodno = 2;

-- 삭제
DELETE FROM contentsgood
WHERE contentsgoodno = 3;

COMMIT;
  
SELECT COUNT(*) as cnt
FROM contentsgood
WHERE contentno = 21 AND memberno = 3;

       CNT
----------
         1 <-- 이미 추천을 함
         
SELECT COUNT(*) as cnt
FROM contentsgood
WHERE contentno = 22 AND memberno = 3;

       CNT
----------
         0 <-- 추천 안한 상태
         
-- 추천
UPDATE contents
SET recom = recom + 1;
WHERE contentno = 1;

-- 비추천
UPDATE contents
SET recom = recom - 1;
WHERE contentno = 1;
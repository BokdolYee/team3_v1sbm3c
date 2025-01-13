drop table FAVORITES CASCADE CONSTRAINTS;
DROP SEQUENCE FAVORITES_seq;

CREATE TABLE FAVORITES (
    favoritno	        NUMBER(10)	NOT NULL PRIMARY KEY,
    cnt	                NUMBER(10)	NULL,
    rdate	            DATE	    NULL,
    newscateno          NUMBER(10)	NOT NULL,
    memberno	        NUMBER(10)	NOT NULL,
    FOREIGN KEY (newscateno) REFERENCES NEWSCATE(newscateno), -- 컬럼 이름 확인
    FOREIGN KEY (memberno) REFERENCES MEMBER(memberno)
);

CREATE SEQUENCE FAVORITES_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;    
  
-- create
INSERT INTO FAVORITES(favoritno, cnt, rdate, newscateno, memberno)
VALUES(FAVORITES_seq.nextval, 2, sysdate, 7, 23);

-- read
SELECT favoritno, cnt, rdate, newscateno, memberno
FROM FAVORITES
WHERE favoritno = 2;

-- list
SELECT favoritno, cnt, rdate, newscateno, memberno FROM FAVORITES
ORDER BY cnt ASC;

-- delete
DELETE FROM FAVORITES
WHERE favoritno = 2;

COMMIT;
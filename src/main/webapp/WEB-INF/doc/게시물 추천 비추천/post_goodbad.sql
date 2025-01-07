DROP TABLE post_goodbad;
COMMIT;

CREATE TABLE post_goodbad (
    goodbadno	number(10)		NOT NULL,   -- 추천번호 기본키
    goodbad 	varchar(1)		NOT NULL,   -- 추천 또는 비추천
    rdate	    date		    NOT NULL,   -- 추천 또는 비추천 날짜
    postno	    number(10)		NOT NULL,   -- 게시물 번호(외래키)
    memberno	number(10)		NOT NULL,   -- 회원 번호(외래키)
    
    PRIMARY KEY(goodbadno),
    FOREIGN KEY(postno) REFERENCES post_earning (postno),
    FOREIGN KEY(memberno) REFERENCES member (memberno)
);

ALTER TABLE post_goodbad
DROP CONSTRAINT SYS_C007771;

ALTER TABLE post_goodbad
ADD CONSTRAINT FK_POST_GOODBAD
FOREIGN KEY(postno) REFERENCES post_earning(postno)
ON DELETE CASCADE;

COMMENT ON TABLE post_goodbad is '게시물 추천 비추천';
COMMENT ON COLUMN post_goodbad.goodbadno is '추천 번호';
COMMENT ON COLUMN post_goodbad.goodbad is '추천 비추천';
COMMENT ON COLUMN post_goodbad.rdate is '추천 비추천 날짜';
COMMENT ON COLUMN post_goodbad.postno is '게시물 번호';
COMMENT ON COLUMN post_goodbad.memberno is '회원 번호';

DROP SEQUENCE post_goodbad_seq;

CREATE SEQUENCE post_goodbad_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
INSERT INTO post_goodbad(goodbadno, goodbad, rdate, postno, memberno)
VALUES(post_goodbad_seq.nextval, 'b', sysdate, 5, 22);

INSERT INTO post_goodbad(goodbadno, goodbad, rdate, postno, memberno)
VALUES(post_goodbad_seq.nextval, 'b', sysdate, 4, 9);

INSERT INTO post_goodbad(goodbadno, goodbad, rdate, postno, memberno)
VALUES(post_goodbad_seq.nextval, 'g', sysdate, 3, 22);

-- 테이블 3개 JOIN
SELECT g.*, p.postno, m.nickname
FROM post_goodbad g INNER JOIN post_earning p
                    ON g.postno = p.postno
                    INNER JOIN member m
                    ON g.memberno = m.memberno;
                    
-- 테이블 2개 JOIN                    
SELECT g.*, m.nickname
FROM post_goodbad g INNER JOIN member m
                    ON g.memberno = m.memberno;

SELECT * FROM post_goodbad;

SELECT COUNT(g.memberno), g.goodbad
FROM post_goodbad g INNER JOIN post_earning p
                    ON g.postno = p.postno AND g.memberno = p.memberno group by g.goodbad;
--WHERE postno = 5 AND memberno = 22; 

DELETE FROM post_goodbad;
DROP TABLE termbook;

SET DEFINE OFF;

CREATE TABLE termbook (
	termno	NUMBER(10)	NOT NULL PRIMARY KEY,    -- 고유 번호
	term_title	VARCHAR2(255)	NOT NULL,        -- 글 제목
	memo	CLOB	NOT NULL,                    -- 글 내용
	category	VARCHAR2(100)	NOT NULL,        -- 범주
	rdate	DATE	NOT NULL,                    -- 날짜  
	memberno	NUMBER(10)	NOT NULL,            -- 회원번호    
    FOREIGN KEY (memberno) REFERENCES member (memberno) -- FK
);

DROP SEQUENCE termbook_seq;

CREATE SEQUENCE termbook_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- 예시 데이터 삽입 (member 테이블에서 memberno 1, 2, 3이 있다고 가정)
INSERT INTO termbook (termno, term_title, memo, category, rdate, memberno)
VALUES (termbook_seq.nextval, 'S&P', 'S&P 500은 미국의 500개 주요 기업의 주식으로 구성된 주식 시장 지수입니다.', '지수', sysdate, 3);

SELECT termno, term_title, memo, category, rdate, memberno
FROM termbook
ORDER BY termno DESC;

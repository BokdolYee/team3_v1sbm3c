DROP TABLE summarize CASCADE CONSTRAINTS; 

CREATE TABLE summarize (
	summaryno	NUMBER(10)	NOT NULL    PRIMARY KEY,
	content	VARCHAR(300)	NULL,
	newsno	Number(10)	NOT NULL --FK
);

-- summarize 테이블의 외래 키 추가
ALTER TABLE summarize
ADD CONSTRAINT fk_summarize_news_origin
FOREIGN KEY (NEWSNO) REFERENCES news (NEWSNO);

DROP SEQUENCE SUMMARIZE_SEQ

CREATE SEQUENCE SUMMARIZE_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

SELECT table_name FROM user_tables WHERE table_name = 'news';

SELECT summaryno, content, newsno
FROM summarize
ORDER BY summaryno DESC;

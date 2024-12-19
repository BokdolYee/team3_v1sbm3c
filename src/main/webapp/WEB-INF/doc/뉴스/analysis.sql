DROP TABLE Analysis CASCADE CONSTRAINTS; 

CREATE TABLE Analysis (
	analysisno	NUMBER(10)	NOT NULL    PRIMARY KEY,
	impact	VARCHAR(300)	NULL,
	newsno	NUMBER(10)	NOT NULL  --FK
);

-- analysis 테이블의 외래 키 추가
ALTER TABLE analysis
ADD CONSTRAINT fk_analysis_summarize
FOREIGN KEY (SUMMARYNO) REFERENCES summarize (SUMMARYNO);

DROP SEQUENCE ANALYSIS_SEQ

CREATE SEQUENCE ANALYSIS_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

SELECT analysisno, impact, summaryno
FROM analysis
ORDER BY analysisno DESC;

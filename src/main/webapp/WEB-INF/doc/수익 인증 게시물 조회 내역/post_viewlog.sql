DROP TABLE post_viewlog;

CREATE TABLE post_viewlog(
 post_viewlogno     NUMBER(10)              NOT NULL, -- 게시물 조회 내역 번호(기본키)
 memberno           NUMBER(10)              NOT NULL, -- 회원 번호(외래키)
 postno             NUMBER(10)              NOT NULL,
 rdate              DATE                    NOT NULL, -- 첫 조회일
 ldate              DATE                    NOT NULL, -- 마지막 조회일
 viewcnt            NUMBER(10) DEFAULT 0    NOT NULL, -- 조회수(회원이 해당 게시물을 몇 번 조회했는지)
 
 PRIMARY KEY (post_viewlogno),
 FOREIGN KEY(memberno) REFERENCES member (memberno),
 FOREIGN KEY(postno) REFERENCES post_earning (postno)
);

DROP SEQUENCE post_viewlog_seq;

CREATE SEQUENCE post_viewlog_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

SELECT * FROM post_viewlog;
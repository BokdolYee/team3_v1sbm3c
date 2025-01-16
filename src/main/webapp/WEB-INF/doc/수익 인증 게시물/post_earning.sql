DROP TABLE post_earning;
DROP TABLE post_earning CASCADE CONSTRAINTS;
DROP SEQUENCE post_earning_seq;
COMMIT;

CREATE TABLE post_earning (
  postno     NUMBER(10)                NOT NULL, -- 게시물 번호, 레코드를 구분하는 컬럼, 기본키
  memberno   NUMBER(10)                NOT NULL, -- 회원 번호, 외래키
  viewcnt    NUMBER(5)       DEFAULT 0 NOT NULL, -- 조회수, 게시물 등록 시 기본적으로 0
  replycnt   NUMBER(4)       DEFAULT 0 NOT NULL, -- 등록된 댓글 수, 게시물 등록 시 기본적으로 0
  goodcnt    NUMBER(4)       DEFAULT 0 NOT NULL, -- 추천수, 게시물 등록 시 기본적으로 0
  badcnt     NUMBER(4)       DEFAULT 0 NOT NULL, -- 비추천수, 게시물 등록 시 기본적으로 0
  title      VARCHAR(60)               NOT NULL, -- 제목
  content    VARCHAR(1000)             NOT NULL, -- 내용
  fcnt       NUMBER(2)       DEFAULT 0 NOT NULL, -- 첨부파일 개수
  rdate      DATE                      NOT NULL, -- 등록일
  udate      DATE,                               -- 수정일, 처음 댓글 등록 시 수정 상태가 아니므로 NULL 허용
  
  PRIMARY KEY (postno),
  FOREIGN KEY(memberno) REFERENCES member (memberno)
);

COMMENT ON TABLE post_earning is '수익 인증 게시물';
COMMENT ON COLUMN  post_earning.postno is '게시물 번호';
COMMENT ON COLUMN  post_earning.memberno is '회원 번호';
COMMENT ON COLUMN  post_earning.viewcnt is '조회수';
COMMENT ON COLUMN  post_earning.replycnt is '댓글수';
COMMENT ON COLUMN  post_earning.goodcnt is '추천수';
COMMENT ON COLUMN  post_earning.badcnt is '비추천수';
COMMENT ON COLUMN  post_earning.title is '제목';
COMMENT ON COLUMN  post_earning.content is '내용';
COMMENT ON COLUMN  post_earning.rdate is '등록일';
COMMENT ON COLUMN  post_earning.udate is '수정일';

DROP SEQUENCE post_earning_seq;

CREATE SEQUENCE post_earning_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
SELECT * FROM post_earning ORDER BY postno DESC; -- 모든 행 조회

SELECT * FROM USER_CONSTRAINTS  -- post_earning 이랑 얽혀 있는 테이블 조회
WHERE R_CONSTRAINT_NAME IN (
    SELECT CONSTRAINT_NAME 
    FROM USER_CONSTRAINTS 
    WHERE TABLE_NAME = 'POST_EARNING'
);

-- 작성된 글의 회원 닉네임을 조회하기 위한 JOIN(외래키인 memberno 이용)
SELECT P.*, m.nickname
FROM post_earning p INNER JOIN member m ON p.memberno = m.memberno
WHERE p.postno = postno;

ROLLBACK;

DELETE FROM post_earning;
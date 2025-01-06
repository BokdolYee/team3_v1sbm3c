DROP TABLE reply CASCADE CONSTRAINTS;
DROP SEQUENCE reply_seq;

CREATE TABLE reply (
	replyno	        NUMBER(10)	        NOT NULL,
	rdate	        DATE	            NOT NULL,
	content	        VARCHAR(255)	    NOT NULL,
	parentreplyno	NUMBER(10)	        NULL,
	memberno	    NUMBER(10)	        NOT NULL,
	contentno	    NUMBER(10)	        NOT NULL,
    file1           VARCHAR2(100 BYTE)  NULL,  -- 메인 이미지
    file1saved      VARCHAR2(100 BYTE)  NULL,  -- 실제 저장된 메인 이미지
    thumb1          VARCHAR2(100 BYTE)  NULL,  -- 메인 이미지 Preview
    size1           NUMBER(10,0)        NULL,  -- 메인 이미지 크기
    PRIMARY KEY (replyno)
);

COMMENT ON TABLE reply is '댓글';
COMMENT ON COLUMN reply.replyno is '댓글 번호';
COMMENT ON COLUMN reply.rdate is '작성 날짜';
COMMENT ON COLUMN reply.delete_flag is '삭제 여부';
COMMENT ON COLUMN reply.content is '내용';
COMMENT ON COLUMN reply.parentreplyno is '부모댓글번호';
COMMENT ON COLUMN reply.memberno is '회원 번호';
COMMENT ON COLUMN reply.contentno is '컨텐츠 번호';

CREATE SEQUENCE reply_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지

SELECT r.replyno, r.content, r.rdate, r.contentno, r.memberno, r.parentreplyno, r.file1, r.file1saved, r.thumb1, r.size1, 
       m.id AS member_id, m.name AS member_name
FROM reply r
JOIN member m ON m.memberno = r.memberno
WHERE r.contentno = 21  -- 테스트하려는 contentno를 넣으세요
ORDER BY r.replyno DESC

    SELECT id, replyno, contentno, memberno, content, rdate
    FROM (
          SELECT m.id, 
                 p.replyno, 
                 p.contentno, 
                 p.memberno, 
                 p.content, 
                 p.rdate,
                 ROW_NUMBER() OVER (ORDER BY p.replyno DESC) AS r
          FROM member m
          JOIN reply p ON m.memberno = p.memberno
          WHERE p.contentno = 21
    )
    WHERE r <= 500;

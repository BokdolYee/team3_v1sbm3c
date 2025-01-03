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




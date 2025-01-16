CREATE TABLE attachment (
    attachmentno    NUMBER(10)                 NOT NULL, -- 첨부파일 번호, 기본키
    postno          NUMBER(10)                 NOT NULL, -- 게시물 번호, 외래키
    upfile          VARCHAR(100)               NOT NULL, -- 업로드된 파일명
    thumb           VARCHAR(100)                       , -- 썸네일 이미지
    fname           VARCHAR(100)               NOT NULL, -- 원본 파일명
    fsize           NUMBER(10)                 NOT NULL, -- 파일 크기
    rdate           DATE                       NOT NULL, -- 등록일
    udate           DATE                               , -- 수정일
    visible         CHAR(1)      DEFAULT 'Y'   NOT NULL, -- 썸네일 이미지 표시 여부
    uuid            VARCHAR(40)                NOT NULL, -- 파일 고유 식별자
    
    PRIMARY KEY (attachmentno),
    FOREIGN KEY (postno) REFERENCES post_earning (postno)
);

COMMENT ON TABLE attachment is '첨부파일';
COMMENT ON COLUMN  attachment.attachmentno is '첨부파일 번호';
COMMENT ON COLUMN  attachment.postno is '게시물 번호';
COMMENT ON COLUMN  attachment.upfile is '업로드된 파일명';
COMMENT ON COLUMN  attachment.thumb is '썸네일 이미지';
COMMENT ON COLUMN  attachment.fname is '원본 파일명';
COMMENT ON COLUMN  attachment.fsize is '파일 크기';
COMMENT ON COLUMN  attachment.rdate is '등록일';
COMMENT ON COLUMN  attachment.udate is '수정일';
COMMENT ON COLUMN  attachment.visible is '썸네일 이미지 표시 여부';
COMMENT ON COLUMN  attachment.uuid is '파일 고유 식별자';

DROP SEQUENCE attachment_seq;

CREATE SEQUENCE attachment_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1              -- 증가값
  MAXVALUE 9999999999         -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                    -- 다시 1부터 생성되는 것을 방지
  
SELECT constraint_name 
FROM user_constraints 
WHERE table_name = 'ATTACHMENT' 
AND constraint_type = 'R';
  
ALTER TABLE attachment
DROP CONSTRAINT SYS_C008038;

ALTER TABLE attachment
ADD CONSTRAINT FK_ATTACHMENT
FOREIGN KEY(postno) REFERENCES post_earning(postno)
ON DELETE CASCADE;

COMMIT;

DROP TABLE attachment;  -- 테이블 삭제

SELECT * FROM attachment;   -- 모든 행 조회

DELETE FROM attachment; -- 모든 행 삭제


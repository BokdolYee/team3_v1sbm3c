DROP TABLE member;
DROP TABLE member CASCADE CONSTRAINTS;
DROP SEQUENCE member_seq;
COMMIT;

CREATE TABLE member (
  memberno   NUMBER(7)      NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼, 기본키 
  id         VARCHAR(30)    NOT NULL UNIQUE, -- 이메일(아이디), 중복 안됨, 레코드를 구분 
  passwd     VARCHAR(200)   NOT NULL, -- 비밀번호, 영숫자 조합, 암호화
  name       VARCHAR(30)    NOT NULL, -- 성명, 한글 10자 저장 가능
  birth      VARCHAR(30)    NOT NULL, -- 생년월일 ex) 20241205
  nickname   VARCHAR(36)    NOT NULL UNIQUE,
  tel        VARCHAR(14)    NOT NULL, -- 전화번호
  gender     VARCHAR(6)     NOT NULL, -- 성별, 남성 or 여성
  zipcode    VARCHAR(5)     NOT NULL, -- 우편번호, ex) 03153
  address    VARCHAR(80)        NULL, -- 상세 주소
  replycnt   NUMBER(6)      DEFAULT 0 NOT NULL, -- 작성한 댓글 수 가입 시 기본적으로 0
  rdate      DATE           NOT NULL, -- 가입일    
  grade      NUMBER(2)      NOT NULL, -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원)
  PRIMARY KEY (memberno)
);

COMMENT ON TABLE MEMBER is '회원';
COMMENT ON COLUMN MEMBER.MEMBERNO is '회원 번호';
COMMENT ON COLUMN MEMBER.ID is '아이디';
COMMENT ON COLUMN MEMBER.PASSWD is '패스워드';
COMMENT ON COLUMN MEMBER.NAME is '성명';
COMMENT ON COLUMN MEMBER.BIRTH is '생년월일';
COMMENT ON COLUMN MEMBER.NICKNAME is '닉네임';
COMMENT ON COLUMN MEMBER.TEL is '전화번호';
COMMENT ON COLUMN MEMBER.GENDER is '성별';
COMMENT ON COLUMN MEMBER.ZIPCODE is '우편번호';
COMMENT ON COLUMN MEMBER.ADDRESS is '상세 주소';
COMMENT ON COLUMN MEMBER.REPLYCNT is '댓글수';
COMMENT ON COLUMN MEMBER.RDATE is '가입일';
COMMENT ON COLUMN MEMBER.GRADE is '등급';

CREATE SEQUENCE member_seq
  START WITH 1             -- 시작 번호
  INCREMENT BY 1           -- 증가값
  MAXVALUE 9999999         -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                  -- 2번은 메모리에서만 계산
  NOCYCLE;                 -- 다시 1부터 생성되는 것을 방지

-- 관리자 계정 등록
INSERT INTO member(memberno, id, passwd, name, birth, nickname, 
                tel, gender, zipcode, address, replycnt, rdate, grade)
VALUES(member_seq.nextval, 'doom@gmail.com', 'fS/kjO+fuEKk06Zl7VYMhg==', '관리자', '20241205', 'doom', 
                '01012345678', '남성', '12345', '5층 512호', 0, sysdate, 1);
                
INSERT INTO member(memberno, id, passwd, name, birth, nickname, 
                tel, gender, zipcode, address, replycnt, rdate, grade)
VALUES(member_seq.nextval, 'admin', 'fS/kjO+fuEKk06Zl7VYMhg==', 'admin', '20241205', 'admin', 
                '01012345678', '남성', '12345', '5층 512호', 0, sysdate, 1);

SELECT * FROM member;
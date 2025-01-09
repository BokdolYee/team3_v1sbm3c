DROP TABLE calendargood;

CREATE TABLE calendargood (
    calendargoodno    NUMBER(10)        NOT NULL PRIMARY KEY,
    rdate            DATE        NOT NULL,
    calendarno        NUMBER(10)        NOT NULL,
    memberno        NUMBER(10)        NOT NULL,
    FOREIGN KEY (calendarno) REFERENCES calendar(calendarno),
    FOREIGN KEY (memberno) REFERENCES member(memberno)
);

SELECT constraint_name, delete_rule
FROM user_constraints
WHERE table_name = 'CALENDARGOOD' 
  AND constraint_type = 'R';  -- 외래 키 제약 조건만 조회
  
  ALTER TABLE calendargood
DROP CONSTRAINT SYS_C007791;

ALTER TABLE calendargood
DROP CONSTRAINT SYS_C007792;

ALTER TABLE calendargood
ADD CONSTRAINT fk_calendargood_calendar
FOREIGN KEY (calendarno)
REFERENCES calendar (calendarno)
ON DELETE CASCADE;



DROP SEQUENCE calendargood_seq;

CREATE SEQUENCE calendargood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지


-- 데이터 삽입
INSERT INTO calendargood(calendargoodno, rdate, calendarno, memberno)
VALUES (calendargood_seq.nextval, sysdate, 1, 2);

INSERT INTO calendargood(calendargoodno, rdate, calendarno, memberno)
VALUES (calendargood_seq.nextval, sysdate, 2, 3);

INSERT INTO calendargood(calendargoodno, rdate, calendarno, memberno)
VALUES (calendargood_seq.nextval, sysdate, 1, 4);

INSERT INTO calendargood(calendargoodno, rdate, calendarno, memberno)
VALUES (calendargood_seq.nextval, sysdate, 5, 3);

COMMIT;

-- 전체 목록
SELECT calendargoodno, rdate, calendarno, memberno
FROM calendargood
ORDER BY calendargoodno DESC;

calendarGOODNO RDATE               calendarNO   MEMBERNO
-------------- ------------------- ---------- ----------
             5 2025-01-07 10:51:32          3          5
             3 2025-01-07 10:50:51          1          4
             2 2025-01-07 10:50:34          1          3
             1 2025-01-07 10:50:17          1          1

-- 조회
SELECT calendargoodno, rdate, calendarno, memberno
FROM calendargood
WHERE calendargoodno = 3;

-- 캘린더, 멤버 다
SELECT calendargoodno, rdate, calendarno, memberno
FROM calendargood
WHERE calendargoodno = 3 AND memberno =3;

-- 삭제
DELETE FROM calendargood
WHERE calendargoodno = 4;

commit;

SELECT COUNT(*) as cnt
FROM calendargood
WHERE calendarno=2 AND memberno=3;

       CNT
----------
         1 <-- 이미 추천을 함
         
SELECT COUNT(*) as cnt
FROM calendargood
WHERE calendarno=2 AND memberno=5;

       CNT
----------
         0 <-- 추천 안됨
         
-- 추천
UPDATE calendar
SET recom = recom + 1
WHERE calendarno = 1;

-- 비추천
UPDATE calendar
SET recom = recom - 1
WHERE calendarno = 1;

ALTER TABLE calendargood
ADD cnt NUMBER(7) DEFAULT 0 NOT NULL;

--join, 어느 설문을 누가 추천 했는가?
SELECT calendargoodno, rdate, calendarno, memberno
FROM calendargood
ORDER BY calendargoodno DESC;

-- 테이블 2개 join 
SELECT r.calendargoodno, r.rdate, r.calendarno, c.title, r.memberno
FROM calendar c, calendargood r
WHERE c.calendarno = r.calendarno
ORDER BY calendargoodno DESC;

-- 테이블 3개 join 
SELECT r.calendargoodno, r.rdate, r.calendarno, c.title as c_title, r.memberno, m.id, m.name as m_name
FROM calendar c, calendargood r, member m
WHERE c.calendarno = r.calendarno AND r.calendarno = m.memberno
ORDER BY calendargoodno DESC;

-- 기존 외래 키 제약 조건이 있는지 확인하고, 있으면 삭제
ALTER TABLE calendargood
DROP CONSTRAINT fk_calendargood_calendarno;

-- 외래 키 제약 조건에 ON DELETE CASCADE 추가
ALTER TABLE calendargood
ADD CONSTRAINT fk_calendargood_calendarno
FOREIGN KEY (calendarno)
REFERENCES calendar(calendarno)
ON DELETE CASCADE;

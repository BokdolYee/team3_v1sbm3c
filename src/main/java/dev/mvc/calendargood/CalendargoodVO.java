package dev.mvc.calendargood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE calendargood (
//    calendargoodno  NUMBER(10)    NOT NULL PRIMARY KEY,
//    rdate         DATE    NOT NULL,
//    calendarno      NUMBER(10)    NOT NULL,
//    memberno      NUMBER(10)    NOT NULL,
//      FOREIGN KEY (calendarno) REFERENCES calendar(calendarno),
//      FOREIGN KEY (memberno) REFERENCES member(memberno)
//  );

@Getter @Setter @ToString
public class CalendargoodVO {
  /** 컨텐츠 추천 번호 */
  private int calendargoodno;
  
  /** 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int calendarno;
  
  /** 회원 번호 */
  private int memberno;
  
  private int cnt;
  
}
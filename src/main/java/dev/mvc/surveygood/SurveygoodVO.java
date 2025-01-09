package dev.mvc.surveygood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE surveygood (
//    surveygoodno  NUMBER(10)  NOT NULL PRIMARY KEY,
//    rdate       DATE  NOT NULL,
//    surveyno    NUMBER(10) NOT NULL,
//    memberno      NUMBER(10) NOT NULL,
//    FOREIGN KEY(surveyno) REFERENCES survey(surveyno),
//    FOREIGN KEY(memberno) REFERENCES member(memberno)
//    
//);

@Getter @Setter @ToString
public class SurveygoodVO{
  
  /** 설문조사 추천 번호 */
  private int surveygoodno;
  
  /** 등록 날짜 */
  private String rdate;
  
  /** 설문조사 번호 */
  private int surveyno;
  
  /** 회원 번호 */
  private int memberno;
  
 
  
}
package dev.mvc.surveymember;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//surveymemberno                NUMBER(10) NOT NULL PRIMARY KEY,
//rdate                          DATE NOT NULL,
//surveyitemno                  NUMBER(10) NULL ,
//memberno                            NUMBER(6) NULL ,
//  FOREIGN KEY (surveyitemno) REFERENCES surveyitem (surveyitemno),
//  FOREIGN KEY (memberno) REFERENCES MEMBER (memberno)
//);

@Getter @Setter @ToString
public class SurveymemberVO{
  
  /** 설문조사 추천 번호 */
  private int surveymemberno;
  
  /** 등록 날짜 */
  private String rdate;
  
  /** 설문조사 번호 */
  private int surveyitemno;
  
  /** 회원 번호 */
  private int memberno;
  
 
  
}
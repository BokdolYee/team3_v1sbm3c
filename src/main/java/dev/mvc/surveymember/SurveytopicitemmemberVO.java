package dev.mvc.surveymember;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//SELECT r.surveymemberno, r.rdate, r.surveyitemno, c.title, r.memberno, m.id, m.name, i.item, s.topic
//FROM surveytopic c, surveymember r, member m, surveyitem i, survey s
//WHERE i.surveyitemno = r.surveyitemno AND r.memberno = m.memberno
//ORDER BY r.surveymemberno DESC;

@Getter @Setter @ToString
public class SurveytopicitemmemberVO{
  
  /** 설문조사 추천 번호 */
  private int surveymemberno;
  
  /** 등록 날짜 */
  private String rdate;
  
  /** 설문조사 번호 */
  private int surveyitemno;
  
  /** 제목 */
  private String topic= "";
  
  private String item= "";
  
  private String title= "";
  
  /** 회원 번호 */
  private int memberno;
  
  /** 아이디 */
  private String id = "";
  
  /** 회원 성명 */
  private String name = "";
  
}
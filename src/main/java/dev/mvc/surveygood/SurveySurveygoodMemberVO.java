package dev.mvc.surveygood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//SELECT r.surveygoodno, r.rdate, r.surveyno, c.topic, r.memberno, m.id, m.name
//FROM survey c, surveygood r, member m
//Where c.surveyno= r.surveyno AND r.memberno = m.memberno
//ORDER BY surveygoodno DESC;

@Getter @Setter @ToString
public class SurveySurveygoodMemberVO{
  
  /** 설문조사 추천 번호 */
  private int surveygoodno;
  
  /** 등록 날짜 */
  private String rdate;
  
  /** 설문조사 번호 */
  private int surveyno;
  
  /** 제목 */
  private String c_topic= "";
  
  /** 회원 번호 */
  private int memberno;
  
  /** 아이디 */
  private String id = "";
  
  /** 회원 성명 */
  private String name = "";
  
}
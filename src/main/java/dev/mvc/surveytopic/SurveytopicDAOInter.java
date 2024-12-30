package dev.mvc.surveytopic;

import java.util.ArrayList;
import java.util.Map;





public interface SurveytopicDAOInter {

  // 등록
  public int create(SurveytopicVO surveytopicVO);

  // 조회
  public SurveytopicVO read(int surveytopicno);

  // 수정
  public int update(SurveytopicVO surveytopicVO);

  // 삭제
  public int delete(int surveytopicno);
  
  /**
   * 설문조사별 설문조사 개별 문제 목록
   * @param surveyno
   * @return
   */
  
  ArrayList<SurveytopicVO> listBySurveyno(int surveyno);

  /**
   * 우선순위 상승
   * @param newscateno 카테고리 번호
   * @return 수정된 레코드 개수
   */
  public int update_seqno_forward(int surveyno);

  /**
   * 우선순위 하락
   * @param newscateno 카테고리 번호
   * @return 수정된 레코드 개수
   */
  public int update_seqno_backward(int surveyno);

  
  
  
}

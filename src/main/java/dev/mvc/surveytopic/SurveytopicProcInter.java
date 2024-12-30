package dev.mvc.surveytopic;

import java.util.ArrayList;

import dev.mvc.surveytopic.SurveytopicVO;






public interface SurveytopicProcInter {
  /**
   * 등록
   * @param surveyVO
   * @return
   */
  public int create(SurveytopicVO surveytopicVO);
  /**
   * 조회
   * @param surveyVO
   * @return
   */
  public SurveytopicVO read(int surveytopicno);
  /**
   * 수정
   * @param surveyVO
   * @return
   */
  public int update(SurveytopicVO surveytopicVO);
  /**
   * 삭제
   * @param surveyVO
   * @return
   */
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

  




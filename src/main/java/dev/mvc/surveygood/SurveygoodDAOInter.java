package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveygoodDAOInter {
  /**
   * 등록, 추상 메소드
   * @param calendarVO
   * @return
   */
  public int create(SurveygoodVO surveygoodVO);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<SurveygoodVO> list_all();
  
  /**
   * 삭제
   * @param surveygoodno
   * @return
   */
  public int delete(int surveygoodno);
  
  /**
   *  특정 설문조사의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int hartCnt(HashMap<String, Object> map);
  
  /**
   * 조회
   * @param surveygoodno
   * @return
   */
  public SurveygoodVO read(int surveygoodno);
  
  /**
   * 또다른조화
   * @param map
   * @return
   */
  public SurveygoodVO readBySurveynoMemberno(HashMap<String, Object> map);
  
  /**
   * 테이블 3개 조인
   * @return
   */
  public ArrayList<SurveySurveygoodMemberVO> list_all_join();
}


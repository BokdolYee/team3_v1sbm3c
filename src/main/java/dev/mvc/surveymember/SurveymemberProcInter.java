package dev.mvc.surveymember;

import java.util.ArrayList;
import java.util.HashMap;


import dev.mvc.survey.SurveyVO;

public interface SurveymemberProcInter {
  /**
   * 등록, 추상 메소드
   * @param calendarVO
   * @return
   */
  public int create(SurveymemberVO surveymemberVO);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<SurveymemberVO> list_all();
  
  /**
   * 삭제
   * @param surveygoodno
   * @return
   */
  public int delete(int surveymemberno);
  

  public SurveymemberVO read(int surveymemberno);
  
  
//  /**
//   * 또다른조화
//   * @param map
//   * @return
//   */
//  public SurveymemberVO readBySurveynoMemberno(HashMap<String, Object> map);
//  
  
  /**
   * 테이블 3개 join
   * @return
   */
  public ArrayList<SurveytopicitemmemberVO> list_all_join();
}

  

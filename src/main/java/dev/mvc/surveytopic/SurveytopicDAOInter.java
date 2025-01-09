package dev.mvc.surveytopic;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.survey.SurveyVO;
import dev.mvc.surveygood.SurveySurveygoodMemberVO;





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
   * title별 등록된 설문조사 목록
   * @param surveyno
   * @return
   */
  public ArrayList<SurveytopicVO> list_all();
  
  /**
   * 설문조사별 설문조사 개별 문제 목록
   * @param surveyno
   * @return
   */
  
  ArrayList<SurveytopicVO> listBySurveyno(int surveyno);

  // 검색 개수
  public Integer list_search_count(String word);

  // 검색 목록
  public ArrayList<SurveySurveytopicVO> list_search(String word);

  // 페이징 목록
  public ArrayList<SurveySurveytopicVO> list_paging(Map<String, Object> map);

  // 검색 + 페이징
  public ArrayList<SurveySurveytopicVO> list_search_paging(Map<String, Object> map);

  /**
   * 테이블 3개 조인
   * @return
   */
  public ArrayList<SurveySurveytopicVO> list_search_paging_join();
  
  
  
}

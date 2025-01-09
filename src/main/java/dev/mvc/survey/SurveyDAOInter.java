package dev.mvc.survey;

import java.util.ArrayList;
import java.util.Map;





public interface SurveyDAOInter {

  // 등록
  public int create(SurveyVO surveyVO);

  // 조회
  public SurveyVO read(int surveyno);

  // 수정
  public int update(SurveyVO surveyVO);

  // 삭제
  public int delete(int surveyno);

  // 전체 목록
  public ArrayList<SurveyVO> list_all();

  // 검색 개수
  public Integer list_search_count(String word);

  // 검색 목록
  public ArrayList<SurveyVO> list_search(String word);

  // 페이징 목록
  public ArrayList<SurveyVO> list_paging(Map<String, Object> map);

  // 검색 + 페이징
  public ArrayList<SurveyVO> list_search_paging(Map<String, Object> map);

  // 공개 설정
  public int update_visible_y(int surveyno);

  // 비공개 설정
  public int update_visible_n(int surveyno);

  public int increaseCnt(int surveyno);
  
  // 추천수 증가
  public int increaseRecom(int surveyno);
  
  // 추천수 감소
  public int decreaseRecom(int surveyno);
  
  public int deleteSurveygood(int surveyno);
  
  public int good(int surveyno);
}

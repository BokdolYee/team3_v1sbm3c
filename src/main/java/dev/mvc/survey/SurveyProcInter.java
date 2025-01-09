package dev.mvc.survey;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.newscate.NewsCateVO;
import dev.mvc.newscate.NewsCateVOMenu;



public interface SurveyProcInter {
  /**
   * 등록
   * @param surveyVO
   * @return
   */
  public int create(SurveyVO surveyVO);
  /**
   * 조회
   * @param surveyVO
   * @return
   */
  public SurveyVO read(int surveyno);
  /**
   * 수정
   * @param surveyVO
   * @return
   */
  public int update(SurveyVO surveyVO);
  /**
   * 삭제
   * @param surveyVO
   * @return
   */
  public int delete(int surveyno);
  /**
   * title별 등록된 설문조사 목록
   * @param surveyno
   * @return
   */
  public ArrayList<SurveyVO> list_all();
  
  /**
   * 검색 갯수
   * @param word
   * @return
   */
  public Integer list_search_count(String word);
  
  /**
   * 검색 목록 
   * @return
   */
  public ArrayList<SurveyVO> list_search(String word);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  /**
   * 페이징 목록
   * select id="list_paging" resultType="dev.mvc.survey.SurveyVO" parameterType="Map"
   * @param map
   * @return
   */
  public ArrayList<SurveyVO> list_paging(String word, int now_page, int record_per_page);
  
  public int update_visible_n(int surveyno);
  
  public int update_visible_y(int surveyno);
  
  public ArrayList<SurveyVO> list_search_paging(String word, int now_page, int record_per_page);
  
  public int increaseCnt(int surveyno);
  
//추천수 증가
 public int increaseRecom(int surveyno);
 
 // 추천수 감소
 public int decreaseRecom(int surveyno);
 
 public int deleteSurveygood(int surveyno);
 
 public int good(int surveyno);
}

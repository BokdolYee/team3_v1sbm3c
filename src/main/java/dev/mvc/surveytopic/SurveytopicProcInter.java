package dev.mvc.surveytopic;

import java.util.ArrayList;

import dev.mvc.survey.SurveyVO;






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
  public ArrayList<SurveySurveytopicVO> list_search(String word);
  
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
  public ArrayList<SurveySurveytopicVO> list_paging(String word, int now_page, int record_per_page);
   
  
  
  public ArrayList<SurveySurveytopicVO> list_search_paging_join(String word, int now_page, int record_per_page);
}

  




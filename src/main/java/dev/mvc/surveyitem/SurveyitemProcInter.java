package dev.mvc.surveyitem;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.mvc.surveymember.SurveytopicitemmemberVO;
import dev.mvc.surveytopic.SurveytopicVO;

public interface SurveyitemProcInter {
  // 등록
  public int create(SurveyitemVO surveyitemVO);

  // 조회
  public SurveyitemVO read(int surveyitemno);

  // 수정
  public int update(SurveyitemVO surveyitemVO);

  // 삭제
  public int delete(int surveyitemno);
  
  // 선택 인원 수 증가
  public int increaseItemCount(String item);
  
  // 하나의 설문조사에 대한 개별문제 리스트 출력 
  public ArrayList<SurveyitemVO>listBySurveytopicno(int surveytopicno);
  
  public ArrayList<SurveytopicitemVO> list_all();
  
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
  public ArrayList<SurveytopicitemVO> list_search(String word);
  
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
  public ArrayList<SurveytopicitemVO> list_paging(String word, int now_page, int record_per_page);
  
  public ArrayList<SurveytopicitemVO> list_search_paging(String word, int now_page, int record_per_page);



 
  


}


package dev.mvc.newscate;

import java.util.ArrayList;

import dev.mvc.newscate.NewsCateVOMenu;


public interface NewsCateProcInter {
  
  public int create(NewsCateVO newscateVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<NewsCateVO> list_all();
  
  public NewsCateVO read(Integer newscateno);
  
  /**
   * 수정
   * @param movieVO 수정할내용
   * @return 수정된 레코드 갯수
   */
  public int update(NewsCateVO newscateVO);
  
  /**
   * 삭제
   * @param Moviecateno 삭제할내용 PK
   * @return 삭제된 레코드 갯수
   */
  public int delete(int newscateno);
  
  public int deleteContents(int newscateno);
  
  /**
   * <!--우선 순위 높임, 10 등 -> 1 등-->
   * @param moviecateno
   * @return
   */
  public int update_seqno_forward(int newscateno);
  
  /**
   *  <!--우선 순위 낮춤, 1 등 -> 10 등-->
   * @param moviecateno
   * @return
   */
  public int update_seqno_backward(int newscateno);
  
  /**
   * <!-- 카테고리 공개 설정 -->
   * @param moviecateno
   * @return
   */
  public int update_visible_y(int newscateno);
  
  /**
   * <!-- 카테고리 공개 설정 -->
   * @param moviecateno
   * @return
   */
  public int update_visible_n(int newscateno);
  
  ArrayList<NewsCateVO> list_all_categrp_y();
  
  ArrayList<NewsCateVO> list_all_cate_y(String genre);
  
  /**
   * 화면 상단 메뉴
   * @return
   */
  public ArrayList<NewsCateVOMenu> menu();
  
  /**
   * 장르 목록
   * @return
   */
  public ArrayList<String> genreset();
  
  public ArrayList<NewsCateVO> list_search(String word);
  
  /**
   * 검색 갯수
   * @param word
   * @return
   */
  public Integer list_search_count(String word);
  
  /**
   * 검색 + 페이징 목록
   * select id="list_search_paging" resultType="dev.mvc.cate.CateVO" parameterType="Map" 
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<NewsCateVO> list_search_paging(String word, int now_page, int record_per_page);

  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block);

}


package dev.mvc.newscate;

import java.util.ArrayList;
import java.util.Map;

public interface NewsCateDAOInter {
  
  /**
   * 카테고리 생성
   * @param newscateVO 생성할 카테고리 정보
   * @return 생성된 레코드 개수
   */
  public int create(NewsCateVO newscateVO);

  /**
   * 전체 목록 조회
   * @return 카테고리 목록
   */
  public ArrayList<NewsCateVO> list_all();

  /**
   * 특정 카테고리 조회
   * @param newscateno 카테고리 번호
   * @return 카테고리 정보
   */
  public NewsCateVO read(Integer newscateno);

  /**
   * 카테고리 수정
   * @param newscateVO 수정할 카테고리 정보
   * @return 수정된 레코드 개수
   */
  public int update(NewsCateVO newscateVO);

  /**
   * 카테고리 삭제
   * @param newscateno 삭제할 카테고리 번호
   * @return 삭제된 레코드 개수
   */
  public int delete(int newscateno);

  /**
   * 콘텐츠 삭제 (한국 관련)
   * @param newscateno 카테고리 번호
   * @return 삭제된 콘텐츠 레코드 개수
   */
  public int deleteContents(int newscateno);

  /**
   * 우선순위 상승
   * @param newscateno 카테고리 번호
   * @return 수정된 레코드 개수
   */
  public int update_seqno_forward(int newscateno);

  /**
   * 우선순위 하락
   * @param newscateno 카테고리 번호
   * @return 수정된 레코드 개수
   */
  public int update_seqno_backward(int newscateno);

  /**
   * 카테고리 공개 설정
   * @param newscateno 카테고리 번호
   * @return 수정된 레코드 개수
   */
  public int update_visible_y(int newscateno);

  /**
   * 카테고리 비공개 설정
   * @param newscateno 카테고리 번호
   * @return 수정된 레코드 개수
   */
  public int update_visible_n(int newscateno);

  /**
   * 숨김 처리되지 않은 카테고리 그룹 목록 조회
   * @return 공개된 카테고리 그룹 목록
   */
  public ArrayList<NewsCateVO> list_all_categrp_y();

  /**
   * 특정 장르에 대한 공개된 카테고리 목록 조회
   * @param genre 장르명
   * @return 공개된 카테고리 목록
   */
  public ArrayList<NewsCateVO> list_all_cate_y(String genre);

  /**
   * 장르 목록 조회
   * @return 장르 목록
   */
  public ArrayList<String> genreset();

  /**
   * 검색 조건에 따른 카테고리 목록 조회
   * @param word 검색어
   * @return 검색된 카테고리 목록
   */
  public ArrayList<NewsCateVO> list_search(String word);

  /**
   * 검색 조건에 따른 카테고리 개수 조회
   * @param word 검색어
   * @return 검색된 카테고리 개수
   */
  public Integer list_search_count(String word);

  /**
   * 검색 및 페이징된 카테고리 목록 조회
   * @param map 페이징 및 검색 정보 (start_num, end_num, word)
   * @return 검색 및 페이징된 카테고리 목록
   */
  public ArrayList<NewsCateVO> list_search_paging(Map<String, Object> map);
}

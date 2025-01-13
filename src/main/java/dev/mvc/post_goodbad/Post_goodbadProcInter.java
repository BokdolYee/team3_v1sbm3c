package dev.mvc.post_goodbad;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.dto.SearchDTO;

public interface Post_goodbadProcInter {
  
  /**
   * 추천 비추천 등록
   * @param post_goodbadVO
   * @return
   */
  public int create(Post_goodbadVO post_goodbadVO);
  
  /**
   * 회원이 해당 게시물에 추천 or 비추천 했는지 조회
   * @param map
   * @return
   */
  public int check_cnt(HashMap<String, Object>map);
  
  /**
   * 추천 혹은 비추천을 했으면 추천인지 비추천인지 조회
   * @param map
   * @return
   */
  public String check_goodbad(HashMap<String, Object>map);
  
  /**
   * 추천수 증가
   * @param postno
   * @return
   */
  public int increase_goodcnt(int postno);
  
  /**
   * 비추천수 증가
   * @param postno
   * @return
   */
  public int increase_badcnt(int postno);
  
  /**
   * 추천, 비추천 검색 + 페이징 목록
   * @param searchDTO
   * @return
   */
  public ArrayList<Post_goodbadVO> list_search_paging(SearchDTO searchDTO);
  
  /**
   * 검색 조건에 맞는 총 추천 비추천 내역 수
   * @param searchDTO
   * @return
   */
  public int list_search_count(SearchDTO searchDTO);
}

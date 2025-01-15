package dev.mvc.post_viewlog;

import java.util.ArrayList;

import dev.mvc.dto.SearchDTO;

public interface Post_viewlogProcInter {
  
  /**
   * 등록
   * @param post_viewlogVO
   * @return
   */
  public int create(Post_viewlogVO post_viewlogVO);
  
  /**
   * 조회한 적이 있는지 검사
   * @return
   */
  public int check(int postno);
  
  /**
   * 조회할 때마다 조회수 증가
   * @param postno
   * @return
   */
  public int increase_viewcnt(int postno);
  
  /**
   * 조회할 때마다 마지막 조회일 변경
   * @param postno
   * @return
   */
  public int update_ldate(int postno);
  
  /**
   * 조회 내역 검색 + 페이징 목록
   * @param searchDTO
   * @return
   */
  public ArrayList<Post_viewlogVO> list(SearchDTO searchDTO);
  
  /**
   * 검색 조건에 맞는 총 내역 수
   * @param searchDTO
   * @return
   */
  public int list_count(SearchDTO searchDTO);
}

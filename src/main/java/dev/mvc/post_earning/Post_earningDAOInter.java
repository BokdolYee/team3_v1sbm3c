package dev.mvc.post_earning;

import java.util.ArrayList;

import dev.mvc.dto.SearchDTO;

/**
 * Spring Boot가 자동으로 구현
 * @author soldesk
 *
 */
public interface Post_earningDAOInter {
  
  /**
   * 등록
   * @param post_earningVO
   * @return
   */
  public int create(Post_earningVO post_earningVO);
  
  /**
   * 게시물 조회(클릭해서 읽기)
   * @param postno
   * @return
   */
  public Post_earningVO read_join_nickname(int postno);
  
  /**
   * 조회수 증가
   * @param postno
   * @return
   */
  public int increase_viewcnt(int postno);
  
  /**
   * 게시물 검색 + 페이징 목록
   * @param searchDTO 검색 조건 및 페이징 정보
   * @return
   */
  public ArrayList<Post_earningVO> list_by_postno_search_paging(SearchDTO searchDTO);

  /**
   * 검색 조건에 맞는 총 게시물 수
   * @param searchDTO 검색 조건(searchType, keyword)
   * @return
   */
  public int list_by_postno_search_count(SearchDTO searchDTO);
  
  /**
   * 게시물 수정(제목, 글내용)
   * @param post_earningVO
   * @return
   */
  public int update_text(Post_earningVO post_earningVO);
  
  /**
   * 게시물 삭제
   * @param postno
   * @return
   */
  public int delete(int postno);
}

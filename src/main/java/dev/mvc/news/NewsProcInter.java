package dev.mvc.news;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.news.NewsVO;

public interface NewsProcInter {
  
  /**
   * 등록
   * @param newsVO
   * @return
   */
  public int create(NewsVO newsVO);

  /**
   * 조회
   * @param newsno
   * @return
   */
  public NewsVO read(Integer newsno);

  /**
   * 수정
   * @param newsVO
   * @return
   */
  public int update(NewsVO newsVO);

  /**
   * 삭제
   * @param newsno
   * @return
   */
  public int delete(int newsno);
  
  /**
   * 검색 목록
   * @param word
   * @return
   */
  public ArrayList<NewsVO> search(String keyword);

  /**
   * 검색 + 페이징 목록
   * @param word
   * @param now_page
   * @param record_per_page
   * @return
   */
  public ArrayList<NewsVO> listPaging(Map<String, Object> param); 
}
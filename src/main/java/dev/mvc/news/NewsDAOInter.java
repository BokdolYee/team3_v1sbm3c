package dev.mvc.news;

import java.util.ArrayList;
import java.util.Map;

public interface NewsDAOInter {
  
  NewsVO read(int newsno);
  int update(NewsVO newsVO);
  
  /**
   * <pre>
   * MyBATIS: insert id="create" parameterType="dev.mvc.news.newsVO"
   * insert: int를 리턴, 등록한 레코드 갯수를 리턴
   * id="create": 메소드명으로 사용
   * parameterType="dev.mvc.news.newsVO": 메소드의 파라미터
   * Spring Boot가 자동으로 구현
   * </pre>
   * @param newsVO
   * @return
   */
  public int create(NewsVO newsVO);


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
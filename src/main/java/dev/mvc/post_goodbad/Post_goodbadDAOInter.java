package dev.mvc.post_goodbad;

import java.util.HashMap;

public interface Post_goodbadDAOInter {
  
  /**
   * 추천 비추천 등록
   * @param post_goodbadVO
   * @return
   */
  public int create(Post_goodbadVO post_goodbadVO);
  
  /**
   * 추천 비추천 했는지 여부 검사
   * @param map
   * @return
   */
  public String check(HashMap<Integer, Integer>map);
  
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
}

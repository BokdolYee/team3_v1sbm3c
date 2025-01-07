package dev.mvc.post_goodbad;

public interface Post_goodbadDAOInter {
  
  /**
   * 추천 비추천 등록
   * @param post_goodbadVO
   * @return
   */
  public int create(Post_goodbadVO post_goodbadVO);
}

package dev.mvc.post_earning;

import dev.mvc.post_earning.Post_earningVO;
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

}

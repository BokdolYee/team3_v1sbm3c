package dev.mvc.post_goodbad;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.post_goodbad.Post_goodbadProc")
public class Post_goodbadProc implements Post_goodbadProcInter{
  
  @Autowired
  Post_goodbadDAOInter post_goodbadDAO;
  
  public Post_goodbadProc() {
    System.out.println(" -> Post_goodbadProc 생성됨");
  }

  /**
   * 추천 비추천 등록
   */
  @Override
  public int create(Post_goodbadVO post_goodbadVO) {
    int cnt = this.post_goodbadDAO.create(post_goodbadVO);
    
    return cnt;
  }

  /**
   * 추천 비추천 여부 검사
   */
  @Override
  public String check(HashMap<Integer, Integer> map) {
    String goodbad = this.post_goodbadDAO.check(map);
    
    return goodbad;
  }

  @Override
  public int increase_goodcnt(int postno) {
    int cnt = this.post_goodbadDAO.increase_goodcnt(postno);
    
    return cnt;
  }

  @Override
  public int increase_badcnt(int postno) {
    int cnt = this.post_goodbadDAO.increase_badcnt(postno);
    
    return cnt;
  }
  
}

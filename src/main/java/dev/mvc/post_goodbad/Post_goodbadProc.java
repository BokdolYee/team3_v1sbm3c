package dev.mvc.post_goodbad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.post_goodbad.Post_goodbadProc")
public class Post_goodbadProc implements Post_goodbadDAOInter{
  
  @Autowired
  Post_goodbadDAOInter post_goodbadDAO;
  
  public Post_goodbadProc() {
    System.out.println(" -> Post_goodbadProc 생성됨");
  }

  @Override
  public int create(Post_goodbadVO post_goodbadVO) {
    int cnt = this.post_goodbadDAO.create(post_goodbadVO);
    
    return cnt;
  }
  
}

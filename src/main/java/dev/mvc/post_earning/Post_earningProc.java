package dev.mvc.post_earning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.post_earning.Post_earningProc")
public class Post_earningProc implements Post_earningProcInter{
  
  //Post_earningDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당
  @Autowired Post_earningDAOInter post_earningDAO;

  @Override
  public int create(Post_earningVO post_earningVO) {
    int cnt = this.post_earningDAO.create(post_earningVO);
    
    return cnt;
  }
  
  
}

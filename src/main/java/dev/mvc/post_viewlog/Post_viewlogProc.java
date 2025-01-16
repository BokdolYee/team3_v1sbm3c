package dev.mvc.post_viewlog;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.dto.SearchDTO;

@Component("dev.mvc.post_viewlog.Post_viewlogProc")
public class Post_viewlogProc implements Post_viewlogProcInter{

  //Post_viewlogDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당
  @Autowired 
  Post_viewlogDAOInter post_viewlogDAO;
  
  public Post_viewlogProc() {
    System.out.println(" -> Post_viewlogProc 생성됨");
  }

  /**
   * 조회 내역 등록
   */
  @Override
  public int create(Post_viewlogVO post_viewlogVO) {
    int cnt = this.post_viewlogDAO.create(post_viewlogVO);
    
    return cnt;
  }

  /**
   * 조회한 적이 있는지 검사
   */
  @Override
  public int check(int postno) {
    int cnt = this.post_viewlogDAO.check(postno);
    
    return cnt;
  }

  /**
   * 조회할 때마다 조회수 증가
   */
  @Override
  public int increase_viewcnt(int postno) {
    int cnt = this.post_viewlogDAO.increase_viewcnt(postno);
    
    return cnt;
  }

  @Override
  public int update_ldate(int postno) {
    int cnt = this.post_viewlogDAO.update_ldate(postno);
    
    return cnt;
  }

  /**
   * 조회 내역 검색 + 목록 페이징
   */
  @Override
  public ArrayList<Post_viewlogVO> list(SearchDTO searchDTO) {
    return this.post_viewlogDAO.list(searchDTO);
  }

  /**
   * 조건에 맞는 내역 수
   */
  @Override
  public int list_count(SearchDTO searchDTO) {
    return this.post_viewlogDAO.list_count(searchDTO);
  }

  
  
}

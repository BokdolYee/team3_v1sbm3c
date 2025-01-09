package dev.mvc.post_earning;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.dto.SearchDTO;

@Component("dev.mvc.post_earning.Post_earningProc")
public class Post_earningProc implements Post_earningProcInter{
  
  //Post_earningDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당
  @Autowired 
  Post_earningDAOInter post_earningDAO;
  
  public Post_earningProc() {
    System.out.println(" -> Post_earningProc 생성됨");
  }

  /**
   * 게시물 등록
   */
  @Override
  public int create(Post_earningVO post_earningVO) {
    int cnt = this.post_earningDAO.create(post_earningVO);
    
    return cnt;
  }
  
  /**
   * 게시물 조회
   */
  @Override
  public Post_earningVO read_join_nickname(int postno) {
    Post_earningVO post_earningVO = this.post_earningDAO.read_join_nickname(postno);
    
    return post_earningVO;
  }
  
  /**
   * 조회수 증가
   */
  @Override
  public int increase_viewcnt(int postno) {
    int cnt = this.post_earningDAO.increase_viewcnt(postno);
    
    return cnt;
  }

  /**
   * 게시물 검색 + 목록 페이징
   */
  @Override
  public ArrayList<Post_earningVO> list_by_postno_search_paging(SearchDTO searchDTO) {
    return this.post_earningDAO.list_by_postno_search_paging(searchDTO);
  }

  /**
   * 조건에 맞는 게시물 수
   */
  @Override
  public int list_by_postno_search_count(SearchDTO searchDTO) {
    return this.post_earningDAO.list_by_postno_search_count(searchDTO);
  }

  /**
   * 게시물 수정(제목, 글내용)
   */
  @Override
  public int update_text(Post_earningVO post_earningVO) {
    int cnt = this.post_earningDAO.update_text(post_earningVO);
    
    return cnt;
  }

  @Override
  public int delete(int postno) {
    int cnt = this.post_earningDAO.delete(postno);
    
    return cnt;
  }
  
  
}

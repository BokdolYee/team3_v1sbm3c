package dev.mvc.loginlog;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.dto.SearchDTO;

@Component("dev.mvc.loginlog.LoginlogProc")
public class LoginlogProc implements LoginlogProcInter {

  @Autowired
  private LoginlogProc loginlogDAO;
  
  public LoginlogProc(){
    System.out.println("-> LoginlogProc 생성됨.");
  }
  
  /**
   * 로그인 로그 삽입
   */
  @Override
  public int login_log(LoginlogVO loginlogVO) {
    int cnt = this.loginlogDAO.login_log(loginlogVO);
    return cnt;
  }

  /**
   * 조건에 맞는 로그인 로그 수
   */
  @Override
  public int list_search_count(SearchDTO searchDTO) {
    return this.loginlogDAO.list_search_count(searchDTO);
  }

  /**
   * 로그인 로그 검색 + 페이징
   */
  @Override
  public ArrayList<LoginlogVO> list_search_paging(SearchDTO searchDTO) {
    return this.loginlogDAO.list_search_paging(searchDTO);
  }

  /**
   * 로그인 로그 조회
   */
  @Override
  public LoginlogVO read(int loginlogno) {
    LoginlogVO loginlogVO = this.loginlogDAO.read(loginlogno);
    return loginlogVO;
  }

  /**
   * 로그인 로그 삭제
   */
  @Override
  public int delete(int loginlogno) {
    int cnt = this.loginlogDAO.delete(loginlogno);
    return cnt;
  }

  
}

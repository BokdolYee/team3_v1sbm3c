package dev.mvc.loginlog;

import java.util.ArrayList;

import dev.mvc.dto.SearchDTO;

public interface LoginlogProcInter {
  
  /**
   * 로그인 시도 기록 삽입
   * @param loginlogVO
   * @return
   */
  public int login_log(LoginlogVO loginlogVO);
  
  /**
   * 검색 조건에 맞는 총 로그인 로그 수
   * @param searchDTO 검색 조건(searchType, keyword)
   * @return 총 회원 수
   */
  public int list_search_count(SearchDTO searchDTO);
  
  /**
   * 로그인 로그 검색 + 페이징 목록
   * searchDTO 검색 조건 및 페이징 정보
   * @return 로그인 로그 목록
   */
  public ArrayList<LoginlogVO> list_search_paging(SearchDTO searchDTO);
  
  /**
   * loginlogno로 로그인 로그 정보 조회
   * @param loginlogno
   * @return
   */
  public LoginlogVO read(int loginlogno);
  
  /**
   * loginlogno로 로그인 로그 삭제
   * @param loginlogno
   * @return
   */
  public int delete(int loginlogno);

}

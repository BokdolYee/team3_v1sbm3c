package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.dto.SearchDTO;
import jakarta.servlet.http.HttpSession;

public interface MemberProcInter {
  /**
   * 아이디 중복 검사
   * @param id
   * @return 중복 아이디 개수
   */
  public int checkID(String id);
  
  /**
   * 닉네임 중복 검사
   * @param nickname
   * @return 중복 닉네임 개수
   */
  public int checkNICKNAME(String nickname);
  
  /**
   * 회원 가입
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
  
  /**
   * 검색 조건에 맞는 총 회원 수
   * @param searchDTO 검색 조건(searchType, keyword)
   * @return 총 회원 수
   */
  public int list_search_count(SearchDTO searchDTO);
  
  /**
   * 회원 검색 + 페이징 목록
   * searchDTO 검색 조건 및 페이징 정보
   * @return 회원 목록
   */
  public ArrayList<MemberVO> list_search_paging(SearchDTO searchDTO);
  
  /**
   * memberno로 회원 정보 조회
   * @param memberno
   * @return
   */
  public MemberVO read(int memberno);
  
  /**
   * id로 회원 정보 조회
   * @param id
   * @return
   */
  public MemberVO readByID(String id);
  
  /**
   * 로그인된 회원 계정인지 검사
   * @param session
   * @return true: 사용자
   */
  public boolean isMember(HttpSession session);
  
  /**
   * 로그인된 관리자 계정인지 검사
   * @param session
   * @return true: 관리자
   */
  public boolean isAdmin(HttpSession session);

  /**
   * 회원 정보 수정 처리
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
  
  /**
   * 회원 탈퇴 처리(db에서 삭제가 아닌 등급을 탈퇴로 변경)
   * @param memberVO
   * @return
   */
  public int withdraw(MemberVO memberVO);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
  /**
   * 현재 패스워드 검사
   * @param map
   * @return 1: 일치, 0: 불일치
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * passwd 수정 처리
   * @param map
   * @return 변경된 passwd 개수
   */
  public int update_passwd(HashMap<String, Object> map);
}
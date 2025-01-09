package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.dto.SearchDTO;

public interface MemberDAOInter {
  /**
   * 아이디 중복 검사
   * @param id
   * @return
   */
  public int checkID(String id);
  
  /**
   * 닉네임 중복 검사
   * @param nickname
   * @return
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
   * 회원 정보 수정 처리
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
  
  /**
   * 회원 탈퇴 처리(아예 삭제가 아닌 grade를 99(탈퇴 회원 번호)로 변경)
   * @param memberVO
   * @return
   */
  public int withdraw(MemberVO memberVO);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
  /**
   * 현재 비밀번호 검사
   * @param map
   * @return 1: 일치, 0: 불일치
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * 비밀번호 수정 처리
   * @param map
   * @return 변경된 passwd 개수
   */
  public int update_passwd(HashMap<String, Object> map);
  
  /**
   * 문자 인증 성공 시 비밀번호 수정 처리
   * @param map
   * @return 수정한 비밀번호 개수
   */
  public int update_passwd_find(HashMap<String, String> map);
  
  /**
   * 이름, 전화번호 입력받아서 일치하는 회원이 있는지 검사
   * @return 찾은 id
   */
  public String find_id_check(HashMap<String, String> map);
  
  /**
   * 아이디, 전화번호 입력받아서 일치하는 회원이 있는지 검사
   * @return 찾은 id 개수
   */
  public int find_passwd_check(HashMap<String, String> map);
}
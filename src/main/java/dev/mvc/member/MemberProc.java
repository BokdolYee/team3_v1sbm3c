package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import dev.mvc.dto.SearchDTO;

@Component("dev.mvc.member.MemberProc")
public class MemberProc implements MemberProcInter {
  @Autowired
  private MemberDAOInter memberDAO;
  
  @Autowired
  Security security;
  
  public MemberProc(){
    System.out.println("-> MemberProc 생성됨.");
  }
  
  /**
   * 아이디(이메일) 중복 확인
   */
  @Override
  public int checkID(String id) {
    int cnt = this.memberDAO.checkID(id);
    return cnt;
  }

  /**
   * 닉네임 중복 확인
   */
  @Override
  public int checkNICKNAME(String nickname) {
    int cnt = this.memberDAO.checkNICKNAME(nickname);
    return cnt;
  }

  /**
   * 비밀번호 암호화 후 회원가입 처리
   */
  @Override
  public int create(MemberVO memberVO) {
    // 비밀번호 암호화
    String passwd = memberVO.getPasswd();
    String passwd_encoded = this.security.aesEncode(passwd);
    memberVO.setPasswd(passwd_encoded);
    
    int cnt = this.memberDAO.create(memberVO);
    return cnt;
  }
  
  /**
   * 조건에 맞는 회원 수
   */
  @Override
  public int list_search_count(SearchDTO searchDTO) {
    return memberDAO.list_search_count(searchDTO);
  }
  
  /**
   * 회원 검색 + 목록 페이징
   */
  @Override
  public ArrayList<MemberVO> list_search_paging(SearchDTO searchDTO) {
    return memberDAO.list_search_paging(searchDTO);
  }

  /**
   * memberno로 회원 정보 조회
   */
  @Override
  public MemberVO read(int memberno) {
    MemberVO memberVO = this.memberDAO.read(memberno);
    return memberVO;
  }
  
  /**
   * id로 회원 정보 조회
   */
  @Override
  public MemberVO readByID(String id) {
    MemberVO memberVO = this.memberDAO.readByID(id);
    return memberVO;
  }

  /**
   * 회원인지 검사
   */
  @Override
  public boolean isMember(HttpSession session) {
    boolean examine = false;
    String grade = (String)session.getAttribute("grade");
    
    if(grade != null) {
      if(grade.equals("member")) {
        examine = true;
      }
    }
    
    return examine;
  }
  
  /**
   * 관리자인지 검사
   */  
  @Override
  public boolean isAdmin(HttpSession session){
    boolean examine = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin")) {
        examine = true;  // 로그인 한 경우
      }      
    }
    
    //sw = true;  // 테스트를 위한 임시 true
    return examine;
  }

  /**
   * 회원 정보 수정(아이디, 비밀번호 제외)
   */
  @Override
  public int update(MemberVO memberVO) {
    int cnt = this.memberDAO.update(memberVO);
    return cnt;
  }

  /**
   * 회원 탈퇴 처리(grade 값을 탈퇴로 지정한 99로 변경)
   */
  @Override
  public int withdraw(MemberVO memberVO) {
    int cnt = this.memberDAO.withdraw(memberVO);
    return cnt;
  }

  /**
   * 로그인 처리
   */
  @Override
  public int login(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.login(map);
    return cnt;
  }

  /**
   * 현재 비밀번호 검사
   */
  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.passwd_check(map);
    return cnt;
  }

/**
 * 비밀번호 변경 처리
 */
  @Override
  public int update_passwd(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.update_passwd(map);
    return cnt;
  }

  @Override
  public int update_passwd_find(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.memberDAO.update_passwd_find(map);
    return cnt;
  }
  
  @Override
  public int find_id_check(HashMap<String, String> map) {
    int cnt = this.memberDAO.find_id_check(map);
    
    return cnt;
  }
  
  @Override
  public int find_passwd(String id, String tel) {
    int cnt = this.memberDAO.find_passwd(id, tel);
    
    return cnt;
  }
 
}
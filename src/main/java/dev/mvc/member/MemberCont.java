package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.dto.SearchDTO;
import dev.mvc.loginlog.LoginlogProcInter;
import dev.mvc.loginlog.LoginlogVO;
import dev.mvc.member.MemberVO.UpdateValidationGroup;
import dev.mvc.dto.PageDTO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping("/member")
@Controller
public class MemberCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.loginlog.LoginlogProc")
  private LoginlogProcInter loginlogProc;

  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;

  public MemberCont() {
    System.out.println("MemberCont 생성됨");
  }

  /**
   * 아이디 중복 확인
   * 
   * @param id
   * @return
   */
  @GetMapping(value = "/checkID") // http://localhost:9093/member/checkID?id=test
  @ResponseBody
  public String checkID(@RequestParam(name = "id", defaultValue = "") String id) {
    System.out.println("-> id: " + id);
    int cnt = this.memberProc.checkID(id);

    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);

    return obj.toString();
  }

  /**
   * 닉네임 중복 확인
   * 
   * @param nickname
   * @return
   */
  @GetMapping(value = "/checkNICKNAME") // http://localhost:9093/member/checkNICKNAME?nickname=test
  @ResponseBody
  public String checkNICKNAME(@RequestParam(name = "nickname", defaultValue = "") String nickname) {
    System.out.println("-> nickname: " + nickname);
    int cnt = this.memberProc.checkNICKNAME(nickname);
    System.out.println(cnt);

    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);

    return obj.toString();
  }

  /**
   * 회원 가입 폼
   * 
   * @param model
   * @param memberVO
   * @return
   * 
   */
  @GetMapping(value = "/create") // http://localhost:9093/member/create
  public String create_form(Model model, @ModelAttribute("memberVO") MemberVO memberVO) {

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    return "/th/member/create"; // /template/member/create.html
  }

  /**
   * 회원 가입 처리
   * 
   * @param model
   * @param memberVO
   * @return
   */
  @PostMapping(value = "/create")
  public String create_proc(Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
    int checkID_cnt = this.memberProc.checkID(memberVO.getId());
    int checkNICKNAME_cnt = this.memberProc.checkNICKNAME(memberVO.getNickname());

    // id랑 닉네임 둘 다 중복 아닐 경우
    if (checkID_cnt == 0 && checkNICKNAME_cnt == 0) {
      memberVO.setGrade(11); // 11~20: 회원
      int cnt = this.memberProc.create(memberVO);

      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("id", memberVO.getId());
        model.addAttribute("nickname", memberVO.getNickname());
      } else {
        model.addAttribute("code", "create_fail");
      }
      model.addAttribute("cnt", cnt);
    } else { // id나 닉네임 또는 둘 다 중복일 경우
      model.addAttribute("code", "duplicate_fail");
      model.addAttribute("cnt", 0);
    }

    return "/th/index";
  }

//  /**
//   * 회원 목록(관리자만 접근 가능)
//   * 
//   * @param model
//   * @param session
//   * @return
//   */
//  @GetMapping(value = "/list")
//  public String list(Model model, HttpSession session) {
//    if (this.memberProc.isAdmin(session)) {
//      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
//      model.addAttribute("menu", menu);
//
//      ArrayList<MemberVO> list = this.memberProc.list();
//
//      model.addAttribute("list", list);
//
//      return "/member/list"; // templates/member/list.html
//    } else {
//      return "redirect:/member/login_cookie_need"; // redirect
//    }
//  }

  @GetMapping(value = "/list")
  public String list_search_paging(Model model, HttpSession session,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
    if (this.memberProc.isAdmin(session)) {
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);

      // 검색 조건 설정
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);
      searchDTO.setSize(page * 10);
      searchDTO.setOffset((page - 1) * 10);

      // 전체 회원 수 조회
      int total = this.memberProc.list_search_count(searchDTO);
      
      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if(total == 0 && page > 1) {
        return "redirect:/member/list?searchType=" + searchType + "&keyword=" + keyword;
      }

      // 페이징 정보 계산
      PageDTO pageDTO = new PageDTO(total, page);

      // 회원 목록 조회
      ArrayList<MemberVO> list = memberProc.list_search_paging(searchDTO);

      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("total", total);

      return "/th/member/list_search";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }

  /**
   * 회원 정보 조회 (내 정보 보기)
   * 
   * @param session
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model,
      @RequestParam(name = "memberno", required = false) Integer memberno) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    
    if (this.memberProc.isMember(session) || this.memberProc.isAdmin(session)) {
      // memberno 파라미터가 없는 경우 세션에서 가져옴 (일반 회원인 경우)
      if (memberno == null) {
        memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
      } else {
        // 관리자가 아닌데 다른 회원의 정보를 조회하려는 경우
        if (!this.memberProc.isAdmin(session)) {
          memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
        }
        // 관리자인 경우 전달받은 memberno 사용
      }

      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/read"; // /templates/member/read
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }

  /**
   * 회원 정보 수정 폼(아이디, 비밀번호 제외) 회원은 자기 계정만, 관리자도 자기 계정만 수정 가능(회원 계정 수정X)
   * 
   * @param session
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/update")
  public String update_form(HttpSession session, Model model,
      @RequestParam(name = "nickname", required = false) String nickname,
      @RequestParam(name = "memberno", required = false) Integer memberno) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    // 회원은 회원 등급만 처리, 관리자: 1 ~ 10, 회원: 11 ~ 20
    memberno = (int) session.getAttribute("memberno");

    // 회원: member && 11 ~ 20
    if (this.memberProc.isMember(session) && memberno == (int) session.getAttribute("memberno")) {
      System.out.println("memberno: " + memberno);
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/update"; // templates/member/read.html

    } else if (this.memberProc.isAdmin(session) && memberno == (int) session.getAttribute("memberno")) { // 관리자 1~10
      System.out.println("-> admin memberno: " + memberno);
      MemberVO memberVO = this.memberProc.read(memberno);

      model.addAttribute("memberVO", memberVO);

      return "/th/member/update"; // templates/member/update.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }

  }

  /**
   * 회원 정보 수정 처리(아이디, 비밀번호 제외) 회원은 자기 계정만, 관리자도 자기 계정만 수정 가능(회원 계정 수정X)
   * 
   * @param session
   * @param model
   * @param memberno
   * @param memberVO
   * @return
   */
  @PostMapping(value = "/update")
  public String update_proc(HttpSession session, Model model,
      @Validated(UpdateValidationGroup.class) @ModelAttribute("memberVO") MemberVO memberVO) {
    
    String nicknameSession = (String) session.getAttribute("nickname");

    // 회원 본인일 때
    if (this.memberProc.isMember(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int checkNICKNAME_cnt = this.memberProc.checkNICKNAME(memberVO.getNickname());
      // 닉네임 중복되지 않았을 경우
      if (checkNICKNAME_cnt == 0) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setBirth(memberVO.getBirth().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress(memberVO.getAddress().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt1: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress());

        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      // 닉네임 중복됐지만 기존 회원 닉네임이랑 같을 경우
      else if (checkNICKNAME_cnt == 1 && nicknameSession.equals(memberVO.getNickname())) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setBirth(memberVO.getBirth().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress(memberVO.getAddress().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress());

        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      return "redirect:/member/read"; // templates/member/read.html
    } // 관리자 본인일 때
    else if (this.memberProc.isAdmin(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int checkNICKNAME_cnt = this.memberProc.checkNICKNAME(memberVO.getNickname());
      // 닉네임 중복되지 않았을 경우
      if (checkNICKNAME_cnt == 0) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setBirth(memberVO.getBirth().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress(memberVO.getAddress().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress());
        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      // 닉네임 중복됐지만 기존 회원 닉네임이랑 같을 경우
      else if (checkNICKNAME_cnt == 1 && nicknameSession.equals(memberVO.getNickname())) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setBirth(memberVO.getBirth().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress(memberVO.getAddress().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress());

        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      return "redirect:/member/read"; // /templates/member/read.html
    } else {
      return "redirect:/member/login_cookie_need"; // /templates/member/read.html
    }
  }

  /**
   * 비밀번호 수정 폼
   * 
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/update_passwd")
  public String update_passwd_form(HttpSession session, Model model) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isMember(session)) {
      int memberno = (int) session.getAttribute("memberno"); // session에서 가져오기

      MemberVO memberVO = this.memberProc.read(memberno);

      model.addAttribute("memberVO", memberVO);

      return "/th/member/update_passwd"; // /tamplates/member/pass_update.html
    } else if (this.memberProc.isAdmin(session)) {
      int memberno = (int) session.getAttribute("memberno"); // session에서 가져오기

      MemberVO memberVO = this.memberProc.read(memberno);

      model.addAttribute("memberVO", memberVO);

      return "/th/member/update_passwd"; // /tamplates/member/update_passwd.html
    } else {
      return "redirect:/member/login_cookie_need";
    }

  }

  /**
   * 현재 비밀번호 확인
   * 
   * @param session
   * @param current_passwd
   * @return 1: 일치, 0: 불일치
   */
  @PostMapping(value = "/passwd_check")
  @ResponseBody
  public String passwd_check(HttpSession session, @RequestBody String json_src) {
    System.out.println("json_src: " + json_src); // json_src: {"current_passwd":"1234"}

    JSONObject src = new JSONObject(json_src); // String -> JSON

    String current_passwd = (String) src.get("current_passwd"); // 값 가져오기
    System.out.println("-> current_passwd: " + current_passwd);

    int memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("passwd", current_passwd);

    int cnt = this.memberProc.passwd_check(map); // 현재 로그인한 사용자의 패스워드 확인

    JSONObject json = new JSONObject();
    json.put("cnt", cnt); // 1: 패스워드 일치, 0: 불일치
    System.out.println(json.toString());

    return json.toString();
  }

  /**
   * 비밀번호 수정 처리
   * 
   * @param session
   * @param model
   * @param current_passwd 현재 패스워드
   * @param passwd         새로운 패스워드
   * @return
   */
  @PostMapping(value = "/update_passwd")
  public String update_passwd_proc(HttpSession session, Model model,
      @RequestParam(value = "current_passwd", defaultValue = "") String current_passwd,
      @RequestParam(value = "passwd", defaultValue = "") String passwd) {

    if (this.memberProc.isMember(session)) { // 로그인된 회원일 경우
      int memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("memberno", memberno);
      map.put("passwd", current_passwd);

      int cnt = this.memberProc.passwd_check(map);

      if (cnt == 0) { // 패스워드 불일치
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);

      } else { // 패스워드 일치
        map = new HashMap<String, Object>();
        map.put("memberno", memberno);
        map.put("passwd", passwd);

        int passwd_change_cnt = this.memberProc.update_passwd(map);

        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }

      return "redirect:/member/read"; // 수정 처리 하고 새로고침
    } else if (this.memberProc.isAdmin(session)) { // 로그인된 관리자일 경우
      int memberno = (int) session.getAttribute("memberno"); // session에서 가져오기
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("memberno", memberno);
      map.put("passwd", current_passwd);

      int cnt = this.memberProc.passwd_check(map);

      if (cnt == 0) { // 패스워드 불일치
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);

      } else { // 패스워드 일치
        map = new HashMap<String, Object>();
        map.put("memberno", memberno);
        map.put("passwd", passwd);

        int passwd_change_cnt = this.memberProc.update_passwd(map);

        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }

      return "redirect:/member/read"; // 수정 처리 하고 새로고침
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }

  /**
   * 회원 탈퇴 폼
   * @param session
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/withdraw")
  public String withdraw_form(HttpSession session, Model model,
      @RequestParam(name = "memberno", required = false) Integer memberno) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isMember(session)) {
      memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/withdraw";
    } else if (this.memberProc.isAdmin(session)) {
      memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/withdraw";
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 회원 탈퇴 처리
   * 
   * @param model
   * @param memberno 삭제할 회원 번호
   * @return
   */
  @PostMapping(value = "/withdraw")
  public String withdraw_process(HttpSession session, Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
    
    //회원 및 회원 본인일 경우
    if (this.memberProc.isMember(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int cnt = this.memberProc.withdraw(memberVO);

      if (cnt == 1) {
        model.addAttribute("code", "withdraw_success");
        model.addAttribute("memberno", memberVO.getMemberno());
        model.addAttribute("grade", memberVO.getGrade());
        
        System.out.println("memberno: " + memberVO.getMemberno() + ", id: " + memberVO.getId() + "nickname: " + memberVO.getNickname() + " has withdrown");
        
        session.invalidate();
        return "redirect:/";
      } else {
        model.addAttribute("code", "withdraw_fail");
        return "/th/member/error";
      }
    }
    // 관리자 및 관리자 본인일 경우
    else if (this.memberProc.isAdmin(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int cnt = this.memberProc.withdraw(memberVO);

      if (cnt == 1) {
        model.addAttribute("code", "withdraw_success");
        model.addAttribute("memberno", memberVO.getMemberno());
        model.addAttribute("grade", memberVO.getGrade());
        
        System.out.println("admin memberno: " + memberVO.getMemberno() + "id: " + memberVO.getId() + "nickname: " + memberVO.getNickname() + " has withdrown");
        
        session.invalidate();
        return "redirect:/";
      } else {
        model.addAttribute("code", "withdraw_fail");
        return "/th/member/error";
      }
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  // ----------------------------------------------------------------------------------
  // Cookie 사용 회원 로그인 관련 코드
  // ----------------------------------------------------------------------------------
  /**
   * Cookie 기반 로그인 폼
   * 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/login")
  public String login_form(Model model, HttpServletRequest request) {
    // Cookie 관련 코드---------------------------------------------------------

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;

    String check_id = ""; // id 저장
    String check_id_save = ""; // id 저장 여부 체크
    String check_passwd = ""; // passwd 저장
    String check_passwd_save = ""; // passwd 저장 여부 체크

    if (cookies != null) { // 쿠키가 존재한다면
      for (int i = 0; i < cookies.length; i++) {
        cookie = cookies[i]; // 쿠키 객체 추출

        if (cookie.getName().equals("check_id")) { // 아이디
          check_id = cookie.getValue(); // email
        } else if (cookie.getName().equals("check_id_save")) { // 아이디 저장 여부
          check_id_save = cookie.getValue(); // Y, N
        } else if (cookie.getName().equals("check_passwd")) { // 비밀번호
          check_passwd = cookie.getValue(); // 1234
        } else if (cookie.getName().equals("check_passwd_save")) { // 비밀번호 저장 여부
          check_passwd_save = cookie.getValue(); // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    model.addAttribute("check_id", check_id);
    model.addAttribute("check_id_save", check_id_save);
    model.addAttribute("check_passwd", check_passwd);
    model.addAttribute("check_passwd_save", check_passwd_save);

    return "/th/member/login_cookie"; // templates/member/login_cookie.html
  }

  /**
   * Cookie 기반 로그인 처리
   * 
   * @param session
   * @param request
   * @param response
   * @param model
   * @param id          아이디
   * @param passwd      패스워드
   * @param id_save     아이디 저장 여부
   * @param passwd_save 패스워드 저장 여부
   * @return
   */
  @PostMapping(value = "/login")
  public String login_proc(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model,
      RedirectAttributes ra, @RequestParam(value = "id", defaultValue = "") String id,
      @RequestParam(value = "passwd", defaultValue = "") String passwd,
      @RequestParam(value = "id_save", defaultValue = "") String id_save,
      @RequestParam(value = "passwd_save", defaultValue = "") String passwd_save) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", id);
    map.put("passwd", passwd);

    int cnt = this.memberProc.login(map);
    System.out.println("login_proc cnt: " + cnt);
    
    // 로그인 시도한 IP 주소 가져오기
    String ip = request.getRemoteAddr();
    
    // 로그인 로그를 기록하기 위한 객체 생성
    LoginlogVO loginlogVO = new LoginlogVO();
    loginlogVO.setId(id);
    loginlogVO.setIp(ip);
    loginlogVO.setResult(cnt == 1 ? "T" : "F"); // 로그인 성공 여부
    
    // 로그인 기록 저장
    this.loginlogProc.login_log(loginlogVO);

    if (cnt == 1) { // 로그인 성공
      
      // id를 이용하여 회원 정보 조회
      MemberVO memberVO = this.memberProc.readByID(id);
      
      if(memberVO.getGrade() == 99) {
        ra.addFlashAttribute("cnt", 0);
        ra.addFlashAttribute("code", "withdrawn member");
        return "redirect:/member/login";
      }
      
      session.setAttribute("memberno", memberVO.getMemberno());
      session.setAttribute("id", memberVO.getId());
      session.setAttribute("name", memberVO.getName());
      session.setAttribute("nickname", memberVO.getNickname());
      session.setAttribute("grade", memberVO.getGrade());

      if (memberVO.getGrade() >= 1 && memberVO.getGrade() <= 10) {
        session.setAttribute("grade", "admin");
      } else if (memberVO.getGrade() >= 11 && memberVO.getGrade() <= 20) {
        session.setAttribute("grade", "member");
      } else if (memberVO.getGrade() >= 41 && memberVO.getGrade() <= 49) {
        session.setAttribute("grade", "stopped");
      }

      // Cookie 관련 코드---------------------------------------------------------
      // -------------------------------------------------------------------
      // id 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
        Cookie check_id = new Cookie("check_id", id);
        check_id.setPath("/"); // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
        check_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
        response.addCookie(check_id); // id 저장
      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
        Cookie check_id = new Cookie("check_id", "");
        check_id.setPath("/");
        check_id.setMaxAge(0);
        response.addCookie(check_id); // id 저장
      }

      // id를 저장할지 선택하는 CheckBox 체크 여부
      Cookie check_id_save = new Cookie("check_id_save", id_save);
      check_id_save.setPath("/");
      check_id_save.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유효
      response.addCookie(check_id_save);
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // passwd 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
        Cookie check_passwd = new Cookie("check_passwd", passwd);
        check_passwd.setPath("/");
        check_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
        response.addCookie(check_passwd);
      } else { // N, 패스워드를 저장하지 않을 경우
        Cookie check_passwd = new Cookie("check_passwd", "");
        check_passwd.setPath("/");
        check_passwd.setMaxAge(0);
        response.addCookie(check_passwd);
      }
      // passwd를 저장할지 선택하는 CheckBox 체크 여부
      Cookie check_passwd_save = new Cookie("check_passwd_save", passwd_save);
      check_passwd_save.setPath("/");
      check_passwd_save.setMaxAge(60 * 60 * 24 * 7); // 7일 동안 유효
      response.addCookie(check_passwd_save);
      // -------------------------------------------------------------------
      // ----------------------------------------------------------------------------

      return "/th/index";
    } else {  // 로그인 실패
      ra.addFlashAttribute("cnt", cnt); // 새로고침 시에도 cnt 값이 살아서 전달될 수 있도록 RedirectAttributes 클래스의 addFlashAttribute 함수 사용
      model.addAttribute("code", "login_fail");
      System.out.println("login_fail");
      return "redirect:/member/login";
    }
  }

  /**
   * 로그아웃
   * 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate(); // 모든 세션 변수 삭제
    return "redirect:/";
  }

  /**
   * 로그인 요구에 따른 로그인 폼 출력
   * 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/login_cookie_need")
  public String login_cookie_need(Model model, HttpServletRequest request) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;

    String check_id = ""; // id 저장
    String check_id_save = ""; // id 저장 여부를 체크
    String check_passwd = ""; // passwd 저장
    String check_passwd_save = ""; // passwd 저장 여부를 체크

    if (cookies != null) { // 쿠키가 존재한다면
      for (int i = 0; i < cookies.length; i++) {
        cookie = cookies[i]; // 쿠키 객체 추출

        if (cookie.getName().equals("check_id")) {
          check_id = cookie.getValue(); // email
        } else if (cookie.getName().equals("check_id_save")) {
          check_id_save = cookie.getValue(); // Y, N
        } else if (cookie.getName().equals("check_passwd")) {
          check_passwd = cookie.getValue(); // 1234
        } else if (cookie.getName().equals("check_passwd_save")) {
          check_passwd_save = cookie.getValue(); // Y, N
        }
      }
    }

    model.addAttribute("check_id", check_id);

    model.addAttribute("check_id_save", check_id_save);

    model.addAttribute("check_passwd", check_passwd);
    model.addAttribute("check_passwd_save", check_passwd_save);

    return "/th/member/login_cookie_need"; // templates/member/login_cookie_need.html
  }
}

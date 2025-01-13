package dev.mvc.calendargood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.calendar.CalendarVO;
import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.post_goodbad.Post_goodbadVO;
import dev.mvc.stock.StockVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/calendargood")
public class CalendargoodCont {
  @Autowired
  @Qualifier("dev.mvc.calendargood.CalendargoodProc") 
  CalendargoodProcInter calendargoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public CalendargoodCont() {
    System.out.println("-> calendargoodCont created.");
  }
  
//  public int record_per_page = 4;
//  public int page_per_block = 10;
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody CalendargoodVO calendargoodVO) {
    System.out.println("-> 수신 데이터:" + calendargoodVO.toString());
    
    int memberno = 3; // test 
    // int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    calendargoodVO.setMemberno(memberno);
    
    int cnt = this.calendargoodProc.create(calendargoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
//  /**
//   * 목록
//   * @param model
//   * @return
//   */
//  @GetMapping(value = "/list_all")
//  public String list_all(Model model) {
//    ArrayList<CalendargoodVO> list = this.calendargoodProc.list_all();
//    model.addAttribute("list", list);
//
//    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "/calendargood/list_all"; // /templates/calendar/list_all.html
//  }
  
  /**
   * 삭제 폼 http:// localhost:9091/calendar/delete?calendarno=1
   *
   */
  @GetMapping(path = "/delete/{calendargoodno}")
  public String delete(HttpSession session, 
      Model model, 
      @PathVariable("calendargoodno") int calendargoodno, 
      RedirectAttributes ra) {    
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한경우
      CalendargoodVO calendargoodVO = this.calendargoodProc.read(calendargoodno);
      model.addAttribute("calendargoodVO", calendargoodVO);

      return "/th/calendargood/delete"; // /templates/calendar/delete.html
    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/contents/msg"; // @GetMapping(value = "/msg")
      return "/th/member/login_cookie_need"; // /templates/member/login_cookie_need.html
    }
  }
  
  /**
   * 삭제 처리 http://localhost:9091/calendar/delete?calendarno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="calendargoodno", defaultValue = "0") int calendargoodno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isAdmin(session)) { // 관리자 로그인 확인
      this.calendargoodProc.delete(calendargoodno);

      return "redirect:/calendargood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/calendargood/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
//  /**
//   * 목록
//   * @param model
//   * @return
//   */
//  @GetMapping(value = "/list_all")
//  public String list_all_join(Model model) {
//    ArrayList<CalendarCalendargoodMemberVO> list = this.calendargoodProc.list_all_join();
//    model.addAttribute("list", list);
//
//    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "/th/calendargood/list_all"; // /templates/calendar/list_all.html
//  }
  
  /**
   * 추천, 비추천 내역 전체 목록(검색 + 페이징, 관리자만 조회 가능)
   * @param session
   * @param model
   * @param page
   * @param searchType
   * @param keyword
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_search_paging(HttpSession session, 
      Model model,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {

    if (this.memberProc.isAdmin(session)) {
      // 메뉴 데이터
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);

      // 검색 조건 설정
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);

      // 한 페이지당 4개의 데이터 출력
      searchDTO.setSize(4);  // 한 페이지에 4개의 데이터만 출력
      searchDTO.setOffset((page - 1) * 4); // 페이지 오프셋 계산 (4개씩)

      // 전체 내역 수 조회
      int total = this.calendargoodProc.list_search_count(searchDTO);

      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if (total == 0 && page > 1) {
        return "redirect:/calendargood/list_all?page=1&searchType=" + searchType + "&keyword=" + keyword;
      }

      // 페이징 정보 계산
      int totalPages = (int) Math.ceil((double) total / 4);  // 전체 페이지 수 계산
      int startPage = (int) (Math.floor((page - 1) / 10.0) * 10) + 1; // 10개씩 페이지를 나누고, 시작 페이지 계산
      int endPage = Math.min(startPage + 9, totalPages);  // 끝 페이지는 10개 페이지 번호까지만 표시

      // PageDTO 객체 생성
      PageDTO pageDTO = new PageDTO(total, page);
      pageDTO.setDisplayPageNum(10);  // 최대 10개의 페이지 번호를 표시

      model.addAttribute("totalPages", totalPages);
      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("pageDTO", pageDTO);

      // 게시물 목록 조회
      ArrayList<CalendarCalendargoodMemberVO> list = this.calendargoodProc.list_search_paging(searchDTO);

      // 모델에 데이터 추가
      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("total", total);

      return "/th/calendargood/list_all"; // /templates/th/post_goodbad/list.html;
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }


}
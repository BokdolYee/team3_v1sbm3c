package dev.mvc.calendargood;

import java.util.ArrayList;

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
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
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
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all_join(Model model) {
    ArrayList<CalendarCalendargoodMemberVO> list = this.calendargoodProc.list_all_join();
    model.addAttribute("list", list);

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    return "/th/calendargood/list_all"; // /templates/calendar/list_all.html
  }
  
}
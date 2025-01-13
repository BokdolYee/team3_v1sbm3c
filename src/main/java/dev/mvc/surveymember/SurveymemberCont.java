package dev.mvc.surveymember;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/surveymember")
public class SurveymemberCont {
  @Autowired
  @Qualifier("dev.mvc.surveymember.SurveymemberProc") 
  SurveymemberProcInter surveymemberProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc") // @Component("dev.mvc.cate.CateProc")
  private NewsCateProcInter newscateProc;

  
  public SurveymemberCont() {
    System.out.println("-> SurveymemberCont created.");
  }
  
 
  
//  /**
//   * 목록
//   * 
//   * @param model
//   * @return
//   */
//  // http://localhost:9091/cate/list_all
//  @GetMapping(value = "/list_all")
//  public String list_all(Model model) {
//    ArrayList<SurveygoodVO> list = this.surveygoodProc.list_all();
//    model.addAttribute("list", list);
//
//    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "/surveygood/list_all"; // /templates/calendar/list_all.html
//  }
  
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody SurveymemberVO surveymemberVO) {
    System.out.println("-> 수신 데이터:" + surveymemberVO.toString());
    
    if (this.memberProc.isAdmin(session)) {
      
    int memberno = 22;
    //int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    surveymemberVO.setMemberno(memberno);
    
    int cnt = this.surveymemberProc.create(surveymemberVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
    } else {
      return "redirect:/member/login_cookie_need"; // 로그인 안되어 있으면 리다이렉트
  }
}
  /**
   * 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/cate/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    
    ArrayList<SurveytopicitemmemberVO> list = this.surveymemberProc.list_all_join();
    model.addAttribute("list", list);



    return "/th/surveymember/list_all"; // /templates/calendar/list_all.html
  }
  /**
   * 삭제 처리 http://localhost:9091/calendar/delete?calendarno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="surveymemberno", defaultValue = "0") int surveymemberno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isAdmin(session)) { // 관리자 로그인 확인
      this.surveymemberProc.delete(surveymemberno);

      return "redirect:/surveymember/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/surveymember/post2get"; // @GetMapping(value = "/msg")
    }

  }
}
package dev.mvc.post_earning;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value="/post_earning")
@Controller
public class Post_earningCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc") // @Component("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  @Autowired
  @Qualifier("dev.mvc.post_earning.Post_earningProc") // @Component("dev.mvc.post_earning.Post_earningProc")
  private Post_earningProcInter post_earningProc;
  
  
  public Post_earningCont() {
    System.out.println("Post_earningCont 생성됨");
  }

  /**
   * POST 요청 후 리다이렉트를 통한 중복 제출 방지
   * 
   * @param url 리다이렉트할 대상 URL
   * @return 리다이렉트 URL
   */
  @PostMapping(value = "/redirect")
  public String handlePostRedirect(@RequestParam(name = "url", required = true) String url) {
      // 리다이렉트 URL 반환
      return "redirect:" + url;
  }
  
  /**
   * 게시물 등록 폼
   * @param model
   * @param post_earningVO
   * @param postno
   * @return
   */
  @GetMapping(value="/create")
  public String create_form(Model model, HttpSession session,
        @ModelAttribute("post_earningVO")Post_earningVO post_earningVO,
        @RequestParam(name="postno", defaultValue = "0")int postno) {
    
    if(memberProc.isMember(session) == true|| memberProc.isAdmin(session) == true) {
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);
      
      return "/th/post_earning/create";
    } else {
      return "redirect:/member/login_cookie_need";
    }
    
  }
  
  /**
   * 게시물 등록 처리
   * @param request
   * @param session
   * @param model
   * @param post_earningVO
   * @param ra
   * @return
   */
  @PostMapping(value="/create")
  public String create_proc(HttpServletRequest request,
                       HttpSession session,
                       Model model,
                       @ModelAttribute("post_earningVO")Post_earningVO post_earningVO,
                       RedirectAttributes ra) {
    
    // 회원 또는 관리자로 로그인 한 경우
    if(memberProc.isMember(session) == true|| memberProc.isAdmin(session) == true) {
      
      int memberno = (int)session.getAttribute("memberno");
      post_earningVO.setMemberno(memberno);
      int cnt = this.post_earningProc.create(post_earningVO);
      
      JSONObject obj = new JSONObject();
      obj.put("cnt", cnt);
      
      return obj.toString();
    }
    else {  // 로그인 상태가 아닌데 경로에 들어온 경우
      return "redirect:/member/login_cookie_need";  // member/login_cookie_need.html
    }
  }
  
  
  
}

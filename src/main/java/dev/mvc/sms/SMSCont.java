package dev.mvc.sms;

import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;

@Controller
@RequestMapping("/sms")
public class SMSCont {
  public SMSCont() {
    System.out.println("-> SMSCont created.");
  }
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  // http://localhost:9093/sms/form
  /**
   * 사용자의 전화번호 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/form")
  public String form(HttpSession session) {
    // ID 찾은 경우 어느 회원의 패스워드를 변경하는지 확인할 목적으로 id를 session 에 저장
    session.setAttribute("id", "user1");
    
    return "/th/sms/form";
  }

  // http://localhost:9093/sms/proc
  /**
   * 사용자에게 인증 번호를 생성하여 전송
   * 
   * @param session
   * @param request
   * @return
   */
  @PostMapping(value = "/proc")
  public ModelAndView proc(HttpSession session, HttpServletRequest request) {
    ModelAndView mav = new ModelAndView();

    // ------------------------------------------------------------------------------------------------------
    // 0 ~ 9, 번호 6자리 생성
    // ------------------------------------------------------------------------------------------------------
    String auth_no = "";
    Random random = new Random();
    for (int i = 0; i <= 5; i++) {
      auth_no = auth_no + random.nextInt(10); // 0 ~ 9, 번호 6자리 생성
    }
    session.setAttribute("auth_no", auth_no); // 생성된 번호를 비교를위하여 session에 저장
    // ID를 찾은 경우 어느 회원의 비밀번호를 변경할지 확인할 목적으로 id를 session에 저장
    session.setAttribute("id", "user1");
    // System.out.println(auth_no);
    // ------------------------------------------------------------------------------------------------------

    System.out.println("-> IP:" + request.getRemoteAddr()); // 접속자의 IP 수집

    // 번호, 전화 번호, ip, auth_no, 날짜 -> SMS Oracle table 등록, 문자 전송 내역 관리 목적으로 저장(필수 아니나
    // 권장)

    String msg = "[www.resort.co.kr] [" + auth_no + "]을 인증번호란에 입력해주세요.";
    System.out.print(msg);

    mav.addObject("msg", msg); // request.setAttribute("msg")
    mav.setViewName("/sms/proc"); // /WEB-INF/views/sms/proc.jsp

    return mav;
  }

  // http://localhost:9093/sms/proc_next
  /**
   * 사용자가 수신받은 인증번호 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/proc_next")
  public String proc_next() {
    return "/th/sms/proc_next";
  }

  // http://localhost:9093/sms/confirm
  /**
   * 문자로 전송된 번호와 사용자가 입력한 번호를 비교한 결과 화면
   * 
   * @param session 사용자당 할당된 서버의 메모리
   * @param auth_no 사용자가 입력한 번호
   * @return
   */
  @PostMapping(value = "/confirm")
  public String confirm(Model model, HttpSession session, @RequestParam(name="auth_no", defaultValue = "")String auth_no) {

    System.out.println("-> auth_no: " + auth_no);
    String session_auth_no = (String) session.getAttribute("auth_no"); // 사용자에게 전송된 번호 session에서 꺼냄

    String msg = "";

    if (session_auth_no.equals(auth_no)) {
      String id = (String) session.getAttribute("id");
      msg = id + "의 비밀번호 변경 화면으로 이동합니다.<br><br>";
      msg += "패스워드 수정 화면등 출력";
    } else {
      msg = "입력된 번호가 일치하지않습니다. 다시 인증 번호를 요청해주세요.";
      msg += "<br><br><A href='./form'>인증번호 재요청</A>";
    }

    model.addAttribute("msg", msg); // request.setAttribute("msg")

    return "/th/sms/confirm";
  }

}

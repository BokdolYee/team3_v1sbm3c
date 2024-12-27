package dev.mvc.loginlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.newscate.NewsCateProcInter;

@RequestMapping("/loginlog")
@Controller
public class LoginlogCont {

  @Autowired
  @Qualifier("dev.mvc.loginlog.LoginlogProc")
  private LoginlogProcInter loginlogProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  public LoginlogCont() {
    System.out.println("LoginlogCont 생성됨");
  }
  
  public String login_log_proc(Model model, @ModelAttribute("loginlogVO")LoginlogVO loginlogVO) {
    
    return "/index";
  }
  
}

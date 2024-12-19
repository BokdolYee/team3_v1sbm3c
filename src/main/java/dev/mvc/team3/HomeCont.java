package dev.mvc.team3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.tool.Security;

@Controller
public class HomeCont {
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newsCateProc;
  
  public HomeCont() {
    System.out.println("-> HomeCont 생성됨");
  }
  
  @GetMapping(value={"/", "/index.do"}) // http://localhost:9093
  public String home(Model model) { // 파일명 return
    
    ArrayList<NewsCateVOMenu> menu = this.newsCateProc.menu();
    model.addAttribute("menu", menu);
    
    return "index"; // /templates/index.html  
  }
  
}
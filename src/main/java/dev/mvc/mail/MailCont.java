package dev.mvc.mail;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.tool.MailTool;

@Controller
@RequestMapping("/mail")
public class MailCont {
  public MailCont() {
    System.out.println("-> MailCont created.");
  }

  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  // http://localhost:9093/mail/form
  /**
   * 메일 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/form")
  public String form(Model model) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    return "/th/mail/form";
  }
  
  /**
   * 아이디 찾기에서 가입 유무 확인 버튼 클릭 시 db에서 검증
   * @param name
   * @param tel
   * @return
   */
  @GetMapping(value="/isExist") // http://localhost:9093/mail/isExist?name=name&tel=tel
  @ResponseBody
  public String isExist(@RequestParam(name="name", defaultValue = "")String name,
                        @RequestParam(name="tel", defaultValue = "")String tel) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", name);
    map.put("tel", tel);
    
    System.out.println("-> name: " + name);
    System.out.println("-> tel: " + tel);
    
    String id = this.memberProc.find_id_check(map);
    
    System.out.println("-> id: " + id);
    
    JSONObject obj = new JSONObject();
    obj.put("id", id);
    
    return obj.toString();
  }

  // http://localhost:9093/mail/send
  /**
   * 메일 전송
   * 
   * @return
   */
  @PostMapping(value = "/send")
  public String send(@RequestParam(name="email", defaultValue = "")String email,
                     @RequestParam(name="name", defaultValue = "")String name, 
                     @RequestParam(name="tel", defaultValue = "")String tel) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", name);
    map.put("tel", tel);
    String id = this.memberProc.find_id_check(map);
    
    String from = "zmffpdkq1213@gmail.com";
    String title = "[Hoak]" + name + " 회원님의 아이디입니다.";
    String content = "[Hoak]" + name + " 회원님의 아이디는 " + id + "입니다.";
    MailTool mailTool = new MailTool();
    mailTool.send(email, from, title, content); // 메일 전송

    return "/th/mail/sended";
  }
}

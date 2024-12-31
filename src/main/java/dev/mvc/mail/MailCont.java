package dev.mvc.mail;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.tool.MailTool;

@Controller
@RequestMapping("/mail")
public class MailCont {
  public MailCont() {
    System.out.println("-> MailCont created.");
  }

  // http://localhost:9093/mail/form
  /**
   * 메일 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/form")
  public String form(Model model) {

    return "/th/mail/form";
  }

  // http://localhost:9093/mail/send
  /**
   * 메일 전송
   * 
   * @return
   */
  @PostMapping(value = "/send")
  public String send(String receiver, String from, String title, String content) {
    MailTool mailTool = new MailTool();
    mailTool.send(receiver, from, title, content); // 메일 전송

    return "/th/mail/sended";
  }
}

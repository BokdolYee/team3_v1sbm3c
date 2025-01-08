package dev.mvc.contentsgood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.contents.ContentsProcInter;
import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;


@RequestMapping(value = "/contentsgood")
@Controller
public class ContentsGoodCont {
	public ContentsGoodCont(){
		System.out.println("-> ContentsGood Create");
	}
	  @Autowired
	  @Qualifier("dev.mvc.contentsgood.ContentsGoodProc") // @Component("dev.mvc.contents.ContentsProc")
	  private ContentsGoodProcInter contentsgoodProc;
	  
	  @Autowired
	  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
	  private MemberProcInter memberProc;
	  
	  @Autowired
	  @Qualifier("dev.mvc.contents.ContentsProc") // @Component("dev.mvc.contents.ContentsProc")
	  private ContentsProcInter contentsProc;
	 
	  @PostMapping(value="/create")
	  @ResponseBody
	  public String create(HttpSession session, @RequestBody ContentsGoodVO contentsgoodVO) {
		    System.out.println("-> json_src: " + contentsgoodVO.toString()); // json_src: {"current_passwd":"1234"}
		    
		    int memberno = 3;
		    // int memberno = (int)session.getAttribute("memberno");
		    contentsgoodVO.setMemberno(memberno);
		    
		    int cnt = this.contentsgoodProc.create(contentsgoodVO);
		    		
		    JSONObject json = new JSONObject(); // String -> JSON
		    json.put("res", cnt);
		    
		    return json.toString();
	  }
	  
}

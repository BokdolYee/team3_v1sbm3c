package dev.mvc.post_goodbad;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.member.MemberProcInter;
import dev.mvc.post_earning.Post_earningVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/post_goodbad")
public class Post_goodbadCont {

  @Autowired
  @Qualifier("dev.mvc.post_goodbad.Post_goodbadProc")
  private Post_goodbadProcInter post_goodbadProc;

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  public Post_goodbadCont() {
    System.out.println(" -> Post_goodbadCont 생성됨");
  }

  @PostMapping(value = "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody Map<String, Object> payload) {

    JSONObject obj = new JSONObject();

    if (this.memberProc.isMember(session) || this.memberProc.isAdmin(session)) {
      int memberno = (int) session.getAttribute("memberno");
      int postno = Integer.parseInt(payload.get("postno").toString());
      String goodbad = payload.get("goodbad").toString();

      HashMap<String, Object> map = new HashMap<>();
      map.put("postno", postno);
      map.put("memberno", memberno);
      
      int check_cnt = this.post_goodbadProc.check_cnt(map); // 추천 혹은 비추천을 해서 테이블에 존재하는지 검사

      if (check_cnt == 1) { // 추천 혹은 비추천을 한 레코드가 존재할 경우
        String result = this.post_goodbadProc.check_goodbad(map); // 추천(g), 비추천(b) 값을 조회
        if (result.equals("g")) {
          obj.put("cnt", result); // cnt -> "g"
        } else if (result.equals("b")) {
          obj.put("cnt", result); // cnt -> "b"
        }
      } else {  // 추천 혹은 비추천 레코드가 존재하지 않을 경우
        Post_goodbadVO post_goodbadVO = new Post_goodbadVO();
        post_goodbadVO.setMemberno(memberno);
        post_goodbadVO.setPostno(postno);
        post_goodbadVO.setGoodbad(goodbad);

        int cnt_create = this.post_goodbadProc.create(post_goodbadVO);
        obj.put("cnt", cnt_create); // cnt -> 1

        if (goodbad.equals("g")) { // 추천일 경우 추천수 증가
          this.post_goodbadProc.increase_goodcnt(postno);
        } else if (goodbad.equals("b")) { // 비추천일 경우 비추천수 증가
          this.post_goodbadProc.increase_badcnt(postno);
        }
      }
    } else {
      obj.put("cnt", "login_fail"); // cnt -> "login_fail"
    }

    return obj.toString();
  }

}

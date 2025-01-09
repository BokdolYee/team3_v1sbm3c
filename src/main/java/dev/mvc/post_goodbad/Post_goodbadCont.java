package dev.mvc.post_goodbad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.post_earning.Post_earningProcInter;
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
  
  @Autowired
  @Qualifier("dev.mvc.post_earning.Post_earningProc")
  private Post_earningProcInter post_earningProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc") // @Component("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;

  public Post_goodbadCont() {
    System.out.println(" -> Post_goodbadCont 생성됨");
  }

  /**
   * 추천 비추천 등록(한 게시물에 대해 추천 비추천 둘 중 하나만 한번 누를 수 있음)
   * @param session
   * @param payload
   * @return
   */
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
      
      int check_cnt = this.post_goodbadProc.check_cnt(map); // 추천 혹은 비추천이 이미 테이블에 존재하는지 검사

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
        
        if (goodbad.equals("g")) { // 추천일 경우
          this.post_goodbadProc.increase_goodcnt(postno); // 추천수 증가
          Post_earningVO post_earningVO = this.post_earningProc.read_join_nickname(postno); // 증가된 추천수를 추출하기 위해 게시물 정보를 갖고 옴
          int goodcnt = post_earningVO.getGoodcnt();  // 추천수 증가가 반영된 게시물의 추천수 가져옴.
          obj.put("goodcnt", goodcnt);
        } else if (goodbad.equals("b")) { // 비추천일 경우
          this.post_goodbadProc.increase_badcnt(postno);  // 비추천수 증가
          Post_earningVO post_earningVO = this.post_earningProc.read_join_nickname(postno); // 증가된 추천수를 추출하기 위해 게시물 정보를 갖고 옴
          int badcnt = post_earningVO.getBadcnt();  // 비추천수 증가가 반영된 게시물의 비추천수 가져옴.
          obj.put("badcnt", badcnt);
        }
      }
    } else {
      obj.put("cnt", "login_fail"); // cnt -> "login_fail"
    }

    return obj.toString();
  }
  
  /**
   * 추천, 비추천 내역 전체 목록(검색 + 페이징, 관리자만 조회 가능)
   * @param session
   * @param model
   * @param page
   * @param searchType
   * @param keyword
   * @return
   */
  @GetMapping(value = "/list")
  public String list_search_paging(HttpSession session, 
      Model model,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
    
    if(this.memberProc.isAdmin(session)) {
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);

      // 검색 조건 설정
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);
      searchDTO.setSize(page * 10);
      searchDTO.setOffset((page - 1) * 10);
      
      // 전체 내역 수 조회
      int total = this.post_goodbadProc.list_search_count(searchDTO);
      System.out.println(total);
      
      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if(total == 0 && page > 1) {
        return "redirect:/post_goodbad/list?searchType=" + searchType + "&keyword=" + keyword;
      }
      
      // 페이징 정보 계산
      PageDTO pageDTO = new PageDTO(total, page);
      
      // 게시물 목록 조회
      ArrayList<Post_goodbadVO> list = this.post_goodbadProc.list_search_paging(searchDTO);
      
      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("total", total);
      
      return "/th/post_goodbad/list"; // /templates/th/post_goodbad/list.html;
    }
    else {
      return "redirect:/member/login_cookie_need";
    }
  }
  
  

}

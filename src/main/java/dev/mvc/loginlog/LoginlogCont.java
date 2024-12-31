package dev.mvc.loginlog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/loginlog")
@Controller
public class LoginlogCont {

  @Autowired
  @Qualifier("dev.mvc.loginlog.LoginlogProc")
  private LoginlogProcInter loginlogProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  public LoginlogCont() {
    System.out.println("LoginlogCont 생성됨");
  }
  
  /**
   * 로그인 로그 검색 + 페이징
   * @param model
   * @param session
   * @param page
   * @param searchType
   * @param keyword
   * @return
   */
  @GetMapping(value = "/list")
  public String list_search_paging(Model model, HttpSession session,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
    if (this.memberProc.isAdmin(session)) {
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);

      // 검색 조건 설정
      SearchDTO searchDTO = new SearchDTO();
      searchDTO.setSearchType(searchType);
      searchDTO.setKeyword(keyword);
      searchDTO.setPage(page);
      searchDTO.setSize(page * 10);                // 페이지당 표시할 항목 수
      searchDTO.setOffset((page - 1) * 10);

      // 전체 로그인 로그 수 조회
      int total = this.loginlogProc.list_search_count(searchDTO);

      // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
      if(total == 0 && page > 1) {
        return "redirect:/loginlog/list?searchType=" + searchType + "&keyword=" + keyword;
      }
      
      // 페이징 정보 계산
      PageDTO pageDTO = new PageDTO(total, page);

      // 로그인 로그 목록 조회
      ArrayList<LoginlogVO> list = this.loginlogProc.list_search_paging(searchDTO);

      // 모델에 데이터 추가
      model.addAttribute("list", list);
      model.addAttribute("searchDTO", searchDTO);
      model.addAttribute("pageDTO", pageDTO);
      model.addAttribute("total", total);

      return "/th/loginlog/list_search";
    } else {
      return "redirect:/member/login_cookie_need";
    }
  }
  
  /**
   * 회원 Delete process
   * @param model
   * @param memberno 삭제할 레코드 번호
   * @return
   */
  @PostMapping(value="/delete/{loginlogno}")
  @ResponseBody
  public Map<String, String> delete_proc(HttpSession session,
                                         @PathVariable("loginlogno")int loginlogno) {
    Map<String, String> result = new HashMap<>();
    // 관리자일 경우에만
    if (this.memberProc.isAdmin(session)){
      int cnt = this.loginlogProc.delete(loginlogno);
      
      if (cnt == 1) {
        result.put("result", "success");
      } else {
        result.put("result", "fail");
      } 
    } else {
      result.put("result", "fail");
    }
    return result;
  }
}

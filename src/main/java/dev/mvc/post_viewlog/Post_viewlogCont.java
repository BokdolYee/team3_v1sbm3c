package dev.mvc.post_viewlog;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.dto.PageDTO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.post_earning.Post_earningProcInter;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value="/post_viewlog")
@Controller
public class Post_viewlogCont {
  
  @Autowired
  @Qualifier("dev.mvc.post_viewlog.Post_viewlogProc")
  private Post_viewlogProcInter post_viewlogProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc") // @Component("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
  
  @Autowired
  @Qualifier("dev.mvc.post_earning.Post_earningProc") // @Component("dev.mvc.post_earning.Post_earningProc")
  private Post_earningProcInter post_earningProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public Post_viewlogCont() {
    System.out.println("Post_viewlogCont 생성됨");
  }
  
  // 조회 내역 등록은 폼이 따로 없고, 회원이 글을 조회할 때 처음 조회할 시 조회 내역 db에서 해당 게시물번호가 있는 레코드가 없는 경우에 등록
  
  
  @GetMapping(value="/list")
  public String list(HttpSession session, Model model,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "searchType", required = false) String searchType,
      @RequestParam(value = "keyword", defaultValue = "") String keyword) {
    
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
    int total = this.post_viewlogProc.list_count(searchDTO);
    
    // 검색 페이지 결과가 없고 페이지가 1보다 큰 경우 첫 페이지로 리다이렉트
    if (total == 0 && page > 1) {
      return "redirect:/post_viewlog/list?searchType=" + searchType + "&keyword=" + keyword;
    }
    
    // 페이징 정보 계산
    PageDTO pageDTO = new PageDTO(total, page);
    
    // 조회 내역 목록 조회
    ArrayList<Post_viewlogVO> list = this.post_viewlogProc.list(searchDTO);
    
    model.addAttribute("list", list);
    model.addAttribute("searchDTO", searchDTO);
    model.addAttribute("pageDTO", pageDTO);
    model.addAttribute("total", total);
    
    return "/th/post_viewlog/list";
  }
}

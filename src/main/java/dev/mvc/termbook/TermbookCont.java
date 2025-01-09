package dev.mvc.termbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import dev.mvc.member.MemberProcInter;

@Controller
@RequestMapping("/termbook")
public class TermbookCont {
  @Autowired
  @Qualifier("dev.mvc.termbook.TermbookProc")
  private TermbookProcInter termbookProc;
  
    
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public int record_per_page = 4;
  public int page_per_block = 10;

  public TermbookCont() {
    System.out.println("-> TermbookCont created");
  }  
  
  // 1. Create - 데이터 추가 폼
  @GetMapping("/create")
  public String createForm(Model model) {
    TermbookVO termbookVO = new TermbookVO();
    model.addAttribute("termbookVO", termbookVO);
    return "/th/termbook/create"; // create.html로 이동
  }

//1. Create - 데이터 추가 처리
@PostMapping("/create")
public String create(Model model,
   @RequestParam(name="termno", defaultValue = "0") int termno, 
   @ModelAttribute("termbookVO") TermbookVO termbookVO,
   HttpSession session, RedirectAttributes ra) {

 // 1. 관리자 체크: 관리자인 경우
 if (this.memberProc.isAdmin(session)) {
 
   // 세션에서 로그인한 사용자의 memberno 가져오기
   Integer memberno = (Integer) session.getAttribute("memberno");
   if (memberno == null) {
     // 세션에 memberno가 없으면 로그인 페이지로 리다이렉트
     return "redirect:/member/login_cookie_need";
   }

   termbookVO.setMemberno(memberno); // memberno 설정

// 저장 시 줄바꿈 처리 (필요에 따라 <br>로 변환하거나 그대로 저장)
   String memo = termbookVO.getMemo();
   termbookVO.setMemo(memo.replace("\n", "<br>"));  // 줄바꿈을 <br>로 변환하여 저장
   
   // 데이터 삽입
   int cnt = this.termbookProc.create(termbookVO);
 
   if (cnt == 1) {
     ra.addAttribute("termno", termbookVO.getTermno()); // controller -> controller
     return "redirect:/termbook/list_all"; // 성공 시 목록 페이지로 리다이렉트
   } else {
     ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
     return "redirect:/termbook/msg"; // 실패 시 메시지 페이지로 이동
   }
 } else { // 관리자가 아닌 경우
   return "redirect:/member/login_cookie_need";
 }
}


  // 2. List - 전체 데이터 목록
  @GetMapping("/list_all")
  public String list_all(@RequestParam(name = "searchTerm_title", defaultValue = "") String searchTerm_title,
                         @RequestParam(name = "searchCategory", defaultValue = "") String searchCategory,
                         @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                         Model model) {

      // start_num 계산: 1페이지일 때 0, 2페이지일 때 10, 3페이지일 때 20
      int start_num = (now_page - 1) * record_per_page + 1;
      // end_num 계산: start_num + record_per_page - 1
      int end_num = start_num + record_per_page - 1;

      Map<String, Object> params = new HashMap<>();
      params.put("searchTerm_title", searchTerm_title);
      params.put("searchCategory", searchCategory);
      params.put("nowPage", now_page);
      params.put("start_num", start_num); // start_num
      params.put("end_num", end_num);     // end_num
      params.put("recordsPerPage", this.record_per_page);

      // 페이징 및 검색 데이터 목록
      List<TermbookVO> termbookList = termbookProc.listSearchPaging(params);
      model.addAttribute("termbookList", termbookList);

      // 전체 개수
      int search_count = termbookProc.list_search_count(params);

      // 페이징 HTML 생성
      String paging = termbookProc.pagingBox(now_page, searchTerm_title, searchCategory, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      return "/th/termbook/list_all";
  }



  // 3. List - 검색된 데이터 목록
  @GetMapping("/list_search")
  public String list_search_paging(@RequestParam(name="word", defaultValue = "") String word,
                                   @RequestParam(name="now_page", defaultValue="1") int now_page,
                                   Model model) {
    Map<String, Object> params = Map.of(
      "word", word,
      "nowPage", now_page,
      "recordsPerPage", record_per_page
    );

    List<TermbookVO> termbookList = termbookProc.listSearchPaging(params);
    model.addAttribute("termbookList", termbookList);

    int search_count = termbookProc.list_search_count(params);
    model.addAttribute("search_count", search_count);
    model.addAttribute("word", word);

    String paging = termbookProc.pagingBox(now_page, "", "", search_count, record_per_page, page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    int no = search_count - ((now_page - 1) * record_per_page);
    model.addAttribute("no", no);

    return "termbook/list_search"; // termbook/list_search.html
  }

  // 4. Read - 특정 데이터 읽기
//  @GetMapping("/read/{termbookno}")
//  public String read(@PathVariable("termbookno") int termbookno,
//                    @RequestParam(name="now_page", defaultValue = "1") int now_page,
//                    Model model) {
//    termbookVO termbookVO = termbookProc.read(termbookno);
//    model.addAttribute("termbookVO", termbookVO);
//
//    int search_count = termbookProc.list_search_count(Map.of());
//    String paging = termbookProc.pagingBox(now_page, "", "", "", list_file_name, search_count, record_per_page, page_per_block);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    int no = search_count - ((now_page - 1) * record_per_page);
//    model.addAttribute("no", no);
//
//    return "termbook/read"; // termbook/read.html
//  }
  
  //2. Read - 특정 데이터 읽기
  @GetMapping("/read/{termno}")
  public String read(@PathVariable("termno") int termno, Model model) {
     TermbookVO termbookVO = termbookProc.read(termno);
     model.addAttribute("termbookVO", termbookVO);
     
     // memo에 줄바꿈을 <br>로 변환
     String formattedMemo = termbookVO.getMemo().replace("\n", "<br>");
     model.addAttribute("formattedMemo", formattedMemo);  // 변환된 내용을 model에 추가
  
     return "/th/termbook/read"; // read.html로 이동
  }

  // 5. Update - 데이터 수정 폼
  @GetMapping("/update/{termno}")
  public String updateForm(@PathVariable("termno") int termno, Model model) {
   TermbookVO termbookVO = termbookProc.read(termno);
    model.addAttribute("termbookVO", termbookVO);
    return "/th/termbook/update"; // termbook/update.html
  }

  // 5. Update - 데이터 수정 처리
  @PostMapping("/update")
  public String update(@ModelAttribute TermbookVO termbookVO) {
    termbookProc.update(termbookVO);
    return "redirect:/termbook/list_all"; // 수정 후 목록 페이지로 이동
  }

  // 6. Delete - 데이터 삭제 폼
  @GetMapping("/delete/{termno}")
  public String deleteForm(@PathVariable("termno") int termno, Model model) {
    TermbookVO termbookVO = termbookProc.read(termno);
    model.addAttribute("termbookVO", termbookVO);
    return "/th/termbook/delete"; // termbook/delete.html
  }

  // 6. Delete - 데이터 삭제 처리
  @PostMapping("/delete")
  public String delete(@RequestParam("termno") int termno) {
    System.out.print("yes");
    termbookProc.delete(termno);
    return "redirect:/termbook/list_all"; // 삭제 후 목록 페이지로 이동
  }
  

}

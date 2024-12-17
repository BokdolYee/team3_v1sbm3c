package dev.mvc.newscate;

import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.contents.ContentsProcInter;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/cate")
public class NewsCateCont {
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;

  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc") // @Component("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 4;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/cate/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public NewsCateCont() {
    System.out.println("-> NewsCateCont created");
  }
  
//  @GetMapping(value="/create") //  http://localhost:9092/cate/create
//  @ResponseBody // html 파일 내용임.
//  public String create() {
//    return "<h2>Create test</h2>";
//  }
//  @GetMapping(value="/create") //  http://localhost:9092/cate/create
//  public String create() {
//    return "/cate/create"; // /templates/cate/create.html
//  }
  
  @GetMapping(value="/create") //  http://localhost:9092/cate/create
  public String create(Model model) {
    NewsCateVO newscateVO = new NewsCateVO();
    model.addAttribute("newscateVO", newscateVO);
    
    newscateVO.setGenre("장르");
    newscateVO.setName("카테고리 이름을 입력하세요."); // Form으로 초기값을 전달
    return "/cate/create"; // /templates/cate/create.html
  }
  /**
   * 등록처리,  http://localhost:9092/cate/create
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @param cateVO Form 태그 값-> 검증 -> cateVO 자동 저장, requestParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value="/create")
  public String create(Model model, @Valid @ModelAttribute("newscateVO") NewsCateVO newscateVO, BindingResult bindingResult) {
 //   System.out.println("-> create post");
    if(bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
   //   System.out.println("-> ERROR 발생");
      return "/cate/create"; //templates/cate/create.html
    }
    
    newscateVO.setGenre(newscateVO.getGenre().trim());
    newscateVO.setName(newscateVO.getName().trim());
    
//    System.out.println(movieVO.getName());
//    System.out.println(movieVO.getSeqno());
//    System.out.println(movieVO.getVisible());
    int cnt = this.newscateProc.create(newscateVO);
    System.out.println("-> cnt :" + cnt);
    
    if (cnt ==1) {
      //model.addAttribute("code", "create_success");
      //model.addAttribute("name", movieVO.getName());
      
      //return "redirect:/cate/list_all"; //  @GetMapping(value="/list_all")
      return "redirect:/cate/list_search";
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
    
    return "/cate/msg"; //templates/cate/msg.html
  }
  
  /**
   * 등록 폼 및 목록
   * @param model
   * @return
   */
  @GetMapping(value="/list_all") 
  public String list_all(Model model) {
    NewsCateVO newscateVO = new NewsCateVO();
    //movieVO.setGenre("분류");
    //movieVO.setName("카테고리 이름을 입력하세요.");
    
    //카테고리 그룹 목록
    ArrayList<String> list_genre = this.newscateProc.genreset();
    newscateVO.setGenre(String.join("/", list_genre));
    
    model.addAttribute("newscateVO", newscateVO);
    
  
    ArrayList<NewsCateVO> list = this.newscateProc.list_all();
    model.addAttribute("list", list);
    
//    ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
//    model.addAttribute("menu", menu);
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    
    return "/cate/list_all"; // templates/cate/list_all.html
  }
  
  /**
   * 조회
   *  http://localhost:9092/cate/read/1
   * @return
   */
  @GetMapping(value="/read/{newscateno}") 
  public String read(Model model, @PathVariable("newscateno") Integer newscateno,
                            @RequestParam(name="word", defaultValue = "") String word,
                            @RequestParam(name="now_page", defaultValue = "") int now_page) {
    NewsCateVO newscateVO = this.newscateProc.read(newscateno);
    model.addAttribute("newscateVO", newscateVO);
    

    //ArrayList<MovieVO> list = this.moviecateProc.list_all();
    //ArrayList<MovieVO> list = this.moviecateProc.list_search(word);
    ArrayList<NewsCateVO> list = this.newscateProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);
    
//  ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
//  model.addAttribute("menu", menu);
  
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    
 // --------------------------------------------------------------------------------------
    // 페이지 번호 목록 생성
    // --------------------------------------------------------------------------------------
    int search_count = this.newscateProc.list_search_count(word);
    String paging = this.newscateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    
    return "/cate/read";
  }
  
  /**
   * 수정폼
   *  http://localhost:9092/cate/update/1
   * @return
   */
  @GetMapping(value="/update/{newscateno}") 
  public String update(HttpSession session,Model model, @PathVariable("newscateno") Integer newscateno,
                               @RequestParam(name="word", defaultValue = "") String word,
                               @RequestParam(name="now_page", defaultValue = "") int now_page) {
    
    if (this.memberProc.isAdmin(session)) {
      NewsCateVO newscateVO = this.newscateProc.read(newscateno);
      model.addAttribute("newscateVO", newscateVO);
      

      //ArrayList<MovieVO> list = this.moviecateProc.list_all();
      ArrayList<NewsCateVO> list = this.newscateProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
//    ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
//    model.addAttribute("menu", menu);
    
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);
      
    //카테고리 그룹 목록
      ArrayList<String> list_genre = this.newscateProc.genreset();
      model.addAttribute("list_genre", String.join("/", list_genre));
      
      model.addAttribute("word", word);
      
      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.newscateProc.list_search_count(word);
      String paging = this.newscateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      return "/cate/update"; //templates/cate/update.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }

    
  }
  
  
  /**
   * 등록처리,  http://localhost:9092/cate/create
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @param NewsCateVO Form 태그 값-> 검증 -> cateVO 자동 저장, requestParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value="/update")
  public String update(HttpSession session, Model model, 
                               @Valid @ModelAttribute("newscateVO") NewsCateVO newscateVO, 
                               BindingResult bindingResult,
                               @RequestParam(name="word", defaultValue = "") String word,
                               @RequestParam(name="now_page", defaultValue = "") int now_page,
                               RedirectAttributes ra) {
    if (this.memberProc.isAdmin(session)) {
      //   System.out.println("-> update post");
      if(bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
     //   System.out.println("-> ERROR 발생");
        return "/cate/update"; //templates/cate/update.html
      }
      
//      System.out.println(movieVO.getName());
//      System.out.println(movieVO.getSeqno());
//      System.out.println(movieVO.getVisible());
      
      newscateVO.setGenre(newscateVO.getGenre().trim());
      newscateVO.setName(newscateVO.getName().trim());
      
      int cnt = this.newscateProc.update(newscateVO);
      System.out.println("-> cnt :" + cnt);
      
      if (cnt ==1) {
       // model.addAttribute("code", "update_success");
       // model.addAttribute("genre", movieVO.getGenre());
        //model.addAttribute("name", movieVO.getName());
        
        ra.addAttribute("word", word);
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
        
        return "redirect:/cate/update/" + newscateVO.getNewscateno();
        
      } else {
        model.addAttribute("code", "update_fail");
      }
      
      model.addAttribute("cnt", cnt);
      
      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.newscateProc.list_search_count(word);
      String paging = this.newscateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------
      
      return "/cate/msg"; //templates/cate/msg.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }


  }
  
  /**
   * 삭제폼
   *  http://localhost:9092/cate/delete/1
   * @return
   */
  @GetMapping(value="/delete/{newscateno}") 
  public String delete(HttpSession session, Model model, @PathVariable("newscateno") Integer newscateno,
                            @RequestParam(name="word", defaultValue="") String word,
                            @RequestParam(name="now_page", defaultValue = "") int now_page) {
    if (this.memberProc.isAdmin(session)) {
      NewsCateVO newscateVO = this.newscateProc.read(newscateno);
      model.addAttribute("newscateVO", newscateVO);
      

      //ArrayList<MovieVO> list = this.moviecateProc.list_all();
      ArrayList<NewsCateVO> list = this.newscateProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
//    ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
//    model.addAttribute("menu", menu);
    
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);
      
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.newscateProc.list_search_count(word);
      String paging = this.newscateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // ---------------
      return "/cate/delete"; //templates/cate/delete.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }

   
  }
  
  /**
   * 삭제처리,  http://localhost:9092/cate/delete
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @param NewsCateVO Form 태그 값-> 검증 -> cateVO 자동 저장, requestParameter() 자동 실행
   * @param bindingResult 폼에 에러가 있는지 검사 지원
   * @return
   */
  @PostMapping(value="/delete")
  public String delete_process(HttpSession session, Model model, 
      @RequestParam(name = "newscateno", defaultValue = "0") Integer newscateno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra) {
    if (this.memberProc.isAdmin(session)) {
      System.out.println("-> delete_process");
      
      NewsCateVO newscateVO = this.newscateProc.read(newscateno); //삭제전에 삭제 결과를 출력할 레코드 조회
      model.addAttribute("newscateVO", newscateVO);
      
      this.newscateProc.deleteContents(newscateno); // 자식 죽이기!!!
      
      this.contentsProc.updateCntCount(newscateno); // 자식 죽이기
      this.contentsProc.resetCnt(newscateno); // 자식 죽이기
      this.contentsProc.updateCnt(newscateno); // 자식 죽이기
      
      int cnt = this.newscateProc.delete(newscateno);
      System.out.println("-> cnt :" + cnt);
      
      if (cnt ==1) {
//        model.addAttribute("code", "delete_success");
//        model.addAttribute("genre", movieVO.getGenre());
//        model.addAttribute("name", movieVO.getName());
        ra.addAttribute("word", word); // redirect로 데이터 전송
        
        
        // ----------------------------------------------------------------------------------------------------------
        // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
        int search_cnt = this.newscateProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1; // 최소 시작 페이지
          }
        }
        
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
        
        return "redirect:/cate/list_search";
        
      } else {
        model.addAttribute("code", "delete_fail");
      }
      
      model.addAttribute("cnt", cnt);
      
      return "/cate/msg"; //templates/cate/msg.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }

   
  }
  
  /**
   *  <!--우선 순위 높임, 10 등 -> 1 등-->,  http://localhost:9092/cate/update_seqno_forward/1
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value="/update_seqno_forward/{newscateno}")
  public String update_seqno_forward(Model model, @PathVariable("newscateno") Integer newscateno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra ) {
    this.newscateProc.update_seqno_forward(newscateno);
     
    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
    
    return "redirect:/cate/list_search"; // @GetMapping(value="/list_all")
  }
  
  /**
   *  <!--우선 순위 높임, 10 등 -> 1 등-->,  http://localhost:9092/cate/update_seqno_backward/1
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value="/update_seqno_backward/{newscateno}")
  public String update_seqno_backward(Model model, @PathVariable("newscateno") Integer newscateno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra ) {
    this.newscateProc.update_seqno_backward(newscateno);
    
    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
    
    return "redirect:/cate/list_search"; // @GetMapping(value="/list_all")
  }
  
  /**
   *   <!-- 카테고리 공개 설정 -->,  http://localhost:9091/cate/update_visible_y/1
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value="/update_visible_y/{newscateno}")
  public String update_visible_y(HttpSession session, Model model, @PathVariable("newscateno") Integer newscateno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra ) {
    
    if (this.memberProc.isAdmin(session)) {
      this.newscateProc.update_visible_y(newscateno);
      
      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
      
      return "redirect:/cate/list_search"; // @GetMapping(value="/list_all")
    
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }
    
  }
  
  /**
   *   <!-- 카테고리 비공개 설정 -->,  http://localhost:9091/cate/update_visible_n/1
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value="/update_visible_n/{newscateno}")
  public String update_visible_n(HttpSession session, Model model, @PathVariable("newscateno") Integer newscateno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra ) {
    
    if (this.memberProc.isAdmin(session)) {
      this.newscateProc.update_visible_n(newscateno);
      
      ra.addAttribute("word", word); // redirect로 데이터 전송
      ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
      
      return "redirect:/cate/list_search"; // @GetMapping(value="/list_all")
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }

   
  }
  
//  /**
//   * 등록 폼 및 검색 목록
//   *  http://localhost:9091/cate/list_search
//   *  http://localhost:9091/cate/list_search?word=
//   *  http://localhost:9091/cate/list_search?word= 카페
//   * @param model
//   * @return
//   */
//  @GetMapping(value="/list_search") 
//  public String list_search(Model model,
//                                @RequestParam(name="word", defaultValue = "")String word) {
//    MovieVO movieVO = new MovieVO();
//    //movieVO.setGenre("분류");
//    //movieVO.setName("카테고리 이름을 입력하세요.");
//    
//    //카테고리 그룹 목록
//    ArrayList<String> list_genre = this.moviecateProc.genreset();
//    movieVO.setGenre(String.join("/", list_genre));
//    
//    model.addAttribute("movieVO", movieVO);
//    
//    word = Tool.checkNull(word);
//    
//    ArrayList<MovieVO> list = this.moviecateProc.list_search(word);
//    model.addAttribute("list", list);
//    
////    ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
////    model.addAttribute("menu", menu);
//    
//    ArrayList<MovieVOMenu> menu = this.moviecateProc.menu();
//    model.addAttribute("menu", menu);
//    
//    int search_cnt = this.moviecateProc.list_search_count(word);
//    model.addAttribute("search_cnt", search_cnt);
//    
//    model.addAttribute("word", word);
//    
//    return "/cate/list_search"; // templates/cate/list_search.html
//  }
  
  /**
  * 등록 폼 및 검색 목록
  *  http://localhost:9091/cate/list_search
  *  http://localhost:9091/cate/list_search?word=
  *  http://localhost:9091/cate/list_search?word= 카페
  * @param model
  * @return
  */
  @GetMapping(value="/list_search") 
  public String list_search_paging(HttpSession session,Model model,
                               @RequestParam(name="word", defaultValue = "")String word,
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
    if (this.memberProc.isAdmin(session)) {
      NewsCateVO newscateVO = new NewsCateVO();
      //movieVO.setGenre("분류");
      //movieVO.setName("카테고리 이름을 입력하세요.");
      
      //카테고리 그룹 목록
      ArrayList<String> list_genre = this.newscateProc.genreset();
      newscateVO.setGenre(String.join("/", list_genre));
      
      model.addAttribute("newscateVO", newscateVO);
      
      word = Tool.checkNull(word);
      
      ArrayList<NewsCateVO> list = this.newscateProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
     // ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
     // model.addAttribute("menu", menu);
      
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);
      
      int search_cnt = this.newscateProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);
      
      model.addAttribute("word", word);
      
      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.newscateProc.list_search_count(word);
      String paging = this.newscateProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------    
      
      return "/cate/list_search";  // /templates/cate/list_search.html
    } else {
      return "redirect:/member/login_cookie_need";  // redirect
    }


 }
 
}

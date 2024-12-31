package dev.mvc.contents;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVO;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.stock.StockProcInter;
import dev.mvc.stock.StockVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.news.NewsProcInter;
import dev.mvc.news.NewsVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping(value = "/contents")
@Controller
public class ContentsCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc") // @Component("dev.mvc.cate.CateProc")
  private NewsCateProcInter newscateProc;

  @Autowired
  @Qualifier("dev.mvc.news.NewsProc")
  private NewsProcInter newsProc;
  
  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc") // @Component("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;
  
  @Autowired
  @Qualifier("dev.mvc.stock.StockProc") // @Component("dev.mvc.contents.ContentsProc")
  private StockProcInter stockProc;

  public ContentsCont() {
    System.out.println("-> ContentsCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("contentsVO") ContentsVO contentsVO, 
      @ModelAttribute("newsVO") NewsVO newsVO,
      @RequestParam(name = "newscateno", defaultValue = "1") int newscateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page,
      @RequestParam(name = "newsno", defaultValue = "0") int newsno,
      @RequestParam(name = "stockno", defaultValue = "0") int stockno) {
      
      // 뉴스 카테고리 메뉴
      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);

      // 카테고리 정보 조회
      NewsCateVO newscateVO = this.newscateProc.read(newscateno);
      model.addAttribute("newscateVO", newscateVO);

      // 모든 뉴스 리스트 조회
      ArrayList<NewsVO> newsList = this.newsProc.list();
      model.addAttribute("newsList", newsList);

      ArrayList<StockVO> stockList = this.stockProc.list();
      model.addAttribute("stockList", stockList);
      
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      return "/th/contents/create"; // /templates/th/contents/create.html
  }


  /**
   * 등록 처리 
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
      HttpSession session, 
      @ModelAttribute("contentsVO") ContentsVO contentsVO,
      @RequestParam(name="newscateno", defaultValue="0") int newscateno,
      RedirectAttributes ra) {
    
      contentsVO.setNewscateno(newscateno);
    
      if (memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
     // 파일 업로드 처리
        String file1 = ""; // 원본 파일명 image
        String file1saved = ""; // 저장된 파일명, image
        String thumb1 = ""; // preview image

        String upDir = Contents.getUploadDir(); // 파일을 업로드할 폴더 준비
        MultipartFile mf = contentsVO.getFile1MF(); // 파일 처리

        if (!mf.isEmpty()) { // 파일이 있는 경우
          file1 = mf.getOriginalFilename(); // 원본 파일명
          long size1 = mf.getSize(); // 파일 크기

          if (Tool.checkUploadFile(file1)) { // 업로드 가능한 파일인지 검사
            file1saved = Upload.saveFileSpring(mf, upDir); // 파일 저장
            if (Tool.isImage(file1saved)) { // 이미지인지 검사
              thumb1 = Tool.preview(upDir, file1saved, 200, 150); // 썸네일 생성
            }

            // 파일 정보 설정
            contentsVO.setFile1(file1);
            contentsVO.setFile1saved(file1saved);
            contentsVO.setThumb1(thumb1);
            contentsVO.setSize1(size1);
          } else {
            ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 실패
            ra.addFlashAttribute("cnt", 0);
            ra.addFlashAttribute("url", "/Contents/msg");
            return "redirect:/Contents/msg";
          }
        }
          // contents 테이블에 데이터 등록
          int cnt = this.contentsProc.create(contentsVO);

          if (cnt == 1) {
              ra.addAttribute("newscateno", contentsVO.getNewscateno()); // controller -> controller
              return "redirect:/contents/list_by_cateno";
          } else {
              ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
              return "redirect:/contents/msg";
          }
      } else { // 관리자가 아닌 경우
          return "redirect:/member/login_cookie_need";
      }
  }


  /**
   * 전체 목록, 관리자만 사용 가능
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isAdmin(session)) { // 관리자만 조회 가능
      ArrayList<ContentsVO> list = this.contentsProc.list_all(); // 모든 목록

      model.addAttribute("list", list);
      return "/th/contents/list_all";

    } else {
      return "redirect:/member/login_cookie_need";

    }

  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 
   * 
   * @return
   */
  @GetMapping(value = "/list_by_cateno")
  public String list_by_cateno_search_paging(
      HttpSession session, 
      Model model, 
      @RequestParam(name = "newscateno", defaultValue = "1") int newscateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> cateno: " + cateno);

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    NewsCateVO newscateVO = this.newscateProc.read(newscateno);
    model.addAttribute("newscateVO", newscateVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("newscateno", newscateno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<ContentsVO> list = this.contentsProc.list_by_cateno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.contentsProc.list_by_cateno_search_count(map);
    String paging = this.contentsProc.pagingBox(newscateno, now_page, word, "/th/contents/list_by_cateno", search_count,
        Contents.RECORD_PER_PAGE, Contents.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/th/contents/list_by_cateno_search_paging"; // /templates/contents/list_by_cateno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * 
   * @return
   */
  @GetMapping(value = "/list_by_cateno_grid")
  public String list_by_cateno_search_paging_grid(HttpSession session, 
      Model model, 
      @RequestParam(name = "newscateno", defaultValue = "1") int newscateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> cateno: " + cateno);

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    NewsCateVO newscateVO = this.newscateProc.read(newscateno);
    model.addAttribute("newscateVO", newscateVO);
    
    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("newscateno", newscateno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<ContentsVO> list = this.contentsProc.list_by_cateno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.contentsProc.list_by_cateno_search_count(map);
    String paging = this.contentsProc.pagingBox(newscateno, now_page, word, "/th/contents/list_by_cateno_grid", search_count,
        Contents.RECORD_PER_PAGE, Contents.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Contents.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/contents/list_by_cateno_search_paging_grid.html
    return "/th/contents/list_by_cateno_search_paging_grid";
  }

  /**
   * 조회 
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name="contentno", defaultValue = "1") int contentno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page,
      @RequestParam(name = "newsno", defaultValue = "0") int newsno,
      @RequestParam(name = "stockno", defaultValue = "1") int stockno) {
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    ArrayList<NewsVO> newsList = this.newsProc.list();
    model.addAttribute("newsList", newsList);
    ArrayList<StockVO> stockList = this.stockProc.list();
    model.addAttribute("stockList", stockList);
    
    ContentsVO contentsVO = this.contentsProc.read(contentno);
    
    model.addAttribute("contentsVO", contentsVO);

    NewsCateVO newscateVO = this.newscateProc.read(contentsVO.getNewscateno());
    model.addAttribute("newscateVO", newscateVO);

    NewsVO newsVO = this.newsProc.read(contentsVO.getNewsno());
    model.addAttribute("newsVO", newsVO);
    
    StockVO stockVO = this.stockProc.read(contentsVO.getStockno());
    model.addAttribute("stockVO", stockVO);
    
    long size1 = contentsVO.getSize1();
    String size1_label = Tool.unit(size1);
    contentsVO.setSize1_label(size1_label);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/th/contents/read";
  }


  /**
   * 수정 폼 http:// localhost:9091/contents/update_text?contentno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @RequestParam(name="contentno", defaultValue = "1") int contentno,
      RedirectAttributes ra, 
      @RequestParam(name="word", defaultValue = "") String word,
      @RequestParam(name="now_page", defaultValue = "0") int now_page,
      @RequestParam(name = "newsno", defaultValue = "0") int newsno) {
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한경우
      ContentsVO contentsVO = this.contentsProc.read(contentno);
      model.addAttribute("contentsVO", contentsVO);

      NewsCateVO newscateVO = this.newscateProc.read(contentsVO.getNewscateno());
      model.addAttribute("newscateVO", newscateVO);

      NewsVO newsVO = this.newsProc.read(contentsVO.getNewsno());
      model.addAttribute("newsVO", newsVO);
      
      return "/th/contents/update_text";

    } else {
      return "/th/member/login_cookie_need"; // /templates/th/member/login_cookie_need.html
    }

  }

  /**
   * 수정 처리 http://localhost:9091/contents/update_text?contentno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, 
      Model model, 
      @ModelAttribute("contentsVO") ContentsVO contentsVO, 
      RedirectAttributes ra,
      @RequestParam(name="search_word", defaultValue = "") String search_word, // contentsVO.word와 구분 필요
      @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    
    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.memberProc.isAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("contentno", contentsVO.getContentno());
      map.put("passwd", contentsVO.getPasswd());

      if (this.contentsProc.password_check(map) == 1) { // 패스워드 일치
        this.contentsProc.update_text(contentsVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("contentno", contentsVO.getContentno());
        ra.addAttribute("newscateno", contentsVO.getNewscateno());
        return "redirect:/contents/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/contents/msg"); // msg.html, redirect parameter 적용

        return "redirect:/contents/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/contents/post2get"; // @GetMapping(value = "/msg")
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9091/contents/update_file?contentno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
                                     @RequestParam(name="contentno", defaultValue = "0") int contentno,
                                     @RequestParam(name="word", defaultValue = "") String word, 
                                     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    ContentsVO contentsVO = this.contentsProc.read(contentno);
    model.addAttribute("contentsVO", contentsVO);

    NewsCateVO newscateVO = this.newscateProc.read(contentsVO.getNewscateno());
    model.addAttribute("newscateVO", newscateVO);
    
    NewsVO newsVO = this.newsProc.read(contentsVO.getNewsno());
    model.addAttribute("newsVO", newsVO);

    return "/th/contents/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/contents/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                                     @ModelAttribute("contentsVO") ContentsVO contentsVO,
                                     @RequestParam(name="word", defaultValue = "") String word, 
                                     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.memberProc.isAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      ContentsVO contentsVO_old = contentsProc.read(contentsVO.getContentno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = contentsVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = contentsVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Contents.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = contentsVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      contentsVO.setFile1(file1);
      contentsVO.setFile1saved(file1saved);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.contentsProc.update_file(contentsVO); // Oracle 처리
      ra.addAttribute ("contentno", contentsVO.getContentno());
      ra.addAttribute("newscateno", contentsVO.getNewscateno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/contents/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/contents/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/contents/delete?contentno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
      @RequestParam(name="newscateno", defaultValue = "0") int newscateno,
      @RequestParam(name="contentno", defaultValue = "0") int contentno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page,
      @RequestParam(name = "stockno", defaultValue = "0") int stockno) {

    if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
      model.addAttribute("newscateno", newscateno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
      model.addAttribute("menu", menu);
      ArrayList<StockVO> stockList = this.stockProc.list();
      model.addAttribute("stockList", stockList);

      // 콘텐츠 정보 가져오기
      ContentsVO contentsVO = this.contentsProc.read(contentno);
      model.addAttribute("contentsVO", contentsVO);

      // 뉴스 카테고리 정보 가져오기
      NewsCateVO newscateVO = this.newscateProc.read(contentsVO.getNewscateno());
      model.addAttribute("newscateVO", newscateVO);

      // stockno로 StockVO 객체를 가져와서 초기화
      StockVO stockVO = this.stockProc.read(contentsVO.getStockno());
      model.addAttribute("stockVO", stockVO);
      System.out.println(stockno);
      return "/th/contents/delete"; // forward

    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/contents/msg"; 
    }
  }

  
  /**
   * 삭제 처리 http://localhost:9091/contents/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra, Model model,
      @RequestParam(name="newscateno", defaultValue = "0") int newscateno,
      @RequestParam(name="contentno", defaultValue = "0") int contentno,
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page,
      @RequestParam(name="stockno", defaultValue = "0") int stockno) {
    
    StockVO stockVO = this.stockProc.read(stockno);
    model.addAttribute("stockVO", stockVO);
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    ContentsVO ContentsVO_read = contentsProc.read(contentno);
        
    String file1saved = ContentsVO_read.getFile1saved();
    String thumb1 = ContentsVO_read.getThumb1();
    
    String uploadDir = Contents.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
    
    this.contentsProc.delete(contentno); // DBMS 삭제
    this.contentsProc.updateCntCount(contentno);
    this.contentsProc.resetCnt(contentno);
    this.contentsProc.updateCnt(contentno);
    this.contentsProc.delete(contentno); // DBMS 글 삭제
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("newscateno", newscateno);
    map.put("word", word);
    
    if (this.contentsProc.list_by_cateno_search_count(map) % Contents.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("newscateno", newscateno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/contents/list_by_cateno";    
    
  }   
   
 
}


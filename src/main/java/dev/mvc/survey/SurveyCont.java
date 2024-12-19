package dev.mvc.survey;

import java.util.ArrayList;

import org.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;



import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/survey")
public class SurveyCont {
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/survey/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  // // http://localhost:9091/survey/create
    @GetMapping(value = "/create")
    public String create(HttpSession session, Model model) {
     if (this.memberProc.isAdmin(session)) {
        SurveyVO surveyVO = new SurveyVO();
        model.addAttribute("surveyVO", surveyVO);
        
        return "/survey/create";
      } else {
        return "redirect:/member/login_cookie_need";
      }
    }
    
  /**
  * 등록 처리, http://localhost:9093/cate/create
  */ 
    @PostMapping(value = "/create")
    public String create(HttpServletRequest request,
                                HttpSession session,
                                Model model, 
                                @ModelAttribute("surveyVO") SurveyVO surveyVO, 
                                BindingResult bindingResult,
                                RedirectAttributes ra) {
      if (memberProc.isAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = "";
      String file1saved = ""; // 저장된 파일명, image
      String poster = ""; // preview image

      String upDir = Survey.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = surveyVO.getFile1MF();
      System.out.println("-> mf: " + mf);
      
      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            poster = Tool.preview(upDir, file1saved, 200, 150);
          }

          surveyVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          surveyVO.setPoster(poster); // 원본이미지 축소판
          surveyVO.setFile1(file1);
          surveyVO.setSize1(size1);
        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/contents/msg"); // msg.html, redirect parameter 적용
          return "redirect:/contents/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------
      
      if (bindingResult.hasErrors() == true) {
        return "/survey/create";
       }
      
      int cnt = this.surveyProc.create(surveyVO);
      System.out.println("-> cnt: " + cnt);
      
      if (cnt == 1) {
        return "redirect:/survey/list_search";
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/survey/msg"); // msg.html, redirect parameter 적용
        return "redirect:/survey/msg"; // Post -> Get - param...
      }
      
      
      
      
      } else { // 로그인 실패 한 경우
        return "redirect:/member/login_cookie_need"; // /member/login_cookie_need.html
      }      

    }
    
    /**
     * 등록 폼 및 목록
     * http://localhost:9093/survey/list_all
     * @param model
     * @return
     */
    @GetMapping(value = "/list_all")
    public String list_all(HttpSession session, Model model,
                                @RequestParam(name="word", defaultValue = "") String word,
                                @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                @RequestParam(name="now_page", defaultValue = "1") int now_page) {
      if (this.memberProc.isAdmin(session)) {
      SurveyVO surveyVO = new SurveyVO();
      model.addAttribute("surveyVO", surveyVO);
      
      ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, now_page);
      model.addAttribute("list", list);
      
      
      model.addAttribute("word", word);
      
      // 페이지 번호 목록 생성
      int search_count = this.surveyProc.list_search_count(word);
      String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------      
     
      return "/survey/list_all";
      } else {
        return "redirect:/member/login_cookie_need"; // redirect
      }
    }
    

    /**
     * 조회 http://localhost:9093/survey/read/1
     */
    @GetMapping(value = "/read/{surveyno}")
    public String read(Model model, @PathVariable("surveyno") Integer surveyno) {
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);

      return "/survey/read";    
    }    
    
    /**
     * 수정폼 http://localhost:9093/survey/update/1
     */
    @GetMapping(value = "/update/{surveyno}")
    public String update(HttpSession session, Model model, 
                                  @RequestParam(name="word", defaultValue = "") String word,
                                  @PathVariable("surveyno") Integer surveyno,
                                  @RequestParam(name = "now_page", defaultValue = "") int now_page) {
      if (this.memberProc.isAdmin(session)) {
        SurveyVO surveyVO = this.surveyProc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);

        // 페이지 번호 목록 생성
        int search_count = this.surveyProc.list_search_count(word);
        String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        return "/survey/update"; // templaes/cate/update.html
      } else {
        return "redirect:/member/login_cookie_need"; // redirect
      }
    }
    
    /**
     * 수정 처리, http://localhost:9091/cate/update
     * 
     * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
     * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
     *                      자동 실행
     * @param bindingResult 폼에 에러가 있는지 검사 지원
     * @return
     */
    @PostMapping(value = "/update")
    public String update(HttpSession session, Model model, @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, BindingResult bindingResult,
                                    @RequestParam(name = "now_page", defaultValue = "") int now_page, 
                                    @RequestParam(name="word", defaultValue = "") String word,
                                    RedirectAttributes ra) {
      if (this.memberProc.isAdmin(session)) {
        
        if (bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
          return "/cate/update"; // /templates/cate/update.html
        }

//      System.out.println(cateVO.getName());
//      System.out.println(cateVO.getSeqno());
//      System.out.println(cateVO.getVisible());

      int cnt = this.surveyProc.update(surveyVO);
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

        return "redirect:/survey/update/" + surveyVO.getSurveyno(); // @GetMapping(value="/update/{cateno}")
      } else {
        model.addAttribute("code", "update_fail");
      }

      model.addAttribute("cnt", cnt);

      // 페이지 번호 목록 생성
      int search_count = this.surveyProc.list_search_count(word);
      String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      return "/survey/msg"; // /templates/cate/msg.html
      } else {
        return "redirect:/member/login_cookie_need";  // redirect
      }
      
    }
    
    /**
     * 삭제폼 http://localhost:9091/cate/delete/1
     */
    @GetMapping(value = "/delete/{surveyno}")
    public String delete(HttpSession session, Model model, @PathVariable("surveyno") Integer surveyno,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "now_page", defaultValue = "") int now_page) {
      if (this.memberProc.isAdmin(session)) {
        SurveyVO surveyVO = this.surveyProc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        model.addAttribute("now_page", now_page);

        // 페이지 번호 목록 생성
        int search_count = this.surveyProc.list_search_count(word);
        String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);


        return "/survey/delete"; // templaes/cate/delete.html

      } else {
        return "redirect:/member/login_cookie_need";  // redirect
      }
      
    }

    /**
     * 삭제 처리, http://localhost:9091/cate/delete?cateno=1
     * 
     * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
     * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
     *                      자동 실행
     * @param bindingResult 폼에 에러가 있는지 검사 지원
     * @return
     */
    @PostMapping(value = "/delete")
    public String delete_process(HttpSession session, Model model, @RequestParam(name = "surveyno", defaultValue = "0") Integer surveyno,
        @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
      if (this.memberProc.isAdmin(session)) {
        System.out.println("-> delete_process");

        SurveyVO surveyVO = this.surveyProc.read(surveyno); // 삭제전에 삭제 결과를 출력할 레코드 조회
        model.addAttribute("surveyVO", surveyVO);
        
        
        int cnt = this.surveyProc.delete(surveyno);
        System.out.println("-> cnt: " + cnt);

        return "/survey/msg"; // /templates/cate/msg.html
      } else {
        return "redirect:/member/login_cookie_need";  // redirect
      }

    }
    
    /**
     * 등록 폼 및 검색 목록 + 페이징
     * @param model
     * @return
     */
    @GetMapping(value = "/list_search")
    public String list_search_paging(HttpSession session, Model model,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
        @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
     
      if (this.memberProc.isAdmin(session)) {
        SurveyVO surveyVO = new SurveyVO();

        
        model.addAttribute("surveyVO", surveyVO);
        
        word = Tool.checkNull(word);

        ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, this.record_per_page);
        model.addAttribute("list", list);


        int search_cnt = this.surveyProc.list_search_count(word);
        model.addAttribute("search_cnt", search_cnt);

        model.addAttribute("word", word); // 검색어

        int search_count = this.surveyProc.list_search_count(word);
        String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        int no = search_count - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);

        return "/survey/list_search"; 
      } else {
        return "redirect:/member/login_cookie_need";
      }
    }
}

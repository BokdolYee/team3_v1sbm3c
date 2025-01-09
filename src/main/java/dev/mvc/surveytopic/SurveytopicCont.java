package dev.mvc.surveytopic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;


import dev.mvc.member.MemberProcInter;
import dev.mvc.survey.SurveyProcInter;
import dev.mvc.survey.SurveyVO;
import dev.mvc.surveygood.SurveySurveygoodMemberVO;
import dev.mvc.surveygood.SurveygoodProcInter;
import dev.mvc.surveyitem.SurveyitemProcInter;
import dev.mvc.surveyitem.SurveyitemVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/surveytopic")
public class SurveytopicCont {
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/surveytopic/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.surveytopic.SurveytopicProc")
  private SurveytopicProcInter surveytopicProc;

  @Autowired
  @Qualifier("dev.mvc.surveyitem.SurveyitemProc")
  private SurveyitemProcInter surveyitemProc;
  
  @Autowired
  @Qualifier("dev.mvc.surveygood.SurveygoodProc")
  private SurveygoodProcInter surveygoodProc;
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyproc;
  
  
  @GetMapping(value = "/create")
  public String create(HttpSession session, Model model,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO) {


          SurveyVO surveyVO = this.surveyproc.read(surveyno);
          model.addAttribute("surveyVO", surveyVO);

          
          return "/th/surveytopic/create"; // survey/create.html로 리턴
      } 
  

    
  @PostMapping(value = "/create")
  public String create(
      HttpServletRequest request,
      HttpSession session,
      Model model,
      @Valid @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO,
      @RequestParam(name = "surveyno", defaultValue = "") int surveyno,
      BindingResult bindingResult,
      RedirectAttributes ra) {
    if (this.memberProc.isAdmin(session)) {
    // 유효성 검사 실패 시 다시 입력 폼으로 이동
    if (bindingResult.hasErrors()) {
      return "/th/surveytopic/create"; // templates/survey/create.html
    }
    System.out.println("Received surveyno: " + surveytopicVO.getSurveyno());
    
   

    // 데이터베이스에 설문 데이터 저장
    surveytopicVO.setTitle(surveytopicVO.getTitle().trim()); // 제목 공백 제거
    int cnt = this.surveytopicProc.create(surveytopicVO);   // 개별문제 등록
    if (cnt == 1) {
      return "redirect:/surveytopic/read?" + "surveyno=" + surveytopicVO.getSurveyno();  // 설문조사 선택화면으로 이동";
    } else {
      model.addAttribute("code", "create_fail"); // 등록 실패 메시지
    }

    model.addAttribute("cnt", cnt); // 결과 추가
    return "/th/surveytopic/msg"; // 메시지 페이지로 이동
  } else {
    return "redirect:/member/login_cookie_need"; // redirect
  }
 }

    

     

  /**
   * 조회 http://localhost:9093/surveytopic/read/1
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model,
      @RequestParam(name="surveyno", defaultValue = "") Integer surveyno) {
    
    
    
    SurveyVO surveyVO = this.surveyproc.read(surveyno);
    model.addAttribute("surveyVO", surveyVO);
    
    // 추천 관련
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("surveyno", surveyno);

    int hartCnt = 0;
    if (session.getAttribute("memberno") != null) { // 회원인 경우만 카운트 처리
        int memberno = (int) session.getAttribute("memberno");
        map.put("memberno", memberno);

        hartCnt = this.surveygoodProc.hartCnt(map);
    }

    model.addAttribute("hartCnt", hartCnt);
  
 
                
    ArrayList<SurveytopicVO> surveytopicList = this.surveytopicProc.listBySurveyno(surveyno);
    model.addAttribute("surveytopicList", surveytopicList);
    
     // 각 주제에 대한 항목을 저장할 맵
     Map<Integer, ArrayList<SurveyitemVO>> itemsByTopic = new HashMap<>();

        // 각 설문 주제를 반복하면서 해당 설문 주제 번호로 항목 조회
        for (SurveytopicVO topic : surveytopicList) {
            ArrayList<SurveyitemVO> surveyitemList = this.surveyitemProc.listBySurveytopicno(topic.getSurveytopicno());
            itemsByTopic.put(topic.getSurveytopicno(), surveyitemList); // 주제 번호를 키로 하고 항목 리스트를 값으로 저장
            System.out.println("Items for topic " + topic.getSurveytopicno() + ": " + surveyitemList.size());
        }
        
     // 추가: surveytopicVO를 모델에 추가
        if (!surveytopicList.isEmpty()) {
            model.addAttribute("surveytopicVO", surveytopicList.get(0));  // 첫 번째 주제를 추가
        }
        
        model.addAttribute("itemsByTopic", itemsByTopic);
        
    System.out.println("-> surveytopicList:" + surveytopicList );
    
    return "/th/surveytopic/read";    
  }    
    
  @PostMapping("/submit-responses")
  @ResponseBody
  public ResponseEntity<?> handleSurveyResponses(@ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO, 
                                                 @RequestBody Map<String, List<String>> surveyResponses) {
      
      // 수신된 설문 응답 로그
      System.out.println("수신된 설문 응답: " + surveyResponses);

      for (Map.Entry<String, List<String>> entry : surveyResponses.entrySet()) {
          List<String> items = entry.getValue(); // 선택된 항목 목록

          for (String item : items) {
              // item으로 항목 이름을 사용
            System.out.println("-> item: "+ item);
              this.surveyitemProc.increaseItemCount(item); // 항목 수 증가
          }
      }

      // 성공적인 응답을 JSON 형식으로 반환
      Map<String, String> response = new HashMap<>();
      response.put("message", "응답이 성공적으로 처리되었습니다.");
      response.put("receivedData", surveyResponses.toString()); // 수신된 데이터 로깅

      // 적절한 응답을 반환합니다.
      return ResponseEntity.ok(response); // JSON으로 반환
  }

  
    /**
     * 
     * 수정폼 http://localhost:9093/survey/update/1
     */
    @GetMapping(value = "/update")
    public String update(HttpSession session, Model model, 
                                  @RequestParam(name="surveytopicno", defaultValue = "0") int surveytopicno,
                                  @RequestParam(name = "surveyno", defaultValue = "0") int surveyno) {
      if (this.memberProc.isAdmin(session)) {
        model.addAttribute("surveytopicno", surveytopicno);
        model.addAttribute("surveyno", surveyno); 
        
        SurveyVO surveyVO = this.surveyproc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);
        

  
        return "/th/surveytopic/update"; // templaes/cate/update.html
      } else {
        return "redirect:/member/login_cookie_need"; // redirect
      }
    }
    
    /**
     * 파일 수정 처리
     * 
     * @return
     */
    @PostMapping(value = "/update")
    public String update(HttpSession session, Model model, RedirectAttributes ra,
        @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO,
        @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
        @RequestParam(name = "surveyno", defaultValue = "0") int surveyno) {

        // 관리자 권한 체크
        if (this.memberProc.isAdmin(session)) {

          
            // Survey 업데이트
            int cnt = this.surveytopicProc.update(surveytopicVO);
            System.out.println("-> cnt: " + cnt);



            return "redirect:/surveytopic/read?" + "surveyno=" + surveytopicVO.getSurveyno();  // 설문조사 선택화면으로 이동";
        } else {
            return "redirect:/member/login_cookie_need"; // 권한이 없을 경우 로그인 페이지로 리다이렉트
        }
    }


   
    
    /**
     * 삭제폼 http://localhost:9091/cate/delete/1
     */
    @GetMapping(value = "/delete")
    public String delete(HttpSession session, Model model, 
                                @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
                                @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                RedirectAttributes ra) {
      if (this.memberProc.isAdmin(session)) {
        model.addAttribute("surveytopicno", surveytopicno);
        model.addAttribute("surveyno", surveyno); 
        
        SurveyVO surveyVO = this.surveyproc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);

        return "/th/surveytopic/delete"; // templaes/cate/delete.html
        
      } else {
        model.addAttribute("code", "delete_fail");
        return "redirect:/surveytopic/msg";  // redirect
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
    public String delete_process(HttpSession session, Model model, 
                                           @RequestParam(name = "surveytopicno", defaultValue = "0") Integer surveytopicno,
                                            @RequestParam(name = "surveyno", defaultValue = "0") Integer surveyno) {
      if (this.memberProc.isAdmin(session)) {
        System.out.println("-> delete_process");

        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno); // 삭제전에 삭제 결과를 출력할 레코드 조회
        model.addAttribute("surveytopicVO", surveytopicVO);
        
        
        int cnt = this.surveytopicProc.delete(surveytopicno);
        System.out.println("-> cnt: " + cnt);
       
        return "redirect:/surveytopic/read?" + "surveyno=" + surveytopicVO.getSurveyno();  // 설문조사 선택화면으로 이동";
        
      } else {
        model.addAttribute("code", "delete_fail");
        return "redirect:/surveytopic/msg";
      }
   
    }
    
//    @GetMapping(value = "/list_all")
//    public String list_all(HttpSession session, Model model,
//          @RequestParam(name="word", defaultValue = "") String word,
//          @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
//          @RequestParam(name="now_page", defaultValue = "1") int now_page) {
//  
//  
//      SurveytopicVO surveytopicVO = new SurveytopicVO();
//  model.addAttribute("surveytopicVO", surveytopicVO);
//  
//  ArrayList<SurveySurveytopicVO> list = this.surveytopicProc.list_search_paging_join();
//  model.addAttribute("list", list);
//  
//  
//  model.addAttribute("word", word);
//  
//  // 페이지 번호 목록 생성
//  int search_count = this.surveytopicProc.list_search_count(word);
//  String paging = this.surveytopicProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
//  this.page_per_block);
//  model.addAttribute("paging", paging);
//  model.addAttribute("now_page", now_page);
//  
//  // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
//  int no = search_count - ((now_page - 1) * this.record_per_page);
//  model.addAttribute("no", no);
//  // --------------------------------------------------------------------------------------      
//  
//  return "/surveytopic/list_all";
//  } 

    @GetMapping(value = "/list_search")
    public String list_search_paging(HttpSession session, Model model,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
        @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
        
        word = Tool.checkNull(word);  // 검색어 처리

        // 리스트를 가져오는 부분
        ArrayList<SurveySurveytopicVO> list = this.surveytopicProc.list_search_paging_join();
        model.addAttribute("list", list);
        
        System.out.println("-> list" + list);
        
        // 검색 결과 개수 처리
        int search_cnt = this.surveytopicProc.list_search_count(word);
        model.addAttribute("search_cnt", search_cnt);

        // 페이징 처리
        String paging = this.surveytopicProc.pagingBox(now_page, word, this.list_file_name, search_cnt, this.record_per_page, this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        int no = search_cnt - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);

        // 화면에 전달할 모델을 반환
        return "/th/surveytopic/list_search";
    }
}


    
    


 

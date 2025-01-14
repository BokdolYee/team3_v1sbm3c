package dev.mvc.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.calendargood.CalendargoodProcInter;
import dev.mvc.calendargood.CalendargoodVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVO;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.stock.StockVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/calendar")
public class CalendarCont {
  @Autowired
  @Qualifier("dev.mvc.calendar.CalendarProc")
  private CalendarProcInter calendarProc;
  
  @Autowired
  @Qualifier("dev.mvc.newscate.NewsCateProc")
  private NewsCateProcInter newscateProc;
    
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.calendargood.CalendargoodProc") 
  CalendargoodProcInter calendargoodProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 4;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;  
  
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
  
  
  /**
   * 등록폼
   * @param model
   * @param calendarVO
   * @param bindingResult
   * @return
   */
  @GetMapping(value="/create")
  public String create(Model model) {
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);
    
    return "/th/calendar/create";    
  }
  
  /**
   * 등록 처리
   * @param model
   * @param calendarVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpSession session, Model model, 
           @ModelAttribute("calendarVO") CalendarVO calendarVO) {
    
    int memberno = 3; // 테스트용
    calendarVO.setMemberno(memberno);
    
    int cnt = this.calendarProc.create(calendarVO);

    if (cnt == 1) {
      // model.addAttribute("code", "create_success");
      // model.addAttribute("name", cateVO.getName());

      return "redirect:/calendar/list_all"; // @GetMapping(value="/list_all")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/th/calendar/msg"; 
  }
  
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, 
      @RequestParam(name = "searchLabel", defaultValue = "") String searchLabel,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page,
      Model model) {
    
 // start_num 계산: 1페이지일 때 0, 2페이지일 때 10, 3페이지일 때 20
    int start_num = (now_page - 1) * record_per_page + 1;
    // end_num 계산: start_num + record_per_page - 1
    int end_num = start_num + record_per_page - 1;
    
    Map<String, Object> params = new HashMap<>();
    params.put("searchLabel", searchLabel);
    params.put("nowPage", now_page);
    params.put("start_num", start_num); // start_num
    params.put("end_num", end_num);     // end_num
    params.put("recordsPerPage", this.record_per_page);
    
    // 페이징 및 검색 데이터 목록
    List<CalendarVO> calendarList = calendarProc.listSearchPaging(params);
    model.addAttribute("calendarList", calendarList);

    // 전체 개수
    int search_count = calendarProc.list_search_count(params);
    
    // 페이징 HTML 생성
    String paging = calendarProc.pagingBox(now_page, searchLabel, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
//    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
//    model.addAttribute("menu", menu);

    return "/th/calendar/list_all"; // /templates/calendar/list_all.html
  }
  
//3. List - 검색된 데이터 목록
 @GetMapping("/list_search")
 public String list_search_paging(@RequestParam(name="word", defaultValue = "") String word,
                                  @RequestParam(name="now_page", defaultValue="1") int now_page,
                                  Model model) {
   Map<String, Object> params = Map.of(
     "word", word,
     "nowPage", now_page,
     "recordsPerPage", record_per_page
   );

   List<CalendarVO> calendarList = calendarProc.listSearchPaging(params);
   model.addAttribute("calendarList", calendarList);

   int search_count = calendarProc.list_search_count(params);
   model.addAttribute("search_count", search_count);
   model.addAttribute("word", word);

   String paging = calendarProc.pagingBox(now_page, "", search_count, record_per_page, page_per_block);
   model.addAttribute("paging", paging);
   model.addAttribute("now_page", now_page);

   int no = search_count - ((now_page - 1) * record_per_page);
   model.addAttribute("no", no);

   return "/th/calendar/list_search"; // stock/list_search.html
 }
  
  /**
   * 조회
   * 
   * @return
   */
  @GetMapping(path = "/read/{calendarno}")
  public String read(HttpSession session, Model model, 
      @PathVariable("calendarno") int calendarno) {
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    this.calendarProc.increaseCnt(calendarno); // 조회수 증가
    
    CalendarVO calendarVO = this.calendarProc.read(calendarno);

    model.addAttribute("calendarVO", calendarVO);
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("calendarno", calendarno);
    
    int heartCnt = 0;
    if (session.getAttribute("memberno") !=null ) { // 회원인 경우만 카운트 처리
      int memberno = (int)session.getAttribute("memberno");
      map.put("memberno", memberno);
      
      heartCnt = this.calendargoodProc.heartCnt(map);
    } 
    
    model.addAttribute("heartCnt", heartCnt);

    return "/th/calendar/read";
  }
    
  /**
   * 수정 폼
   *
   */
  @GetMapping(value = "/update")
  public String update_text(HttpSession session, 
      Model model, 
      @RequestParam(name="calendarno", defaultValue = "0") int calendarno, 
      RedirectAttributes ra) {    
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한경우
      CalendarVO calendarVO = this.calendarProc.read(calendarno);
      model.addAttribute("calendarVO", calendarVO);

      return "/th/calendar/update"; // /templates/calendar/update.html
    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/contents/msg"; // @GetMapping(value = "/msg")
      return "/th/member/login_cookie_need"; 
    }

  }
    
  /**
   * 수정 처리
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, 
      Model model, 
      @ModelAttribute("calendarVO") CalendarVO calendarVO, 
      RedirectAttributes ra) {
    
    if (this.memberProc.isAdmin(session)) { // 관리자 로그인 확인
      this.calendarProc.update(calendarVO); // 글수정

      return "redirect:/calendar/read/" + calendarVO.getCalendarno(); // @GetMapping(value = "/read")

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/calendar/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
  
  /**
   * 삭제폼
   */
  @GetMapping(path = "/delete/{calendarno}")
  public String delete(HttpSession session, 
      Model model, 
      @PathVariable("calendarno") int calendarno, 
      RedirectAttributes ra) {    
    
    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    if (this.memberProc.isAdmin(session)) {
      CalendarVO calendarVO = this.calendarProc.read(calendarno);
      model.addAttribute("calendarVO", calendarVO);

      return "/th/calendar/delete"; // /templates/calendar/delete.html
    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/contents/msg"; // @GetMapping(value = "/msg")
      return "/th/member/login_cookie_need"; 
    }
  }

  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="calendarno", defaultValue = "0") int calendarno, 
      RedirectAttributes ra) {    
    
    if (this.memberProc.isAdmin(session)) { // 관리자 로그인 확인
      this.calendarProc.delete(calendarno);

      return "redirect:/calendar/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/calendar/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
  /**
   * 특정 날짜의 목록
   * 현재 월: http://localhost:9091/calendar/list_calendar
   * 이전 월: http://localhost:9091/calendar/list_calendar?year=2024&month=12 
   * 다음 월: http://localhost:9091/calendar/list_calendar?year=2024&month=1
   * @param model
   * @return
   */
  @GetMapping(value = "/list_calendar")
  public String list_calendar(Model model,
      @RequestParam(name="year", defaultValue = "0") int year,
      @RequestParam(name="month", defaultValue = "0") int month) {
    
    if (year == 0) {
        // 현재 날짜를 가져옴
        LocalDate today = LocalDate.now();

        // 년도와 월 추출
        year = today.getYear();
        month = today.getMonthValue();
    } 
    
    String month_str = String.format("%02d", month); // 두 자리 형식으로
    System.out.println("-> month: " + month_str);
  
    String date = year + "-" + month;
    System.out.println("-> date: " + date);
    
//    ArrayList<CalendarVO> list = this.calendarProc.list_calendar(date);
//    model.addAttribute("list", list);

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    
    model.addAttribute("year", year);
    model.addAttribute("month", month-1);  // javascript는 1월이 0임. 
    
    return "/th/calendar/list_calendar"; // /templates/calendar/list_calendar.html
  }
  
  /**
   * 특정 날짜의 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/calendar/list_calendar_day?labeldate=2025-01-03
  @GetMapping(value = "/list_calendar_day")
  @ResponseBody
  public String list_calendar_day(Model model, @RequestParam(name="labeldate", defaultValue = "") String labeldate) {
  System.out.println("-> labeldate: " + labeldate);
  
    ArrayList<CalendarVO> list = this.calendarProc.list_calendar_day(labeldate);
    model.addAttribute("list", list);

    JSONArray schedule_list = new JSONArray();
    
    for (CalendarVO calendarVO: list) {
        JSONObject schedual = new JSONObject();
        schedual.put("calendarno", calendarVO.getCalendarno());
        schedual.put("labeldate", calendarVO.getLabeldate());
        schedual.put("label", calendarVO.getLabel());
        schedual.put("seqno", calendarVO.getSeqno());
        
        schedule_list.put(schedual);
    }

    return schedule_list.toString();
    
  }
  
  /**
   * 추천 처리
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, RedirectAttributes ra, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int calendarno = (int)src.get("calendarno"); // 값 가져오기
    System.out.println("-> calendarno: " + calendarno);
    
    
    if (this.memberProc.isMember(session) || this.memberProc.isAdmin(session)) { // 회원 확인
      // 추천을 한 상태
      int memberno = (int)session.getAttribute("memberno");
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("calendarno", calendarno);
      map.put("memberno", memberno);
      
      int good_cnt = this.calendargoodProc.heartCnt(map);
      System.out.println("-> good_cnt: " + good_cnt);
      
          
      if (good_cnt == 1) {
        System.out.println("-> 추천 해제: " + calendarno + ' ' + memberno);
        // 추천 해제
        CalendargoodVO calendargoodVO = this.calendargoodProc.readByboth(map);
        
        this.calendargoodProc.delete(calendargoodVO.getCalendargoodno());
        this.calendarProc.decreaseRecom(calendarno);
        
      } else {
        System.out.println("-> 추천: " + calendarno + ' ' + memberno);
        // 추천
        CalendargoodVO calendargoodVO_new = new CalendargoodVO();
        calendargoodVO_new.setCalendarno(calendarno);
        calendargoodVO_new.setMemberno(memberno);
        
        this.calendargoodProc.create(calendargoodVO_new);
        this.calendarProc.increaseRecom(calendarno); // 카운트 증가
      }
      
   // 추천 여부가 변경되어 다시 새로운 값을 읽어옴.
      int heartCnt = this.calendargoodProc.heartCnt(map);
      int recom = this.calendarProc.read(calendarno).getRecom();
      
      JSONObject result = new JSONObject();
      result.put("isMember", 1); // 로그인: 1
      result.put("heartCnt", heartCnt); // 추천여부, 추천: 1
      result.put("recom", recom); // 추천 수
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
      
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isMember", 0); // 로그인: 1, 비회원: 0
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }

  }
  
  /**
   *  <!--우선 순위 높임, 10 등 -> 1 등-->,  http://localhost:9092/cate/update_seqno_forward/1
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value="/update_seqno_forward/{calendarno}")
  public String update_seqno_forward(Model model, @PathVariable("calendarno") Integer calendarno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra ) {
    this.calendarProc.update_seqno_forward(calendarno);
     
    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
    
    return "redirect:/calendar/list_all"; // @GetMapping(value="/list_all")
  }
  
  /**
   *  <!--우선 순위 높임, 10 등 -> 1 등-->,  http://localhost:9092/cate/update_seqno_backward/1
   * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
   * @return
   */
  @GetMapping(value="/update_seqno_backward/{calendarno}")
  public String update_seqno_backward(Model model, @PathVariable("calendarno") Integer calendarno,
      @RequestParam(name="word", defaultValue="") String word,
      @RequestParam(name="now_page", defaultValue = "") int now_page,
      RedirectAttributes ra ) {
    this.calendarProc.update_seqno_backward(calendarno);
    
    ra.addAttribute("word", word); // redirect로 데이터 전송
    ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
    
    return "redirect:/calendar/list_all"; // @GetMapping(value="/list_all")
  }
  
  /**
   * 특정 날짜의 목록
   * 현재 월: http://localhost:9091/calendar/list_calendar
   * 이전 월: http://localhost:9091/calendar/list_calendar?year=2024&month=12 
   * 다음 월: http://localhost:9091/calendar/list_calendar?year=2024&month=1
   * @param model
   * @return
   */
  @GetMapping(value = "/main_list_calendar")
  public String main_list_calendar(Model model,
      @RequestParam(name="year", defaultValue = "0") int year,
      @RequestParam(name="month", defaultValue = "0") int month) {
    
    if (year == 0) {
        // 현재 날짜를 가져옴
        LocalDate today = LocalDate.now();

        // 년도와 월 추출
        year = today.getYear();
        month = today.getMonthValue();
    } 
    
    String month_str = String.format("%02d", month); // 두 자리 형식으로
    System.out.println("-> month: " + month_str);
  
    String date = year + "-" + month;
    System.out.println("-> date: " + date);
    
//    ArrayList<CalendarVO> list = this.calendarProc.list_calendar(date);
//    model.addAttribute("list", list);

    ArrayList<NewsCateVOMenu> menu = this.newscateProc.menu();
    model.addAttribute("menu", menu);

    
    model.addAttribute("year", year);
    model.addAttribute("month", month-1);  // javascript는 1월이 0임. 
    
    return "/th/calendar/main_list_calendar"; // /templates/calendar/list_calendar.html
  }
  
  /**
   * 특정 날짜의 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/calendar/list_calendar_day?labeldate=2025-01-03
  @GetMapping(value = "/main_list_calendar_day")
  @ResponseBody
  public String main_list_calendar_day(Model model, @RequestParam(name="labeldate", defaultValue = "") String labeldate) {
    System.out.println("-> labeldate: " + labeldate);  // console에서 labeldate 확인
        
        // 추가적인 검사
        if ("undefined".equals(labeldate)) {
            System.out.println("No labeldate parameter provided.");
        }
  
    ArrayList<CalendarVO> list = this.calendarProc.list_calendar_day(labeldate);
    model.addAttribute("list", list);

    JSONArray schedule_list = new JSONArray();
    
    for (CalendarVO calendarVO: list) {
        JSONObject schedual = new JSONObject();
        schedual.put("calendarno", calendarVO.getCalendarno());
        schedual.put("labeldate", calendarVO.getLabeldate());
        schedual.put("label", calendarVO.getLabel());
        schedual.put("seqno", calendarVO.getSeqno());
        
        schedule_list.put(schedual);
    }

    return schedule_list.toString();
    
  }

  
}
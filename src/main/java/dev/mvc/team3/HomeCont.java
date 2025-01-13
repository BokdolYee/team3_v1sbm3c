package dev.mvc.team3;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
import dev.mvc.calendar.CalendarProcInter;
import dev.mvc.calendar.CalendarVO;
import dev.mvc.issue.IssueProcInter;
import dev.mvc.issue.IssueVO;

@Controller
public class HomeCont {

    @Autowired
    @Qualifier("dev.mvc.newscate.NewsCateProc")
    private NewsCateProcInter newsCateProc;

    @Autowired
    @Qualifier("dev.mvc.issue.IssueProc")
    private IssueProcInter issueProc;
    
    @Autowired
    @Qualifier("dev.mvc.calendar.CalendarProc")
    private CalendarProcInter calendarProc;

    public HomeCont() {
        System.out.println("-> HomeCont 생성됨");
    }

    @GetMapping(value={"/", "/index.do"}) // http://localhost:9093
    public String home(Model model, @ModelAttribute("issueVO") IssueVO issueVO) {
        // 긴급 공지사항 목록 가져오기
        ArrayList<IssueVO> urgentIssues = issueProc.listUrgent();
        model.addAttribute("urgentIssues", urgentIssues);

        // 긴급 공지사항이 있으면 팝업을 위한 URL 설정
        if (urgentIssues != null && !urgentIssues.isEmpty()) {
            model.addAttribute("urgentIssueUrl", "/issue/urgent");
        } else {
            System.out.println("긴급 공지사항 없음, URL 설정 안됨");
        }

        // 카테고리 메뉴 가져오기
        ArrayList<NewsCateVOMenu> menu = this.newsCateProc.menu();
        model.addAttribute("menu", menu);

        return "/th/index"; // /templates/index.html
    }

 // 메인 페이지에서 사용되는 달력
    @GetMapping("/main_list_calendar")
    public String mainListCalendar(Model model,
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

        // 메뉴 정보 가져오기
        ArrayList<NewsCateVOMenu> menu = this.newsCateProc.menu();
        model.addAttribute("menu", menu);

        // 달력 관련 데이터 전달
        model.addAttribute("year", year);
        model.addAttribute("month", month-1); // javascript는 1월이 0임. 
        
        return "/th/calendar/main_list_calendar"; // main_list_calendar.html을 반환
    }

    // 특정 날짜의 목록을 JSON으로 반환
    @GetMapping(value = "/main_list_calendar_day")
    @ResponseBody
    public String mainListCalendarDay(Model model, @RequestParam(name="labeldate", defaultValue = "") String labeldate) {
        System.out.println("-> labeldate: " + labeldate);  // 콘솔에서 labeldate 확인
        
        // 추가적인 검사
        if ("undefined".equals(labeldate)) {
            System.out.println("No labeldate parameter provided.");
        }

        ArrayList<CalendarVO> list = this.calendarProc.main_list_calendar_day(labeldate);
        model.addAttribute("list", list);

        // JSON 형식으로 변환
        JSONArray schedule_list = new JSONArray();
        
        for (CalendarVO calendarVO : list) {
            JSONObject schedule = new JSONObject();
            schedule.put("calendarno", calendarVO.getCalendarno());
            schedule.put("labeldate", calendarVO.getLabeldate());
            schedule.put("label", calendarVO.getLabel());
            schedule.put("seqno", calendarVO.getSeqno());
            schedule_list.put(schedule);
        }

        return schedule_list.toString(); // JSON 반환
    }
}

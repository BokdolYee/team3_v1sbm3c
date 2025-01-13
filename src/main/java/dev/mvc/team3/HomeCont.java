package dev.mvc.team3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import dev.mvc.newscate.NewsCateProcInter;
import dev.mvc.newscate.NewsCateVOMenu;
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

    // 메인 페이지에서 사용되는 list_calendar.html
    @GetMapping("/calendar/main_list_calendar")  
    public String showCalendarPage(Model model) {
        model.addAttribute("year", 2025);
        model.addAttribute("month", 0); // 예시: 1월 (0부터 시작)
        return "/th/calendar/main_list_calendar"; // templates/calendar/list_calendar.html을 반환
    }
}

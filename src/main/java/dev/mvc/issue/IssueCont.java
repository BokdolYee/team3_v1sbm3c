package dev.mvc.issue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;  

@CrossOrigin(origins = "*") // React 앱의 주소
@Controller
@RequestMapping("/issue")
public class IssueCont {

    @Autowired
    @Qualifier("dev.mvc.issue.IssueProc")
    private IssueProcInter issueProc;
    
// // 공지사항 목록 API
//    @GetMapping
//    public List<IssueVO> getAllIssues() {
//        return issueProc.list();  // 공지사항 목록 반환
//    }
//
//    // 공지사항 상세보기 API
//    @GetMapping("/{issueno}")
//    public IssueVO getIssue(@PathVariable int issueno) {
//        return issueProc.read(issueno);  // 특정 공지사항 반환
//    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("issueVO", new IssueVO()); // 빈 객체 전달
        return "issue/create"; // 템플릿 이름
    }


    @PostMapping(value = "/create")
    public String create(
        HttpSession session,
        Model model,
        @Valid @ModelAttribute("issueVO") IssueVO issueVO,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            System.out.println("-> 유효성 검사 실패");
            return "/issue/create";
        }

        Integer grade = (Integer) session.getAttribute("grade"); // 세션에서 관리자 등급 정보 가져오기
        if (grade == null || grade < 1 || grade > 10) {
            System.out.println("-> 관리자 권한이 없는 사용자: " + grade);
            model.addAttribute("code", "invalid_admin_grade");
            return "/issue/msg";
        }

        // 공지사항 생성 처리
        int cnt = this.issueProc.create(issueVO);
        System.out.println("-> cnt: " + cnt);

        if (cnt == 1) {
            return "redirect:/issue/list"; // 성공 시 공지사항 목록으로 이동
        } else {
            model.addAttribute("code", "create_fail");
        }

        model.addAttribute("cnt", cnt);
        return "issue/msg";
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model) {
        ArrayList<IssueVO> list = this.issueProc.list();
        System.out.println("List size: " + list.size());
        for (IssueVO issueVO : list) {
            System.out.println("issueVO: " + issueVO);  // 각 IssueVO 객체 출력
        }
        model.addAttribute("list", list);
        return "/issue/list";
    }



    @GetMapping("/read/{issueno}")
    public String read(@PathVariable int issueno, Model model) {
        IssueVO issueVO = issueProc.read(issueno);
        model.addAttribute("issueVO", issueVO);
        
        return "issue/read";
    }

    @GetMapping("/update/{issueno}")
    public String updateForm(@PathVariable int issueno, Model model) {
        IssueVO issueVO = issueProc.read(issueno);
        model.addAttribute("issueVO", issueVO);
        return "issue/update";
    }

    @PostMapping("/update")
    public String update(IssueVO issueVO) {
        issueProc.update(issueVO);
        return "redirect:/issue/list";
    }

    @PostMapping("/delete/{issueno}")
    public String delete(@PathVariable int issueno) {
        issueProc.delete(issueno);
        return "redirect:/issue/list";
    }
}

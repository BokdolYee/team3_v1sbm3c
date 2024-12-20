package dev.mvc.issue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/issue")
public class IssueCont {

    @Autowired
    @Qualifier("dev.mvc.issue.IssueProc")
    private IssueProcInter issueProc;

    @GetMapping("/create")
    public String createForm() {
        return "issue/create";
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
        return "/issue/msg";
    }


    @GetMapping("/list")
    public String list(Model model) {
        List<IssueVO> list = issueProc.list();
        model.addAttribute("list", list);
        return "issue/list";
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

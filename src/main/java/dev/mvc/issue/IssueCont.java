package dev.mvc.issue;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import dev.mvc.member.MemberVO;
import dev.mvc.member.MemberProc;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*") // React 앱의 주소
@Controller
@RequestMapping("/issue")
public class IssueCont {

    @Autowired
    @Qualifier("dev.mvc.issue.IssueProc")
    private IssueProcInter issueProc;

    @Autowired
    private MemberProc memberProc; // MemberProc 추가

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
        @ModelAttribute("memberVO") MemberVO memberVO,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            System.out.println("-> 유효성 검사 실패");
            return "/issue/create";
        }

        // 관리자 권한 확인
        if (!memberProc.isAdmin(session)) {  // MemberProc의 isAdmin 메서드 사용
            System.out.println("-> 관리자 권한이 없는 사용자");
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
    public String list(HttpSession session, Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
        // 세션에서 로그인한 사용자 정보 가져오기
        MemberVO sessionMember = (MemberVO) session.getAttribute("memberVO");

        // 관리자인지 체크
        boolean isAdmin = memberProc.isAdmin(session);  // MemberProc의 isAdmin 메서드 사용

        // 전체 공지사항 목록 가져오기
        ArrayList<IssueVO> list = this.issueProc.list();
        model.addAttribute("list", list);

        // 관리자인지 여부를 모델에 추가
        model.addAttribute("isAdmin", isAdmin);

        return "/issue/list";
    }

    @GetMapping("/read/{issueno}")
    public String read(@PathVariable("issueno") int issueno, Model model) {
        IssueVO issueVO = issueProc.read(issueno);
        model.addAttribute("issueVO", issueVO);
        
        return "issue/read";
    }

    @GetMapping("/update/{issueno}")
    public String updateForm(@PathVariable("issueno") int issueno, Model model) {
        IssueVO issueVO = issueProc.read(issueno);
        model.addAttribute("issueVO", issueVO);
        return "issue/update";
    }

    @PostMapping("/update/{issueno}")
    public String update(@PathVariable("issueno") int issueno, @ModelAttribute IssueVO issueVO) {
        issueVO.setIssueno(issueno); // issueno를 명시적으로 설정
        issueProc.update(issueVO);
        return "redirect:/issue/list"; 
    }

    @GetMapping("/delete/{issueno}")
    public String deleteForm(@PathVariable("issueno") int issueno, Model model) {
        IssueVO issueVO = issueProc.read(issueno); 
        model.addAttribute("issueVO", issueVO);
        return "issue/delete"; 
    }
    
    @PostMapping("/delete/{issueno}")
    public String delete(@PathVariable("issueno") int issueno) {
        issueProc.delete(issueno);
        return "redirect:/issue/list";
    }

}

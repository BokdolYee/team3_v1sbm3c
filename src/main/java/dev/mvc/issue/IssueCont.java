package dev.mvc.issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import dev.mvc.member.MemberVO;
import dev.mvc.tool.Tool;
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

    /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
    public int record_per_page = 8;

    /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
    public int page_per_block = 10;
    
    /** 페이징 목록 주소 */
    private String list_file_name = "/issue/list_search";
    
    // 관리자 권한 확인 메서드
    private boolean checkAdmin(HttpSession session, Model model) {
        if (!memberProc.isAdmin(session)) {  // MemberProc의 isAdmin 메서드 사용
            System.out.println("-> 관리자 권한이 없는 사용자");
            model.addAttribute("code", "invalid_admin_grade");
            return false;  // 관리자 권한이 없으면 false 반환
        }
        return true;
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session, @ModelAttribute("memberVO") MemberVO memberVO) {
        boolean isAdmin = memberProc.isAdmin(session);
        model.addAttribute("isAdmin", isAdmin);
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

        // 관리자 권한 확인
        if (!checkAdmin(session, model)) {  // 공통 메서드 사용
            return "issue/msg"; // 관리자 권한이 없으면 실패 메시지 출력
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
    public String updateForm(@PathVariable("issueno") int issueno, HttpSession session, Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
        boolean isAdmin = memberProc.isAdmin(session);
        model.addAttribute("isAdmin", isAdmin);
        IssueVO issueVO = issueProc.read(issueno);
        model.addAttribute("issueVO", issueVO);
        return "issue/update";
    }

    @PostMapping("/update/{issueno}")
    public String update(@PathVariable("issueno") int issueno, @ModelAttribute IssueVO issueVO, HttpSession session, Model model) {
        // 관리자 권한 확인
        if (!checkAdmin(session, model)) {  // 공통 메서드 사용
            return "issue/msg"; // 관리자 권한이 없으면 실패 메시지 출력
        }

        issueVO.setIssueno(issueno); // issueno를 명시적으로 설정
        issueProc.update(issueVO);
        return "redirect:/issue/list_search"; 
    }

    @GetMapping("/delete/{issueno}")
    public String deleteForm(@PathVariable("issueno") int issueno, HttpSession session, Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
        boolean isAdmin = memberProc.isAdmin(session);
        model.addAttribute("isAdmin", isAdmin);
        IssueVO issueVO = issueProc.read(issueno); 
        model.addAttribute("issueVO", issueVO);
        return "issue/delete"; 
    }

    @PostMapping("/delete/{issueno}")
    public String delete(@PathVariable("issueno") int issueno, HttpSession session, Model model) {
        boolean isAdmin = memberProc.isAdmin(session);
        model.addAttribute("isAdmin", isAdmin);
        issueProc.delete(issueno);
        return "redirect:/issue/list_search"; // 삭제 후 공지사항 목록으로 이동
    }
    
    // 페이징 처리된 공지사항 목록을 가져오는 메서드
    @GetMapping(value = "/list_search")
    public String list_search_paging(HttpSession session, Model model,
        @ModelAttribute("memberVO") MemberVO memberVO,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
      
        // 세션에서 로그인한 사용자 정보 가져오기
        MemberVO sessionMember = (MemberVO) session.getAttribute("memberVO");
  
        // 관리자인지 체크
        boolean isAdmin = memberProc.isAdmin(session);  // MemberProc의 isAdmin 메서드 사용
        // 관리자인지 여부를 모델에 추가
        model.addAttribute("isAdmin", isAdmin);
        
            word = Tool.checkNull(word);

            // 검색 결과 목록 가져오기
            ArrayList<IssueVO> list = this.issueProc.list_search_paging(word, now_page, this.record_per_page);
            model.addAttribute("list", list);

            // 검색 결과 카운트
            int search_cnt = this.issueProc.list_search_count(word);
            model.addAttribute("search_cnt", search_cnt);

            model.addAttribute("word", word); // 검색어

            // 페이지 번호 목록 생성
            int search_count = this.issueProc.list_search_count(word);
            String paging = this.issueProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
                this.page_per_block);
            model.addAttribute("paging", paging);
            model.addAttribute("now_page", now_page);

            // 일련 번호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
            int no = search_count - ((now_page - 1) * this.record_per_page);
            model.addAttribute("no", no);

            return "/issue/list_search"; // /templates/issue/list_search.html
        } 
    


}

package dev.mvc.issue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/issue")
public class IssueCont {

    @Autowired
    private IssueProcInter issueProc;

    @GetMapping("/create")
    public String createForm() {
        return "issue/create";
    }

    @PostMapping("/create")
    public String create(IssueVO issueVO) {
        issueProc.create(issueVO);
        return "redirect:/issue/list";
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

package dev.mvc.stock;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stock") // 기본 URL 매핑
public class StockCont {
  @Autowired
  @Qualifier("dev.mvc.stock.StockProc")
    private StockProcInter stockProc;

    // 1. Create - 데이터 추가 폼
    @GetMapping("/create")
    public String createForm() {
        return "stock/create"; // create.jsp로 이동
    }

    // 1. Create - 데이터 추가 처리
    @PostMapping("/create")
    public String create(@ModelAttribute StockVO stockVO) {
        stockProc.create(stockVO);
        return "redirect:/stock/list"; // 데이터 추가 후 목록 페이지로 이동
    }

    // 2. Read - 특정 데이터 읽기
    @GetMapping("/read/{stockno}")
    public String read(@PathVariable int stockno, Model model) {
        StockVO stockVO = stockProc.read(stockno);
        model.addAttribute("stockVO", stockVO);
        return "stock/read"; // read.jsp로 이동
    }

    // 3. List - 전체 데이터 목록
    @GetMapping("/list")
    public String list(Model model) {
        List<StockVO> stockList = stockProc.list();
        model.addAttribute("stockList", stockList);
        return "stock/list"; // list.jsp로 이동
    }

    // 4. Update - 데이터 수정 폼
    @GetMapping("/update/{stockno}")
    public String updateForm(@PathVariable int stockno, Model model) {
        StockVO stockVO = stockProc.read(stockno);
        model.addAttribute("stockVO", stockVO);
        return "stock/update"; // update.jsp로 이동
    }

    // 4. Update - 데이터 수정 처리
    @PostMapping("/update")
    public String update(@ModelAttribute StockVO stockVO) {
        stockProc.update(stockVO);
        return "redirect:/stock/list"; // 데이터 수정 후 목록 페이지로 이동
    }

    // 5. Delete - 데이터 삭제 폼
    @GetMapping("/delete/{stockno}")
    public String deleteForm(@PathVariable int stockno, Model model) {
        StockVO stockVO = stockProc.read(stockno);
        model.addAttribute("stockVO", stockVO);
        return "stock/delete"; // delete.jsp로 이동
    }

    // 5. Delete - 데이터 삭제 처리
    @PostMapping("/delete")
    public String delete(@RequestParam int stockno) {
        stockProc.delete(stockno);
        return "redirect:/stock/list"; // 데이터 삭제 후 목록 페이지로 이동
    }
    
    /**
     * 메뉴 데이터 저장 API
     * http://localhost:9091/menu/api/save
     *
     * @param menuVO 메뉴 정보
     * @return 성공 여부 (JSON 응답)
     */
    @PostMapping("/api/save")
    @ResponseBody
    public HashMap<String, Object> saveMenuApi(@RequestBody StockVO stockVO) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            int cnt = stockProc.create(stockVO);
            if (cnt == 1) {
                response.put("status", "success");
                response.put("message", "Stock saved successfully.");
            } else {
                response.put("status", "fail");
                response.put("message", "Stock save failed.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "An error occurred while saving Stock data: " + e.getMessage());
        }

        return response;
    }
}

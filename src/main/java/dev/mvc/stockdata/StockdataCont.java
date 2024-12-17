package dev.mvc.stockdata;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stockdata") // 기본 URL 매핑
public class StockdataCont {

    @Autowired
    @Qualifier("dev.mvc.stockdata.StockdataProc") // StockdataProc Bean 이름과 맞추어 지정
    private StockdataProcInter stockdataProc;

    // 1. Create - 데이터 추가 폼
    @GetMapping("/create")
    public String createForm(@RequestParam int stockno, Model model) {
        model.addAttribute("stockno", stockno); 
        return "stockdata/create"; 
    }

    // 1. Create - 데이터 추가 처리
    @PostMapping("/create")
    public String create(@ModelAttribute StockdataVO stockdataVO) {
        stockdataProc.create(stockdataVO);
        return "redirect:/stockdata/list?stockno=" + stockdataVO.getStockno();
    }

    // 2. Read - 특정 데이터 읽기
    @GetMapping("/read/{sdatano}")
    public String read(@PathVariable int sdatano, Model model) {
        StockdataVO stockdataVO = stockdataProc.read(sdatano);
        model.addAttribute("stockdataVO", stockdataVO);
        return "stockdata/read";
    }

    // 3. List - 특정 종목의 데이터 목록
    @GetMapping("/list")
    public String list(@RequestParam int stockno, Model model) {
        List<StockdataVO> stockdataList = stockdataProc.listByStockno(stockno);
        model.addAttribute("stockdataList", stockdataList);
        model.addAttribute("stockno", stockno);
        return "stockdata/list"; 
    }

    // 4. Update - 데이터 수정 폼
    @GetMapping("/update/{sdatano}")
    public String updateForm(@PathVariable int sdatano, Model model) {
        StockdataVO stockdataVO = stockdataProc.read(sdatano);
        model.addAttribute("stockdataVO", stockdataVO);
        return "stockdata/update";
    }

    // 4. Update - 데이터 수정 처리
    @PostMapping("/update")
    public String update(@ModelAttribute StockdataVO stockdataVO) {
        stockdataProc.update(stockdataVO);
        return "redirect:/stockdata/list?stockno=" + stockdataVO.getStockno();
    }

    // 5. Delete - 데이터 삭제 폼
    @GetMapping("/delete/{sdatano}")
    public String deleteForm(@PathVariable int sdatano, Model model) {
        StockdataVO stockdataVO = stockdataProc.read(sdatano);
        model.addAttribute("stockdataVO", stockdataVO);
        return "stockdata/delete";
    }

    // 5. Delete - 데이터 삭제 처리
    @PostMapping("/delete")
    public String delete(@RequestParam int sdatano, @RequestParam int stockno) {
        stockdataProc.delete(sdatano);
        return "redirect:/stockdata/list?stockno=" + stockno;
    }
}

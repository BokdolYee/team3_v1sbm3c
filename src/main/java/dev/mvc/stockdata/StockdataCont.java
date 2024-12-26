package dev.mvc.stockdata;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.mvc.stock.StockProcInter;
import dev.mvc.stock.StockVO;

@Controller
@RequestMapping("/stockdata") // 기본 URL 매핑
public class StockdataCont {
  
  @Autowired
  @Qualifier("dev.mvc.stockdata.StockdataProc")
  private StockdataProcInter stockdataProc;
  
  @Autowired
  @Qualifier("dev.mvc.stock.StockProc")
  private StockProcInter stockProc;

  // 1. Create - 데이터 추가 폼
  @GetMapping("/create")
  public String createForm(@RequestParam(value = "sdatano", required = false) Integer sdatano, 
                           @ModelAttribute("stockdataVO") StockdataVO stockdataVO, 
                           Model model) {
      if (sdatano != null) {
          stockdataVO = stockdataProc.read(sdatano); // 기존 데이터 조회
      } else {
          // sdatano가 null일 경우 기본값 설정 (예: 새 데이터 추가)
          stockdataVO.setStockno(0); // stockno가 null이면 기본값으로 0 설정
      }
      model.addAttribute("stockdataVO", stockdataVO);
      return "stockdata/create"; // create.html로 이동
  }



  // 1. Create - 데이터 추가 처리
  @PostMapping("/create")
  public String create(@ModelAttribute StockdataVO stockdataVO) {
    
      // 데이터 유효성 검사, 비즈니스 로직 추가 가능
      stockdataProc.create(stockdataVO); // 데이터를 추가
      return "redirect:/stockdata/list_all"; // 데이터 추가 후 목록 페이지로 이동
  }

  @GetMapping("/read/{sdatano}")
  public String read(@PathVariable("sdatano") int sdatano, 
                     @RequestParam(value = "stockno", required = false, defaultValue = "0") int stockno, 
                     Model model) {
      // 특정 주식 데이터 조회
      StockdataVO stockdataVO = stockdataProc.read(sdatano);
      if (stockdataVO == null) {
          model.addAttribute("errorMessage", "No stock data found.");
      } else {
          model.addAttribute("stockdataVO", stockdataVO);
      }

      // 주식 정보 조회 (stockno가 0이 아니면)
      if (stockno != 0) {
          StockVO stockVO = stockProc.read(stockno);
          if (stockVO == null) {
              model.addAttribute("errorMessage", "No stock information found for stockno: " + stockno);
          } else {
              model.addAttribute("stockVO", stockVO);
          }
      } else {
          // stockno를 stockdataVO에서 가져와서 사용
          if (stockdataVO.getStockno() != null) {
              StockVO stockVO = stockProc.read(stockdataVO.getStockno());
              if (stockVO != null) {
                  model.addAttribute("stockVO", stockVO);
              } else {
                  model.addAttribute("errorMessage", "No stock information found for stockno: " + stockdataVO.getStockno());
              }
          }
      }

      // read.html로 이동
      return "stockdata/read";
  }

  // 3. List - 전체 데이터 목록
  @GetMapping("/list_all")
  public String list(Model model) {
      List<StockdataVO> stockdataList = stockdataProc.list(); // 모든 데이터 목록 가져오기
      model.addAttribute("stockdataList", stockdataList); // stockdataList 모델에 추가
      return "stockdata/list_all"; // list_all.html로 이동
  }

  // 4. Update - 데이터 수정 폼
  @GetMapping("/update/{sdatano}")
  public String updateForm(@PathVariable("sdatano") int sdatano, Model model) {
      // stockdataVO 가져오기
      StockdataVO stockdataVO = stockdataProc.read(sdatano); // 수정할 주식 데이터 조회
      model.addAttribute("stockdataVO", stockdataVO);

      // stockno가 null이 아닌지 확인하고, stockno에 해당하는 StockVO 조회
      if (stockdataVO.getStockno() != null) {
          StockVO stockVO = stockProc.read(stockdataVO.getStockno()); // stockno에 해당하는 종목 정보
          model.addAttribute("stockVO", stockVO);  // stockVO를 모델에 추가
      } else {
          model.addAttribute("errorMessage", "No stock information found for the given stockno.");
      }

      return "stockdata/update"; // update.html로 이동
  }

  // 4. Update - 데이터 수정 처리
  @PostMapping("/update")
  public String update(@ModelAttribute StockdataVO stockdataVO) {
      // rdate를 수동으로 설정하지 않고, 쿼리에서 sysdate가 자동으로 처리되도록 함
      stockdataProc.update(stockdataVO); // 수정된 데이터 처리
      return "redirect:/stockdata/list_all"; // 데이터 수정 후 목록 페이지로 이동
  }



  // 5. Delete - 데이터 삭제 폼
  @GetMapping("/delete/{sdatano}")
  public String deleteForm(@PathVariable("sdatano") int sdatano, Model model) {
     StockdataVO stockdataVO = stockdataProc.read(sdatano); // 삭제할 주식 데이터 조회
     model.addAttribute("stockdataVO", stockdataVO); // 삭제할 주식 데이터 전달
     return "stockdata/delete"; // delete.html로 이동
  }
  
  // 5. Delete - 데이터 삭제 처리
  @PostMapping("/delete")
  public String delete(@RequestParam("sdatano") int sdatano) {
      stockdataProc.delete(sdatano); // sdatano로 삭제 작업
      return "redirect:/stockdata/list_all"; // 삭제 후 목록 페이지로 이동
  }
}

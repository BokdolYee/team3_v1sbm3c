package dev.mvc.stockdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.mvc.member.MemberProcInter;
import dev.mvc.stock.StockProcInter;
import dev.mvc.stock.StockVO;
import dev.mvc.team3.StockCrawlerScheduler;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/stockdata") // 기본 URL 매핑
public class StockdataCont {
  
  @Autowired
  @Qualifier("dev.mvc.stockdata.StockdataProc")
  private StockdataProcInter stockdataProc;
  
  @Autowired
  @Qualifier("dev.mvc.stock.StockProc")
  private StockProcInter stockProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;  
  
  @Autowired
  private StockCrawlerScheduler stockCrawlerScheduler;

  // 페이지 로딩 시 버튼을 포함한 HTML 반환
  @RequestMapping("/updatePage")
  public String showUpdatePage() {
      return "list_all";  // list_all.html 페이지를 반환
  }

  // 버튼 클릭 시 크롤링 작업 실행
  @PostMapping("/updateStockData")
  public String updateStockData() {
      // 크롤링 작업을 즉시 실행
      stockCrawlerScheduler.fetchAndUpdateStockDataOnDemand();  // 즉시 실행되는 메소드 호출
      return "redirect:/stockdata/list_all";  // 크롤링이 완료된 후 동일 페이지로 돌아가기
  }
  
  public int record_per_page = 4;
  public int page_per_block = 10;
  private String list_file_name = "/stockdata/list_search";

  //1. Create - 데이터 추가 폼
  @GetMapping("/create")
  public String createForm(@RequestParam(value = "sdatano", required = false) Integer sdatano, 
                          @ModelAttribute("stockdataVO") StockdataVO stockdataVO, 
                          Model model, HttpSession session) {
     // 세션에서 관리자 확인
     if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
         if (sdatano != null) {
             stockdataVO = stockdataProc.read(sdatano); // 기존 데이터 조회
         } else {
             // sdatano가 null일 경우 기본값 설정 (예: 새 데이터 추가)
             stockdataVO.setStockno(0); // stockno가 null이면 기본값으로 0 설정
         }
         model.addAttribute("stockdataVO", stockdataVO);
         return "/th/stockdata/create"; // create.html로 이동
     } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
         return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
     }
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
      return "/th/stockdata/read";
  }

  @GetMapping("/list_all")
  public String list(@RequestParam(name = "searchName", defaultValue = "") String searchName,
                     @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                     Model model) {

      int start_num = (now_page - 1) * record_per_page + 1;
      int end_num = start_num + record_per_page - 1;

      Map<String, Object> params = new HashMap<>();
      params.put("searchName", searchName);
      params.put("nowPage", now_page);
      params.put("start_num", start_num); // start_num
      params.put("end_num", end_num);     // end_num
      params.put("recordsPerPage", this.record_per_page);

      // 페이징 및 검색 데이터 목록
      List<StockdataVO> stockdataList = stockdataProc.listSearchPaging(params);
      model.addAttribute("stockdataList", stockdataList); // stockdataList 모델에 추가

      // stockno로 name을 가져오기
      for (StockdataVO stockdataVO : stockdataList) {
          String stockName = stockdataProc.getStockNameByStockno(stockdataVO.getStockno()); // stockno로 이름 조회
          stockdataVO.setStock_name(stockName); // stock_name 설정
      }

      // 전체 개수
      int search_count = stockdataProc.list_search_count(params);

      // 페이징 HTML 생성
      String paging = stockdataProc.pagingBox(now_page, searchName, "/stockdata/list_all", search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      return "/th/stockdata/list_all"; // list_all.html로 이동
  }



  //4. Update - 데이터 수정 폼
  @GetMapping("/update/{sdatano}")
  public String updateForm(@PathVariable("sdatano") int sdatano, Model model, HttpSession session) {
     // 세션에서 관리자 확인
     if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
         // stockdataVO 가져오기
         StockdataVO stockdataVO = stockdataProc.read(sdatano); // 수정할 주식 데이터 조회
         model.addAttribute("stockdataVO", stockdataVO);
  
         if (stockdataVO.getStockno() != null) {
             StockVO stockVO = stockProc.read(stockdataVO.getStockno()); // stockno에 해당하는 종목 정보
             stockdataVO.setStockVO(stockVO); // stockVO에 해당하는 종목 정보 설정
             model.addAttribute("stockVO", stockVO);  // stockVO를 모델에 추가
         } else {
             model.addAttribute("errorMessage", "No stock information found for the given stockno.");
         }
  
         return "/th/stockdata/update"; // update.html로 이동
     } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
         return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
     }
  }



  @PostMapping("/update")
  public String update(@ModelAttribute StockdataVO stockdataVO, Model model) {
      try {
          // stockdataVO 값 확인
          System.out.println("Received stockdataVO: " + stockdataVO);

          // sdatano가 0일 경우 처리
          if (stockdataVO.getSdatano() == 0) {
              model.addAttribute("errorMessage", "Invalid sdatano value received.");
              return "errorPage"; // sdatano가 0일 경우 에러 페이지로 이동
          }

          // 데이터 수정
          int result = stockdataProc.update(stockdataVO);
          
          // 수정 후 결과 확인
          if (result > 0) {
              return "redirect:/stockdata/list_all"; // 수정 성공 시 목록 페이지로 리다이렉트
          } else {
              model.addAttribute("errorMessage", "No rows were updated.");
              return "errorPage"; // 수정이 안된 경우 에러 페이지로 이동
          }
      } catch (Exception e) {
          // 예외 발생 시 상세 정보 출력
          e.printStackTrace();  // 로그로 상세 예외 출력
          model.addAttribute("errorMessage", "An error occurred: " + e.getMessage());  // 모델에 예외 메시지 추가
          return "errorPage";  // 에러 페이지로 이동
      }
  }




  //5. Delete - 데이터 삭제 폼
  @GetMapping("/delete/{sdatano}")
  public String deleteForm(@PathVariable("sdatano") int sdatano, Model model, HttpSession session) {
     // 세션에서 관리자 확인
     if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
         StockdataVO stockdataVO = stockdataProc.read(sdatano); // 삭제할 주식 데이터 조회
         model.addAttribute("stockdataVO", stockdataVO); // 삭제할 주식 데이터 전달
         return "/th/stockdata/delete"; // delete.html로 이동
     } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
         return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
     }
  }

  
  // 5. Delete - 데이터 삭제 처리
  @PostMapping("/delete")
  public String delete(@RequestParam("sdatano") int sdatano) {
      System.out.print("yes");
      stockdataProc.delete(sdatano); // sdatano로 삭제 작업
      return "redirect:/stockdata/list_all"; // 삭제 후 목록 페이지로 이동
  }
}

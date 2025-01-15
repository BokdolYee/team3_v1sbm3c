package dev.mvc.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/stock")
public class StockCont {
  
  @Autowired
  @Qualifier("dev.mvc.stock.StockProc")
  private StockProcInter stockProc;

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public int record_per_page = 4;
  public int page_per_block = 10;
  private String list_file_name = "/th/stock/list_search";

  public StockCont() {
    System.out.println("-> StockCont created");
  }

  //1. Create - 데이터 추가 폼
  @GetMapping("/create")
  public String createForm(Model model, HttpSession session) {
     // 세션에서 관리자 확인
     if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
         StockVO stockVO = new StockVO();
         model.addAttribute("stockVO", stockVO);
         return "/th/stock/create"; // create.html로 이동
     } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
         return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
     }
  }


//1. Create - 데이터 추가 처리
@PostMapping("/create")
public String create(Model model, @Valid @ModelAttribute("stockVO") StockVO stockVO, 
                    BindingResult bindingResult, HttpSession session, RedirectAttributes ra) {
   
   // 1. 유효성 검사
   if (bindingResult.hasErrors()) {
       return "/th/stock/create"; // 오류가 있으면 폼으로 돌아가기
   }

   // 2. 파일 업로드 처리
   // 파일 변수 선언
   String file1 = ""; // 원본 파일명
   String file1saved = ""; // 저장된 파일명
   String thumb1 = ""; // 썸네일 이미지

   String upDir = Stock.getUploadDir(); // 파일을 업로드할 폴더 준비
   System.out.println("-> upDir: " + upDir);

   // 전송 파일 처리
   MultipartFile mf = stockVO.getFile1MF(); // 파일 업로드된 MultipartFile 객체

   file1 = mf.getOriginalFilename(); // 원본 파일명 산출
   System.out.println("-> 원본 파일명 산출 file1: " + file1);

   long size1 = mf.getSize(); // 파일 크기
   if (size1 > 0) { // 파일 크기 체크, 파일이 있는 경우
       if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
           // 파일 저장 후 저장된 파일명이 리턴됨
           file1saved = Upload.saveFileSpring(mf, upDir);

           if (Tool.isImage(file1saved)) { // 이미지인지 검사
               // 썸네일 이미지 생성 후 파일명 리턴
               thumb1 = Tool.preview(upDir, file1saved, 200, 150);
           }

           // stockVO에 파일 정보 저장
           stockVO.setFile1(file1); // 원본 파일명
           stockVO.setFile1saved(file1saved); // 저장된 파일명
           stockVO.setThumb1(thumb1); // 썸네일 이미지
           stockVO.setSize1(size1); // 파일 크기
       } else { // 업로드할 수 없는 파일 형식일 경우
           ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 실패 메시지
           ra.addFlashAttribute("cnt", 0);
           ra.addFlashAttribute("url", "/stock/msg");
           return "redirect:/stock/msg"; // 에러 페이지로 리다이렉트
       }
   } else { // 파일이 없을 경우
       System.out.println("-> 글만 등록");
   }

   // 3. DB에 데이터 삽입
   int cnt = stockProc.create(stockVO); // stockVO에 담긴 정보를 DB에 저장
   if (cnt == 1) {
       return "redirect:/stock/list_all"; // 성공 시 목록 페이지로 리다이렉트
   } else {
       model.addAttribute("code", "create_fail");
       model.addAttribute("cnt", cnt);
       return "/th/stock/msg"; // 처리 실패 시 메시지 출력
   }
}



  // 2. List - 전체 데이터 목록
  @GetMapping("/list_all")
  public String list_all(@RequestParam(name = "searchSymbol", defaultValue = "") String searchSymbol,
                         @RequestParam(name = "searchName", defaultValue = "") String searchName,
                         @RequestParam(name = "searchIndustry", defaultValue = "") String searchIndustry,
                         @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                         Model model) {
      
    if (searchSymbol == null) {
      searchSymbol = "";  // 빈 문자열로 설정
    }
    if (searchName == null) {
        searchName = "";  // 빈 문자열로 설정
    }
    if (searchIndustry == null) {
        searchIndustry = "";  // 빈 문자열로 설정
    }
    
      // start_num 계산: 1페이지일 때 0, 2페이지일 때 10, 3페이지일 때 20
      int start_num = (now_page - 1) * record_per_page + 1;
      // end_num 계산: start_num + record_per_page - 1
      int end_num = start_num + record_per_page - 1;

      Map<String, Object> params = new HashMap<>();
      params.put("searchSymbol", searchSymbol);
      params.put("searchName", searchName);
      params.put("searchIndustry", searchIndustry);
      params.put("nowPage", now_page);
      params.put("start_num", start_num); // start_num
      params.put("end_num", end_num);     // end_num
      params.put("recordsPerPage", this.record_per_page);

      // 페이징 및 검색 데이터 목록
      List<StockVO> stockList = stockProc.listSearchPaging(params);
      model.addAttribute("stockList", stockList);

      // 전체 개수
      int search_count = stockProc.list_search_count(params);

      // 페이징 HTML 생성
      String paging = stockProc.pagingBox(now_page, searchSymbol, searchName, searchIndustry, "/stock/list_all", search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      return "/th/stock/list_all";
  }



  // 3. List - 검색된 데이터 목록
  @GetMapping("/list_search")
  public String list_search_paging(@RequestParam(name = "searchSymbol", defaultValue = "") String searchSymbol,
      @RequestParam(name = "searchName", defaultValue = "") String searchName,
      @RequestParam(name = "searchIndustry", defaultValue = "") String searchIndustry,@RequestParam(name="word", defaultValue = "") String word,
                                   @RequestParam(name="now_page", defaultValue="1") int now_page,
                                   Model model) {
    
    if (searchSymbol == null) {
      searchSymbol = "";  // 빈 문자열로 설정
    }
    if (searchName == null) {
        searchName = "";  // 빈 문자열로 설정
    }
    if (searchIndustry == null) {
        searchIndustry = "";  // 빈 문자열로 설정
    }
    
    Map<String, Object> params = Map.of(
      "word", word,
      "nowPage", now_page,
      "recordsPerPage", record_per_page
    );

    List<StockVO> stockList = stockProc.listSearchPaging(params);
    model.addAttribute("stockList", stockList);

    int search_count = stockProc.list_search_count(params);
    model.addAttribute("search_count", search_count);
    model.addAttribute("word", word);

    String paging = stockProc.pagingBox(now_page, "", "", "", list_file_name, search_count, record_per_page, page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    int no = search_count - ((now_page - 1) * record_per_page);
    model.addAttribute("no", no);

    return "/th/stock/list_search"; // stock/list_search.html
  }

  // 4. Read - 특정 데이터 읽기
//  @GetMapping("/read/{stockno}")
//  public String read(@PathVariable("stockno") int stockno,
//                    @RequestParam(name="now_page", defaultValue = "1") int now_page,
//                    Model model) {
//    StockVO stockVO = stockProc.read(stockno);
//    model.addAttribute("stockVO", stockVO);
//
//    int search_count = stockProc.list_search_count(Map.of());
//    String paging = stockProc.pagingBox(now_page, "", "", "", list_file_name, search_count, record_per_page, page_per_block);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//    int no = search_count - ((now_page - 1) * record_per_page);
//    model.addAttribute("no", no);
//
//    return "/th/stock/read"; // stock/read.html
//  }
  
  //2. Read - 특정 데이터 읽기
   @GetMapping("/read/{stockno}")
   public String read(@PathVariable("stockno") int stockno, Model model) {
       StockVO stockVO = stockProc.read(stockno);
       model.addAttribute("stockVO", stockVO);
       return "/th/stock/read"; // read.html로 이동
   }

  // 5. Update - 데이터 수정 폼
  @GetMapping("/update/{stockno}")
  public String updateForm(@PathVariable("stockno") int stockno, Model model, HttpSession session) {
      // 세션에서 관리자 확인
      if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
          StockVO stockVO = stockProc.read(stockno);
          model.addAttribute("stockVO", stockVO);
          return "/th/stock/update"; // stock/update.html
      } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
          return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
      }
  }


  // 5. Update - 데이터 수정 처리
  @PostMapping("/update")
  public String update(@ModelAttribute StockVO stockVO) {
    stockProc.update(stockVO);
    return "redirect:/stock/list_all"; // 수정 후 목록 페이지로 이동
  }

  //6. Delete - 데이터 삭제 폼
  @GetMapping("/delete/{stockno}")
  public String deleteForm(@PathVariable("stockno") int stockno, Model model, HttpSession session) {
     // 세션에서 관리자 확인
     if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
         StockVO stockVO = stockProc.read(stockno);
         model.addAttribute("stockVO", stockVO);
         return "/th/stock/delete"; // stock/delete.html
     } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
         return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
     }
  }


  // 6. Delete - 데이터 삭제 처리
  @PostMapping("/delete")
  public String delete(@RequestParam("stockno") int stockno) {
    stockProc.delete(stockno);
    return "redirect:/stock/list_all"; // 삭제 후 목록 페이지로 이동
  }
  
  //1. 파일 수정 폼 (수정 화면)
  @GetMapping("/update_file")
  public String update_file(@RequestParam(name = "stockno") int stockno, HttpSession session, Model model, 
                           @RequestParam(name="word", defaultValue = "") String word, 
                           @RequestParam(name="now_page", defaultValue = "1") int now_page) {
     // 세션에서 관리자 확인
     if (this.memberProc.isAdmin(session)) { // 관리자로 로그인한 경우
         model.addAttribute("word", word);
         model.addAttribute("now_page", now_page);
  
         StockVO stockVO = this.stockProc.read(stockno);
         model.addAttribute("stockVO", stockVO);
  
         return "/th/stock/update_file"; // 수정 화면
     } else { // 관리자가 아닌 경우 로그인 페이지로 리다이렉트
         return "redirect:/member/login_cookie_need"; // 관리자만 접근 가능
     }
  }



//2. 파일 수정 처리 (수정된 파일 저장)
@PostMapping(value = "/update_file")
public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                          @ModelAttribute("stockVO") StockVO stockVO,
                          @RequestParam(name="word", defaultValue = "") String word, 
                          @RequestParam(name="now_page", defaultValue = "1") int now_page) {

    // 기존 파일 삭제
    String file1saved = stockVO.getFile1saved();  // 기존 저장된 파일명
    String thumb1 = stockVO.getThumb1();  // 기존 썸네일 이미지 파일명
    long size1 = 0;

    String upDir = Stock.getUploadDir();  // 업로드할 폴더 경로
    Tool.deleteFile(upDir, file1saved);  // 기존 파일 삭제
    Tool.deleteFile(upDir, thumb1);  // 기존 썸네일 이미지 삭제

    // 새로운 파일 업로드 처리
    String file1 = "";  // 원본 파일명

    MultipartFile mf = stockVO.getFile1MF();  // 새로 업로드된 MultipartFile 객체
    file1 = mf.getOriginalFilename();  // 원본 파일명
    size1 = mf.getSize();  // 파일 크기

    if (size1 > 0) {  // 파일이 있을 경우에만
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) {  // 이미지인지 확인
            // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
            thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }
    } else {  // 파일이 없는 경우, 기존 파일을 유지
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
    }

    stockVO.setFile1(file1);  // 원본 파일명
    stockVO.setFile1saved(file1saved);  // 저장된 파일명
    stockVO.setThumb1(thumb1);  // 썸네일 이미지 파일명
    stockVO.setSize1(size1);  // 파일 크기

    // DB에 파일 수정 정보 저장
    this.stockProc.update_file(stockVO);  // stockProc의 update_file 메서드 호출하여 DB 업데이트

    // Add redirect attributes for redirection after file update
    ra.addAttribute("stockno", stockVO.getStockno());  // stockno parameter
    ra.addAttribute("word", word);  // word parameter
    ra.addAttribute("now_page", now_page);  // current page parameter

    // Ensure that after updating, you are redirecting to the correct view.
    return "redirect:/stock/read/" + stockVO.getStockno();   // Redirect to the updated file page
}



}

package dev.mvc.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mvc.issue.IssueVO;
import dev.mvc.member.MemberVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/news")
public class NewsCont {

    @Autowired  
    private NewsProc newsProc;
    
    @Autowired
    private PythonAPIClient pythonAPIClient;

    @GetMapping("/list")
    public String list(HttpSession session, Model model, @ModelAttribute("NewsVO") NewsVO newsVO) {

        ArrayList<NewsVO> list = this.newsProc.list();
        model.addAttribute("list", list);
        return "/th/news/list";
    }
    
    /**
     * 뉴스 상세 조회
     * @param newsno
     * @return
     */
    @GetMapping("/detail/{newsno}")
    public String getNewsDetail(@PathVariable("newsno") int newsno, Model model) {
        System.out.printf("Spring에서 받아온 newsno: %d\n", newsno);
        // 뉴스 상세 정보 가져오기
        NewsVO newsVO = newsProc.read(newsno);
        if (newsVO == null) {
            model.addAttribute("error", "뉴스를 찾을 수 없습니다.");
            return "/th/errorPage";  // /th/errorPage.html로 이동
        }
        model.addAttribute("news", newsVO);
        return "/th/news/detail";  // /th/news/detail.html로 이동
    }

    @GetMapping("/start_crawl")
    @ResponseBody
    public ResponseEntity<String> startCrawl() {
        try {
            // Flask API 호출
            String apiUrl = "http://localhost:5000/start_crawl";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            // 성공적으로 처리되었을 경우 응답 반환
            return ResponseEntity.ok("뉴스 최신화 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("뉴스 최신화 중 오류 발생: " + e.getMessage());
        }
    }
    
    @PostMapping("/analyze/{newsno}")
    public String analyzeAndSummarize(@PathVariable("newsno") int newsno, Model model) {
        // 뉴스 본문 가져오기
        NewsVO newsVO = newsProc.read(newsno);
        if (newsVO == null) {
            model.addAttribute("error", "뉴스를 찾을 수 없습니다.");
            return "/th/error";  // error.html 페이지로 이동
        }

        String text = newsVO.getText(); // 뉴스 본문

        try {
            // Python API 분석 및 요약 요청
            Map<String, String> analysisAndSummary = pythonAPIClient.analyzeAndSummarize(text);
            if (analysisAndSummary == null) {
                throw new Exception("분석 및 요약 결과가 null입니다.");
            }

            // 분석 및 요약 결과 추출
            String decodedAnalysis = analysisAndSummary.get("analysis");
            String decodedSummary = analysisAndSummary.get("summary");

            // 분석 결과와 요약을 뉴스 객체에 저장
            newsVO.setImpact(decodedAnalysis);
            newsVO.setSummary(decodedSummary);

            // 분석 및 요약 정보 저장 (News 테이블에 반영)
            newsProc.update(newsVO);

            // 분석 결과와 요약을 모델에 담아서 analyze.html로 이동
            model.addAttribute("summary", decodedSummary);
            model.addAttribute("impact", decodedAnalysis);
            model.addAttribute("news", newsVO); // 뉴스 정보도 다시 모델에 추가

        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 모델에 에러 메시지 추가
            e.printStackTrace();  // 콘솔에 예외 상세 정보 출력
            model.addAttribute("error", "API 호출 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "/th/news/analyze"; // 분석 결과 페이지로 이동
    }

    // JSON 디코딩 함수
    private String decodeJson(String jsonData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonData);

        // "summary" 또는 "analysis" 필드 값 추출
        String decodedValue = null;
        if (jsonNode.has("summary")) {
            decodedValue = jsonNode.get("summary").asText();
        } else if (jsonNode.has("analysis")) {
            decodedValue = jsonNode.get("analysis").asText();
        }

        // 디코딩 값이 없다면 예외 발생
        if (decodedValue == null) {
            throw new Exception("JSON 데이터에서 값 추출 실패");
        }

        return decodedValue;
    }
}

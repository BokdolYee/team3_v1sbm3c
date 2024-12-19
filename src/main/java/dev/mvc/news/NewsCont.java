package dev.mvc.news;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.mvc.analysis.AnalysisProc;
import dev.mvc.analysis.AnalysisVO;
import dev.mvc.summarize.SummarizeProc;
import dev.mvc.summarize.SummarizeVO;

@Controller
@RequestMapping("/news")
public class NewsCont {

    @Autowired  
    private NewsProc newsProc;

    @Autowired
    private AnalysisProc analysisProc;
    
    @Autowired
    private SummarizeProc summarizeProc;
    
    @Autowired
    private PythonAPIClient pythonAPIClient;

    /**
     * 뉴스 상세 조회
     * @param newsno
     * @return
     */
    @GetMapping("/detail/{newsno}")
    public String getNewsDetail(@PathVariable("newsno") int newsno, Model model) {
        System.out.printf("Spring에서 받아온 newsno: %d\n", newsno);  // newsno 값 출력 (디버깅)

        // 뉴스 상세 정보 가져오기
        NewsVO newsVO = newsProc.read(newsno);
        if (newsVO == null) {
            model.addAttribute("error", "뉴스를 찾을 수 없습니다.");
            return "errorPage";  // errorPage.html로 이동
        }

        model.addAttribute("news", newsVO);
        return "/news/detail";  // news/detail.html로 이동
    }


    /**
     * 뉴스 분석 및 요약 처리
     * @param newsno
     * @param model
     * @return
     */
    @PostMapping("/analyze/{newsno}")
    public String analyzeAndSummarize(@PathVariable("newsno") int newsno, Model model) {
        // 뉴스 본문 가져오기
        NewsVO newsVO = newsProc.read(newsno);
        if (newsVO == null) {
            model.addAttribute("error", "뉴스를 찾을 수 없습니다.");
            return "error";  // error.html 페이지로 이동
        }

        String text = newsVO.getText(); // 뉴스 본문

        try {
            // Python API 분석 요청
            String analysisResult = pythonAPIClient.analyze(text);
            if (analysisResult == null) {
                throw new Exception("분석 결과가 null입니다.");
            }

            // 분석 결과 디코딩 (JSON에서 데이터 추출)
            String decodedAnalysis = decodeJson(analysisResult);

            // 분석 결과 저장
            AnalysisVO analyzeVO = new AnalysisVO();
            analyzeVO.setNewsno(newsno);
            analyzeVO.setImpact(decodedAnalysis);
            analysisProc.create(analyzeVO);

            // Python API 요약 요청
            String summary = pythonAPIClient.summarize(text);
            if (summary == null) {
                throw new Exception("요약 결과가 null입니다.");
            }

            // 요약 디코딩 (JSON에서 데이터 추출)
            String decodedSummary = decodeJson(summary);

            // 요약 저장
            SummarizeVO summarizeVO = new SummarizeVO();
            summarizeVO.setNewsno(newsno);
            summarizeVO.setContent(decodedSummary);
            summarizeProc.create(summarizeVO);

            // 분석 결과와 요약을 모델에 담아서 analyze.html로 이동
            model.addAttribute("summary", decodedSummary);
            model.addAttribute("impact", decodedAnalysis);
            model.addAttribute("news", newsVO); // 뉴스 정보도 다시 모델에 추가

        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 모델에 에러 메시지 추가
            e.printStackTrace();  // 콘솔에 예외 상세 정보 출력
            model.addAttribute("error", "API 호출 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "/news/analyze"; // 분석 결과 페이지로 이동
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

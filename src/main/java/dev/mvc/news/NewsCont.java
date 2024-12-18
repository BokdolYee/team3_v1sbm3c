package dev.mvc.news;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import dev.mvc.analysis.AnalysisProc;
import dev.mvc.analysis.AnalysisVO;
import dev.mvc.summarize.SummarizeProc;
import dev.mvc.summarize.SummarizeVO;

@CrossOrigin(origins = "http://localhost:3000")  // React 앱의 주소
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
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNewsDetail(@PathVariable("newsno") int newsno) {
        System.out.printf("Spring에서 받아온 newsno:", newsno);  // newsno 값 출력 (디버깅)

        Map<String, Object> result = new HashMap<>();
        
        // 뉴스 상세 정보 가져오기
        NewsVO newsVO = newsProc.read(newsno);
        if (newsVO == null) {
            result.put("error", "뉴스를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        result.put("news", newsVO);
        return ResponseEntity.ok(result);  // 뉴스 상세 정보 반환
    }

    /**
     * 뉴스 분석 및 요약 처리
     * @param newsno
     * @return
     */
    @PostMapping("/analyze/{newsno}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> analyzeAndSummarize(@PathVariable("newsno") int newsno) {
        Map<String, String> result = new HashMap<>();
        
        // 뉴스 본문 가져오기
        NewsVO newsVO = newsProc.read(newsno);
        if (newsVO == null) {
            result.put("error", "뉴스를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        String text = newsVO.getText(); // 뉴스 본문

        try {
            // Python API 분석 요청
            String analysisResult = pythonAPIClient.analyze(text);

            // 분석 결과 저장
            AnalysisVO analyzeVO = new AnalysisVO();
            analyzeVO.setNewsno(newsno);
            analyzeVO.setImpact(analysisResult);
            analysisProc.create(analyzeVO);

            // Python API 요약 요청
            String summary = pythonAPIClient.summarize(text);

            // 요약 저장
            SummarizeVO summarizeVO = new SummarizeVO();
            summarizeVO.setNewsno(newsno);
            summarizeVO.setContent(summary);
            summarizeProc.create(summarizeVO);

            result.put("message", "뉴스 분석 및 요약이 완료되었습니다.");
            result.put("summary", summary);
            result.put("impact", analysisResult);

        } catch (Exception e) {
            result.put("error", "API 호출 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        return ResponseEntity.ok(result);  // 분석 및 요약 결과 반환
    }
}

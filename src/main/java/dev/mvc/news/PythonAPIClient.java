package dev.mvc.news;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Map;

@Service
public class PythonAPIClient {

    private static final String BASE_URL = "http://localhost:5000/start_crawl"; // Python 서버 URL

    public Map<String, String> analyzeAndSummarize(String text) {
        RestTemplate restTemplate = new RestTemplate();
        
        // JSON 데이터 만들기
        Map<String, String> requestData = Map.of("text", text);
        
        // POST 요청 만들기
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestData, headers);
        
        // Python 서버로 POST 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, request, Map.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();  // Python API로부터 받은 분석 및 요약 결과 반환
        } else {
            throw new RuntimeException("API 호출 실패: " + response.getStatusCode());
        }
    }
}

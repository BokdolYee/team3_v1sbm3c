package dev.mvc.news;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;

@Component
public class PythonAPIClient {

    private final String ANALYZE_API_URL = "http://localhost:5001/analyze";
    private final String SUMMARIZE_API_URL = "http://localhost:5000/summarize";

    private final RestTemplate restTemplate = new RestTemplate();

    // 분석 요청
    public String analyze(String text) {
        try {
            // 요청 헤더 설정 (Content-Type: application/json)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 본문 설정 (text를 JSON 형태로 변환)
            Map<String, String> body = new HashMap<>();
            body.put("text", text);

            // HttpEntity 생성 (본문과 헤더 포함)
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            // POST 요청 보내기
            return restTemplate.postForObject(ANALYZE_API_URL, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // 요약 요청
    public String summarize(String text) {
        try {
            // 요청 헤더 설정 (Content-Type: application/json)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 본문 설정 (text를 JSON 형태로 변환)
            Map<String, String> body = new HashMap<>();
            body.put("text", text);

            // HttpEntity 생성 (본문과 헤더 포함)
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            // POST 요청 보내기
            return restTemplate.postForObject(SUMMARIZE_API_URL, entity, String.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

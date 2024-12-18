package dev.mvc.news;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PythonAPIClient {

    private final String ANALYZE_API_URL = "http://localhost:5001/analyze";
    private final String SUMMARIZE_API_URL = "http://localhost:5000/summation";

    private final RestTemplate restTemplate = new RestTemplate();

    // 분석 요청
    public String analyze(String text) {
        return restTemplate.postForObject(ANALYZE_API_URL, text, String.class);
    }

    // 요약 요청
    public String summarize(String text) {
        return restTemplate.postForObject(SUMMARIZE_API_URL, text, String.class);
    }
}

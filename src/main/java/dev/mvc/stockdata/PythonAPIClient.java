package dev.mvc.stockdata;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("stockdataPythonAPIClient")  // Bean 이름을 명시적으로 설정
public class PythonAPIClient {

    private final String STOCK_DATA_API_URL = "http://localhost:5000/fetch_stock_data";  // Python 서버의 엔드포인트

    private final RestTemplate restTemplate = new RestTemplate();

    // 주식 데이터 요청
    public String fetchStockData() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // GET 요청 보내기
            return restTemplate.exchange(STOCK_DATA_API_URL, HttpMethod.GET, entity, String.class).getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

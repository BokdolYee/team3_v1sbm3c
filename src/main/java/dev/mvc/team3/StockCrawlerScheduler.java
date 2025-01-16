package dev.mvc.team3;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("dev.mvc.team3.StockCrawlerScheduler")
public class StockCrawlerScheduler {

    // 1시간마다 크롤링 작업을 실행 (3600000ms = 1시간)
    @Scheduled(fixedRate = 3600000)  // 1시간마다 실행
    public void fetchAndUpdateStockData() {
        System.out.println("Crawling and updating stock data...");

        // 파이썬 스크립트의 경로 (프로젝트 디렉토리 기준으로 절대 경로 설정)
        String pythonScriptPath = "src/main/resources/static/python_files/stockdatacrawler.py"; 
        
        // 실제 절대 경로로 변환 (예시)
        String absolutePath = System.getProperty("user.dir") + "/" + pythonScriptPath;

        try {
            // ProcessBuilder를 사용하여 파이썬 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python", absolutePath);
            Process process = processBuilder.start();  // 파이썬 스크립트 실행

            // 스크립트 실행 후 출력 결과 처리 (선택 사항)
            int exitCode = process.waitFor();  // 프로세스 종료까지 대기
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.out.println("Python script execution failed.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
 // 버튼 클릭 시 즉시 크롤링 작업을 실행
    public void fetchAndUpdateStockDataOnDemand() {
        System.out.println("Crawling and updating stock data on demand...");

        String pythonScriptPath = "src/main/resources/static/python_files/stockdatacrawler.py";
        String absolutePath = System.getProperty("user.dir") + "/" + pythonScriptPath;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", absolutePath);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.out.println("Python script execution failed.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

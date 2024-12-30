package dev.mvc.stock;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockVO {
    private int stockno;       // 종목 번호 (Primary Key)
    private String symbol;     // 종목 코드
    private String name;       // 종목 이름
    private String industry;   // 산업
    private String description;// 설명

    // 검색용 필드
    private String searchSymbol; // 검색할 심볼
    private String searchName;   // 검색할 이름
    private String searchIndustry; // 검색할 산업군

    // 페이징 관련 필드
    private int nowPage;       // 현재 페이지
    private int totalRecords;  // 전체 레코드 수
    private int recordsPerPage = 4; // 한 페이지에 표시할 레코드 수, 기본 4

    // 파일 업로드 관련
    // -----------------------------------------------------------------------------------
    /**
    이미지 파일
    <input type='file' class="form-control" name='file1MF' id='file1MF' 
               value='' placeholder="파일 선택">
    */
    private MultipartFile file1MF = null;
    /** 메인 이미지 크기 단위, 파일 크기 */
    private String file1 = "";
    /** 실제 저장된 메인 이미지 */
    private String file1saved = "";
    /** 메인 이미지 preview */
    private String thumb1 = "";
    /** 메인 이미지 크기 */
    private long size1 = 0;
    
    // 기본 생성자
    public StockVO() {}

    // 매개변수 생성자
    public StockVO(int stockno, String symbol, String name, String industry, String description) {
        this.stockno = stockno;
        this.symbol = symbol;
        this.name = name;
        this.industry = industry;
        this.description = description;
    }

    // 검색용 필드에 대한 Getter, Setter 추가
    public String getSearchSymbol() {
        return searchSymbol;
    }

    public void setSearchSymbol(String searchSymbol) {
        this.searchSymbol = searchSymbol;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchIndustry() {
        return searchIndustry;
    }

    public void setSearchIndustry(String searchIndustry) {
        this.searchIndustry = searchIndustry;
    }

    // 페이징 필드에 대한 Getter, Setter 추가
    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    // toString() 메서드
    @Override
    public String toString() {
        return "StockVO{" +
                "stockno=" + stockno +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", industry='" + industry + '\'' +
                ", description='" + description + '\'' +
                ", searchSymbol='" + searchSymbol + '\'' +
                ", searchName='" + searchName + '\'' +
                ", searchIndustry='" + searchIndustry + '\'' +
                ", nowPage=" + nowPage +
                ", totalRecords=" + totalRecords +
                ", recordsPerPage=" + recordsPerPage +
                '}';
    }
}

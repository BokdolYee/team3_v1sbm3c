package dev.mvc.termbook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE termbook (
//  termno  NUMBER(10)  NOT NULL PRIMARY KEY,    -- 고유 번호
//  term_title  VARCHAR2(255) NOT NULL,        -- 글 제목
//  memo  CLOB  NOT NULL,                    -- 글 내용
//  category  VARCHAR2(100) NOT NULL,        -- 범주
//  rdate DATE  NOT NULL,                    -- 날짜  
//  memberno  NUMBER(10)  NOT NULL,            -- 회원번호    
//    FOREIGN KEY (memberno) REFERENCES member (memberno) -- FK
//);

@Getter @Setter @ToString
public class TermbookVO{

  /** 고유 번호 */
  private int termno;
  
  /** 글 제목 */
  private String term_title;
  
  /** 글 내용 */
  private String memo;
  
  /** 범주 */
  private String category;
  
  /** 날짜 */
  private String rdate = "";  
  
  /** 회원 번호 */
  private int memberno;  
  
  // 검색용 필드
  private String searchTerm_title; // 검색할 심볼
  private String searchCategory;   // 검색할 이름

  // 페이징 관련 필드
  private int nowPage;       // 현재 페이지
  private int totalRecords;  // 전체 레코드 수
  private int recordsPerPage = 4; // 한 페이지에 표시할 레코드 수, 기본 4

  // 기본 생성자
  public TermbookVO() {}

  // 매개변수 생성자
  public TermbookVO(int termno, String term_title, String memo, String category) {
      this.termno = termno;
      this.term_title = term_title;
      this.memo = memo;
      this.category = category;
  }

  // 검색용 필드에 대한 Getter, Setter 추가
  public String getSearchTerm_title() {
      return searchTerm_title;
  }

  public void setSearchTerm_title(String searchTerm_title) {
      this.searchTerm_title = searchTerm_title;
  }

  public String getSearchCategory() {
      return searchCategory;
  }

  public void setSearchCategory(String searchCategory) {
      this.searchCategory = searchCategory;
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
              "termno=" + termno +
              ", term_title='" + term_title + '\'' +
              ", memo='" + memo + '\'' +
              ", category='" + category + '\'' +
              ", searchTerm_tile='" + searchTerm_title + '\'' +
              ", searchCategory='" + searchCategory + '\'' +
              ", nowPage=" + nowPage +
              ", totalRecords=" + totalRecords +
              ", recordsPerPage=" + recordsPerPage +
              '}';
  }
  
  
}
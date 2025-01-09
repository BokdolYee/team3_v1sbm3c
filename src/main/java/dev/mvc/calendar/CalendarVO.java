package dev.mvc.calendar;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE calendar (
//    calendarno  NUMBER(10)    NOT NULL  PRIMARY KEY, -- AUTO_INCREMENT 대체
//    labeldate   VARCHAR2(10)  NOT NULL, -- 출력할 날짜 2013-10-20
//    label       VARCHAR2(50)  NOT NULL, -- 달력에 출력될 레이블
//    title       VARCHAR2(100) NOT NULL, -- 제목(*)
//    content     CLOB          NOT NULL, -- 글 내용
//    cnt         NUMBER        DEFAULT 0, -- 조회수
//    seqno       NUMBER(5)     DEFAULT 1     NOT NULL, -- 일정 출력 순서
//    regdate     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 등록 날짜
//    memberno    NUMBER(10)     NOT NULL, -- FK
//    FOREIGN KEY (memberno) REFERENCES member (memberno) -- 일정을 등록한 관리자
//  );

@Getter @Setter @ToString
public class CalendarVO{
  
  /** 일정 번호 */
  private int calendarno;
  
  /** 관리자 번호 */
  private int memberno;
  
  /** 경기 일정 */
  private String labeldate = "";
  
  /** 출력할 레이블 */
  private String label = "";
  
  /** 제목 */
  private String title = "";
  
  /** 글 내용 */
  private String content = "";
  
  /** 조회수 */
  private int cnt = 0;
  
  /** 일정 출력 순서 */
  @NotNull(message="출력 순서는 필수 입력 항목입니다.")
  @Min(value=1)
  @Max(value=1000000) 
  private Integer seqno;
  
  /** 등록 날짜 */
  private String regdate = "";
  
  private int recom;
  
  // 검색용 필드
  private String searchLabel; // 검색할 심볼
  
//페이징 관련 필드
  private int nowPage;       // 현재 페이지
  private int totalRecords;  // 전체 레코드 수
  private int recordsPerPage = 4; // 한 페이지에 표시할 레코드 수, 기본 4
  
//기본 생성자
  public CalendarVO() {}

  // 매개변수 생성자
  public CalendarVO(int calendarno, String label, String title, String content) {
      this.calendarno = calendarno;
      this.label = label;
      this.title = title;
      this.content = content;
  }
  
  public String getSearchLabel() {
    return searchLabel;
  }
  
  public void setSearchLabel(String searchLabel) {
      this.searchLabel = searchLabel;
}  
  
//페이징 필드에 대한 Getter, Setter 추가
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
  
//toString() 메서드
  @Override
  public String toString() {
      return "CalendarVO{" +
              "calendarno=" + calendarno +
              ", label='" + label + '\'' +
              ", title='" + title + '\'' +
              ", content='" + content + '\'' +
              ", searchLabel='" + searchLabel + '\'' +
              ", nowPage=" + nowPage +
              ", totalRecords=" + totalRecords +
              ", recordsPerPage=" + recordsPerPage +
              '}';
  }
  
}
package dev.mvc.stockdata;

import java.sql.Date;

import dev.mvc.stock.StockVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class StockdataVO {
    private int sdatano;       // 데이터 번호 (Primary Key)
    private String rdate;      // 기록 날짜
    private float open_price;  // 시가
    private float close_price; // 종가
    private String volume;     // 거래량
    private String change;     // 변동률
    private Integer stockno;       // 종목 번호 (Foreign Key)
    private String symbol;
    
    private String stock_name;
    
    private StockVO stockVO;
    
    private String searchName;   // 검색할 이름
    
    // 페이징 관련 필드
    private int nowPage;       // 현재 페이지
    private int totalRecords;  // 전체 레코드 수
    private int recordsPerPage = 4; // 한 페이지에 표시할 레코드 수, 기본 4
    
    // 기본 생성자
    public StockdataVO() {
    }

    // 매개변수 생성자
    public StockdataVO(int sdatano, String rdate, float open_price, float close_price, String volume, String change, Integer stockno, String symbol) {
        this.sdatano = sdatano;
        this.rdate = rdate;
        this.open_price = open_price;
        this.close_price = close_price;
        this.volume = volume;
        this.change = change;
        this.stockno = stockno;
        this.symbol = symbol;
    }

    // Getter 및 Setter
    public int getSdatano() {
        return sdatano;
    }

    public void setSdatano(int sdatano) {
        this.sdatano = sdatano;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public float getOpen_price() {
        return open_price;
    }

    public void setOpen_price(float open_price) {
        this.open_price = open_price;
    }

    public float getClose_price() {
        return close_price;
    }

    public void setClose_price(float close_price) {
        this.close_price = close_price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public Integer getStockno() {
        return stockno;
    }

    public void setStockno(Integer stockno) {
        this.stockno = stockno;
    }
   

    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
      this.symbol = symbol;
    }

    // toString() 메서드
    @Override
    public String toString() {
        return "StockdataVO{" +
                "sdatano=" + sdatano +
                ", rdate='" + rdate + '\'' +
                ", open_price=" + open_price +
                ", close_price=" + close_price +
                ", volume=" + volume +
                ", change=" + change +
                ", stockno=" + stockno +
                ", symbol=" + symbol +
                ", stock_name='" + stock_name + '\'' +
                ", searchName='" + searchName + '\'' +
                ", nowPage=" + nowPage +
                ", totalRecords=" + totalRecords +
                ", recordsPerPage=" + recordsPerPage +
                '}';
    }

    public void setRdate(Date date) {
      // TODO Auto-generated method stub
      
    }
    
    public void setStock_name(String stock_name) {
      this.stock_name = stock_name;
    }
    
    public String getStock_name() {
      return stock_name;
  }
    
    public String getSearchName() {
      return searchName;
  }

  public void setSearchName(String searchName) {
      this.searchName = searchName;
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
}

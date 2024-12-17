package dev.mvc.stockdata;

public class StockdataVO {
    private int sdatano;       // 데이터 번호 (Primary Key)
    private String rdate;      // 기록 날짜
    private float open_price;  // 시가
    private float close_price; // 종가
    private String volume;      // 거래량
    private String change;      // 변동률
    private int stockno;       // 종목 번호 (Foreign Key)

    // 기본 생성자
    public StockdataVO() {
    }

    // 매개변수 생성자
    public StockdataVO(int sdatano, String rdate, float open_price, float close_price, String volume, String change, int stockno) {
        this.sdatano = sdatano;
        this.rdate = rdate;
        this.open_price = open_price;
        this.close_price = close_price;
        this.volume = volume;
        this.change = change;
        this.stockno = stockno;
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

    public int getStockno() {
        return stockno;
    }

    public void setStockno(int stockno) {
        this.stockno = stockno;
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
                '}';
    }
}

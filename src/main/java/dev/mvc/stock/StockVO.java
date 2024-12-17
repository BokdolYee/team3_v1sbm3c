package dev.mvc.stock;

public class StockVO {
    private int stockno;       // 종목 번호 (Primary Key)
    private String symbol;     // 종목 코드
    private String name;       // 종목 이름
    private String industry;   // 산업
    private String description;// 설명

    // 기본 생성자
    public StockVO() {
    }

    // 매개변수 생성자
    public StockVO(int stockno, String symbol, String name, String sector, String industry, String description) {
        this.stockno = stockno;
        this.symbol = symbol;
        this.name = name;
        this.industry = industry;
        this.description = description;
    }

    // Getter 및 Setter
    public int getStockno() {
        return stockno;
    }

    public void setStockno(int stockno) {
        this.stockno = stockno;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                '}';
    }
}

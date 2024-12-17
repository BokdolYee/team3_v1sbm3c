package dev.mvc.stockdata;

import java.util.List;

public interface StockdataProcInter {
    public int create(StockdataVO stockdataVO); // 데이터 추가
    public StockdataVO read(int sdatano); // 특정 데이터 읽기
    public List<StockdataVO> listByStockno(int stockno); // 종목 번호별 데이터 목록
    public int update(StockdataVO stockdataVO); // 데이터 수정
    public int delete(int sdatano); // 데이터 삭제
}

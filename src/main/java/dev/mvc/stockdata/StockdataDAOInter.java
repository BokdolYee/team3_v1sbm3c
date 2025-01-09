package dev.mvc.stockdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.mvc.stock.StockVO;

public interface StockdataDAOInter {

    public int create(StockdataVO stockdataVO); // 데이터 추가
    
    public StockdataVO read(int sdatano); // 특정 데이터 읽기
    
    public int update(StockdataVO stockdataVO); // 데이터 수정
    
    public int delete(int sdatano); // 데이터 삭제
    
    public List<StockdataVO> list(); // stockno에 해당하는 데이터 목록
    
    // stockno에 해당하는 StockVO를 조회하는 메서드 추가
    public StockVO readStock(int stockno); // 특정 종목 조회
    
    public Integer list_search_count(Map<String, Object> map);

    public ArrayList<StockdataVO> listSearchPaging(Map<String, Object> map);
    
    public String getStockNameByStockno(Integer stockno);


}

package dev.mvc.stock;

import java.util.List;

public interface StockDAOInter {
    public int create(StockVO stockVO); // 데이터 추가
    
    public StockVO read(int stockno); // 특정 데이터 읽기
    
    public List<StockVO> list(); // 데이터 목록 가져오기
    
    public int update(StockVO stockVO); // 데이터 수정
    
    public int delete(int stockno); // 데이터 삭제
}

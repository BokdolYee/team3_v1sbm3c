package dev.mvc.stockdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.mvc.stock.StockVO;

public interface StockdataProcInter {
    public int create(StockdataVO stockdataVO); // 데이터 추가
    public StockdataVO read(int sdatano); // 특정 데이터 읽기
    public int update(StockdataVO stockdataVO); // 데이터 수정
    public int delete(int sdatano); // 데이터 삭제
    public List<StockdataVO> list(); // stockno에 해당하는 데이터 목록
    
    public Integer list_search_count(Map<String, Object> map);

    public String pagingBox(int now_page, String searchName,
                            String list_file_name, int search_count, int record_per_page, int page_per_block);
    
    public ArrayList<StockdataVO> listSearchPaging(Map<String, Object> map);
    
    public String getStockNameByStockno(Integer stockno);
    
    public List<StockdataVO> getStockdata();


}

package dev.mvc.stock;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.contents.ContentsVO;

public interface StockProcInter {
  public int create(StockVO stockVO);

  public ArrayList<StockVO> list();

  public StockVO read(Integer stockno);

  public int update(StockVO stockVO);

  public int delete(int stockno);

  public Integer list_search_count(Map<String, Object> map);

  public String pagingBox(int now_page, String searchSymbol, String searchName, String searchIndustry,
                          String list_file_name, int search_count, int record_per_page, int page_per_block);
  
  public ArrayList<StockVO> listSearchPaging(Map<String, Object> map);

  /**
   * 파일 정보 수정
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(StockVO stockVO);
}

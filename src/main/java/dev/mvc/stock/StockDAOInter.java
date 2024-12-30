package dev.mvc.stock;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.contents.ContentsVO;

public interface StockDAOInter {
  public int create(StockVO stockVO);

  public ArrayList<StockVO> list();

  public StockVO read(Integer stockno);

  public int update(StockVO stockVO);

  public int delete(int stockno);

  public Integer list_search_count(Map<String, Object> map);

  public ArrayList<StockVO> listSearchPaging(Map<String, Object> map);

  /**
   * 파일 정보 수정
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(StockVO stockVO);
}

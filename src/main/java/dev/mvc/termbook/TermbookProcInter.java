package dev.mvc.termbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public interface TermbookProcInter {
  
  /**
   * 등록, 추상 메소드
   * @param termbookVO
   * @return
   */
  public int create(TermbookVO termbookVO);
  
  public ArrayList<TermbookVO> list();

  public TermbookVO read(Integer termno);

  public int update(TermbookVO termbookVO);

  public int delete(int termno);

  public Integer list_search_count(Map<String, Object> map);

  public String pagingBox(int now_page, String searchTerm_title, String searchCategory,
                      int search_count, int record_per_page, int page_per_block);
  
  public ArrayList<TermbookVO> listSearchPaging(Map<String, Object> map);


}
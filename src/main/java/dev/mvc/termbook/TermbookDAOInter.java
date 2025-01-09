package dev.mvc.termbook;

import java.util.ArrayList;
import java.util.Map;

public interface TermbookDAOInter {
  
  /**
   * 등록, 추상 메소드
   * @param TermbookVO
   * @return
   */
  public int create(TermbookVO termbookVO);
  
  public ArrayList<TermbookVO> list();

  public TermbookVO read(Integer termno);

  public int update(TermbookVO termbookVO);

  public int delete(int termno);

  public Integer list_search_count(Map<String, Object> map);

  public ArrayList<TermbookVO> listSearchPaging(Map<String, Object> map);

}
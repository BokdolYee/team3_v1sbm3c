package dev.mvc.summarize;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.summarize.SummarizeVO;

public interface SummarizeProcInter {
  
  /**
   * 등록
   * @param SummarizeVO
   * @return
   */
  public int create(SummarizeVO summarizeVO);

  /**
   * 조회
   * @param summaryno
   * @return
   */
  public SummarizeVO read(Integer summaryno);

  /**
   * 수정
   * @param SummarizeVO
   * @return
   */
  public int update(SummarizeVO summarizeVO);

  /**
   * 삭제
   * @param summaryno
   * @return
   */
  public int delete(int summaryno);
  
  /**
   * 검색 목록
   * @param word
   * @return
   */
  public ArrayList<SummarizeVO> search(String keyword);

  /**
   * 검색 + 페이징 목록
   * @param word
   * @param now_page
   * @param record_per_page
   * @return
   */
  public ArrayList<SummarizeVO> listPaging(Map<String, Object> param); 
}
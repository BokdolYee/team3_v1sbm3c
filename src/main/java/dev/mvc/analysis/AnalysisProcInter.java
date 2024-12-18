package dev.mvc.analysis;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.analysis.AnalysisVO;

public interface AnalysisProcInter {
  
  /**
   * 등록
   * @param newsVO
   * @return
   */
  public int create(AnalysisVO analysisVO);

  /**
   * 조회
   * @param newsno
   * @return
   */
  public AnalysisVO read(Integer analysisno);

  /**
   * 수정
   * @param newsVO
   * @return
   */
  public int update(AnalysisVO analysisVO);

  /**
   * 삭제
   * @param newsno
   * @return
   */
  public int delete(int analysisno);
  
  /**
   * 검색 목록
   * @param word
   * @return
   */
  public ArrayList<AnalysisVO> search(String keyword);

  /**
   * 검색 + 페이징 목록
   * @param word
   * @param now_page
   * @param record_per_page
   * @return
   */
  public ArrayList<AnalysisVO> listPaging(Map<String, Object> param); 
}
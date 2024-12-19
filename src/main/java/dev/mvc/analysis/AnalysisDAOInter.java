package dev.mvc.analysis;

import java.util.ArrayList;
import java.util.Map;

public interface AnalysisDAOInter {
  
  /**
   * @param AnalysisVO
   * @return
   */
  public int create(AnalysisVO analysisVO);

  /**
   * 조회
   * @param AnalysisVO
   * @return
   */
  public AnalysisVO read(Integer analysisno);

  /**
   * 수정
   * @param AnalysisVO
   * @return
   */
  public int update(AnalysisVO analysisVO);

  /**
   * 삭제
   * @param analysisno
   * @return
   */
  public int delete(int analysisno);
  
  /**
   * 검색 목록
   * @param word
   * @return
   */
  public ArrayList<AnalysisVO> search(String keyword);

}

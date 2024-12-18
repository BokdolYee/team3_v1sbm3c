package dev.mvc.analysis;

import java.util.ArrayList;
import java.util.Map;

public interface AnalysisDAOInter {
  
  /**
   * <pre>
   * MyBATIS: insert id="create" parameterType="dev.mvc.analysis.analysisVO"
   * insert: int를 리턴, 등록한 레코드 갯수를 리턴
   * id="create": 메소드명으로 사용
   * parameterType="dev.mvc.analysis.analysisVO": 메소드의 파라미터
   * Spring Boot가 자동으로 구현
   * </pre>
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

  /**
   * 검색 + 페이징 목록
   * @param word
   * @param now_page
   * @param record_per_page
   * @return
   */
  public ArrayList<AnalysisVO> listPaging(Map<String, Object> param); 
}

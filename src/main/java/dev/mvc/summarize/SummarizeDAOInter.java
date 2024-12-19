package dev.mvc.summarize;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.news.NewsVO;

public interface SummarizeDAOInter {
  
  /**
   * <pre>
   * MyBATIS: insert id="create" parameterType="dev.mvc.summarize.summarizeVO"
   * insert: int를 리턴, 등록한 레코드 갯수를 리턴
   * id="create": 메소드명으로 사용
   * parameterType="dev.mvc.summarize.summarizeVO": 메소드의 파라미터
   * Spring Boot가 자동으로 구현
   * </pre>
   * @param summarizeVO
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
   * @param summarizeVO
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
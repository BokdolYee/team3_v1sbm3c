package dev.mvc.calendargood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.mvc.calendar.CalendarVO;
import dev.mvc.dto.SearchDTO;
import dev.mvc.post_goodbad.Post_goodbadVO;
import dev.mvc.stock.StockVO;

public interface CalendargoodProcInter {
  /**
   * 등록, 추상 메소드
   * @param contentsgoodVO
   * @return
   */
  public int create(CalendargoodVO calendargoodVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<CalendargoodVO> list_all();  
  
  /**
   * 조회
   * @param calendarno
   * @return
   */
  public CalendargoodVO read(int calendargoodno);
  
  /**
   * 글 수정
   * @param calendargoodVO
   * @return 처리된 레코드 갯수
   */
  public int update(CalendargoodVO calendargoodVO);
  
  /**
   * 삭제
   * @param calendargoodno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int calendargoodno);
  
  /**
   * 특정 컨텐츠 회원 추천
   * @param map
   * @return
   */
  public int heartCnt(HashMap<String, Object> map);
  
  /**
   * 조회
   * @param calendarno, memberno
   * @return
   */
  public CalendargoodVO read(HashMap<String, Object> map);
  
  public CalendargoodVO readByboth(HashMap<String, Object> map);
  
  public ArrayList<CalendarCalendargoodMemberVO> list_all_join();  
  
  /**
   * 추천, 비추천 검색 + 페이징 목록
   * @param searchDTO
   * @return
   */
  public ArrayList<CalendarCalendargoodMemberVO> list_search_paging(SearchDTO searchDTO);
  
  /**
   * 검색 조건에 맞는 총 추천 비추천 내역 수
   * @param searchDTO
   * @return
   */
  public int list_search_count(SearchDTO searchDTO);

}
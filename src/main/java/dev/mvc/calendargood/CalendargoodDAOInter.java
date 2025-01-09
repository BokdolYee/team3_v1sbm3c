package dev.mvc.calendargood;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.calendar.CalendarVO;

public interface CalendargoodDAOInter {
  /**
   * 등록, 추상 메소드
   * @param calendargoodVO
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
   * @param calendargoodno
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
  
}
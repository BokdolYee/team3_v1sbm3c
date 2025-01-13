package dev.mvc.calendargood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import dev.mvc.calendar.CalendarVO;
import dev.mvc.calendargood.CalendargoodDAOInter;
import dev.mvc.dto.SearchDTO;
import dev.mvc.post_goodbad.Post_goodbadVO;
import dev.mvc.stock.StockVO;

@Service("dev.mvc.calendargood.CalendargoodProc")
public class CalendargoodProc implements CalendargoodProcInter {

  @Autowired
  private CalendargoodDAOInter calendargoodDAO;  

  public CalendargoodProc() {
    System.out.println("-> CalendargoodProc created.");
}

  @Override
  public int create(CalendargoodVO calendargoodVO) {
    int cnt = this.calendargoodDAO.create(calendargoodVO);
    
    return cnt;
  }
  
  /** 목록 */
  @Override
  public ArrayList<CalendargoodVO> list_all() {
    ArrayList<CalendargoodVO> list = this.calendargoodDAO.list_all();
    
    return list;
  }
  
  /** 조회 */
  @Override
  public CalendargoodVO read(int calendargoodno) {
    CalendargoodVO calendargoodVO = this.calendargoodDAO.read(calendargoodno);
    return calendargoodVO;
  }
  
  /** 수정 */
  @Override
  public int update(CalendargoodVO calendargoodVO) {
    int cnt = this.calendargoodDAO.update(calendargoodVO);
    return cnt;
  }

  
  /** 삭제  */
  @Override
  public int delete(int calendargoodno) {
    int cnt = this.calendargoodDAO.delete(calendargoodno);
    return cnt;
  }
  
  @Override
  public int heartCnt(HashMap<String, Object> map) {
    int cnt = this.calendargoodDAO.heartCnt(map);
    return cnt;
  }

  @Override
  public CalendargoodVO read(HashMap<String, Object> map) {
    CalendargoodVO calendargoodVO = this.calendargoodDAO.read(map);
    return calendargoodVO;
  }

  @Override
  public CalendargoodVO readByboth(HashMap<String, Object> map) {
    CalendargoodVO calendargoodVO = this.calendargoodDAO.readByboth(map);
    return calendargoodVO;
  }
  
  /** 목록 */
  @Override
  public ArrayList<CalendarCalendargoodMemberVO> list_all_join() {
    ArrayList<CalendarCalendargoodMemberVO> list = this.calendargoodDAO.list_all_join();
    
    return list;
  }
  
  /**
   * 추천, 비추천 내역 검색 + 페이징
   */
  @Override
  public ArrayList<CalendarCalendargoodMemberVO> list_search_paging(SearchDTO searchDTO) {
    return this.calendargoodDAO.list_search_paging(searchDTO);
  }

  /**
   * 조건에 맞는 내역 수
   */
  @Override
  public int list_search_count(SearchDTO searchDTO) {
    return this.calendargoodDAO.list_search_count(searchDTO);
  }
}
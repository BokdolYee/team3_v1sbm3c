package dev.mvc.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import dev.mvc.newscate.NewsCateVO;
import dev.mvc.stock.StockVO;

@Service("dev.mvc.calendar.CalendarProc")
public class CalendarProc implements CalendarProcInter {
  
  @Autowired
  private CalendarDAOInter calendarDAO;
  
  public CalendarProc() {
    System.out.println("-> CalendarProc created.");
  }
  
  /** 등록 */
  @Override
  public int create(CalendarVO calendarVO) {
    int cnt = this.calendarDAO.create(calendarVO);
    
    return cnt;
  }
  
  /** 목록 */
//  @Override
//  public ArrayList<CalendarVO> list() {
//    ArrayList<CalendarVO> list = this.calendarDAO.list();
//    
//    return list;
//  }
  
  @Override
  public ArrayList<CalendarVO> list() {
    return calendarDAO.list();
  }
  
  /** 조회 */
  @Override
  public CalendarVO read(int calendarno) {
    CalendarVO calendarVO = this.calendarDAO.read(calendarno);
    return calendarVO;
  }
  
  /** 수정 */
  @Override
  public int update(CalendarVO calendarVO) {
    int cnt = this.calendarDAO.update(calendarVO);
    return cnt;
  }

  
  /** 삭제  */
  @Override
  public int delete(int calendarno) {
    int cnt = this.calendarDAO.delete(calendarno);
    return cnt;
  }

  @Override
  public ArrayList<CalendarVO> list_calendar(String date) {
    ArrayList<CalendarVO> list = this.calendarDAO.list_calendar(date);
    return list;
  }

  @Override
  public ArrayList<CalendarVO> list_calendar_day(String date) {
    ArrayList<CalendarVO> list = this.calendarDAO.list_calendar_day(date);
    return list;
  }    

  @Override
  public int increaseCnt(int calendarno) {
    int cnt = this.calendarDAO.increaseCnt(calendarno);
    return cnt;
  }
  
  @Override
  public int increaseRecom(int calendargoodno) {
    int cnt = this.calendarDAO.increaseRecom(calendargoodno);
    return cnt;
  }

  @Override
  public int decreaseRecom(int calendargoodno) {
    int cnt = this.calendarDAO.decreaseRecom(calendargoodno);
    return cnt;
  }
  
  @Override
  public int good(int calendarno) {
    
    return 0;
  }
  
  
  @Override
  public Integer list_search_count(Map<String, Object> map) {
    return calendarDAO.list_search_count(map);
  }

  @Override
  public ArrayList<CalendarVO> listSearchPaging(Map<String, Object> map) {
      // map이 null이면 HashMap으로 초기화
      if (map == null || !(map instanceof HashMap)) {
          map = new HashMap<>();
      }

      // start_num과 end_num이 없으면 기본값을 설정
      if (map.get("start_num") == null) {
          map.put("start_num", 0);  // 기본값 설정
      }
      if (map.get("end_num") == null) {
          map.put("end_num", 10);  // 기본값 설정
      }

      return calendarDAO.listSearchPaging(map);  // DAO 호출
  }





  @Override
  public String pagingBox(int now_page, String searchLabel,
                           int search_count, int record_per_page, int page_per_block) {
    int total_page = (int) (Math.ceil((double) search_count / record_per_page));
    int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
    int now_grp = (int) (Math.ceil((double) now_page / page_per_block));
    int start_page = ((now_grp - 1) * page_per_block) + 1;
    int end_page = (now_grp * page_per_block);
    StringBuffer str = new StringBuffer();
    
    str.append("<div id='paging'>");

    int _now_page = (now_grp - 1) * page_per_block;
    if (now_grp >= 2) {
      str.append("<span class='span_box_1'><a href='" + "?&word=" + searchLabel  + "&now_page=" + _now_page + "'>이전</a></span>");
    }

    for (int i = start_page; i <= end_page; i++) {
      if (i > total_page) {
        break;
      }
      if (now_page == i) {
        str.append("<span class='span_box_2'>" + i + "</span>");
      } else {
        str.append("<span class='span_box_1'><a href='" + "?word=" + searchLabel  + "&now_page=" + i + "'>" + i + "</a></span>");
      }
    }

    _now_page = (now_grp * page_per_block) + 1;
    if (now_grp < total_grp) {
      str.append("<span class='span_box_1'><a href='" + "?&word=" + searchLabel + "&now_page=" + _now_page + "'>다음</a></span>");
    }

    str.append("</div>");
    return str.toString();
  }
  
  @Override
  public int update_seqno_forward(int calendarno) {
    int cnt = this.calendarDAO.update_seqno_forward(calendarno);
    return cnt;
  }

  @Override
  public int update_seqno_backward(int calendarno) {
    int cnt = this.calendarDAO.update_seqno_backward(calendarno);
    return cnt;
  }

}
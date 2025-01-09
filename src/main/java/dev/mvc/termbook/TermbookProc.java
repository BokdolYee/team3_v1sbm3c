package dev.mvc.termbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("dev.mvc.termbook.TermbookProc")
public class TermbookProc implements TermbookProcInter {
  
  @Autowired
  private TermbookDAOInter termbookDAO;
  
  public TermbookProc() {
    System.out.println("-> TermbookProc created.");
  }
  
  /** 등록 */
  @Override
  public int create(TermbookVO termbookVO) {
    int cnt = this.termbookDAO.create(termbookVO);
    
    return cnt;
  }
  
  @Override
  public ArrayList<TermbookVO> list() {
    return termbookDAO.list();
  }

  @Override
  public TermbookVO read(Integer termno) {
    return termbookDAO.read(termno);
  }

  @Override
  public int update(TermbookVO termbookVO) {
    return termbookDAO.update(termbookVO);
  }

  @Override
  public int delete(int termno) {
    return termbookDAO.delete(termno);
  }

  @Override
  public Integer list_search_count(Map<String, Object> map) {
    return termbookDAO.list_search_count(map);
  }

  @Override
  public ArrayList<TermbookVO> listSearchPaging(Map<String, Object> map) {
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

      return termbookDAO.listSearchPaging(map);  // DAO 호출
  }





  @Override
  public String pagingBox(int now_page, String searchTerm_title, String searchCategory,
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
      str.append("<span class='span_box_1'><a href='" + "?&word=" + searchTerm_title + "&now_page=" + _now_page + "'>이전</a></span>");
    }

    for (int i = start_page; i <= end_page; i++) {
      if (i > total_page) {
        break;
      }
      if (now_page == i) {
        str.append("<span class='span_box_2'>" + i + "</span>");
      } else {
        str.append("<span class='span_box_1'><a href='" + "?word=" + searchTerm_title + "&now_page=" + i + "'>" + i + "</a></span>");
      }
    }

    _now_page = (now_grp * page_per_block) + 1;
    if (now_grp < total_grp) {
      str.append("<span class='span_box_1'><a href='"+ "?&word=" + searchTerm_title + "&now_page=" + _now_page + "'>다음</a></span>");
    }

    str.append("</div>");
    return str.toString();
  }
}
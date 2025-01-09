package dev.mvc.stockdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.stock.StockVO;

@Service("dev.mvc.stockdata.StockdataProc")
public class StockdataProc implements StockdataProcInter {

    @Autowired
    private StockdataDAOInter stockdataDAO;

    @Override
    public int create(StockdataVO stockdataVO) {
        return stockdataDAO.create(stockdataVO); // 데이터를 추가
    }

    @Override
    public StockdataVO read(int sdatano) {
        return stockdataDAO.read(sdatano); // 특정 데이터 읽기
    }

    @Override
    public int update(StockdataVO stockdataVO) {
        System.out.println("Updating stock data: " + stockdataVO);  // 로그 추가
        return stockdataDAO.update(stockdataVO); // 실제 업데이트 호출
    }

    @Override
    public int delete(int sdatano) {
        return stockdataDAO.delete(sdatano); // 데이터 삭제
    }

    @Override
    public List<StockdataVO> list() {
        return stockdataDAO.list(); // stockno에 해당하는 데이터 목록
    }
    
    @Override
    public Integer list_search_count(Map<String, Object> map) {
      return stockdataDAO.list_search_count(map);
    }
    
    @Override
    public ArrayList<StockdataVO> listSearchPaging(Map<String, Object> map) {
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

        return stockdataDAO.listSearchPaging(map);  // DAO 호출
    }
    
    @Override
    public String pagingBox(int now_page, String searchName, 
                            String list_file_name, int search_count, int record_per_page, int page_per_block) {
      int total_page = (int) (Math.ceil((double) search_count / record_per_page));
      int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
      int now_grp = (int) (Math.ceil((double) now_page / page_per_block));
      int start_page = ((now_grp - 1) * page_per_block) + 1;
      int end_page = (now_grp * page_per_block);
      StringBuffer str = new StringBuffer();
      
      str.append("<div id='paging'>");

      int _now_page = (now_grp - 1) * page_per_block;
      if (now_grp >= 2) {
        str.append("<span class='span_box_1'><a href='" + list_file_name + "?&word=" + searchName + "&now_page=" + _now_page + "'>이전</a></span>");
      }

      for (int i = start_page; i <= end_page; i++) {
        if (i > total_page) {
          break;
        }
        if (now_page == i) {
          str.append("<span class='span_box_2'>" + i + "</span>");
        } else {
          str.append("<span class='span_box_1'><a href='" + list_file_name + "?word=" + searchName + "&now_page=" + i + "'>" + i + "</a></span>");
        }
      }

      _now_page = (now_grp * page_per_block) + 1;
      if (now_grp < total_grp) {
        str.append("<span class='span_box_1'><a href='" + list_file_name + "?&word=" + searchName + "&now_page=" + _now_page + "'>다음</a></span>");
      }

      str.append("</div>");
      return str.toString();
    }
    
    @Override
    public String getStockNameByStockno(Integer stockno) {
        return stockdataDAO.getStockNameByStockno(stockno);  // 실제 DB 쿼리 호출
    }

}

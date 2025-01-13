package dev.mvc.surveytopic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.survey.SurveyVO;
import dev.mvc.surveygood.SurveySurveygoodMemberVO;



@Service("dev.mvc.surveytopic.SurveytopicProc")
public class SurveytopicProc implements SurveytopicProcInter{
  @Autowired
  private SurveytopicDAOInter surveytopicDAO;
  @Override
  public int create(SurveytopicVO surveytopicVO) {
    int cnt = this.surveytopicDAO.create(surveytopicVO);
    return cnt;
  }

  @Override
  public SurveytopicVO read(int surveytopicno) {
    SurveytopicVO surveytopicVO = this.surveytopicDAO.read(surveytopicno);
    return surveytopicVO;
  }

  @Override
  public int update(SurveytopicVO surveytopicVO) {
    int cnt = this.surveytopicDAO.update(surveytopicVO);
    return cnt;
  }

  @Override
  public int delete(int surveytopicno) {
    int cnt = this.surveytopicDAO.delete(surveytopicno);
    return cnt;
  }

  @Override
  public ArrayList<SurveytopicVO> listBySurveyno(int surveyno) {
    ArrayList<SurveytopicVO> list = this.surveytopicDAO.listBySurveyno(surveyno);
    return list;
  }

  @Override
  public ArrayList<SurveytopicVO> list_all() {
    ArrayList<SurveytopicVO> list = this.surveytopicDAO.list_all();
    return list;
  }
  
  @Override
  public String pagingBox(int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block) {
    // 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
    int total_page = (int)(Math.ceil((double)search_count / record_per_page));
    // 전체 그룹  수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
    int total_grp = (int)(Math.ceil((double)total_page / page_per_block)); 
    // 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
    int now_grp = (int)(Math.ceil((double)now_page / page_per_block));  
    
    // 1 group: 1, 2, 3 ... 9, 10
    // 2 group: 11, 12 ... 19, 20
    // 3 group: 21, 22 ... 29, 30
    int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지  
    int end_page = (now_grp * page_per_block);               // 특정 그룹의 마지막 페이지   
     
    StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름 
    
    // style이 java 파일에 명시되는 경우는 로직에 따라 css가 영향을 많이 받는 경우에 사용하는 방법
    str.append("<style type='text/css'>"); 
    str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}"); 
    str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}"); 
    str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}"); 
    str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}"); 
    str.append("  .span_box_1{"); 
    str.append("    text-align: center;");    
    str.append("    font-size: 1em;"); 
    str.append("    border: 1px;"); 
    str.append("    border-style: solid;"); 
    str.append("    border-color: #cccccc;"); 
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("  }"); 
    str.append("  .span_box_2{"); 
    str.append("    text-align: center;");    
    str.append("    background-color: #668db4;"); 
    str.append("    color: #FFFFFF;"); 
    str.append("    font-size: 1em;"); 
    str.append("    border: 1px;"); 
    str.append("    border-style: solid;"); 
    str.append("    border-color: #cccccc;"); 
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("  }"); 
    str.append("</style>"); 
    str.append("<div id='paging'>"); 

    int _now_page = (now_grp - 1) * page_per_block;  
    if (now_grp >= 2){ // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성 
      str.append("<span class='span_box_1'><a href='"+list_file_name+"?&word="+word+"&now_page="+_now_page+"'>이전</a></span>"); 
    } 
 
    // 중앙의 페이지 목록
    for(int i=start_page; i<=end_page; i++){ 
      if (i > total_page){ // 마지막 페이지를 넘어갔다면 페이 출력 종료
        break; 
      } 
  
      if (now_page == i){ // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
        str.append("<span class='span_box_2'>"+i+"</span>"); // 현재 페이지, 강조 
      }else{
        // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
        str.append("<span class='span_box_1'><a href='"+list_file_name+"?word="+word+"&now_page="+i+"'>"+i+"</a></span>");   
      } 
    } 
 
    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page),  nowGrp: 2 (11 ~ 20 page),  nowGrp: 3 (21 ~ 30 page) 
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * page_per_block)+1; //  최대 페이지수 + 1 
    if (now_grp < total_grp){ 
      str.append("<span class='span_box_1'><a href='"+list_file_name+"?&word="+word+"&now_page="+_now_page+"'>다음</a></span>"); 
    } 
    str.append("</div>"); 
     
    return str.toString(); 
  }

  @Override
  public ArrayList<SurveySurveytopicVO> list_paging(String word, int now_page, int record_per_page) {
    /*
    페이지당 10개의 레코드 출력
    1 page: WHERE r >= 1 AND r <= 10
    2 page: WHERE r >= 11 AND r <= 20
    3 page: WHERE r >= 21 AND r <= 30
    
    now_page 1: WHERE r >= 1 AND r <= 10
    now_page 2: WHERE r >= 11 AND r <= 20
    now_page 3: WHERE r >= 21 AND r <= 30
    
    int start_num = (now_page - 1) * record_per_page;
    int end_num=start_num + record_per_page;
    */

   int start_num = ((now_page - 1) * record_per_page) + 1;
   int end_num=(start_num + record_per_page) - 1;

   // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);
   
   Map<String, Object> map = new HashMap<String, Object>();
   map.put("word", word);
   map.put("start_num", start_num);
   map.put("end_num", end_num);
   
   ArrayList<SurveySurveytopicVO> list = this.surveytopicDAO.list_paging(map);
   // System.out.println("-> " + list.size());
   
   return list;

  }

  @Override
  public Integer list_search_count(String word) {
    int cnt = this.surveytopicDAO.list_search_count(word);
    return cnt;
  }

  @Override
  public ArrayList<SurveySurveytopicVO> list_search(String word) {
    ArrayList<SurveySurveytopicVO> list = this.surveytopicDAO.list_search(word);
    return list;
  }

  @Override
  public ArrayList<SurveySurveytopicVO> list_search_paging_join(String word, int now_page, int record_per_page) {
    /*
     페이지당 10개의 레코드 출력
     1 page: WHERE r >= 1 AND r <= 10
     2 page: WHERE r >= 11 AND r <= 20
     3 page: WHERE r >= 21 AND r <= 30
     
     now_page 1: WHERE r >= 1 AND r <= 10
     now_page 2: WHERE r >= 11 AND r <= 20
     now_page 3: WHERE r >= 21 AND r <= 30
     
     int start_num = (now_page - 1) * record_per_page;
     int end_num=start_num + record_per_page;
     */

    int start_num = ((now_page - 1) * record_per_page) + 1;
    int end_num=(start_num + record_per_page) - 1;

    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("word", word);
    map.put("start_num", start_num);
    map.put("end_num", end_num);
    
    ArrayList<SurveySurveytopicVO> list = this.surveytopicDAO.list_search_paging_join(map);
 
    
    return list;
  }

  






  
  

  











}

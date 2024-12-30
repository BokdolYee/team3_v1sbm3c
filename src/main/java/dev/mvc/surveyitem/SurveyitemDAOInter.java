package dev.mvc.surveyitem;

import java.util.ArrayList;
import java.util.Map;





public interface SurveyitemDAOInter {

  // 등록
  public int create(SurveyitemVO surveyitemVO);

  // 조회
  public SurveyitemVO read(int surveyitemno);

  // 수정
  public int update(SurveyitemVO surveyitemVO);

  // 삭제
  public int delete(int surveyitemno);

  // 전체 목록
  public ArrayList<SurveyitemVO> list_all();

  public int update_itemseqno_forward(int surveyitemno);
  
  public int update_itemseqno_backward(int surveyitemno);
  
  public int increaseCnt(int surveyno);
  
  
  
}

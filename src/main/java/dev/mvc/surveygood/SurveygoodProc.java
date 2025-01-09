package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.survey.SurveyVO;

@Component("dev.mvc.surveygood.SurveygoodProc")
public class SurveygoodProc implements SurveygoodProcInter {
  
  @Autowired
  SurveygoodDAOInter surveygoodDAO;
  
  
  @Override
  public int create(SurveygoodVO surveygoodVO) {
    int cnt = this.surveygoodDAO.create(surveygoodVO);
    return cnt;
  }


  @Override
  public ArrayList<SurveygoodVO> list_all() {
    ArrayList<SurveygoodVO> list = this.surveygoodDAO.list_all();
    return list;
  }


  @Override
  public int delete(int surveygoodno) {
    int cnt = this.surveygoodDAO.delete(surveygoodno);
    return cnt;
  }


  @Override
  public int hartCnt(HashMap<String, Object> map) {
    int cnt = this.surveygoodDAO.hartCnt(map);
    return cnt;
  }


  @Override
  public SurveygoodVO read(int surveygoodno) {
    SurveygoodVO surveygoodVO = this.surveygoodDAO.read(surveygoodno);
    return surveygoodVO;
  }


  @Override
  public SurveygoodVO readBySurveynoMemberno(HashMap<String, Object> map) {
    SurveygoodVO surveygoodVO = this.surveygoodDAO.readBySurveynoMemberno(map);
    return surveygoodVO;
  }


  @Override
  public ArrayList<SurveySurveygoodMemberVO> list_all_join() {
    ArrayList<SurveySurveygoodMemberVO> list = this.surveygoodDAO.list_all_join();
    return list;
  }

}

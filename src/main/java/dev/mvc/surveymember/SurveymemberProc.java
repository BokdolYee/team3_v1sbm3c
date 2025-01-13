package dev.mvc.surveymember;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.survey.SurveyVO;

@Component("dev.mvc.surveymember.SurveymemberProc")
public class SurveymemberProc implements SurveymemberProcInter {
  
  @Autowired
  SurveymemberDAOInter surveymemberDAO;
  
  
  @Override
  public int create(SurveymemberVO surveymemberVO) {
    int cnt = this.surveymemberDAO.create(surveymemberVO);
    return cnt;
  }


  @Override
  public ArrayList<SurveymemberVO> list_all() {
    ArrayList<SurveymemberVO> list = this.surveymemberDAO.list_all();
    return list;
  }


  @Override
  public int delete(int surveymemberno) {
    int cnt = this.surveymemberDAO.delete(surveymemberno);
    return cnt;
  }


//  @Override
//  public int hartCnt(HashMap<String, Object> map) {
//    int cnt = this.surveymemberDAO.hartCnt(map);
//    return cnt;
//  }


  @Override
  public SurveymemberVO read(int surveymemberno) {
    SurveymemberVO surveymemberVO = this.surveymemberDAO.read(surveymemberno);
    return surveymemberVO;
  }


//  @Override
//  public SurveygoodVO readBySurveynoMemberno(HashMap<String, Object> map) {
//    SurveygoodVO surveygoodVO = this.surveygoodDAO.readBySurveynoMemberno(map);
//    return surveygoodVO;
//  }


  @Override
  public ArrayList<SurveytopicitemmemberVO> list_all_join() {
    ArrayList<SurveytopicitemmemberVO> list = this.surveymemberDAO.list_all_join();
    return list;
  }

}

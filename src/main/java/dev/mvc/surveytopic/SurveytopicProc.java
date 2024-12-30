package dev.mvc.surveytopic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



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
  public int delete(int surveytopicVO) {
    int cnt = this.surveytopicDAO.delete(surveytopicVO);
    return cnt;
  }

  @Override
  public ArrayList<SurveytopicVO> listBySurveyno(int surveyno) {
    ArrayList<SurveytopicVO> list = this.surveytopicDAO.listBySurveyno(surveyno);
    return list;
  }

  @Override
  public int update_seqno_forward(int surveyno) {
    int cnt = this.surveytopicDAO.update_seqno_forward(surveyno);
    return cnt;
  }

  @Override
  public int update_seqno_backward(int surveyno) {
    int cnt = this.surveytopicDAO.update_seqno_backward(surveyno);
    return cnt;
  }




  
  

  











}

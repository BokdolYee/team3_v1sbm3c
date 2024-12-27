package dev.mvc.issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

public interface IssueDAOInter {
  public int create(IssueVO issueVO);
  public ArrayList<IssueVO> list();
  public IssueVO read(int issueno);
  public int update(IssueVO issueVO);
  public int delete(int issueno);
  public int increaseCnt(int issueno);
  public int list_search_count(String word);
  public ArrayList<IssueVO> list_search_paging(Map<String, Object> map);
  public ArrayList<IssueVO> listUrgent();
}

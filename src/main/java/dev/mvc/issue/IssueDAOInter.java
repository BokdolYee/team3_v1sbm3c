package dev.mvc.issue;

import java.util.List;

public interface IssueDAOInter {
  public int create(IssueVO issueVO);
  public List<IssueVO> list();
  public IssueVO read(int issueno);
  public int update(IssueVO issueVO);
  public int delete(int issueno);
  public int increaseCnt(int issueno);
}

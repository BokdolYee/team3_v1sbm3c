package dev.mvc.issue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.issue.IssueProc")
public class IssueProc implements IssueProcInter{
  @Autowired
  Security security;

  @Autowired
  private IssueDAOInter issueDAO;

  @Override
  public int create(IssueVO issueVO) {
      return issueDAO.create(issueVO);
  }

  @Override
  public List<IssueVO> list() {
      return issueDAO.list();
  }

  @Override
  public IssueVO read(int issueno) {
      issueDAO.increaseCnt(issueno); // 조회수 증가
      return issueDAO.read(issueno);
  }

  @Override
  public int update(IssueVO issueVO) {
      return issueDAO.update(issueVO);
  }

  @Override
  public int delete(int issueno) {
      return issueDAO.delete(issueno);
  }

  @Override
  public int increaseCnt(int issueno) {
      return issueDAO.increaseCnt(issueno);
  }
}

package dev.mvc.issue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

public interface IssueProcInter {
  public int create(IssueVO issueVO);
  public ArrayList<IssueVO> list();
  public IssueVO read(int issueno);
  public int update(IssueVO issueVO);
  public int delete(int issueno);
  public int increaseCnt(int issueno);
  public Integer list_search_count(String word);
  public String pagingBox(int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  public ArrayList<IssueVO> list_search_paging(String word, int now_page, int record_per_page);
}
package dev.mvc.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.contents.ContentsVO;
import dev.mvc.tool.Security;

@Component("dev.mvc.reply.ReplyProc")
public class ReplyProc implements ReplyProcInter {
  @Autowired
  Security security;
  
  @Autowired
  private ReplyDAOInter replyDAO;
  
  @Override
  public int create(ReplyVO replyVO) {
    int cnt = this.replyDAO.create(replyVO);

    return cnt;
  }
  
  @Override
  public ReplyVO read(int replyno) {
    ReplyVO replyVO = this.replyDAO.read(replyno);
    return replyVO;
  }
  
  @Override
  public int update_content(ReplyVO replyVO) {
    int cnt = this.replyDAO.update_content(replyVO);
    return cnt;
  }

  @Override
  public int update_file(ReplyVO replyVO) {
    int cnt = this.replyDAO.update_file(replyVO);
    return cnt;
  }

  @Override
  public int delete(int replyno) {
    int cnt = this.replyDAO.delete(replyno);
    
    return cnt;
  }
  @Override
  public List<ReplyVO> listByContentNoJoin(int contentno) {
      return replyDAO.listByContentNoJoin(contentno);
  }
  
}

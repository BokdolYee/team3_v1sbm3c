package dev.mvc.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import dev.mvc.tool.Tool;

public class Reply {
  @Autowired
  private ReplyDAOInter replyDAO;
  
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc")
  private ReplyProc replyProc;
  
  public List<ReplyVO> listByContentNoJoin(int contentno) {
    List<ReplyVO> list = replyDAO.listByContentNoJoin(contentno);
    String content = "";
    
    // 특수 문자 변경
    for (ReplyVO replyMemberVO:list) {
      content = replyMemberVO.getContent();
      content = Tool.convertChar(content);
      replyMemberVO.setContent(content);
    }
    return list;
  }
}

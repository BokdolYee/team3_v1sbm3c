package dev.mvc.attachment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.attachment.AttachmentProc")
public class AttachmentProc implements AttachmentProcInter{

  //AttachmentDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당
  @Autowired
  AttachmentDAOInter attachmentDAO;
  
  public AttachmentProc() {
    System.out.println(" -> AttachmentProc 생성됨");
  }
  
  /**
   * 첨부파일 등록
   */
  @Override
  public int createBatch(List<AttachmentVO> attachmentList) {
    int cnt = 0;
    for(AttachmentVO vo : attachmentList) {
      cnt += this.attachmentDAO.create(vo);
    }
    
    return cnt;
  }

  /**
   * 첨부 이미지들을 화면에 뿌리기 위해 리스트 형태로 가져오기
   */
  @Override
  public List<AttachmentVO> list_by_postno(int postno) {
    return this.attachmentDAO.list_by_postno(postno);
  }

  @Override
  public int delete(int attachmentno) {
    int cnt = this.attachmentDAO.delete(attachmentno);
    
    return cnt;
  }
}

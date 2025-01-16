package dev.mvc.attachment;

import java.util.List;

public interface AttachmentProcInter {

  /**
   * 첨부파일 등록
   * @param attachmentVO
   * @return
   */
  public int createBatch(List<AttachmentVO> attachmentList);
  
  /**
   * 첨부 이미지들을 화면에 뿌리기 위해 리스트 형태로 가져오기
   * @param postno
   * @return
   */
  public List<AttachmentVO> list_by_postno(int postno);
  
  /**
   * 첨부파일 삭제
   * @param attachmentno
   * @return
   */
  public int delete(int attachmentno);
}

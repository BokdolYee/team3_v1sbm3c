package dev.mvc.reply;

public interface ReplyDAOInter {

  public int create(ReplyVO replyVO);

  public ReplyVO read(int replyno);

  /**
   * 글 정보 수정
   * @param replyVO
   * @return 처리된 레코드 갯수
   */
  public int update_content(ReplyVO replyVO);

  /**
   * 파일 정보 수정
   * @param replyVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(ReplyVO replyVO);
 
  /**
   * 삭제
   * @param replyno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int replyno);
}

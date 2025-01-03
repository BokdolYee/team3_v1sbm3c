package dev.mvc.reply;

import java.util.List;

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
  
  /**
   * 특정 콘텐츠의 댓글 목록 조회
   * @param contentno 콘텐츠 번호
   * @return 댓글 목록
   */
    public List<ReplyVO> listByContentNoJoin(int contentno);
}

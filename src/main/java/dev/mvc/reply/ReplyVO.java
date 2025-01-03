package dev.mvc.reply;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReplyVO {
  /** 컨텐츠 번호 */
  private int replyno;

  /** 컨텐츠 번호 */
  private int contentno;  // contents.xml에서 #{newscateno}를 사용하므로 필드명 동일하게 유지
  
  /** 사용자 번호 */
  private int memberno;
  
  /** 부모 댓글 번호 */
  private int parentreplyno = 0;

  /** 내용 */
  private String content = "";

  // 파일 업로드 관련 필드
  private MultipartFile file1MF; 
  private String file1 = "";
  private String file1saved = "";
  private String thumb1 = "";
  private long size1 = 0;
  private String size1_label = "";
  
  /** 등록 날짜 */
  private Date rdate;  // date 타입으로 수정

}

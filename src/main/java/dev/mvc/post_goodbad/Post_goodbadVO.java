package dev.mvc.post_goodbad;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Post_goodbadVO {

  /** 게시물 추천비추천 번호 */
  private int goodbadno;
  
  /** 추천 or 비추천 */
  private String goodbad = "";
  
  /** 추천비추천 날짜 */
  private String rdate = "";
  
  /** 게시물 번호 */
  private int postno;
  
  /** 회원 번호 */
  private int memberno;
}

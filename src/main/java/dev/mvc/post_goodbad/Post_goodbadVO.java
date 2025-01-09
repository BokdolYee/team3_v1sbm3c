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
  
  
  
  /** memberno를 이용한, member 테이블과 INNER JOIN 하여 조회할 닉네임을 저장할 변수 */
  private String nickname = "";
  
  /** postno를 이용한, post_earning 테이블과 INNER JOIN 하여 조회할 제목을 저장할 변수 */
  private String title = "";
  
  /** postno를 이용한, post_earning 테이블과 INNER JOIN 하여 조회할 내용을 저장할 변수 */
  private String content = "";
}

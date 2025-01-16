package dev.mvc.post_viewlog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 post_viewlogno       NUMBER(10)              NOT NULL, -- 수익 인증 게시물 조회 내역 번호(기본키)
 memberno              NUMBER(10)              NOT NULL, -- 회원 번호(외래키)
 postno                   NUMBER(10)              NOT NULL,
 rdate                     DATE                    NOT NULL, -- 첫 조회일
 ldate                     DATE                    NOT NULL, -- 마지막 조회일
 viewcnt                 NUMBER(10) DEFAULT 0    NOT NULL, -- 조회수(회원이 해당 게시물을 몇 번 조회했는지)
 
 PRIMARY KEY (post_viewlogno),
 FOREIGN KEY(memberno) REFERENCES member (memberno),
 FOREIGN KEY(postno) REFERENCES post_earning (postno) 
  
 
 */

@Getter @Setter @ToString
public class Post_viewlogVO {
  
  /** 수익 인증 게시물 조회 내역 번호 */
  private int post_viewlogno;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 수익 인증 게시물 번호 */
  private int postno;
  
  /** 첫 조회일 */
  private String rdate = "";
  
  /** 마지막 조회일 */
  private String ldate = "";
  
  /** 조회수 */
  private int viewcnt = 0;
  
  
  
  /** postno를 이용한 post_earning 테이블과 JOIN 하여 조회할 제목을 저장할 변수 */
  private String title = "";
  private String content = "";
  private String nickname = "";
  private String thumb = "";
}

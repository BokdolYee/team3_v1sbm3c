package dev.mvc.post_earning;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
    postno     NUMBER(10)                NOT NULL, -- 게시물 번호, 레코드를 구분하는 컬럼, 기본키
    memberno   NUMBER(10)                NOT NULL, -- 회원 번호, 외래키
    viewcnt    NUMBER(5)       DEFAULT 0 NOT NULL, -- 조회수, 게시물 등록 시 기본적으로 0
    replycnt   NUMBER(4)       DEFAULT 0 NOT NULL, -- 등록된 댓글 수, 게시물 등록 시 기본적으로 0
    goodcnt    NUMBER(4)       DEFAULT 0 NOT NULL, -- 추천수, 게시물 등록 시 기본적으로 0
    badcnt     NUMBER(4)       DEFAULT 0 NOT NULL, -- 비추천수, 게시물 등록 시 기본적으로 0
    title      VARCHAR(60)               NOT NULL, -- 제목
    content    VARCHAR(1000)             NOT NULL, -- 내용
    rdate      DATE                      NOT NULL, -- 등록일    
    udate      DATE,                               -- 수정일, 처음 댓글 등록 시 수정 상태가 아니므로 NULL 허용
*/

@Getter @Setter @ToString
public class Post_earningVO {

  /** 게시물 번호 */
  private int postno;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 조회수 */
  private int viewcnt = 0;
  
  /** 댓글수 */
  private int replycnt = 0;
  
  /** 추천수 */
  private int goodcnt = 0;
  
  /** 비추천수 */
  private int badcnt = 0;
  
  /** 제목 */
  private String title = "";
  
  /** 내용 */
  private String content = "";
  
  /** 등록일 */
  private String rdate = "";
  
  /** 수정일 */
  private String udate = "";
  
  
  
  /** memberno를 이용한 member 테이블과 JOIN 하여 조회할 닉네임을 저장할 변수 */
  private String nickname = "";
}

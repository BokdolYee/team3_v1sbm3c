package dev.mvc.loginlog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class LoginlogVO {

  /*
     loginlogno   NUMBER(10)              NOT NULL, -- 로그인 기록 번호, 기본키
     id           VARCHAR(30)             NOT NULL, -- 로그인 시도한 id
     ip           VARCHAR(15)             NOT NULL, -- 로그인 시도 ip ex) 121.160.41.226
     result       VARCHAR(1) DEFAULT 'F'  NOT NULL, -- 로그인 성공 여부, T OR F, ORACLE DB는 BOOLEAN 타입 없어서 NUMBER나 VARCHAR로 해야 함
     ldate        DATE                    NOT NULL, -- 로그인 성공 시 날짜 및 시간 ex) 2024-12-09 14:56:31
 
     PRIMARY KEY(loginlogno) 
   */
  
  /** 회원 번호(기본키) */
  private int loginlogno = 0;
  
  /** 로그인 시도 id */
  private String id = "";
  
  /** 로그인 시도 ip */
  private String ip = "";
  
  /** 로그인 성공 여부 */
  private String result = "F";
  
  /** 로그인 시도 날짜(년월일시분초) */
  private String ldate = "";
  
}

package dev.mvc.attachment;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
  attachmentno    NUMBER(10)                 NOT NULL, -- 첨부파일 번호, 기본키
  postno      NUMBER(10)                 NOT NULL, -- 게시물 번호, 외래키
  upfile      VARCHAR(100)               NOT NULL, -- 업로드된 파일명
  thumb       VARCHAR(100)                       , -- 썸네일 이미지
  fname       VARCHAR(100)               NOT NULL, -- 원본 파일명
  fsize       NUMBER(10)                 NOT NULL, -- 파일 크기
  rdate       DATE                       NOT NULL, -- 등록일
  udate       DATE                               , -- 수정일
  visible     CHAR(1)        DEFAULT 'Y' NOT NULL, -- 썸네일 이미지 표시 여부
  uuid        VARCHAR(40)                NOT NULL, -- 파일 고유 식별자 
 */

@Getter @Setter @ToString
public class AttachmentVO {

  /** 첨부파일 번호 */
  private int attachmentno;
  
  /** 게시물 번호 */
  private int postno;
  
  /** 업로드된 파일명 */
  private String upfile = "";
  
  /** 썸네일 이미지 */
  private String thumb = "";
  
  /** 원본 파일명 */
  private String fname = "";
  
  /** 파일 크기 */
  private long fsize;
  
  /** 등록일 */
  private String rdate = "";
  
  /** 수정일 */
  private String udate = "";
  
  /** 썸네일 표시 여부 */
  private String visible = "Y";
  
  /** 파일 고유 식별자 */
  private String uuid = "";
  
  /** 진짜 파일 */
  private MultipartFile file1;
}

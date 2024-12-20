package dev.mvc.issue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class IssueVO {
  /** 공지사항 번호 */
  private int issueno;
  /** 공지사항 조회수 */
  private int cnt;
  /** 공지사항 내용 */
  private String content;
  /** 공지사항 등록일 */
  private String rdate;
  /** 공지사항 제목 */
  private String title;
}

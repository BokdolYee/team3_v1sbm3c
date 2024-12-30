package dev.mvc.surveyitem;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class SurveyitemVO {

  /** 설문조사 항목 번호 */
  private Integer surveyitemno = 0;
  
  private int surveyno;
  
  /** 설문조사 조사 항목 타이틀 */
  private String item;

  /** 항목 출력 순서 */
  private Integer itemseqno;
  
  /** 항목 선택 인원 수 */
  private String itemcnt;
  
  


  
  
  



}

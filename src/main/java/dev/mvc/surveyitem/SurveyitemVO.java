package dev.mvc.surveyitem;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SurveyitemVO {
  //설문조사 항목 번호
  private Integer surveyitemno;
  
  //설문조사 개별문제 번호
  private Integer surveytopicno;
  
  //항목 내용
  @NotEmpty(message="제목은 필수")
  @Size(min=1, max=200, message="제목은 최소 1자에서 최대 200자입니다.")
  private String item;
  
  //항목 출력순서
  @NotNull(message="출력순서 필수")
  @Min(value = 1, message = "출력순서는 1 이상이어야 합니다.")
  @Max(value = 10, message = "출력순서는 최대 10까지 가능합니다.")
  private Integer itemseq;
  
  //선택 인원 수
  private Integer itemcnt;
}

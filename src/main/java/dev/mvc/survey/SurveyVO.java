package dev.mvc.survey;

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
public class SurveyVO {

  /** 설문조사 번호 */
  private Integer surveyno = 0;
  
  /** 설문조사 타이틀 */
  @NotEmpty(message="제목은 필수")
  @Size(min=2, max=15, message="제목은 최소 2자에서 최대 15자입니다.")
  private String topic;
  /** 시작 날 */
  @NotEmpty(message="시작 날은 필수")
  @Size(min=2, max=40, message="시작 날은 10자.")
  private String startdate;
  /** 완료 날 */
  @Size(min=2, max=40, message="완료 날은 10자.")
  private String enddate;
  /** 진행 여부 */
  @NotEmpty(message="출력 모드는 필수입니다.")
  @Pattern(regexp="^[YN]$", message=" Y또는 N을 입력해야 합니다")
  private String visible;
  /** 선택 인원 수 */
  @NotEmpty(message="조회수는 0이 기본입니다.")
  @NotNull(message="선택 조회 수는 0이 기본")
  private String cnt;
  
  private int recom;
  
  private MultipartFile file1MF = null;
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  /** 메인 이미지 */
  private String file1 = "";
  /** 실제 저장된 메인 이미지 */
  private String file1saved = "";
  /** 메인 이미지 preview */
  private String thumb1 = "";
  /** 메인 이미지 크기 */
  private long size1 = 0;

  
  
  



}

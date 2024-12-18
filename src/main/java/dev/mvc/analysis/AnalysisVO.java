package dev.mvc.analysis;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnalysisVO {
    /** 분석 번호, Sequence에서 자동 생성 */
    private Integer analysisno = 0;

    /** 영향 */
    @Size(max = 300, message = "영향의 입력 글자 수는 최대 300자이어야 합니다.")
    private String impact;

    /** 뉴스 본문 번호 (외래 키) */
    @NotNull(message = "요약 번호는 필수 입력 항목입니다.")
    private Integer newsno = 0;
}

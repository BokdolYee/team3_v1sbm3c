package dev.mvc.news;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NewsVO {
    /** 뉴스 번호, Sequence에서 자동 생성 */
    private Integer newsno = 0;

    /** 뉴스 텍스트 */
    @NotEmpty(message = "텍스트는 필수 입력 항목입니다.")
    private String text;

    /** URL */
    @Size(max = 255, message = "URL은 최대 255자이어야 합니다.")
    private String url;

    /** 출처 */
    @Size(max = 300, message = "출처는 최대 300자이어야 합니다.")
    private String source;

    /** 발행일 */
    @NotEmpty(message = "발행일은 필수 입력 항목입니다.")
    private String publishDate;

    /** 분석 번호 */
    @NotNull(message = "분석 번호는 필수 입력 항목입니다.")
    private Integer analysisno = 0;
}

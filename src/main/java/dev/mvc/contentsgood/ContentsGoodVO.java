package dev.mvc.contentsgood;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentsGoodVO {
	/** 추천 번호 */
    private int contentsgoodno;
    
    /** 등록 날짜 */
    private Date rdate;  // date 타입으로 수정
    
    /** 컨텐츠 번호 */
    private int contentno;
    
    /** 회원 번호 */
    private int memberno;

    /** 추천 or 비추천 */
    private String goodbad = "";
    

}

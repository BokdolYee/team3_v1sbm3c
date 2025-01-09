package dev.mvc.contents;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ContentsVO {
    /** 컨텐츠 번호 */
    private int contentno;

    /** 카테고리 번호 */
    private int newscateno;  // contents.xml에서 #{newscateno}를 사용하므로 필드명 동일하게 유지
    private int newsno;
    /** 제목 */
    private String title = "";

    /** 내용 */
    private String content = "";

    /** 추천수 */
    private int recom;

    /** 조회수 */
    private int cnt = 0;

    /** 댓글수 */
    private int replycnt = 0;

    /** 패스워드 */
    private String passwd = "";

    /** 게시 여부 */
    private char visible;
    private int stockno;
    /** 검색어 */
    private String word = "";

    /** 등록 날짜 */
    private Date rdate;  // date 타입으로 수정

    /** 지도 */
    private String map = "";

    /** Youtube */
    private String youtube = "";

    /** mp4 */
    private String mp4 = "";

    // 파일 업로드 관련 필드
    private MultipartFile file1MF; 
    private String file1 = "";
    private String file1saved = "";
    private String thumb1 = "";
    private long size1 = 0;
    private String size1_label = "";

    /** 총 게시물 수 (all_cnt) */
    private long all_cnt = 0;
    
    private String summary; // 추가된 필드
    private String analysis; // 추가된 필드
}


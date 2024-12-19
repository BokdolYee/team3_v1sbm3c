package dev.mvc.contents;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ContentsVO {
    /** 컨텐츠 번호 */
    private int contentsno;
    /** 관리자 권한의 회원 번호 */
    private int memberno;
    /** 카테고리 번호 */
    private int newscateno;  // contents.xml에서 #{newscateno}를 사용하므로 필드명 동일하게 유지
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
    /** 검색어 */
    private String word = "";
    /** 등록 날짜 */
    private String rdate = "";
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

    // 쇼핑몰 상품 관련 필드
    private int price = 0;
    private int dc = 0;
    private int saleprice = 0;
    private int point = 0;
    private int salecnt = 0;

    // Lombok이 위의 필드에 대해 get/set 메소드를 자동 생성합니다.
    // 예: getNewscateno(), setNewscateno() 등이 자동으로 생성됩니다.
}

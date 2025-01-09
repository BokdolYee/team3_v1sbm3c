package dev.mvc.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.mvc.newscate.NewsCateVO;

/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface ContentsDAOInter {
  /**
   * 등록, 추상 메소드
   * @param contentsVO
   * @return
   */
  public int create(ContentsVO contentsVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<ContentsVO> list_all();
  
  /**
   * 카테고리별 등록된 글 목록
   * @param cateno
   * @return
   */
  public ArrayList<ContentsVO> list_by_cateno(int newscateno);
  
  /**
   * 조회
   * @param contentno
   * @return
   */
  public ContentsVO read(int contentno);
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<ContentsVO> list_by_cateno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_cateno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param contentsVO
   * @return
   */
  public ArrayList<ContentsVO> list_by_cateno_search_paging(HashMap<String, Object> map);
  
  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(ContentsVO contentsVO);

  /**
   * 파일 정보 수정
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(ContentsVO contentsVO);
 
  /**
   * 삭제
   * @param contentno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int contentno);
  
  /**
   * FK cateno 값이 같은 레코드 갯수 산출
   * @param cateno
   * @return
   */
  public int count_by_cateno(int newscateno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param cateno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_cateno(int newscateno);
  
  /**
   * FK memberno 값이 같은 레코드 갯수 산출
   * @param memberno
   * @return
   */
  public int count_by_memberno(int memberno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param memberno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_memberno(int memberno);
  
  /**
   * 글 수 증가
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int contentno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int contentno);
  
  public void updateCntCount(int newscateno);
  
  public void updateCnt(int newscateno);
  
  public void resetCnt(int newscateno);

  public void increaseRecom(int contentno);

  public void decreaseRecom(int contentno);
}
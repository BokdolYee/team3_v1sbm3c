package dev.mvc.contentsgood;

import java.util.ArrayList;
import java.util.HashMap;

public interface ContentsGoodProcInter {
	/**
	   * 등록, 추상 메소드
	   * @param contentsgoodVO
	   * @return
	   */
	  public int create(ContentsGoodVO contentsgoodVO);
	  
	  /**
	   * 모든 목록
	   * @return
	   */
	  public ArrayList<ContentsGoodVO> list_all();
	  
	  /**
	   * 삭제
	   * @param contentsgoodno
	   * @return
	   */
	  public int delete(int contentsgoodno);
	  
	  /**
	   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
	   * @param map
	   * @return
	   */
	  public int hartCnt(HashMap<String, Object> map);  
	  
	  /**
	   * 조회
	   * @param contentsgoodno
	   * @return
	   */
	  public ContentsGoodVO read(int contentsgoodno);

	  /**
	   * contentsno, memberno로 조회
	   * @param map
	   * @return
	   */
	  public ContentsGoodVO readByContentsnoMemberno(HashMap<String, Object> map);
	  
	  /**
	   * 모든 목록, 테이블 3개 join
	   * @return
	   */
	  public ArrayList<ContentsContentsgoodMemberVO> list_all_join();

	  public int delete_conts(int contentno);
}

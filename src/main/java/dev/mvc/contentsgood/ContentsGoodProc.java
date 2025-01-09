package dev.mvc.contentsgood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.contentsgood.ContentsGoodProc")
public class ContentsGoodProc implements ContentsGoodProcInter{

	@Autowired
	Security security;
	  
	@Autowired
	private ContentsGoodDAOInter contentgoodDAO; // Cate DAO 주입

	private Object contentsgoodDAO;
  
	  @Override
	  public int create(ContentsGoodVO contentsgoodVO) {
	    int cnt = this.contentgoodDAO.create(contentsgoodVO);
	    return cnt;
	  }

	  @Override
	  public ArrayList<ContentsGoodVO> list_all() {
	    ArrayList<ContentsGoodVO> list = this.contentgoodDAO.list_all();
	    return list;
	  }

	  @Override
	  public int delete(int contentsgoodno) {
	    int cnt = this.contentgoodDAO.delete(contentsgoodno);
	    return cnt;
	  }
	  
	  @Override
	  public int delete_conts(int contentno) {
	    int cnt = this.contentgoodDAO.delete_conts(contentno);
	    return cnt;
	  }

	  @Override
	  public int hartCnt(HashMap<String, Object> map) {
	    int cnt = this.contentgoodDAO.hartCnt(map);
	    return cnt;
	  }

	  @Override
	  public ContentsGoodVO read(int contentsgoodno) {
		  ContentsGoodVO contentsgoodVO = this.contentgoodDAO.read(contentsgoodno);
	    return contentsgoodVO;
	  }

	  @Override
	  public ContentsGoodVO readByContentsnoMemberno(HashMap<String, Object> map) {
		  ContentsGoodVO contentsgoodVO = this.contentgoodDAO.readByContentsnoMemberno(map);
	    return contentsgoodVO;
	  }

	  @Override
	  public ArrayList<ContentsContentsgoodMemberVO> list_all_join() {
	    ArrayList<ContentsContentsgoodMemberVO> list = this.contentgoodDAO.list_all_join();
	    return list;
	  }
}

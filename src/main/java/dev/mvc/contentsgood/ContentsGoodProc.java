package dev.mvc.contentsgood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.contentsgood.ContentsGoodProc")
public class ContentsGoodProc implements ContentsGoodProcInter{

	@Autowired
	Security security;
	  
	@Autowired
	private ContentsGoodDAOInter contentgoodDAO; // Cate DAO 주입
  
	@Override
	public int create(ContentsGoodVO contentsgoodVO) {
	    int cnt = this.contentgoodDAO.create(contentsgoodVO);
	    return cnt;
	}
	
	
}

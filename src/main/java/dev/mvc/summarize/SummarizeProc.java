package dev.mvc.summarize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.summarize.SummarizeProc")
public class SummarizeProc implements SummarizeProcInter {
    @Autowired
    private SummarizeDAOInter summarizeDAO;
    
    @Override
    public int create(SummarizeVO summarizeVO) {
        return summarizeDAO.create(summarizeVO);
    }

    @Override
    public SummarizeVO read(Integer summaryno) {
        return summarizeDAO.read(summaryno);
    }

    @Override
    public int update(SummarizeVO summarizeVO) {
        return summarizeDAO.update(summarizeVO);
    }

    @Override
    public int delete(int summaryno) {
        return summarizeDAO.delete(summaryno);
    }

    @Override
    public ArrayList<SummarizeVO> search(String keyword) {
        return summarizeDAO.search(keyword);
    }

    @Override
    public ArrayList<SummarizeVO> listPaging(Map<String, Object> param) {
        return summarizeDAO.listPaging(param);
    }
}

package dev.mvc.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.analysis.AnalysisProc")
public class AnalysisProc implements AnalysisProcInter {
    @Autowired
    private AnalysisDAOInter analysisDAO;
    
    @Override
    public int create(AnalysisVO analysisVO) {
        return analysisDAO.create(analysisVO);
    }

    @Override
    public AnalysisVO read(Integer analysisno) {
        return analysisDAO.read(analysisno);
    }

    @Override
    public int update(AnalysisVO analysisVO) {
        return analysisDAO.update(analysisVO);
    }

    @Override
    public int delete(int analysisno) {
        return analysisDAO.delete(analysisno);
    }

    @Override
    public ArrayList<AnalysisVO> search(String keyword) {
        return analysisDAO.search(keyword);
    }

    @Override
    public ArrayList<AnalysisVO> listPaging(Map<String, Object> param) {
        return analysisDAO.listPaging(param);
    }
}

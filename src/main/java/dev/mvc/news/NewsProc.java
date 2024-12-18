package dev.mvc.news;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.news.NewsProc")
public class NewsProc implements NewsProcInter {
    @Autowired
    private NewsDAOInter newsDAO;
    
    @Override
    public int create(NewsVO newsVO) {
        return newsDAO.create(newsVO);
    }

    @Override
    public NewsVO read(Integer newsno) {
        return newsDAO.read(newsno);
    }

    @Override
    public int update(NewsVO newsVO) {
        return newsDAO.update(newsVO);
    }

    @Override
    public int delete(int newsno) {
        return newsDAO.delete(newsno);
    }

    @Override
    public ArrayList<NewsVO> search(String keyword) {
        return newsDAO.search(keyword);
    }

    @Override
    public ArrayList<NewsVO> listPaging(Map<String, Object> param) {
        return newsDAO.listPaging(param);
    }
}


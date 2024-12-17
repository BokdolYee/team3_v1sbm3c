package dev.mvc.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.stock.StockProc")
public class StockProc implements StockProcInter {

    @Autowired
    private StockDAOInter stockDAO;

    @Override
    public int create(StockVO stockVO) {
        return stockDAO.create(stockVO);
    }

    @Override
    public StockVO read(int stockno) {
        return stockDAO.read(stockno);
    }

    @Override
    public List<StockVO> list() {
        return stockDAO.list();
    }

    @Override
    public int update(StockVO stockVO) {
        return stockDAO.update(stockVO);
    }

    @Override
    public int delete(int stockno) {
        return stockDAO.delete(stockno);
    }
}

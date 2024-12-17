package dev.mvc.stockdata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("dev.mvc.stockdata.StockdataProc")
public class StockdataProc implements StockdataProcInter {

    @Autowired
    private StockdataDAOInter stockdataDAO;

    @Override
    public int create(StockdataVO stockdataVO) {
        return stockdataDAO.create(stockdataVO);
    }

    @Override
    public StockdataVO read(int sdatano) {
        return stockdataDAO.read(sdatano);
    }

    @Override
    public List<StockdataVO> listByStockno(int stockno) {
        return stockdataDAO.listByStockno(stockno);
    }

    @Override
    public int update(StockdataVO stockdataVO) {
        return stockdataDAO.update(stockdataVO);
    }

    @Override
    public int delete(int sdatano) {
        return stockdataDAO.delete(sdatano);
    }
}

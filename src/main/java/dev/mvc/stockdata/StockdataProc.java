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
        return stockdataDAO.create(stockdataVO); // 데이터를 추가
    }

    @Override
    public StockdataVO read(int sdatano) {
        return stockdataDAO.read(sdatano); // 특정 데이터 읽기
    }

    @Override
    public int update(StockdataVO stockdataVO) {
        return stockdataDAO.update(stockdataVO); // 데이터 수정
    }

    @Override
    public int delete(int sdatano) {
        return stockdataDAO.delete(sdatano); // 데이터 삭제
    }

    @Override
    public List<StockdataVO> list() {
        return stockdataDAO.list(); // stockno에 해당하는 데이터 목록
    }
}

package com.atyeti.tradewebapp.service;

import com.atyeti.tradewebapp.model.Stock;
import com.atyeti.tradewebapp.repo.StockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepo stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public void saveStock(Stock newStock) {
        stockRepository.save(newStock);
    }

    public Stock findStockById(Long stockId) {
        return stockRepository.findById(stockId).orElseThrow(null);
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}

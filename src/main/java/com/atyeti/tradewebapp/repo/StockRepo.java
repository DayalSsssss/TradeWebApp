package com.atyeti.tradewebapp.repo;

import com.atyeti.tradewebapp.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepo extends JpaRepository<Stock, Long> {


    Stock findBySymbol(String stockSymbol);
}

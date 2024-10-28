package com.atyeti.tradewebapp.repo;

import com.atyeti.tradewebapp.model.Stock;
import com.atyeti.tradewebapp.model.User;
import com.atyeti.tradewebapp.model.UserStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStockRepo extends JpaRepository<UserStock, Long> {
    List<UserStock> findByUser(User user);
    UserStock findByUserAndStock(User user, Stock stock);
}

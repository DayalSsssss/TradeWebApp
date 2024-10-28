package com.atyeti.tradewebapp.repo;

import com.atyeti.tradewebapp.model.Transaction;
import com.atyeti.tradewebapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}

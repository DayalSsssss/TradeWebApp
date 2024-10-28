package com.atyeti.tradewebapp.service;


import com.atyeti.tradewebapp.model.Stock;
import com.atyeti.tradewebapp.model.Transaction;
import com.atyeti.tradewebapp.model.User;
import com.atyeti.tradewebapp.model.UserStock;
import com.atyeti.tradewebapp.repo.StockRepo;
import com.atyeti.tradewebapp.repo.TransactionRepo;
import com.atyeti.tradewebapp.repo.UserRepo;
import com.atyeti.tradewebapp.repo.UserStockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private StockRepo stockRepository;

    @Autowired
    private UserStockRepo userStockRepository;

    @Autowired
    private TransactionRepo transactionRepository;

    public double getUserBalance(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBalance();
    }

    public List<Stock> getAvailableStocks() {
        return stockRepository.findAll();
    }

    public List<UserStock> getOwnedStocks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userStockRepository.findByUser(user);
    }

    @Transactional
    public void buyStock(String username, String stockSymbol, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Stock stock = stockRepository.findBySymbol(stockSymbol);

        double totalCost = stock.getPrice() * quantity;

        // Check if the user has enough balance
        if (user.getBalance() >= totalCost) {
            user.setBalance(user.getBalance() - totalCost);
            userRepository.save(user);
//
//            // Update user's stock ownership
//            UserStock userStock = userStockRepository.findByUserAndStock(user, stock);
//            if (userStock == null) {
//                userStock = new UserStock();
//                userStock.setUser(user);
//                userStock.setStock(stock);
//                userStock.setQuantity(quantity);
//            } else {
//                userStock.setQuantity(userStock.getQuantity() + quantity);
//            }
//            userStockRepository.save(userStock);

            // Record the transaction
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setStock(stock);
            transaction.setQuantity(quantity);
            transaction.setType("BUY");
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setStatus("PENDING");
            transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Insufficient balance to buy stocks.");
        }
    }

    @Transactional
    public void sellStock(String username, String stockSymbol, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Stock stock = stockRepository.findBySymbol(stockSymbol);

        // Check if the user owns enough quantity of the stock
        UserStock userStock = userStockRepository.findByUserAndStock(user, stock);
        if (userStock != null && userStock.getQuantity() >= quantity) {
//            double totalRevenue = stock.getPrice() * quantity;
//            user.setBalance(user.getBalance() + totalRevenue);
//            userRepository.save(user);
//
//            // Update user's stock ownership
//            userStock.setQuantity(userStock.getQuantity() - quantity);
//            if (userStock.getQuantity() == 0) {
//                userStockRepository.delete(userStock);
//            } else {
//                userStockRepository.save(userStock);
//            }

            // Record the transaction
            Transaction transaction = new Transaction();
            transaction.setUser(user);
            transaction.setStock(stock);
            transaction.setQuantity(quantity);
            transaction.setType("SELL");
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setStatus("PENDING");
            transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Insufficient stock quantity to sell.");
        }
    }

    public List<Transaction> getTransactionHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUser(user);
    }



}


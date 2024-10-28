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

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserStockRepo userStockRepo;

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findByRole("ROLE_USER");
    }

    // Get all stocks
    public List<Stock> getAllStocks() {
        return stockRepo.findAll();
    }

    // Get all transactions (buy/sell orders)
    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }

    // Update user details (can be used to disable or update user account)
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    // Delete a user
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    @Transactional
    public void approveTransaction(Long transactionId) {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("PENDING".equals(transaction.getStatus())) {
            User user = transaction.getUser();
            Stock stock = transaction.getStock();
            double totalCost = stock.getPrice() * transaction.getQuantity();

            if ("BUY".equals(transaction.getType())) {
                // Check if the user has enough balance before approval
                    // Deduct the user's balance

                    // Update user's stock ownership
                    UserStock userStock = userStockRepo.findByUserAndStock(user, stock);
                    if (userStock == null) {
                        userStock = new UserStock();
                        userStock.setUser(user);
                        userStock.setStock(stock);
                        userStock.setQuantity(transaction.getQuantity());
                    } else {
                        userStock.setQuantity(userStock.getQuantity() + transaction.getQuantity());
                    }
                    userStockRepo.save(userStock);

                    // Mark the transaction as successful
                    transaction.setStatus("SUCCESS");
                    transactionRepo.save(transaction);
            } else if ("SELL".equals(transaction.getType())) {
                // Add stock quantity to user's stock ownership
                UserStock userStock = userStockRepo.findByUserAndStock(user, stock);
                if (userStock != null && userStock.getQuantity() >= transaction.getQuantity()) {
                    double totalRevenue = stock.getPrice() * transaction.getQuantity();
                    user.setBalance(user.getBalance() + totalRevenue);
                    userRepo.save(user);

                    // Update user's stock ownership
                    userStock.setQuantity(userStock.getQuantity() - transaction.getQuantity());
                    if (userStock.getQuantity() == 0) {
                        userStockRepo.delete(userStock);
                    } else {
                        userStockRepo.save(userStock);
                    }

                    // Mark the transaction as successful
                    transaction.setStatus("SUCCESS");
                    transactionRepo.save(transaction);
                } else {
                    throw new RuntimeException("Insufficient stock quantity to sell.");
                }
            }
        } else {
            throw new RuntimeException("Transaction is not pending.");
        }
    }

    @Transactional
    public void rejectTransaction(Long transactionId) {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("PENDING".equals(transaction.getStatus())) {
//            transaction.setStatus("REJECTED");
//            transactionRepo.save(transaction);
            User user = transaction.getUser();
            Stock stock = transaction.getStock();
            double totalCost = stock.getPrice() * transaction.getQuantity();
            if ("BUY".equals(transaction.getType())) {
                    user.setBalance(user.getBalance() + totalCost);
                    userRepo.save(user);
            }
            transaction.setStatus("REJECTED");
            transactionRepo.save(transaction);
        } else {
            throw new RuntimeException("Transaction is not pending.");
        }
    }

    public List<Transaction> getTransactionHistory(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepo.findByUser(user);
    }
}


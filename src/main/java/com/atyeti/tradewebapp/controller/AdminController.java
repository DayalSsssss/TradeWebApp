package com.atyeti.tradewebapp.controller;

import com.atyeti.tradewebapp.model.Stock;
import com.atyeti.tradewebapp.model.Transaction;
import com.atyeti.tradewebapp.model.User;
import com.atyeti.tradewebapp.repo.StockRepo;
import com.atyeti.tradewebapp.repo.UserRepo;
import com.atyeti.tradewebapp.service.AdminService;
import com.atyeti.tradewebapp.service.StockService;
import com.atyeti.tradewebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StockService stockService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StockRepo stockRepo;

    // Manage users
    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "manage-users";
    }

    // Manage stocks
    @GetMapping("/manage-stocks")
    public String manageStocks(Model model) {
        List<Stock> stocks = adminService.getAllStocks();
        model.addAttribute("stocks", stocks);
        return "manage-stocks";
    }

    // Manage trades
    @GetMapping("/manage-trades")
    public String manageTrades(Model model) {
        List<Transaction> trades = adminService.getAllTransactions();
        model.addAttribute("trades", trades);
        return "manage-trades";
    }

    // Approve trade
    @PostMapping("/manage-trades/approve/{id}")
    public String approveTrade(@PathVariable Long id) {
        adminService.approveTransaction(id);
        return "redirect:/admin/manage-trades";
    }

    // Cancel trade
    @PostMapping("/manage-trades/cancel/{id}")
    public String cancelTrade(@PathVariable Long id) {
        adminService.rejectTransaction(id);
        return "redirect:/admin/manage-trades";
    }

    @GetMapping("/manage-users/update/{id}")
    public String showUserUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userRepo.findById(id).orElse(null);
        model.addAttribute("user", user);
        return "manage-users-update";
    }

    @PutMapping("/manage-users/update/{id}")
    public String updateUser(@PathVariable("id") Long userId, @RequestParam("email") String email,
                             @RequestParam("balance") double balance) {
        // Fetch the existing user from the database
        User existingUser = userRepo.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Invalid user ID: " + userId));

        // Update fields
        existingUser.setEmail(email);

        BigDecimal roundedBalance = new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP);

        existingUser.setBalance(balance);

        // Save updated user
        userRepo.save(existingUser);

        return "redirect:/admin/manage-users";
    }


    // Delete user
    @DeleteMapping("/manage-users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin/manage-users";
    }

    @PostMapping("/manage-stocks/add")
    public String addStock(@RequestParam String symbol,
                           @RequestParam String name,
                           @RequestParam double price) {
        Stock newStock = new Stock();
        newStock.setSymbol(symbol);
        newStock.setName(name);
        newStock.setPrice(price);
        stockService.saveStock(newStock);

        return "redirect:/admin/manage-stocks";  // Redirect back to manage stocks page
    }

    @GetMapping("/manage-stocks/update/{id}")
    public String showStockUpdateForm(@PathVariable("id") Long id, Model model) {
        Stock stock = stockRepo.findById(id).orElse(null);
        model.addAttribute("stock", stock);
        return "manage-stocks-update";
    }

    // Update stock
    @PutMapping("/manage-stocks/update/{id}")
    public String updateStock(@RequestParam Long stockId,
                              @RequestParam String name,
                              @RequestParam double price) {
        Stock existingStock = stockService.findStockById(stockId);

        if (existingStock != null) {
            existingStock.setName(name);
            existingStock.setPrice(price);
            stockService.saveStock(existingStock);  // Save updated stock
        }

        return "redirect:/admin/manage-stocks";  // Redirect back to manage stocks page
    }

    // Delete stock
    @DeleteMapping("/manage-stocks/delete/{id}")
    public String deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return "redirect:/admin/manage-stocks";
    }


}

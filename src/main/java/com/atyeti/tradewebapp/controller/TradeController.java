package com.atyeti.tradewebapp.controller;

import com.atyeti.tradewebapp.model.Stock;
import com.atyeti.tradewebapp.model.Transaction;
import com.atyeti.tradewebapp.model.UserStock;
import com.atyeti.tradewebapp.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @GetMapping("/user/trade")
    public String showTradePage(Model model, @AuthenticationPrincipal UserDetails principal) {
        String username = principal.getUsername();
        double balance = tradeService.getUserBalance(username);
        List<Stock> stocks = tradeService.getAvailableStocks();
        List<UserStock> ownedStocks = tradeService.getOwnedStocks(username);
        List<Transaction> transactions = tradeService.getTransactionHistory(username);

        model.addAttribute("username", username);
        model.addAttribute("balance", String.format("%.2f",balance));
        model.addAttribute("stocks", stocks);
        model.addAttribute("ownedStocks", ownedStocks);
        model.addAttribute("transactions", transactions);

        return "trade";
    }

    @PostMapping("/buy")
    public String buyStock(String stockSymbol, int quantity, @AuthenticationPrincipal UserDetails principal, RedirectAttributes redirectAttributes) {
        String username = principal.getUsername();
        try {
            tradeService.buyStock(username, stockSymbol, quantity);
            redirectAttributes.addFlashAttribute("message", "Order placed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/user/trade";
    }

    @PostMapping("/sell")
    public String sellStock(String stockSymbol, int quantity, @AuthenticationPrincipal UserDetails principal, RedirectAttributes redirectAttributes) {
        String username = principal.getUsername();
        try {
            tradeService.sellStock(username, stockSymbol, quantity);
            redirectAttributes.addFlashAttribute("message", "Order placed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/user/trade";
    }


    @GetMapping("/user/order-history")
    public String showOrderHistory(Model model, @AuthenticationPrincipal UserDetails principal) {
        String username = principal.getUsername();
        double balance = tradeService.getUserBalance(username);
        List<Transaction> transactions = tradeService.getTransactionHistory(username);

        model.addAttribute("username", username);
        model.addAttribute("transactions", transactions);
        model.addAttribute("balance", String.format("%.2f",balance));

        return "order-history";
    }

    @GetMapping("/user/portfolio")
    public String showPortfolioPage(Model model, @AuthenticationPrincipal UserDetails principal) {
        String username = principal.getUsername();
        double balance = tradeService.getUserBalance(username);
        List<UserStock> ownedStocks = tradeService.getOwnedStocks(username);

//        // Calculate PNL
//        double pnl = 0.0;
//        for (UserStock userStock : ownedStocks) {
//            Stock stock = userStock.getStock();
//            double purchasePrice = stock.getPrice(); // Assuming price when bought is same as current price
//            double currentValue = stock.getPrice() * userStock.getQuantity();
//            double purchaseValue = purchasePrice * userStock.getQuantity();
//            pnl += (currentValue - purchaseValue);
//        }

        model.addAttribute("username", username);
        model.addAttribute("balance", String.format("%.2f", balance));
        model.addAttribute("ownedStocks", ownedStocks);

        return "portfolio";
    }


}

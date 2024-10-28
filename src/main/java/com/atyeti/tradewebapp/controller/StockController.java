package com.atyeti.tradewebapp.controller;

import com.atyeti.tradewebapp.model.Stock;
import com.atyeti.tradewebapp.service.StockService;
import com.atyeti.tradewebapp.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private TradeService tradeService;

    @GetMapping("/user/view-stocks")
    public String viewStocks(Model model, @AuthenticationPrincipal UserDetails principal) {
        List<Stock> stocks = stockService.getAllStocks();
        String username = principal.getUsername();
        double balance = tradeService.getUserBalance(username);
        model.addAttribute("stocks", stocks);
        model.addAttribute("balance", String.format("%.2f",balance));

        return "view-stocks";
    }


}
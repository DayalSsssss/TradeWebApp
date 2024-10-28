package com.atyeti.tradewebapp.controller;

import com.atyeti.tradewebapp.model.User;
import com.atyeti.tradewebapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        return "admin-home";
    }

    @GetMapping("/user/home")
    public String userHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User user = userService.findByUsername(userDetails.getUsername());

        model.addAttribute("username", user.getUsername());
        model.addAttribute("balance", String.format("%.2f", user.getBalance()));


        return "user-home";
    }

}

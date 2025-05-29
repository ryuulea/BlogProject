package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogPageController {

    // トップページの表示（ログイン後）
    // ログイン後に表示されるWelcomeページ。
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginAccountInfo", loginAccount);
        return "welcome";
    }

    // 自己紹介ページの表示
    @GetMapping("/profile")
    public String showProfilePage(HttpSession session, Model model) {
        Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }
        model.addAttribute("loginAccountInfo", loginAccount);
        return "profile";
    }
}
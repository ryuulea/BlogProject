package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import com.myblog1.models.entity.Blog;
import com.myblog1.services.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BlogListController {

    @Autowired
    private BlogService blogService;

    // ブログ一覧画面の表示
    // DBからすべてのブログ記事を取得し、一覧ページに表示する。
    @GetMapping("/blog/list")
    public String showBlogList(Model model, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }
        List<Blog> blogList = blogService.findAll();
        model.addAttribute("blogs", blogList);
        model.addAttribute("loginAccountInfo", loginAccount);
        return "blog-list";
    }

    // ブログ検索機能（タイトル・記事の部分一致）
    // タイトルまたは記事内容にキーワードを含むブログを検索し、結果を表示する。
    @GetMapping("/blog/search")
    public String searchBlogs(@RequestParam("keyword") String keyword, Model model) {
        List<Blog> results = blogService.searchByKeyword(keyword);
        model.addAttribute("blogs", results);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("isEmpty", results.isEmpty()); 
        return "blog-list";
    }
}
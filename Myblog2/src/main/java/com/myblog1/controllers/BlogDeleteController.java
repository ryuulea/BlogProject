package com.myblog1.controllers;

import com.myblog1.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BlogDeleteController {

    @Autowired
    private BlogService blogService;

    // ブログ削除処理
    // 指定されたIDのブログを削除する。
    @PostMapping("/blog/delete")
    public String deleteBlog(@RequestParam("id") Long blogId) {
        blogService.deleteById(blogId);
        return "redirect:/blog/list";
    }
}
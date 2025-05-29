package com.myblog1.controllers;

import com.myblog1.models.entity.Blog;
import com.myblog1.services.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class BlogRegisterController {

    @Autowired
    private BlogService blogService;

    // ブログ投稿画面の表示
    @GetMapping("/blog/register")
    public String showRegisterPage() {
        return "blog-register";
    }

    // ブログ投稿処理
    // 入力されたタイトル、カテゴリ、記事、画像などを受け取り、DBに保存する。
    @PostMapping("/blog/register")
    public String registerBlog(@RequestParam("title") String title,
                                @RequestParam("category") String category,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam("article") String article,
                                HttpSession session) throws IOException {
        Blog blog = new Blog();
        blog.setBlogTitle(title);
        blog.setCategoryName(category);
        blog.setArticle(article);

        // 画像が選択されている場合、保存処理
        if (!image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            String uploadPath = new File("src/main/resources/static/images").getAbsolutePath();
            File saveFile = new File(uploadPath, imageName);
            image.transferTo(saveFile);
            blog.setBlogImage(imageName);
        }

        // ログイン中のユーザーIDを取得
        Long accountId = (Long) session.getAttribute("accountId");
        if (accountId == null) {
            return "redirect:/login";
        }
        blog.setAccountId(accountId);
        blogService.saveBlog(blog);

        return "redirect:/blog/list";
    }
}
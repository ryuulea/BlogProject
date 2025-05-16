package com.myblog1.controllers;

import com.myblog1.models.dao.BlogDao;
import com.myblog1.models.entity.Blog;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogDao blogDao;

    // 投稿画面
    @GetMapping("/blog/register")
    public String showRegisterPage() {
        return "blog-register";
    }

    // 投稿処理
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

        if (!image.isEmpty()) {
            String imageName = image.getOriginalFilename();

            // 保存文件到 static/images 文件夹
            String uploadPath = new File("src/main/resources/static/images").getAbsolutePath();
            File saveFile = new File(uploadPath, imageName);
            image.transferTo(saveFile);

            blog.setBlogImage(imageName);  // 保存文件名到数据库
        }

        Long accountId = (Long) session.getAttribute("accountId");
        if (accountId == null) {
            return "redirect:/login";
        }

        blog.setAccountId(accountId);
        blogDao.save(blog);

        return "redirect:/blog/list";
    }

    @GetMapping("/blog/list")
    public String showBlogList(Model model) {
        List<Blog> blogList = blogDao.findAll(); // 全件取得
        model.addAttribute("blogs", blogList);   // HTML に渡す
        return "blog-list"; // templates/blog-list.html を表示
    }

}

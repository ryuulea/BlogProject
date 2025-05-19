package com.myblog1.controllers;

import com.myblog1.models.dao.BlogDao;
import com.myblog1.models.entity.Account;
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

            // 保存 static/images
            String uploadPath = new File("src/main/resources/static/images").getAbsolutePath();
            File saveFile = new File(uploadPath, imageName);
            image.transferTo(saveFile);

            blog.setBlogImage(imageName);  
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
    public String showBlogList(Model model, HttpSession session) {
        Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }

        // ブログ一覧取得
        List<Blog> blogList = blogDao.findAll(); 
        model.addAttribute("blogs", blogList);  
        model.addAttribute("loginAccountInfo", loginAccount); 

        return "blog-list"; 
    }

 // 編集画面の表示処理
    @GetMapping("/blog/edit")
    public String showEditForm(@RequestParam("id") Long blogId, Model model) {
        Blog blog = blogDao.findById(blogId).orElse(null);

        // もしID存在しない場合、ブログリストに戻り
        if (blog == null) {
            return "redirect:/blog/list";
        }

        model.addAttribute("blog", blog);
        return "blog-edit";
    }


    // 編集処理（更新処理）
    @PostMapping("/blog/edit")
    public String updateBlog(@RequestParam("blogId") Long id,
                             @RequestParam("title") String title,
                             @RequestParam("category") String category,
                             @RequestParam("article") String article,
                             @RequestParam("image") MultipartFile image) throws IOException {

        Blog blog = blogDao.findById(id).orElse(null);
        if (blog != null) {
            blog.setBlogTitle(title);
            blog.setCategoryName(category);
            blog.setArticle(article);

            if (!image.isEmpty()) {
                String imageName = image.getOriginalFilename();

                // 保存新しい画像
                String uploadPath = new File("src/main/resources/static/images").getAbsolutePath();
                File saveFile = new File(uploadPath, imageName);
                image.transferTo(saveFile);

             // 画像ファイル名更新
                blog.setBlogImage(imageName);  
            }

            // DB更新
            blogDao.save(blog); 
        }

     // 一覧へリダイレクト
        return "redirect:/blog/list";  
    }
    
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
        if (loginAccount == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginAccountInfo", loginAccount); 
        return "welcome"; 
    }
 // 削除処理
    @PostMapping("/blog/delete")
    public String deleteBlog(@RequestParam("id") Long blogId) {
        blogDao.deleteById(blogId);
        return "redirect:/blog/list";
    }

    
}

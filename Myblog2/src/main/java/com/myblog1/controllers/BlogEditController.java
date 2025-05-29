package com.myblog1.controllers;

import com.myblog1.models.entity.Blog;
import com.myblog1.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.File;
import java.io.IOException;

@Controller
public class BlogEditController {

    @Autowired
    private BlogService blogService;

    // ブログ編集画面の表示
    @GetMapping("/blog/edit")
    public String showEditForm(@RequestParam("id") Long blogId, Model model) {
        Blog blog = blogService.findById(blogId).orElse(null);
        if (blog == null) {
            return "redirect:/blog/list";
        }
        model.addAttribute("blog", blog);
        return "blog-edit";
    }

    // ブログ編集の更新処理
    // 編集フォームから送られた内容で、既存ブログを更新する
    @PostMapping("/blog/edit")
    public String updateBlog(@RequestParam("blogId") Long id,
                              @RequestParam("title") String title,
                              @RequestParam("category") String category,
                              @RequestParam("article") String article,
                              @RequestParam("image") MultipartFile image) throws IOException {
        Blog blog = blogService.findById(id).orElse(null);
        if (blog != null) {
            blog.setBlogTitle(title);
            blog.setCategoryName(category);
            blog.setArticle(article);

            // 画像が新しくアップロードされた場合のみ更新
            if (!image.isEmpty()) {
                String imageName = image.getOriginalFilename();
                String uploadPath = new File("src/main/resources/static/images").getAbsolutePath();
                File saveFile = new File(uploadPath, imageName);
                image.transferTo(saveFile);
                blog.setBlogImage(imageName);
            }
            blogService.saveBlog(blog);
        }
        return "redirect:/blog/list";
    }
}
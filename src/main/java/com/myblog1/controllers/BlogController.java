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

	// ブログ投稿画面の表示
	@GetMapping("/blog/register")
	public String showRegisterPage() {
		return "blog-register";
	}

	// ブログ投稿処理
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

		// DBに保存
		blogDao.save(blog);

		return "redirect:/blog/list";
	}

	// ブログ一覧画面の表示
	@GetMapping("/blog/list")
	public String showBlogList(Model model, HttpSession session) {
		Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
		if (loginAccount == null) {
			return "redirect:/login";
		}

		List<Blog> blogList = blogDao.findAll();
		model.addAttribute("blogs", blogList);
		model.addAttribute("loginAccountInfo", loginAccount);

		return "blog-list";
	}

	// ブログ編集画面の表示
	@GetMapping("/blog/edit")
	public String showEditForm(@RequestParam("id") Long blogId, Model model) {
		Blog blog = blogDao.findById(blogId).orElse(null);

		// 該当ブログが存在しない場合は一覧へリダイレクト
		if (blog == null) {
			return "redirect:/blog/list";
		}

		model.addAttribute("blog", blog);
		return "blog-edit";
	}

	// ブログ編集の更新処理
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

			// 画像が新しくアップロードされた場合のみ更新
			if (!image.isEmpty()) {
				String imageName = image.getOriginalFilename();
				String uploadPath = new File("src/main/resources/static/images").getAbsolutePath();
				File saveFile = new File(uploadPath, imageName);
				image.transferTo(saveFile);
				blog.setBlogImage(imageName);
			}

			blogDao.save(blog);
		}

		return "redirect:/blog/list";
	}

	// トップページの表示（ログイン後）
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		Account loginAccount = (Account) session.getAttribute("loginAccountInfo");
		if (loginAccount == null) {
			return "redirect:/login";
		}

		model.addAttribute("loginAccountInfo", loginAccount);
		return "welcome";
	}

	// ブログ削除処理
	@PostMapping("/blog/delete")
	public String deleteBlog(@RequestParam("id") Long blogId) {
		blogDao.deleteById(blogId);
		return "redirect:/blog/list";
	}

	// 自己紹介ページの表示
	@GetMapping("/profile")
	public String showProfilePage() {
		return "profile";
	}

	// ブログ検索機能（タイトル・記事の部分一致）
	@GetMapping("/blog/search")
	public String searchBlogs(@RequestParam("keyword") String keyword, Model model) {
	    List<Blog> results = blogDao.findByBlogTitleContainingOrArticleContaining(keyword, keyword);
	    model.addAttribute("blogs", results);
	    model.addAttribute("searchKeyword", keyword);
	    return "blog-list";
	}
}


package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import com.myblog1.services.AccountService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountLoginController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private HttpSession session;

	@GetMapping("/login")

	public String getLoginPage() {
		return "login";
	}

	public String getLoginPage(@RequestParam(name = "error", required = false) String error, Model model) {
	    if (error != null) {
	        model.addAttribute("error", "メールアドレスまたはパスワードが間違っています。");
	    }
	    return "login";

	}

	// ログイン処理
	@PostMapping("/login/process")
	public String loginProcess(@RequestParam String email, @RequestParam String password, Model model) {
		// loginCheckメソッドを呼び出してその結果をaccountという変数に格納
		Account account = accountService.loginCheck(email, password);
		// もし、account==nullログイン画面にとどまります。
		if (account == null) {
			// ログイン失敗→画面にとどまる
			model.addAttribute("error", "メールアドレスまたはパスワードが間違っています。");
			return "login";
		} else {
			session.setAttribute("loginAccountInfo", account);
			session.setAttribute("accountId", account.getAccountId()); 
			// ログイン成功→リダイレクト
			return "redirect:/welcome";
		}
	}

	@GetMapping("/welcome")
	public String getWelcomePage(Model model) {
		Account account = (Account) session.getAttribute("loginAccountInfo");
		model.addAttribute("loginAccountInfo", account);
		return "welcome";
	}

}

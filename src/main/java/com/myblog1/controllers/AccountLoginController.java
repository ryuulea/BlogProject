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

	// ログイン画面の表示
	@GetMapping("/login")
	public String getLoginPage() {
		return "login"; // login.html を表示
	}

	// ログイン処理
	@PostMapping("/login/process")
	public String loginProcess(@RequestParam String email,
	                           @RequestParam String password,
	                           Model model) {

		// 入力されたメールアドレスとパスワードでログインチェック
		Account account = accountService.loginCheck(email, password);

		if (account == null) {
			// ログイン失敗 → 再びログイン画面へ
			return "login";
		} else {
			// ログイン成功 → ユーザー情報をセッションに保存
			session.setAttribute("loginAccountInfo", account);
			session.setAttribute("accountId", account.getAccountId());

			// トップページへリダイレクト
			return "redirect:/welcome";
		}
	}

	// ログイン後のウェルカム画面表示
	@GetMapping("/welcome")
	public String getWelcomePage(Model model) {
		Account account = (Account) session.getAttribute("loginAccountInfo");
		model.addAttribute("loginAccountInfo", account);
		return "welcome";
	}
}

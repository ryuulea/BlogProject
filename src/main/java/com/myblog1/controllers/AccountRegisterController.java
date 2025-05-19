package com.myblog1.controllers;

import com.myblog1.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// アカウント登録画面と処理を担当するコントローラー
@Controller
public class AccountRegisterController {

	@Autowired
	private AccountService accountService;

	// 登録画面を表示する
	@GetMapping("/register")
	public String getRegisterPage() {
		return "register";
	}

	// 登録処理を行う
	@PostMapping("/register/process")
	public String registerProcess(@RequestParam("email") String accountEmail,
			@RequestParam("account_name") String accountName, @RequestParam("password") String password) {

		System.out.println("email: " + accountEmail);
		System.out.println("name: " + accountName);
		System.out.println("pass: " + password);

		// サービスを使って登録処理を実行
		if (accountService.createAccount(accountEmail, accountName, password)) {
			// 成功 → ログインページにリダイレクト
			return "redirect:/login";
		} else {
			// 失敗 → 同じ登録画面に戻る
			return "register";
		}

	}
}

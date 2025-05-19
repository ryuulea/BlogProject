package com.myblog1.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountLogoutController {

	private final HttpSession session;

	public AccountLogoutController(HttpSession session) {
		this.session = session;
	}

	@GetMapping("/logout")
	public String logout() {
		// セッションを無効にする（ログイン情報を消す）
		session.invalidate();
		// ログイン画面にリダイレクト
		return "redirect:/login";
	}
}

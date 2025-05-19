package com.myblog1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myblog1.models.dao.AccountDao;
import com.myblog1.models.entity.Account;

@Service
public class AccountService {

	@Autowired
	private AccountDao accountDao;

	// 登録処理
	// すでに同じメールアドレスが登録されていないかチェックし、
	// なければ新しいアカウントを保存して true を返す
	public boolean createAccount(String accountEmail, String accountName, String password) {
		if (accountDao.findByAccountEmail(accountEmail) == null) {
			// 登録されていなければ、新しいアカウントを作成して保存
			accountDao.save(new Account(accountEmail, accountName, password));
			return true;
		} else {
			// メールアドレスがすでに存在している場合は登録失敗
			return false;
		}
	}

	// ログインチェック処理
	// 入力されたメールアドレスとパスワードが一致するアカウントがあるかを確認
	public Account loginCheck(String accountEmail, String password) {
		Account account = accountDao.findByAccountEmailAndPassword(accountEmail, password);
		if (account == null) {
			// 該当するアカウントが存在しない場合（ログイン失敗）
			return null;
		} else {
			// アカウントが見つかった場合（ログイン成功）
			return account;
		}
	}
}

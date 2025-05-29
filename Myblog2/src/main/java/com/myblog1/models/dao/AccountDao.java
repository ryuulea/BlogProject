package com.myblog1.models.dao;

import com.myblog1.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

    // 登録用
    Account save(Account account);

    // 重複チェック
    Account findByAccountEmail(String accountEmail);

    // ログイン用
    Account findByAccountEmailAndPassword(String accountEmail, String password);
}

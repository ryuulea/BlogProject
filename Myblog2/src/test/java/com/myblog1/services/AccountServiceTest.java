package com.myblog1.services;

import com.myblog1.models.dao.AccountDao;
import com.myblog1.models.entity.Account;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountDao accountDao;

    @InjectMocks
    private AccountService accountService;

    public AccountServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginCheck_validCredentials_shouldReturnAccount() {
        String email = "ryuu1@test.com";
        String password = "1234";

        Account mockAccount = new Account();
        mockAccount.setAccountEmail(email);
        mockAccount.setPassword(password);

        when(accountDao.findByAccountEmailAndPassword(email, password)).thenReturn(mockAccount);

        Account result = accountService.loginCheck(email, password);

        assertNotNull(result);
        assertEquals(email, result.getAccountEmail());
    }

    @Test
    public void testLoginCheck_invalidCredentials_shouldReturnNull() {
        String email = "xxx@test.com";
        String password = "wrongpass";

        when(accountDao.findByAccountEmailAndPassword(email, password)).thenReturn(null);

        Account result = accountService.loginCheck(email, password);

        assertNull(result);
    }
}

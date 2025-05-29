package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import com.myblog1.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountLoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    // 测试登录页面的显示（无错误参数）
    @Test
    public void testShowLoginForm_withoutError_shouldDisplayLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // 测试登录页面的显示（带错误参数）
    @Test
    public void testShowLoginForm_withError_shouldDisplayLoginPageWithErrorMessage() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    // 测试登录处理：有效的邮箱和密码
    @Test
    public void testLoginProcess_validCredentials_shouldRedirectToWelcome() throws Exception {
        String email = "test@test.com";
        String password = "pass123";
        Account dummyAccount = new Account();
        dummyAccount.setAccountEmail(email);
        dummyAccount.setPassword(password);

        // 当调用 loginCheck 时返回一个非空的账户对象
        when(accountService.loginCheck(email, password)).thenReturn(dummyAccount);

        mockMvc.perform(post("/login/process")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/welcome"));
    }

    // 测试登录处理：无效的邮箱或密码时返回登录页面并显示错误信息
    @Test
    public void testLoginProcess_invalidCredentials_shouldReturnLoginWithError() throws Exception {
       
    	String email = "xxx@test.com";
    	String password = "wrongpass";

    	when(accountService.loginCheck(email, password)).thenReturn(null);

        mockMvc.perform(post("/login/process")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }
    @Test
    public void testWelcomePage_shouldDisplayAccountFromSession() throws Exception {
        Account mockAccount = new Account();
        mockAccount.setAccountName("ryuu1");

        mockMvc.perform(get("/welcome")
                .sessionAttr("loginAccountInfo", mockAccount))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attributeExists("loginAccountInfo"));
    }
}

package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // --- /profile（ログインあり）---
    @Test
    public void testProfilePage_withSession() throws Exception {
        Account account = new Account();
        account.setAccountId(1L);
        account.setAccountName("Ryuu");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginAccountInfo", account);

        mockMvc.perform(MockMvcRequestBuilders.get("/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("loginAccountInfo"));
    }

    // --- /profile（ログインなし）---
    @Test
    public void testProfilePage_withoutSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // --- /home（ログインあり）---
    @Test
    public void testHomePage_withSession() throws Exception {
        Account account = new Account();
        account.setAccountId(2L);
        account.setAccountName("Lea");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginAccountInfo", account);

        mockMvc.perform(MockMvcRequestBuilders.get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("welcome"))
                .andExpect(model().attributeExists("loginAccountInfo"));
    }

    // --- /home（ログインなし）---
    @Test
    public void testHomePage_withoutSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}

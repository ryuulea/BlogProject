package com.myblog1.controllers;

import com.myblog1.services.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testShowRegisterPage_shouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void testRegister_validInput_shouldRedirectToLogin() throws Exception {
        when(accountService.createAccount("ryuu1@test.com", "ryuu1", "1234")).thenReturn(true);

        mockMvc.perform(post("/register/process")
                .param("email", "ryuu1@test.com")
                .param("account_name", "ryuu1")
                .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(accountService, times(1)).createAccount("ryuu1@test.com", "ryuu1", "1234");
    }

    @Test
    public void testRegister_duplicateEmail_shouldReturnRegisterView() throws Exception {
        when(accountService.createAccount("ryuu1@test.com", "ryuu1", "1234")).thenReturn(false);

        mockMvc.perform(post("/register/process")
                .param("email", "ryuu1@test.com")
                .param("account_name", "ryuu1")
                .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        verify(accountService, times(1)).createAccount("ryuu1@test.com", "ryuu1", "1234");
    }
}

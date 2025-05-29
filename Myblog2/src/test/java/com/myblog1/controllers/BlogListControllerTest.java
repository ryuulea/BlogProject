package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import com.myblog1.models.entity.Blog;
import com.myblog1.services.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    // ログイン状態で一覧表示ページを取得
    @Test
    public void testShowBlogListWithSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Account dummyAccount = new Account();
        dummyAccount.setAccountId(1L);
        dummyAccount.setAccountName("DummyUser");
        session.setAttribute("loginAccountInfo", dummyAccount);

        // モックでブログリストを返す
        Blog dummyBlog = new Blog();
        dummyBlog.setBlogTitle("Test Blog");
        dummyBlog.setCategoryName("test");
        dummyBlog.setArticle("Dummy article");
        when(blogService.findAll()).thenReturn(Arrays.asList(dummyBlog));

        mockMvc.perform(MockMvcRequestBuilders.get("/blog/list").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-list"))
                .andExpect(model().attributeExists("blogs"))
                .andExpect(model().attributeExists("loginAccountInfo"));
    }

    // セッションなし（未ログイン）の場合、ログインページへリダイレクトされる
    @Test
    public void testShowBlogListWithoutSession() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/blog/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
package com.myblog1.controllers;

import com.myblog1.models.entity.Account;
import com.myblog1.models.entity.Blog;
import com.myblog1.services.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogEditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @Test
    public void testShowEditFormWithValidId() throws Exception {
        Blog blog = new Blog();
        blog.setBlogId(1L);
        blog.setBlogTitle("テスト編集");

        when(blogService.findById(1L)).thenReturn(Optional.of(blog));

        mockMvc.perform(MockMvcRequestBuilders.get("/blog/edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-edit"))
                .andExpect(model().attributeExists("blog"));
    }

    @Test
    public void testShowEditFormWithInvalidId() throws Exception {
        when(blogService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/blog/edit")
                        .param("id", "99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"));
    }

    @Test
    public void testUpdateBlog() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/blog/edit")
                        .file(image)
                        .param("blogId", "1")
                        .param("title", "更新後タイトル")
                        .param("category", "更新後カテゴリ")
                        .param("article", "更新後記事"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"));
    }
}
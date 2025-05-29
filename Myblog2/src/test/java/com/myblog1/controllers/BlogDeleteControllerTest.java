package com.myblog1.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogDeleteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 削除処理（正常）
    @Test
    public void testDeleteBlog() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/blog/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"));
    }
}
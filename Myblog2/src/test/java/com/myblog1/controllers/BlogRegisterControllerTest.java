package com.myblog1.controllers;

import com.myblog1.models.entity.Blog;
import com.myblog1.services.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    /**
     * ✅ 正常系：ログイン中のユーザーがブログを投稿する場合
     * - セッションに accountId があるとき、ブログが保存され /blog/list にリダイレクトされる
     */
    @Test
    public void testRegisterBlog_withValidInput_shouldRedirectToBlogList() throws Exception {
        // 💡 MockMultipartFile（空じゃないことを保証）
        MockMultipartFile image = new MockMultipartFile(
                "image",                  // フィールド名
                "test.jpg",               // ファイル名
                "text/plain",             // コンテンツタイプ（テストしやすい）
                "ダミー画像".getBytes("UTF-8") // 内容（非空）
        );

        mockMvc.perform(multipart("/blog/register")
                .file(image)
                .param("title", "テストブログ")
                .param("category", "テストカテゴリ")
                .param("article", "これはテスト記事です")
                .sessionAttr("accountId", 1L)) // ✅ セッションにログイン情報を追加
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"));

        // ✅ 保存処理が一度呼び出されたことを確認
        verify(blogService, times(1)).saveBlog(any(Blog.class));
    }

    /**
     * ✅ 異常系：未ログインの場合、投稿しようとすると /login にリダイレクトされる
     */
    @Test
    public void testRegisterBlog_withoutLogin_shouldRedirectToLogin() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "text/plain",
                "未ログイン".getBytes("UTF-8")
        );

        mockMvc.perform(multipart("/blog/register")
                .file(image)
                .param("title", "未ログイン投稿テスト")
                .param("category", "未ログイン")
                .param("article", "ログインしてないよ！"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // ✅ 保存処理が呼び出されていないことを確認
        verify(blogService, never()).saveBlog(any(Blog.class));
    }

    /**
     * 📝 戻るボタンについては HTML 側の onclick 処理のため、Controller テストは不要
     */
}

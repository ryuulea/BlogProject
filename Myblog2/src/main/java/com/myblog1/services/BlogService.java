package com.myblog1.services;

import com.myblog1.models.dao.BlogDao;
import com.myblog1.models.entity.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogDao blogDao;

    // 新規投稿
    public void saveBlog(Blog blog) {
        blogDao.save(blog);
    }

    // 一覧取得
    public List<Blog> findAll() {
        return blogDao.findAll();
    }

    // IDで検索
    public Optional<Blog> findById(Long id) {
        return blogDao.findById(id);
    }

    // IDで削除
    public void deleteById(Long id) {
        blogDao.deleteById(id);
    }

    // キーワード検索
    public List<Blog> searchByKeyword(String keyword) {
        return blogDao.findByBlogTitleContainingOrArticleContaining(keyword, keyword);
    }
}
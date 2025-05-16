package com.myblog1.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myblog1.models.entity.Blog;

@Repository
public interface BlogDao extends JpaRepository<Blog, Long>{

    // 登録用
    Blog save(Blog blog);

    // 一覧取得
    List<Blog> findAll();

    // 特定ユーザーの投稿を取得（必要に応じて使える）
    List<Blog> findByAccountId(Long accountId);

    // ID指定で1件取得（編集用などに）
    Blog findByBlogId(Long blogId);

}

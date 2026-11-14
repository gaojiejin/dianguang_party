package com.example.spb.service;

import com.example.spb.entity.po.Article;
import com.example.spb.entity.po.ArticleReview;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章审核表 服务类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
public interface ArticleReviewService extends IService<ArticleReview> {

    List<Article> getArticleNeedReview();

    String uploadReview(int articleId,String reviewer, String reviewerId, int reviewState, String advice);

    ArticleReview getReviewResult(int articleId);

    List<ArticleReview> getReviewList(int articleId);
}

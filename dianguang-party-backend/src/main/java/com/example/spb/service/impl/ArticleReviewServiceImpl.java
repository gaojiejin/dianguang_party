package com.example.spb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.entity.po.Article;
import com.example.spb.entity.po.ArticleReview;
import com.example.spb.mapper.ArticleMapper;
import com.example.spb.mapper.ArticleReviewMapper;
import com.example.spb.service.ArticleReviewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 文章审核表 服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Service
public class ArticleReviewServiceImpl extends ServiceImpl<ArticleReviewMapper, ArticleReview> implements ArticleReviewService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleReviewMapper articleReviewMapper;

    @Override
    public List<Article> getArticleNeedReview() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("review_state",0);
        return articleMapper.selectList(queryWrapper);
    }


    @Override

    public String uploadReview(int articleId, String reviewer,String reviewerId, int reviewState, String advice) {


        ArticleReview articleReview = new ArticleReview();
        articleReview.setArticle_id(articleId);
        articleReview.setReviewerId(reviewerId);
        articleReview.setReviewerName(reviewer);
        articleReview.setReviewState(reviewState);
        articleReview.setAdvice(advice);
        articleReview.setReviewTime(LocalDateTime.now());
//        UpdateWrapper<ArticleReview> updateWrapper = new UpdateWrapper<>();
//        articleReviewMapper.update(articleReview,updateWrapper);
        articleReviewMapper.insert(articleReview);

        Article article = new Article();

        if (reviewState == 1){
            UpdateWrapper<Article> articleUpdateWrapper = new UpdateWrapper<>();
            articleUpdateWrapper.eq("article_id",articleId).set("review_state",reviewState).set("reviewer", reviewer).set("reviewer_id", reviewerId);
            articleMapper.update(article,articleUpdateWrapper);
        }
        else if (reviewState == 2){
            UpdateWrapper<Article> articleUpdateWrapper = new UpdateWrapper<>();
            articleUpdateWrapper.eq("article_id",articleId).set("review_state",reviewState).set("reviewer", reviewer).set("reviewer_id", reviewerId);;
            articleMapper.update(article,articleUpdateWrapper);
        }
        return "完成审核！";
    }

    @Override
    public ArticleReview getReviewResult(int articleId) {
        QueryWrapper<ArticleReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        return articleReviewMapper.selectOne(queryWrapper);
    }

    @Override
    public List<ArticleReview> getReviewList(int articleId) {
        QueryWrapper<ArticleReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        return articleReviewMapper.selectList(queryWrapper);
    }


}

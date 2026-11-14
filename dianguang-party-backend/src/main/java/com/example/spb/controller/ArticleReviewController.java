package com.example.spb.controller;


import com.example.spb.entity.po.Article;
import com.example.spb.entity.po.ArticleReview;
import com.example.spb.service.ArticleReviewService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文章审核表 前端控制器
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/articleReview")
public class ArticleReviewController {
    @Autowired
    ArticleReviewService articleReviewService ;

    @GetMapping("getArticleNeedReview")
    @ApiOperation("获取待审核文章")
    public ResponseEntity<List<Article>> getArticleNeedReview(
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(articleReviewService.getArticleNeedReview());
    }


    @PostMapping("uploadReviewState")
    @ApiOperation("提交文章审核,传1通过，2驳回")
    public ResponseEntity<String> uploadReviewState(
            @RequestParam("article_id") int articleId,
            @RequestParam("reviewer")String reviewer,
            @RequestParam("reviewer_id") String reviewerId,
            @RequestParam("review_result") int reviewState,
            @RequestParam(value = "advice", required = false) String advice
    ) throws IOException {
        return ResponseEntity.ok().body(articleReviewService.uploadReview(articleId,reviewer,reviewerId,reviewState, advice));
    }

    @GetMapping("getReviewResult")
    @ApiOperation("查看文章审核结果")
    public ResponseEntity<Object> getReviewResult(
            @RequestParam("article_id") int articleId
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(articleReviewService.getReviewResult(articleId));
    }


    @PostMapping("getReviewList")
    @ApiOperation("获取文章驳回结果")
    public ResponseEntity<List<ArticleReview>> getReviewList(
            @RequestParam("article_id") int articleId
    ) throws IOException {
        return ResponseEntity.ok().body(articleReviewService.getReviewList(articleId));
    }
}


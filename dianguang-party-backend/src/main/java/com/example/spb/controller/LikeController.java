package com.example.spb.controller;


import com.example.spb.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.spb.entity.vo.Result;


@Controller
@Slf4j
@CrossOrigin
@Api("点赞控制器")
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private ArticleService articleService;

    @PutMapping("/{id}")
    @ApiOperation("点赞")
    @ResponseBody
    public Result likeBlog(@PathVariable("id") int article_id, @RequestParam("job_id") String job_id) {

        return articleService.likeArticle(article_id,job_id);
    }

    @GetMapping("/getLikes")
    @ApiOperation("查看点赞数")
    @ResponseBody
    public Long getLikes(@RequestParam("article_id") int article_id) {

        return articleService.getLikes(article_id);
    }
    @GetMapping("/isLiked")
    @ApiOperation("查看是否点赞")
    @ResponseBody
    public Boolean isLiked(int articleId, String jobId){
        return articleService.isLiked(articleId,jobId);
    }
}

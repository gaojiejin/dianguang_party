package com.example.spb.controller;


import com.example.spb.entity.po.Article;
import com.example.spb.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://modao.cc/proto/8wpGiIxsgm18ndDNbiSXr/sharing?view_mode=read_only #未命名原型-分享
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author zff
 * @since 2024-07-24
 */
@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService ;
    @PostMapping("createArticle")
    @ApiOperation("新建文章内容")
    public ResponseEntity<String> createArticle(
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("content") String content,
            @RequestParam("editor") String editor,
            @RequestParam("editorId") String editorId,
            @RequestParam(value = "videoPath", required = false) String videoPath,
            @RequestParam(value = "timestamp", required = false) String time,
            @RequestParam(value = "imagesPath", required = false) List<String> imagesPath) throws IOException {
        // 解析传递过来的字符串为 LocalDateTime
        LocalDateTime dateTime;
        if (time != null){
            dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        }
        else {
            dateTime = LocalDateTime.now();
        }
        String res = articleService.saveArticle(title, type, content, editor, editorId,dateTime,imagesPath,videoPath);

//        Map<String, Object> response = new HashMap<>();
//        response.put("articleId", articleId);
//        response.put("message", "文章上传成功");
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @PostMapping("changeArticleTime")
    @ApiOperation("修改文章时间")
    public ResponseEntity<String> changeArticleTime(
            @RequestParam("timestamp") String time,
            @RequestParam("article_id") int article_id) throws IOException {
        // 解析传递过来的字符串为 LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        String res = articleService.changeArticleTime(dateTime,article_id);

//        Map<String, Object> response = new HashMap<>();
//        response.put("articleId", articleId);
//        response.put("message", "文章上传成功");
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @PostMapping("reSubmitArticle")
    @ApiOperation("重新提交文章内容")
    public ResponseEntity<String> reSubmitArticle(
            @RequestParam("article_id") int articleId,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("content") String content,
            @RequestParam("editor") String editor,
            @RequestParam("editorId") String editorId,
            @RequestParam(value = "imagesPath", required = false) List<String> imagesPath,
            @RequestParam(value = "videoPath", required = false) String videoPath) throws IOException {

        String res = articleService.reSubmitArticle(articleId, title, type, content, editor, editorId, imagesPath, videoPath);
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

//    @PostMapping("createArticleImage")
//    @ApiOperation("新建文章添加图片")
//    public ResponseEntity<String> createArticleImage(
//            @RequestParam("article_id") int article_id,
//            @RequestParam("image") MultipartFile image) throws IOException {
//
//        return ResponseEntity.status(HttpStatus.OK).body(articleService.saveArticleImage(article_id,image));
//    }

    @PostMapping("deleteArticle")
    @ApiOperation("删除文章")
    public ResponseEntity<String> deleteArticle(
            @RequestParam("article_id") int articleId
            ) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(articleService.deleteArticle(articleId));
    }

    @PostMapping("queryAllArticle")
    @ApiOperation("查看全部文章")
    public ResponseEntity<List<Article>> queryAllArticle() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryAllArticle());
    }

    @PostMapping("queryArticleById")
    @ApiOperation("根据支部管理查看个人提交的文章")
    public ResponseEntity<List<Article>> queryArticleById(@RequestParam("editor_id") int editor_id) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryArticleById(editor_id));
    }

    @PostMapping("findArticlesByConditions")
    @ApiOperation("根据自定义条件查看文章")
    public ResponseEntity<?> findArticlesByConditions(
            @RequestParam("type") String type,
            @RequestParam("state") int state,
            @RequestParam("editor_id") String editor_id) throws IOException {
        List<Article> articles = articleService.findArticlesByConditions(type,state,editor_id);
        if (articles == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到符合条件的查询结果");
        }
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    @PostMapping("queryArticlePassed")
    @ApiOperation("查看全部审核通过文章")
    public ResponseEntity<List<Article>> queryArticlePassed() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryArticlePassed());
    }

    @PostMapping("queryArticleRejected")
    @ApiOperation("查看全部审核未通过文章")
    public ResponseEntity<List<Article>> queryArticleRejected() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryArticleRejected());
    }

    @PostMapping("queryArticleNeedReview")
    @ApiOperation("查看全部待审核文章")
    public ResponseEntity<List<Article>> queryArticleNeedReview() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryArticleNeedReview());
    }
//    @PostMapping
//    @ApiOperation("分页查看文章")
//    public ResponseEntity<Article[]> queryArticleByPage() throws IOException {
//
//        return articleService.queryArticleByPage();
//    }

    @PostMapping("queryOneArticle")
    @ApiOperation("查看单一文章")
    public ResponseEntity<Object> queryOneArticle(@RequestParam("article_id") int articleId) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryOneArticle(articleId));
    }

    @PostMapping("queryArticleByType")
    @ApiOperation("根据类型查看文章")
    public ResponseEntity<List<Article>> queryArticleByType(@RequestParam("type") String type) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryArticleByType(type));
    }

    @PostMapping("queryArticleByEditorId")
    @ApiOperation("根据编辑查看文章")
    public ResponseEntity<List<Article>> queryArticleByEditorId(@RequestParam("editor_id") int editorId) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.queryArticleByEditorId(editorId));
    }

}


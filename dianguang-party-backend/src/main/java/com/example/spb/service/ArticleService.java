package com.example.spb.service;

import com.example.spb.entity.po.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import com.example.spb.entity.vo.Result;
/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
public interface ArticleService extends IService<Article> {

    List<Article> findArticlesByConditions(String type, Integer state, String editorId);

    List<Article> queryArticlePassed();

    List<Article> queryArticleRejected();

    List<Article> queryArticleNeedReview();

    List<Article> queryAllArticle();

    List<Article> queryArticleById(int editor_id);

    Article queryOneArticle(int articleId);

    String deleteArticle(int articleId);

    String saveArticle(String title, String type, String content, String editor, String editorId, LocalDateTime time ,List<String> imagesPath,String videoPath) throws IOException;

    String reSubmitArticle(int articleId, String title, String type, String content, String editor, String editorId, List<String> imagesPath,String video_path) throws IOException;

    List<Article> queryArticleByType(String type);

    List<Article> queryArticleByEditorId(int editorId);


    Result likeArticle(int articleId, String jobId);

    Long getLikes(int articleId);

    Boolean isLiked(int articleId, String jobId);

    String changeArticleTime(LocalDateTime dateTime, int articleId);
}

package com.example.spb.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.constants.RedisConstants;
import com.example.spb.entity.po.Article;
import com.example.spb.entity.po.User;
import com.example.spb.entity.vo.Result;
import com.example.spb.entity.po.ArticleReview;
import com.example.spb.mapper.ArticleMapper;
import com.example.spb.mapper.ArticleReviewMapper;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.service.UserService;
import com.example.spb.utils.AliOSSUtils;
import com.example.spb.utils.StringToFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    AliOSSUtils aliOSSUtils;

    @Autowired
    UserService userService;

    @Autowired
    StringToFileUtil stringToFileUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ArticleReviewMapper articleReviewMapper;

    public List<Article> findArticlesByConditions(String type, Integer state, String editorId) {
        // 使用 QueryWrapper 构建查询条件
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        // 根据传入的参数动态添加查询条件
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
        }
        if (state != null) {
            queryWrapper.eq("review_state", state);
        }
        if (editorId != null && !editorId.isEmpty()) {
            queryWrapper.eq("editor_id", editorId);
        }
        queryWrapper.orderByDesc("uploadTime");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        // 执行查询
        return articles;
    }
    @Override
    public List<Article> queryArticlePassed() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("review_state",1);
        queryWrapper.orderByDesc("uploadTime");
        List<Article> passedArticles =  articleMapper.selectList(queryWrapper);
        return passedArticles;
    }

    @Override
    public List<Article> queryArticleRejected() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("review_state",2);
        queryWrapper.orderByDesc("uploadTime");
        List<Article> rejectedArticles =  articleMapper.selectList(queryWrapper);
        return rejectedArticles;
    }

    @Override
    public List<Article> queryArticleNeedReview() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("review_state",0);
        queryWrapper.orderByDesc("uploadTime");
        List<Article> needReviewArticles =  articleMapper.selectList(queryWrapper);
        return needReviewArticles;
    }

    @Override
    public List<Article> queryAllArticle() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("uploadTime");
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Article> queryArticleById(int editor_id){
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("editor_id",editor_id);
        queryWrapper.orderByDesc("uploadTime");
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public Article queryOneArticle(int articleId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        queryWrapper.orderByDesc("uploadTime");
        Article article =  articleMapper.selectOne(queryWrapper);
        return article;
    }



    @Override
    // 事务回滚
    @Transactional
    public String deleteArticle(int articleId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper();
        queryWrapper.eq("article_id",articleId);

        //删除审核内容
        QueryWrapper<ArticleReview> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("article_id",articleId);
        articleReviewMapper.delete(queryWrapper1);

        String videoPath = articleMapper.selectOne(queryWrapper.eq("article_id", articleId)).getVideoPath();
        if (videoPath != null){
            aliOSSUtils.deleteFile(videoPath);
        }

        String imgsUrl = articleMapper.selectOne(queryWrapper.eq("article_id", articleId)).getImagePath();
        if (!Objects.equals(imgsUrl, "")){
            String[] imgPaths = imgsUrl.split(",");
            for (String imgPath : imgPaths){
                if (!Objects.equals(imgPath, " "))
                    aliOSSUtils.deleteFile(imgPath);
            }
        }
        articleMapper.delete(queryWrapper);
        return "成功删除文章";

    }

    @Override
    public String saveArticle(String title, String type, String content,String editor, String editorId,LocalDateTime time,List<String> imagesPath,String videoPath) throws IOException {
        //        判断若为超级管理员上传，不需要审核
        Article article = new Article();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",editorId);
        String role = userMapper.selectOne(queryWrapper).getRole();
        if (role.equals("超级管理员")){
            article.setReviewState(1);
            article.setReviewerId(editorId);
            article.setReviewer(editor);
        }else {
            article.setReviewState(0);
        }
        article.setEditor(editor);
        article.setEditorId(editorId);
        article.setTitle(title);
        article.setType(type);
        article.setLikes(0);
        article.setUploadTime(time);
        article.setContent(content);
        article.setVideoPath(videoPath);


        StringBuilder imagesPathBuilder = new StringBuilder();
        for (String imagePath : imagesPath){
            imagesPathBuilder.append(imagePath).append(",");
        }
        article.setImagePath(imagesPathBuilder.toString());
        articleMapper.insert(article);
        return "内容上传功能";
    }

    @Override
    @Transactional
    public String reSubmitArticle(int articleId, String title, String type, String content, String editor, String editorId, List<String> imagesPath,String video_path) throws IOException{
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        Article article = articleMapper.selectOne(queryWrapper);
        // 重新修改前先把原来的图片都清除
        if (article != null){
            String oldVideoPath = articleMapper.selectOne(queryWrapper.eq("article_id", articleId)).getVideoPath();
            if (!Objects.equals(oldVideoPath, "")){
                aliOSSUtils.deleteFile(oldVideoPath);
            }
            String imgsUrl = article.getImagePath();
            if (!Objects.equals(imgsUrl, "")){
                String[] imgPaths = imgsUrl.split(",");
                for (String imgPath : imgPaths){
                    if (!Objects.equals(imgPath, " "))
                        aliOSSUtils.deleteFile(imgPath);
                }
            }
            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id",editorId);
            String role = userMapper.selectOne(queryWrapper1).getRole();
            if (role.equals("超级管理员")){
                article.setReviewState(1);
                article.setReviewerId(editorId);
                article.setReviewer(editor);
            }else {
                article.setReviewState(0);
            }

            article.setTitle(title);
            article.setType(type);
            article.setEditor(editor);
            article.setEditorId(editorId);
            article.setContent(content);
            article.setVideoPath(video_path);
            StringBuilder imagesPathBuilder = new StringBuilder();
            if (imagesPath!=null){
                for (String imagePath : imagesPath){
                    imagesPathBuilder.append(imagePath).append(",");
                }
                article.setImagePath(imagesPathBuilder.toString());
            }
            articleMapper.updateById(article);
            return "重新提交成功";
        }
        return "重新提交失败";
    }



    @Override
    public List<Article> queryArticleByType(String type) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type",type);
        queryWrapper.orderByDesc("uploadTime");
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public List<Article> queryArticleByEditorId(int editorId) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("editor_id",editorId);
        queryWrapper.orderByDesc("uploadTime");
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public Result likeArticle(int articleId, String jobId) {

        // 2、判断当前登录用户是否已经点赞(redis)
        Boolean isMember = this.isLiked(articleId,jobId);
        // 3、如果未点赞，可以点赞
        if (BooleanUtil.isFalse(isMember)) {
            // 3.1、数据库点赞数 +1
            UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("likes = likes + 1").eq("article_id", articleId);
            articleMapper.update(null, updateWrapper);

            // 保存用户到 Redis 的 set 集合
            stringRedisTemplate.opsForSet().add(RedisConstants.BLOG_LIKED_KEY + articleId, jobId);

            // 4、如果已点赞，取消点赞
        } else {
            // 4.1、数据库点赞数 -1
            UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
            updateWrapper.setSql("likes = likes - 1").eq("article_id", articleId);
            articleMapper.update(null, updateWrapper);

            // 移除用户到 Redis 的 set 集合
            stringRedisTemplate.opsForSet().remove(RedisConstants.BLOG_LIKED_KEY + articleId, jobId);
        }
        return Result.succ();
    }

    @Override
    public Long getLikes(int articleId) {
        String key = RedisConstants.BLOG_LIKED_KEY + articleId;
        long likes = stringRedisTemplate.opsForSet().size(key);
        return likes;
    }

    @Override
    public Boolean isLiked(int articleId, String jobId) {
        String key = RedisConstants.BLOG_LIKED_KEY + articleId;
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, jobId);
        Boolean isLike = BooleanUtil.isTrue(isMember);
        return isLike;
    }

    @Override
    public String changeArticleTime(LocalDateTime dateTime, int articleId) {
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("article_id",articleId).set("uploadTime",dateTime);
        int res = articleMapper.update(null,updateWrapper);
        if (res!=0){
            return "更新文章时间成功！";
        }else {
            return "更新文章时间失败！";
        }
    }

}

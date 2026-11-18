package com.example.spb.controller;

import com.example.spb.entity.po.Article;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleFeedController {
    private final JdbcTemplate jdbc;
    public ArticleFeedController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("passed-page")
    public Map<String, Object> page(@RequestParam String type,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page) {
        if (!Arrays.asList("时政要闻", "支部动态", "微课堂展示").contains(type)
                || page < 1 || keyword.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "查询参数不正确");
        }
        String where = " WHERE review_state=1 AND ";
        List<Object> args = new ArrayList<>();
        if ("微课堂展示".equals(type)) {
            where += "type IN (?,?)";
            args.add(type); args.add("链接展示");
        } else { where += "type=?"; args.add(type); }
        keyword = keyword.trim();
        if (!keyword.isEmpty()) {
            where += " AND title LIKE ? ESCAPE '='";
            args.add("%" + keyword.replace("=", "==").replace("%", "=%").replace("_", "=_") + "%");
        }
        Long total = jdbc.queryForObject("SELECT COUNT(*) FROM article" + where, Long.class, args.toArray());
        long pages = Math.max(1, (total + 4) / 5);
        int current = (int) Math.min(page, pages);
        args.add(5); args.add((current - 1L) * 5);
        List<Article> records = jdbc.query("SELECT * FROM article" + where
                + " ORDER BY uploadTime DESC, article_id DESC LIMIT ? OFFSET ?",
                BeanPropertyRowMapper.newInstance(Article.class), args.toArray());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("records", records); result.put("total", total);
        result.put("page", current); result.put("pageSize", 5);
        return result;
    }
}

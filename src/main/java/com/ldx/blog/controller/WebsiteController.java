package com.ldx.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.ldx.blog.mapper.article.ArticleMapper;
import com.ldx.blog.mapper.article.TagsMapper;
import com.ldx.blog.mapper.category.CategoryMapper;
import com.ldx.blog.mapper.comment.ArticleCommentMapper;
import com.ldx.blog.mapper.user.UserMapper;
import com.ldx.blog.result.Result;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Uaena
 * @date 2023/5/27 12:42
 */
@RestController
@RequestMapping("website")
@CrossOrigin
@Cacheable(cacheNames = {"WEBSITE"},keyGenerator = "redisKeyGenerator")
public class WebsiteController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private TagsMapper tagsMapper;
    @Resource
    private CategoryMapper categoriesMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleCommentMapper articleCommentMapper;
    @GetMapping("info")
    public Result<JSONObject> getWebSiteInfoApi(){
        JSONObject result = new JSONObject();
        Long userCount = userMapper.selectCount(null);
        Long tagCount = tagsMapper.selectCount(null);
        Long categoryCount = categoriesMapper.selectCount(null);
        Long articleCount = articleMapper.selectCount(null);
        Long commentCount = articleCommentMapper.selectCount(null);
        result.put("userCount",userCount);
        result.put("tagCount",tagCount);
        result.put("categoryCount",categoryCount);
        result.put("articleCount",articleCount);
        result.put("commentCount",commentCount);
        return Result.success(result);
    }

}

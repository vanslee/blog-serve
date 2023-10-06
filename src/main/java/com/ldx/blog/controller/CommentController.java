package com.ldx.blog.controller;

import com.ldx.blog.constants.RedisKeys;
import com.ldx.blog.pojo.comment.ArticleComment;
import com.ldx.blog.pojo.dto.CommentPage;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import com.ldx.blog.service.RedisService;
import com.ldx.blog.service.impl.comment.ArticleCommentServiceImpl;
import com.ldx.blog.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Uaena
 * @date 2023/5/19 22:51
 */
@RequestMapping("comment")
@RestController
@Slf4j
@CrossOrigin
public class CommentController {
    @Resource
    private ArticleCommentServiceImpl articleCommentService;
    @Resource
    private RedisService redisService;

    @PostMapping("/comment_brief")
    public Result<Map<String, Object>> commentBriefApi(@RequestBody CommentPage commentPage) {
        log.debug("查询简易评论:{}", commentPage);
        return articleCommentService.getCommentBriefService(commentPage);
    }

    @PostMapping("/add_article_comment")
    public Result<ResultCodeEnum> addArticleCommentApi(HttpServletRequest request, @RequestBody ArticleComment articleComment) {
        log.debug("新增评论:{}", articleComment);
        String ip = IPUtil.ip(request);
        articleComment.setLocation(ip);
        boolean insert_success = articleCommentService.save(articleComment);
        if (insert_success) {
            redisService.hincr(RedisKeys.ARTICLE_COMMENTS_COUNT, "counts", 1);
            return Result.success(ResultCodeEnum.PUBLISH_COMMENT_SUCCESS);
        }
        return Result.fail(ResultCodeEnum.PUBLISH_COMMENT_FAIL);
    }
}

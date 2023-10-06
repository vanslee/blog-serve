package com.ldx.blog.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ldx.blog.constants.RedisKeys;
import com.ldx.blog.pojo.article.Article;
import com.ldx.blog.pojo.article.ArticleCollects;
import com.ldx.blog.pojo.category.PageParams;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import com.ldx.blog.service.AsyncService;
import com.ldx.blog.service.RedisService;
import com.ldx.blog.service.impl.article.ArticleCollectsServiceImpl;
import com.ldx.blog.service.impl.article.ArticleDetailsServiceImpl;
import com.ldx.blog.service.impl.article.ArticleServiceImpl;
import com.ldx.blog.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.ldx.blog.result.Result.fail;
import static com.ldx.blog.result.Result.success;

/**
 * @author Uaena
 * @date 2023/5/16 22:47
 */
@Slf4j
@RestController
@RequestMapping(value = "article")
@CrossOrigin
public class ArticleController {
    @Resource
    private ArticleCollectsServiceImpl articleCollectsService;
    @Resource
    private ArticleServiceImpl articleService;
    @Resource
    private ArticleDetailsServiceImpl articleDetailsService;
    @Resource
    private RedisService redisService;
    @Resource
    private AsyncService asyncService;

    @PostMapping("/list")
    public Result<IPage<Article>> getArticleListApi(@RequestBody PageParams params) {
        return articleService.getArticlePage(params);
    }

//    @GetMapping("/list/{uid}")
//    public Result<IPage<Article>> getArticlesByUid(HttpServletRequest request, @PathVariable long uid) {
//        String size = request.getParameter("size");
//        String keyword = request.getParameter("keyword");
//        String desc = request.getParameter("desc");
//        String current = request.getParameter("current");
//        if (StringUtil.isEmpty(size) || StringUtil.isEmpty(current)) {
//            return Result.fail(ResultCodeEnum.PARAM_IS_INVALID);
//        }
//        return articleService.getArticlesByUid(uid, keyword, desc, size, current);
//    }

    /**
     * 根据article_id获取文章详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{article_id}")
    public Result<Article> getArticleDetailsByIdApi(HttpServletRequest request, @PathVariable("article_id") int id) {
        log.debug("用户查询文章详情: {}", id);
        return articleService.getArticleById(id);
    }

    @PutMapping("/details/{article_id}/view")
    @Async
    public Result<Boolean> updateArticleViewsByAid(HttpServletRequest request, @PathVariable("article_id") String aid) {
        String ip = IPUtil.ip(request);
        String key = RedisKeys.ARTICLE_VIEW.concat(aid).concat(":").concat(ip);
        if (!redisService.hasKey(key)) {
            redisService.set(key, ip, TimeUnit.HOURS.toMillis(1));
            redisService.setHashValue(RedisKeys.ARTICLE_VIEW_COUNT.concat(aid), "last_time", System.currentTimeMillis());
            redisService.incrHashValue(RedisKeys.ARTICLE_VIEW_COUNT.concat(aid), "counts", 1);
//            redisService.incr(RedisKeys.ARTICLE_VIEW_COUNT.concat(aid),1);
        }
        return success(true);
    }

    @PutMapping("/details/{article_id}/like")
    public Result<Boolean> updateArticleLikesByAid(@PathVariable("article_id") String aid) {
        String userId = StpUtil.getLoginId().toString();
        String key = RedisKeys.ARTICLE_LIKE.concat(aid).concat(":").concat(userId);
        if (!redisService.hasKey(key)) {
            redisService.set(key, userId);
            redisService.setHashValue(RedisKeys.ARTICLE_LIKE_COUNT.concat(aid), "last_time", System.currentTimeMillis());
            redisService.incrHashValue(RedisKeys.ARTICLE_LIKE_COUNT.concat(aid), "counts", 1);
        } else {
            redisService.del(key);
            redisService.setHashValue(RedisKeys.ARTICLE_LIKE_COUNT.concat(aid), "last_time", System.currentTimeMillis());
            redisService.incrHashValue(RedisKeys.ARTICLE_LIKE_COUNT.concat(aid), "counts", -1);
        }
        return success(true);
    }

    /**
     * 收藏文章
     */
    @PutMapping("/article/{id}/collect")
    public Result<ResultCodeEnum> collectArticle(@PathVariable("id") String articleId) {
        LambdaQueryWrapper<ArticleCollects> lqw = new LambdaQueryWrapper<>();
        String userId = StpUtil.getLoginId().toString();
        lqw.eq(ArticleCollects::getArticleId, articleId).eq(ArticleCollects::getUserId, userId);
        ArticleCollects hasRowInDb = articleCollectsService.getOne(lqw);
        if (Objects.isNull(hasRowInDb)) {
            ArticleCollects articleCollects = new ArticleCollects(articleId, userId);
            try {
                boolean save = articleCollectsService.save(articleCollects);
                if (save) {
                    redisService.incr(RedisKeys.ARTICLE_COLLECT_COUNT.concat(articleId), 1);
                    return success(ResultCodeEnum.COLLECT_ARTICLE_SUCCESS);
                } else {
                    return fail(ResultCodeEnum.COLLECT_ARTICLE_FAIL);
                }
            } catch (RuntimeException e) {
                return fail(ResultCodeEnum.COLLECT_ARTICLE_FAIL);
            }
        } else {
            boolean delete = hasRowInDb.isDelete();
            hasRowInDb.setDelete(!delete);
            try {
                boolean save = articleCollectsService.updateById(hasRowInDb);
                if (save) {
                    redisService.incr(RedisKeys.ARTICLE_COLLECT_COUNT.concat(articleId), delete ? -1 : 1);
                    return success(ResultCodeEnum.COLLECT_ARTICLE_SUCCESS);
                } else {
                    return fail(ResultCodeEnum.COLLECT_ARTICLE_FAIL);
                }
            } catch (RuntimeException e) {
                return fail(ResultCodeEnum.COLLECT_ARTICLE_FAIL);
            }
        }
    }

    /**
     * 根据article_id获取文章详情
     *
     * @param articleId
     * @return
     */
    @GetMapping("/{article_id}")
    public Result<Article> getArticleById(@PathVariable("article_id") long articleId) {
        return articleService.getArticle(articleId);
    }

    @DeleteMapping("/delete/{article_id}")
    public Result<Boolean> deleteArticleById(@PathVariable("article_id") long articleId) {
        try {
            return success(articleService.removeById(articleId));
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("删除文章失败:{}", articleId);
            return fail(ResultCodeEnum.DELETE_ARTICLE_FAIL);
        }
    }

    @DeleteMapping("/delete/batch")
    public Result<Boolean> batchDeleteArticles(@RequestBody List<Long> ids) {
        try {
            return success(articleService.removeBatchByIds(ids));
        } catch (RuntimeException e) {
            log.error("删除文章失败:{}", ids);
            return fail(ResultCodeEnum.DELETE_ARTICLE_FAIL);
        }
    }

    /**
     * 发布文章
     */
    @PutMapping(value = "/publish")
    public Result<ResultCodeEnum> publishArticleApi(@Valid @RequestBody Article article) {
        boolean save = articleService.publishArticle(article);
        if (save) {
            return success(ResultCodeEnum.PUBLISH_SUCCESS);
        } else {
            return fail(ResultCodeEnum.PUBLISH_FAIL);
        }
    }

    /**
     * 修改文章
     */
    @PutMapping("/update")
    public Result<ResultCodeEnum> updateArticleApi(@Valid @RequestBody Article article) {
        boolean save = articleService.updateArticle(article);
        if (save) {
            return success(ResultCodeEnum.PUBLISH_SUCCESS);
        } else {
            return fail(ResultCodeEnum.PUBLISH_FAIL);
        }
    }


}

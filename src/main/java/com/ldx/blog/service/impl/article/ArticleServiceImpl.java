package com.ldx.blog.service.impl.article;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.constants.RedisKeys;
import com.ldx.blog.mapper.article.ArticleDetailsMapper;
import com.ldx.blog.mapper.article.ArticleMapper;
import com.ldx.blog.mapper.article.ArticleTagMapper;
import com.ldx.blog.mapper.article.TagsMapper;
import com.ldx.blog.mapper.category.ArticleCategoryMapper;
import com.ldx.blog.mapper.category.CategoryMapper;
import com.ldx.blog.mapper.comment.ArticleCommentMapper;
import com.ldx.blog.mapper.user.UserMapper;
import com.ldx.blog.pojo.article.Article;
import com.ldx.blog.pojo.article.ArticleTag;
import com.ldx.blog.pojo.article.Tags;
import com.ldx.blog.pojo.category.ArticleCategory;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.category.PageParams;
import com.ldx.blog.pojo.comment.ArticleComment;
import com.ldx.blog.pojo.user.User;
import com.ldx.blog.result.Result;
import com.ldx.blog.service.RedisService;
import com.ldx.blog.service.article.ArticleService;
import com.ldx.blog.service.impl.category.CategoryServiceImpl;
import com.ldx.blog.utils.FileUtil;
import com.ldx.blog.utils.QiNiuYunOssUtil;
import com.ldx.blog.utils.TextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author ldx
 * @description 针对表【article】的数据库操作Service实现
 * @createDate 2023-05-21 22:17:01
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {
    private String QINIU_CDN;
    private final Integer CONTENT_LENGTH = 20;
    @Value("${website.config.cdn}")
    private String CDN_WEBSITE;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TagsMapper tagsMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoriesMapper;
    @Resource
    private ArticleCategoryMapper articleCategoryMapper;
    @Resource
    private CategoryServiceImpl categoriesService;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private ArticleCommentMapper articleCommentMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private ArticleDetailsMapper articleDetailsMapper;
    @Resource
    private QiNiuYunOssUtil ossUtil;

    public Result<IPage<Article>> getArticlePage(PageParams params) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        long uid = -1;
        try {
            Object loginId = StpUtil.getLoginId();
            uid = Long.parseLong(loginId.toString());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        lqw.eq(uid != -1, Article::getUserId, uid);
        lqw.like(TextUtil.isNotEmpty(params.getKeyword()), Article::getArticleTitle, params.getKeyword());
        lqw.orderByDesc(params.isDesc(), Article::getPublishDate);
        lqw.eq(params.getStatus() != 0, Article::getStatus, params.getStatus());
        IPage<Article> iPage = new Page<>(params.getPageNo(), params.getPageSize());
        page(iPage, lqw);
        iPage.getRecords().forEach(article -> {
            article.setMdUrl(CDN_WEBSITE.concat(article.getMdUrl()));
            article.setImgUrl(CDN_WEBSITE.concat(article.getImgUrl()));
            Long userId = article.getUserId();
            int articleId = article.getId();
            LambdaQueryWrapper<User> lqw3 = new LambdaQueryWrapper<>();
            lqw3.eq(User::getId, userId).select(User::getAvatarImgUrl, User::getUsername, User::getPersonalBrief);
            User user = userMapper.selectOne(lqw3);
            article.setAuthorAvatar(CDN_WEBSITE.concat(user.getAvatarImgUrl()));
            article.setAuthorName(user.getUsername());
            article.setViews(articleViews(articleId));
            article.setLikes(articleLikes(articleId));
            article.setComments(articleComments(articleId));
            article.setCollects(articleCollects(articleId));
            article.setPersonalBrief(user.getPersonalBrief());
            article.setTags(getTags(articleId));
            article.setCategory(getCategoryByAid(articleId));
        });
        return Result.success(iPage);
    }



    public boolean publishArticle(Article article) {
        if (!TextUtil.isNotEmpty(article.getArticleContent()) && !TextUtil.isNotEmpty(article.getMdUrl())) {
            return false;
        }
        try {
            if (TextUtil.isNotEmpty(article.getMdUrl())) {
                article.setMdUrl(article.getMdUrl());
            } else {
                File file = FileUtil.generateMarkdown(article.getArticleTitle(), article.getArticleContent());
                String url = ossUtil.uploadMarkdown(file, StpUtil.getLoginId() + "/");
                article.setMdUrl(url);
            }
            article.setUserId(Long.parseLong(StpUtil.getLoginId().toString()));
            articleMapper.insert(article);
            setTags(article.getTags(), article.getId());
            setCategories(article);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean updateArticle(Article article) {
        try {
            File file = FileUtil.generateMarkdown(article.getArticleTitle(), article.getArticleContent());
            String url = ossUtil.uploadMarkdown(file, StpUtil.getLoginId() + "/");
            article.setMdUrl(url);
            article.setUpdateDate(System.currentTimeMillis() / 1000);
            articleMapper.updateById(article);
            setTags(article.getTags(), article.getId());
            setCategories(article);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Result<Article> getArticleById(int articleId) {
        Article article = articleMapper.selectById(articleId);
        article.setMdUrl(CDN_WEBSITE.concat(article.getMdUrl()));
        article.setImgUrl(CDN_WEBSITE.concat(article.getImgUrl()));
        article.setViews(articleViews(articleId));
        article.setLikes(articleLikes(articleId));
        LambdaQueryWrapper<Article> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Article::getUserId, article.getUserId());
        Long articleCount = articleMapper.selectCount(lqw1);
        // 作者文章数量
        Long userId = article.getUserId();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.select(User::getUsername, User::getFans, User::getAvatarImgUrl, User::getId).eq(User::getId, userId);
        // 作者的用户名粉丝数
        User user = userMapper.selectOne(lqw);
        user.setAvatarImgUrl(CDN_WEBSITE.concat(user.getAvatarImgUrl()));
        user.setArticleCount(articleCount);
        article.setAuthor(user);
        return Result.success(article);
    }

    public Result<Article> getArticle(long articleId) {
        Article article = articleMapper.selectById(articleId);
        List<Tags> tags = getTags(articleId);
        article.setCategory(getCategoryByAid(article.getUserId()));
        article.setTags(tags);
        article.setMdUrl(CDN_WEBSITE.concat(article.getMdUrl()));
        article.setImgUrl(CDN_WEBSITE.concat(article.getImgUrl()));
        LambdaQueryWrapper<Article> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Article::getUserId, article.getUserId());
        // 作者文章数量
        Long userId = article.getUserId();
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.select(User::getUsername, User::getAvatarImgUrl).eq(User::getId, userId);
        // 作者的用户名粉丝数
        User user = userMapper.selectOne(lqw);
        article.setUsername(user.getUsername());
        article.setAvatar(CDN_WEBSITE.concat(user.getAvatarImgUrl()));
        return Result.success(article);
    }

    public Result<IPage<Article>> getArticlesByCid(long articleId, Integer current, Integer size) {
        try {
            LambdaQueryWrapper<ArticleCategory> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ArticleCategory::getCategoryId, articleId);
            List<ArticleCategory> articleCategories = articleCategoryMapper.selectList(lqw);
            List<Integer> articleIds = new ArrayList<>(articleCategories.size());
            articleCategories.forEach(item -> {
                articleIds.add(item.getArticleId());
            });
            IPage<Article> iPage = new Page<>(current, size);
            LambdaQueryWrapper<Article> lqw1 = new LambdaQueryWrapper<>();
            lqw1.in(Article::getId, articleIds);
            page(iPage, lqw1);
            return Result.success(iPage);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.fail();
        }
    }

    public List<Category> getCategoriesByUid(long userId) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getUserId, userId);
        return categoriesMapper.selectList(lqw);
    }

    public Category getCategoryByAid(long articleId) {
        LambdaQueryWrapper<ArticleCategory> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleCategory::getArticleId, articleId);
        ArticleCategory articleCategory = articleCategoryMapper.selectOne(lqw);
        if (Objects.isNull(articleCategory)) {
            return null;
        }
        LambdaQueryWrapper<Category> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Category::getId, articleCategory.getCategoryId());
        return categoriesMapper.selectOne(lqw1);
    }

    public List<Tags> getTags(long articleId) {
        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId, articleId);
        List<ArticleTag> articleTags = articleTagMapper.selectList(lqw);
        List<Integer> tids = new ArrayList<>(articleTags.size());
        articleTags.forEach(tid -> {
            tids.add(tid.getTagId());
        });
        if (CollectionUtils.isEmpty(tids)) {
            return new ArrayList<>(0);
        } else {
            List<Tags> tags = tagsMapper.selectBatchIds(tids);
            return tags;
        }
    }

    public void setTags(List<Tags> tags, int articleId) {
        LambdaQueryWrapper<Tags> lqw = new LambdaQueryWrapper<>();
        List<Tags> tagsInDb = tagsMapper.selectList(lqw);
//        List<Tags> tagsInDb = new ArrayList<>(allTags.size());
//        allTags.forEach(tag -> {
//            tagsInDb.add(tag);
//        });
        tags.forEach(tag -> {
//            tagName = tagName.trim();
            if (!tagsInDb.contains(tag)) {
                tag.setCreateTime(System.currentTimeMillis() / 1000);
                tagsMapper.insert(tag);
                tag.setName(tag.getName().trim());
                ArticleTag articleTag = new ArticleTag(articleId, tag.getId());
                articleTagMapper.insert(articleTag);
            }
        });

    }

    public void setCategories(Article article) {
        articleCategoryMapper.insert(new ArticleCategory(article.getId(), article.getCategoryId()));
    }

    public Integer articleViews(int articleId) {
        Object counts = redisService.getHasValue(RedisKeys.ARTICLE_VIEW_COUNT.concat(String.valueOf(articleId)), "counts");
        if (Objects.isNull(counts)) {
            return 0;
        } else {
            return Integer.parseInt(counts.toString());
        }
    }

    public Integer articleComments(int articleId) {
        Object counts = redisService.get(RedisKeys.ARTICLE_COMMENTS_COUNT.concat(String.valueOf(articleId)));
        if (Objects.isNull(counts)) {
            LambdaQueryWrapper<ArticleComment> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ArticleComment::getArticleId, articleId);
            Long count = articleCommentMapper.selectCount(lqw);
            counts = Objects.isNull(count) ? 0 : count.intValue();
            redisService.set(RedisKeys.ARTICLE_COMMENTS_COUNT.concat(String.valueOf(articleId)), counts);
        }
        return Integer.parseInt(counts.toString());
    }

    public Integer articleLikes(int articleId) {
        Object counts = redisService.getHasValue(RedisKeys.ARTICLE_LIKE_COUNT.concat(String.valueOf(articleId)), "counts");
        if (Objects.isNull(counts)) {
            return 0;
        } else {
            return Integer.parseInt(counts.toString());
        }
    }
    private Integer articleCollects(int articleId) {
        Object counts = redisService.get(RedisKeys.ARTICLE_COLLECT_COUNT.concat(String.valueOf(articleId)));
        if (Objects.isNull(counts)) {
            return 0;
        } else {
            return Integer.parseInt(counts.toString());
        }
    }
}





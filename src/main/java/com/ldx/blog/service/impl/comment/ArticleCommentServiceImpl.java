package com.ldx.blog.service.impl.comment;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.mapper.comment.ArticleCommentMapper;
import com.ldx.blog.pojo.comment.ArticleComment;
import com.ldx.blog.pojo.dto.CommentPage;
import com.ldx.blog.result.Result;
import com.ldx.blog.service.article.ArticleCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ldx
 * @description 针对表【article_comment】的数据库操作Service实现
 * @createDate 2023-05-19 22:53:15
 */
@Service
@Transactional
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
        implements ArticleCommentService {
    @Resource
    private ArticleCommentMapper articleCommentMapper;

    public Result<Map<String,Object>> getCommentBriefService(CommentPage commentPage) {
        Integer page = (commentPage.getCurrent() - 1) * commentPage.getSize();
        Long rootCommentsCount = articleCommentMapper.getRootCommentsCount(commentPage.getArticleId());
        List<ArticleComment> rootComments = articleCommentMapper.getRootComments(commentPage.getArticleId(), page, commentPage.getSize());
        rootComments.forEach(root -> {
            List<ArticleComment> childrenComments = articleCommentMapper.getChildrenComments(commentPage.getArticleId(), root.getId());
            List<ArticleComment> grandsonComments = new ArrayList<>(3);
            for (ArticleComment childrenComment : childrenComments) {
                grandsonComments.addAll(articleCommentMapper.getChildrenComments(commentPage.getArticleId(), childrenComment.getId()));
            }
            childrenComments.addAll(grandsonComments);
            root.setChildrens(childrenComments);
        });
        Map resJson = new HashMap(2);
        resJson.put("data",rootComments);
        resJson.put("total",rootCommentsCount);
        return Result.success(resJson);
    }
}





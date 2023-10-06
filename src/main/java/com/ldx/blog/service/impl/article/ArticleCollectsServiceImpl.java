package com.ldx.blog.service.impl.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.mapper.article.ArticleCollectsMapper;
import com.ldx.blog.pojo.article.ArticleCollects;
import com.ldx.blog.service.article.ArticleCollectsService;
import org.springframework.stereotype.Service;

/**
 * @author Uaena
 * @date 2023/8/8 22:06
 */
@Service
public class ArticleCollectsServiceImpl extends ServiceImpl<ArticleCollectsMapper, ArticleCollects>
        implements ArticleCollectsService {
}

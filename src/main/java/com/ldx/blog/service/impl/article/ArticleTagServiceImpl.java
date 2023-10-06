package com.ldx.blog.service.impl.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.pojo.article.ArticleTag;
import com.ldx.blog.service.article.ArticleTagService;
import com.ldx.blog.mapper.article.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author ldx
* @description 针对表【article_tag】的数据库操作Service实现
* @createDate 2023-05-22 20:47:59
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}





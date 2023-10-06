package com.ldx.blog.service.impl.article;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.pojo.article.ArticleDetails;
import com.ldx.blog.service.article.ArticleDetailsService;
import com.ldx.blog.mapper.article.ArticleDetailsMapper;
import org.springframework.stereotype.Service;

/**
* @author ldx
* @description 针对表【article_details】的数据库操作Service实现
* @createDate 2023-05-19 21:07:49
*/
@Service
public class ArticleDetailsServiceImpl extends ServiceImpl<ArticleDetailsMapper, ArticleDetails>
    implements ArticleDetailsService{

}





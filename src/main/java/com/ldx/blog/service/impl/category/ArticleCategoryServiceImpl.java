package com.ldx.blog.service.impl.category;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.pojo.category.ArticleCategory;
import com.ldx.blog.service.category.ArticleCategoryService;
import com.ldx.blog.mapper.category.ArticleCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author ldx
* @description 针对表【article_category】的数据库操作Service实现
* @createDate 2023-05-22 20:47:47
*/
@Service
public class ArticleCategoryServiceImpl extends ServiceImpl<ArticleCategoryMapper, ArticleCategory>
    implements ArticleCategoryService{

}





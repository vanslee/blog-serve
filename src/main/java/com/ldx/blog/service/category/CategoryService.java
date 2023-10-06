package com.ldx.blog.service.category;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.category.PageParams;

/**
* @author ldx
* @description 针对表【categories】的数据库操作Service
* @createDate 2023-05-22 20:47:41
*/
public interface CategoryService extends IService<Category> {

    IPage<Category> getCategories(PageParams params);

    Boolean insertOrUpdateCategory(Category category);
}

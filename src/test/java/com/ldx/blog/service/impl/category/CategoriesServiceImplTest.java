package com.ldx.blog.service.impl.category;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.category.PageParams;
import com.ldx.blog.service.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

/**
 * @author Uaena
 * @date 2023/7/19 21:02
 */
@SpringBootTest
@ActiveProfiles("test")
class CategoriesServiceImplTest {
@Resource
private CategoryServiceImpl categoriesService;
    @Test
    void getCategories() {
        PageParams categoryPage = new PageParams();
        categoryPage.setPageNo(0);
        categoryPage.setPageSize(10);
        categoryPage.setDesc(true);
        categoryPage.setKeyword("qqq");
        categoryPage.setStatus(1);
        IPage<Category> categories = categoriesService.getCategories(categoryPage);
        System.out.println(categories.getRecords());
    }
}